package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import spark.utils.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties({"type", "_rev", "valid"})
public class Team {
    @SuppressWarnings("unused")
    private final String Type = "Team";

    @JsonProperty("id")
    private String _id;
    private String _rev;

    @NotNull(message = "Name cannot be empty")
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Email cannot be empty")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    private String startTime;
    private String workLength;
    private String breakLength;

    private Boolean checkBreak;
    private Boolean checkSick;
    private Boolean checkTrip;
    private Boolean checkRest;
    private Boolean checkVK;

    @NotNull(message = "User password cannot be empty")
    @NotEmpty(message = "User password cannot be empty")
    private String matePassword;
    @NotNull(message = "Admin password cannot be empty")
    @NotEmpty(message = "Admin password cannot be empty")
    private String leadPassword;

    @AssertTrue(message = "User and Admin passwords cannot be equal")
    private boolean isValid() {
        return leadPassword == null || !leadPassword.equals(matePassword);
    }
}
