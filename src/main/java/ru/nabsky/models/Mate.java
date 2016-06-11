package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties({"type", "_rev"})
@EqualsAndHashCode(callSuper = true)
public class Mate extends CouchDBModel {
    @SuppressWarnings("unused")
    private final String Type = "Mate";

    @NotNull(message = "Name cannot be empty")
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Surname cannot be empty")
    @NotEmpty(message = "Surname cannot be empty")
    private String surname;

    private String birthday;
    private String phone;
    private String email;
    private String vkId;

    private String startTime;
    private String workLength;
    private String breakLength;

    private String unitId;

}
