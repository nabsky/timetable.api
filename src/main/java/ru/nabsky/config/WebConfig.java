package ru.nabsky.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Injector;
import org.eclipse.jetty.http.HttpStatus;
import ru.nabsky.helper.DatabaseHelper;
import ru.nabsky.helper.JSONHelper;
import ru.nabsky.models.Team;
import ru.nabsky.models.ValidationResult;
import ru.nabsky.services.TeamService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

public class WebConfig {

    private Injector injector;

    public WebConfig(Injector injector) {
        this.injector = injector;
        staticFileLocation("/public");
        setupRoutes();
    }

    private void setupRoutes() {

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

            if (team == null) {
                response.status(HttpStatus.UNPROCESSABLE_ENTITY_422);
                data.put("error", "Team data is not valid");
                return JSONHelper.dataToJson(data);
            }

            ValidationResult validationResult = team.validate();
            if(!validationResult.isValid()){
                data.put("error", validationResult.getErrorMessage());
                return JSONHelper.dataToJson(data);
            }

            TeamService teamService = injector.getInstance(TeamService.class);
            if (teamService.exists(team.getName())) {
                response.status(HttpStatus.CONFLICT_409);
                data.put("error", "Team name is already used");
                return JSONHelper.dataToJson(data);
            }

            String teamId = teamService.create(team);
            DatabaseHelper.updateDesignDocuments(team.getName());
            data.put("id", teamId);
            response.status(HttpStatus.CREATED_201);
            return JSONHelper.dataToJson(data);
        });
    }

}
