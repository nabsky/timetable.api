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

    private Long secondsLate;
    private Long secondsFromDayStart;

    private Long workTime;
    private Long breakTime;
    private Long sickTime;
    private Long tripTime;
    private Long restTime;

    private boolean fault;

    @NotNull(message = "MateId cannot be empty")
    @NotEmpty(message = "MateId cannot be empty")
    private String mateId;

    public void addTimeLength(Time time) {
        long length = time.getEnd() - time.getStart();
        switch (time.getMode()) {
            case WORK:
                if (workTime == null) {
                    workTime = length;
                } else {
                    workTime += length;
                }
                break;
            case BREAK:
                if (breakTime == null) {
                    breakTime = length;
                } else {
                    breakTime += length;
                }
                break;
            case SICK:
                if (sickTime == null) {
                    sickTime = length;
                } else {
                    sickTime += length;
                }
                break;
            case TRIP:
                if (tripTime == null) {
                    tripTime = length;
                } else {
                    tripTime += length;
                }
                break;
            case REST:
                if (restTime == null) {
                    restTime = length;
                } else {
                    restTime += length;
                }
                break;
        }
    }
}
