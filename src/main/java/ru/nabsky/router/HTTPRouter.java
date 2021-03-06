package ru.nabsky.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.http.HttpStatus;
import ru.nabsky.dao.TokenDAO;
import ru.nabsky.helper.DatabaseHelper;
import ru.nabsky.helper.JSONHelper;
import ru.nabsky.helper.SecurityHelper;
import ru.nabsky.models.Mate;
import ru.nabsky.models.Team;
import ru.nabsky.models.Token;
import ru.nabsky.models.Unit;
import ru.nabsky.services.TeamService;
import spark.Request;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static spark.Spark.*;

public class HTTPRouter {

    private static final String APPLICATION_JSON                = "application/json";

    private static final String ROUTE_PROTECTED_MASK            = "/api/protected/*";

    private static final String ROUTE_LOGIN                     = "/api/public/login";
    private static final String ROUTE_REGISTER                  = "/api/public/teams";

    private static final String ROUTE_UNITS                     = "/api/protected/units";
    private static final String UNIT_ID_PARAM                   = ":unitId";
    private static final String ROUTE_UNITS_WITH_UNIT_ID_PARAM  = ROUTE_UNITS + "/" + UNIT_ID_PARAM;

    private static final String ROUTE_MATES                     = "/api/protected/mates";
    private static final String MATE_ID_PARAM                   = ":mateId";
    private static final String ROUTE_MATES_WITH_MATE_ID_PARAM = ROUTE_MATES + "/" + MATE_ID_PARAM;

    private Injector injector;

    public HTTPRouter(Injector injector) {
        this.injector = injector;
        staticFileLocation("/public");
        setupRoutes();
    }

