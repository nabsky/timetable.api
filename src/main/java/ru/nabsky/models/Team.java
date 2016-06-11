package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties({"type", "valid", "_rev"})
@EqualsAndHashCode(callSuper = true)
public class Team extends CouchDBModel {
    @SuppressWarnings("unused")
    private final String Type = "Team";

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
