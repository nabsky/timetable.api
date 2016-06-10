package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import spark.utils.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties({"type", "_rev", "valid"})
public class Unit {
    @SuppressWarnings("unused")
    private final String Type = "Unit";

    @JsonProperty("id")
    private String _id;
    private String _rev;

    @NotNull(message="Name cannot be empty")
    @NotBlank(message="Name cannot be empty")
    private String name;

    private String startTime;
    private String workLength;
    private String breakLength;

}
