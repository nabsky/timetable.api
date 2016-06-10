package ru.nabsky.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;
import org.eclipse.jetty.http.HttpStatus;
import ru.nabsky.dao.TokenDAO;
import ru.nabsky.helper.DatabaseHelper;
import ru.nabsky.helper.JSONHelper;
import ru.nabsky.helper.SecurityHelper;
import ru.nabsky.models.Team;
import ru.nabsky.models.Token;
import ru.nabsky.models.Unit;
import ru.nabsky.models.ValidationResult;
import ru.nabsky.services.TeamService;

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
import static spark.Spark.halt;

public class WebConfig {

    private Injector injector;

    public WebConfig(Injector injector) {
        this.injector = injector;
        staticFileLocation("/public");
        setupRoutes();
    }

    private void setupRoutes() {

        before("/api/protected/*", (request, response) -> {
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

        post("/api/public/login", (request, response) -> {
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
            response.type("application/json");

            Map<String, Object> resultData = new HashMap<String, Object>();
            resultData.put("tokenId", token.get_id());
            resultData.put("teamId", token.getTeamId());
            resultData.put("isLead", token.isLeadMode());
            String json = JSONHelper.dataToJson(resultData);
            return json;
        });

        //========================= T E A M S =================================

        post("/api/public/teams", (request, response) -> {
            Map<String, String> data = new HashMap<String, String>();
            response.type("application/json");

            ObjectMapper mapper = new ObjectMapper();

            Team team = null;
            try {
                team = mapper.readValue(request.body(), Team.class);
            } catch (IOException e) {
                response.status(HttpStatus.UNPROCESSABLE_ENTITY_422);
                data.put("error", "Team data is not valid");
                return JSONHelper.dataToJson(data);
            }

            ValidationResult validationResult = team.validate();
            if (!validationResult.isValid()) {
                data.put("error", validationResult.getErrorMessage());
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
        get("/api/protected/units", (request, response) -> {
            Team team = (Team) request.attribute("team");
            TeamService teamService = injector.getInstance(TeamService.class);
            List<Unit> units = teamService.getUnits(team);
            response.status(HttpStatus.OK_200);
            response.type("application/json");
            return JSONHelper.dataToJson(units);
        });

        post("/api/protected/units", (request, response) -> {
            Map<String, String> data = new HashMap<String, String>();
            response.type("application/json");

            Team team = (Team) request.attribute("team");

            ObjectMapper mapper = new ObjectMapper();

            Unit unit = null;
            try {
                unit = mapper.readValue(request.body(), Unit.class);
            } catch (IOException e) {
                response.status(HttpStatus.UNPROCESSABLE_ENTITY_422);
                data.put("error", "Unit data is not valid");
                return JSONHelper.dataToJson(data);
            }

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Unit>> violations = validator.validate(unit);

            if(!violations.isEmpty()){
                data.put("error", violations.iterator().next().getMessage());
                return JSONHelper.dataToJson(data);
            }

            TeamService teamService = injector.getInstance(TeamService.class);
            String unitId = teamService.createUnit(team, unit);
            data.put("id", unitId);
            response.status(HttpStatus.CREATED_201);
            return JSONHelper.dataToJson(data);
        });

    }

}