    private void setupRoutes() {

        before(ROUTE_PROTECTED_MASK, (request, response) -> {
            String tokenId = SecurityHelper.extractTokenId(request);
            if (tokenId == null) {
                halt(HttpStatus.FORBIDDEN_403, "Forbidden");
            }
            TokenDAO tokenDAO = injector.getInstance(TokenDAO.class);
            Token token = tokenDAO.findById(tokenId);
            if (token == null || token.isExpired()) {
                halt(HttpStatus.FORBIDDEN_403, "Forbidden");
            }
            TeamService teamService = injector.getInstance(TeamService.class);
            Team team = teamService.getTeamByTokenId(tokenId);
            request.attribute("team", team);
        });

        post(ROUTE_LOGIN, (request, response) -> {
            Map<String, Object> data = JSONHelper.jsonToData(request.body());
            if (!data.containsKey("team") || !data.containsKey("password")) {
                response.status(HttpStatus.UNAUTHORIZED_401);
                return "";
            }
            String team = data.get("team").toString();
            String password = data.get("password").toString();

            if (team == null || password == null) {
                response.status(HttpStatus.UNAUTHORIZED_401);
                return "";
            }

            TokenDAO tokenDAO = injector.getInstance(TokenDAO.class);
            Token token = tokenDAO.getToken(team, password);
            if (token == null) {
                response.status(HttpStatus.UNAUTHORIZED_401);
                return "";
            }
            response.status(HttpStatus.OK_200);
            response.type(APPLICATION_JSON);

            Map<String, Object> resultData = new HashMap<String, Object>();
            resultData.put("tokenId", token.get_id());
            resultData.put("teamId", token.getTeamId());
            resultData.put("mode", token.getMode());
            String json = JSONHelper.dataToJson(resultData);
            return json;
        });

        //========================= T E A M S =================================

        post(ROUTE_REGISTER, (request, response) -> {
            Map<String, String> data = new HashMap<String, String>();
            response.type(APPLICATION_JSON);

            Team team = (Team) extractObject(request, Team.class);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Team>> violations = validator.validate(team);
            if (!violations.isEmpty()) {
                data.put("error", violations.iterator().next().getMessage());
                return JSONHelper.dataToJson(data);
            }

            TeamService teamService = injector.getInstance(TeamService.class);
            if (teamService.isTeamExists(team.getName())) {
                response.status(HttpStatus.CONFLICT_409);
                data.put("error", "Team name is already used");
                return JSONHelper.dataToJson(data);
            }

            String teamId = teamService.createTeam(team);
            DatabaseHelper.updateDesignDocuments(team.getName());
            data.put("id", teamId);
            response.status(HttpStatus.CREATED_201);
            return JSONHelper.dataToJson(data);
        });

        //========================= U N I T S =================================
        get(ROUTE_UNITS, (request, response) -> {
            Team team = (Team) request.attribute("team");
            TeamService teamService = injector.getInstance(TeamService.class);
            List<Unit> units = teamService.getUnits(team);
            response.status(HttpStatus.OK_200);
            response.type(APPLICATION_JSON);
            return JSONHelper.dataToJson(units);
        });

        post(ROUTE_UNITS, (request, response) -> {
            Team team = (Team) request.attribute("team");
            Unit unit = (Unit) extractObject(request, Unit.class);

            Map<String, String> data = new HashMap<String, String>();
            response.type(APPLICATION_JSON);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Unit>> violations = validator.validate(unit);
            if (!violations.isEmpty()) {
                data.put("error", violations.iterator().next().getMessage());
                return JSONHelper.dataToJson(data);
            }

            TeamService teamService = injector.getInstance(TeamService.class);
            String unitId = teamService.createUnit(team, unit);
            data.put("id", unitId);
            response.status(HttpStatus.CREATED_201);
            return JSONHelper.dataToJson(data);
        });

        put(ROUTE_UNITS_WITH_UNIT_ID_PARAM, (request, response) -> {
            Team team = (Team) request.attribute("team");
            Unit unit = (Unit) extractObject(request, Unit.class);
            Map<String, String> data = new HashMap<String, String>();
            response.type(APPLICATION_JSON);

            TeamService teamService = injector.getInstance(TeamService.class);
            String unitId = request.params(UNIT_ID_PARAM);
            Unit update = teamService.findUnit(team, unitId);
            if (update == null) {
                response.status(HttpStatus.NOT_FOUND_404);
                data.put("error", "Unit with id = " + unitId + " is not found");
                return JSONHelper.dataToJson(data);
            }
            unit.set_id(update.get_id());
            unit.set_rev(update.get_rev());
            BeanUtils.copyProperties(update, unit);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Unit>> violations = validator.validate(update);
            if (!violations.isEmpty()) {
                data.put("error", violations.iterator().next().getMessage());
                return JSONHelper.dataToJson(data);
            }

            unit = teamService.updateUnit(team, update);
            response.status(HttpStatus.OK_200);
            return JSONHelper.dataToJson(unit);
        });

        delete(ROUTE_UNITS_WITH_UNIT_ID_PARAM, (request, response) -> {
            String unitId = request.params(UNIT_ID_PARAM);
            Team team = (Team) request.attribute("team");
            Map<String, String> data = new HashMap<String, String>();
            response.type(APPLICATION_JSON);

            TeamService teamService = injector.getInstance(TeamService.class);
            Unit delete = teamService.findUnit(team, unitId);
            if (delete == null) {
                response.status(HttpStatus.NOT_FOUND_404);
                data.put("error", "Unit with id = " + unitId + " is not found");
                return JSONHelper.dataToJson(data);
            }

            teamService.deleteUnit(team, delete);
            response.status(HttpStatus.OK_200);
            return "{}";
        });

        //========================= M A T E S =================================
        get(ROUTE_MATES, (request, response) -> {
            Team team = (Team) request.attribute("team");
            TeamService teamService = injector.getInstance(TeamService.class);
            List<Mate> mates = teamService.getMates(team);
            response.status(HttpStatus.OK_200);
            response.type(APPLICATION_JSON);
            return JSONHelper.dataToJson(mates);
        });

        post(ROUTE_MATES, (request, response) -> {
            Team team = (Team) request.attribute("team");
            Mate mate = (Mate) extractObject(request, Mate.class);

            Map<String, String> data = new HashMap<String, String>();
            response.type(APPLICATION_JSON);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Mate>> violations = validator.validate(mate);
            if (!violations.isEmpty()) {
                data.put("error", violations.iterator().next().getMessage());
                return JSONHelper.dataToJson(data);
            }

            TeamService teamService = injector.getInstance(TeamService.class);
            String mateId = teamService.createMate(team, mate);
            data.put("id", mateId);
            response.status(HttpStatus.CREATED_201);
            return JSONHelper.dataToJson(data);
        });

        put(ROUTE_MATES_WITH_MATE_ID_PARAM, (request, response) -> {
            Team team = (Team) request.attribute("team");
            Mate mate = (Mate) extractObject(request, Mate.class);
            Map<String, String> data = new HashMap<String, String>();
            response.type(APPLICATION_JSON);

            TeamService teamService = injector.getInstance(TeamService.class);
            String mateId = request.params(MATE_ID_PARAM);
            Mate update = teamService.findMate(team, mateId);
            if (update == null) {
                response.status(HttpStatus.NOT_FOUND_404);
                data.put("error", "Mate with id = " + mateId + " is not found");
                return JSONHelper.dataToJson(data);
            }
            mate.set_id(update.get_id());
            mate.set_rev(update.get_rev());
            BeanUtils.copyProperties(update, mate);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Mate>> violations = validator.validate(update);
            if (!violations.isEmpty()) {
                data.put("error", violations.iterator().next().getMessage());
                return JSONHelper.dataToJson(data);
            }

            mate = teamService.updateMate(team, update);
            response.status(HttpStatus.OK_200);
            return JSONHelper.dataToJson(mate);
        });

        delete(ROUTE_MATES_WITH_MATE_ID_PARAM, (request, response) -> {
            String mateId = request.params(MATE_ID_PARAM);
            Team team = (Team) request.attribute("team");
            Map<String, String> data = new HashMap<String, String>();
            response.type(APPLICATION_JSON);

            TeamService teamService = injector.getInstance(TeamService.class);
            Mate delete = teamService.findMate(team, mateId);
            if (delete == null) {
                response.status(HttpStatus.NOT_FOUND_404);
                data.put("error", "Mate with id = " + mateId + " is not found");
                return JSONHelper.dataToJson(data);
            }

            teamService.deleteMate(team, delete);
            response.status(HttpStatus.OK_200);
            return "{}";
        });

    }

    private Object extractObject(Request request, Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
        Object object = null;
        try {
            object = mapper.readValue(request.body(), clazz);
        } catch (IOException e) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("error", "Unprocessable entity: " + e.getLocalizedMessage());
            halt(HttpStatus.UNPROCESSABLE_ENTITY_422, JSONHelper.dataToJson(data));
        }
        return object;
    }

}
