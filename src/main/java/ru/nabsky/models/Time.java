package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties({"type", "_rev"})
@EqualsAndHashCode(callSuper = true)
public class Time extends CouchDBModel {
    public enum TimeMode {
        WORK, BREAK
    }
    @SuppressWarnings("unused")
    private final String Type = "Time";

    @NotNull(message = "Start cannot be empty")
    private Integer[] start;
    private Integer[] length;

    private Integer[] late;
    private boolean first;
    private boolean fault;

    private TimeMode mode;

    @NotNull(message = "MateId cannot be empty")
    @NotEmpty(message = "MateId cannot be empty")
    private String mateId;

    private String photoStart;
    private String photoEnd;
}
