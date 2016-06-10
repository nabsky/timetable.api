package ru.nabsky.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties({"type", "_rev"})
public class Day {
    @SuppressWarnings("unused")
    private final String Type = "Day";

    @JsonProperty("id")
    private String _id;
    private String _rev;

    private Integer[] date;

    private Long secondsLate;
    private Long secondsFromDayStart;

    private Long workTime;
    private Long breakTime;
    private Long sickTime;
    private Long tripTime;
    private Long restTime;

    private boolean fault;

    private String mateId;

    public void addTimeLength(Time time) {
        long length = time.getEnd() - time.getStart();
        switch (time.getMode()){
            case WORK:
                if(workTime == null){
                    workTime = length;
                } else {
                    workTime += length;
                }
                break;
            case BREAK:
                if(breakTime == null){
                    breakTime = length;
                } else {
                    breakTime += length;
                }
                break;
            case SICK:
                if(sickTime == null){
                    sickTime = length;
                } else {
                    sickTime += length;
                }
                break;
            case TRIP:
                if(tripTime == null){
                    tripTime = length;
                } else {
                    tripTime += length;
                }
                break;
            case REST:
                if(restTime == null){
                    restTime = length;
                } else {
                    restTime += length;
                }
                break;
        }
    }
}
