package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties({"type", "_rev"})
@EqualsAndHashCode(callSuper = true)
public class Unit extends CouchDBModel {
    @SuppressWarnings("unused")
    private final String Type = "Unit";

    @NotNull(message = "Name cannot be empty")
    @NotBlank(message = "Name cannot be empty")
    private String name;

    private String startTime;
    private String workLength;
    private String breakLength;

}
