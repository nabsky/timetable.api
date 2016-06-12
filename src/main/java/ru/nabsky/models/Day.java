package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties({"type", "_rev"})
@EqualsAndHashCode(callSuper = true)
public class Day extends CouchDBModel {
    @SuppressWarnings("unused")
    private final String Type = "Day";

    private Integer[] date;

    private Integer[] late;
    private Integer[] start;

    private Integer[] workTime;
    private Integer[] breakTime;

    private boolean fault;

    @NotNull(message = "MateId cannot be empty")
    @NotEmpty(message = "MateId cannot be empty")
    private String mateId;
}
