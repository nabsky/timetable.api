package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import spark.utils.StringUtils;

@Data
@JsonIgnoreProperties({"type", "_rev", "valid"})
public class Team {
    @SuppressWarnings("unused")
    private final String Type = "Team";

    @JsonProperty("id")
    private String _id;
    private String _rev;

    private String name;
    private String email;

    private String startTime;
    private String workLength;
    private String breakLength;

    private Boolean checkBreak;
    private Boolean checkSick;
    private Boolean checkTrip;
    private Boolean checkRest;
    private Boolean checkVK;

    private String matePassword;
    private String leadPassword;

    public ValidationResult validate() {
        if(StringUtils.isEmpty(name)){
            return new ValidationResult("Team name cannot be empty");
        }

        if(StringUtils.isEmpty(matePassword)){
            return new ValidationResult("User password cannot be empty");
        }

        if(StringUtils.isEmpty(leadPassword)){
            return new ValidationResult("Admin password cannot be empty");
        }

        if(leadPassword.equals(matePassword)){
            return new ValidationResult("User and Admin passwords cannot be equal");
        }

        if(!name.equals(name.toLowerCase())){
            return new ValidationResult("Team name must be in lower case");
        }

        return new ValidationResult();
    }
}
