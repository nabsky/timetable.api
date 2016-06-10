package ru.nabsky.dao.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.extern.java.Log;
import ru.nabsky.dao.DayDAO;
import ru.nabsky.helper.DayHelper;
import ru.nabsky.models.Day;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Log
public class DayDAOCouchDBImpl extends CommonDAOCouchDBImpl<Day> implements DayDAO {

    @Inject
    public DayDAOCouchDBImpl(@Assisted String teamName) {
        super(teamName);
    }


    @Override
    public List<Day> findByDate(Date date) {
        List<Day> dayList = Collections.emptyList();
        Integer[] dateArray = DayHelper.dateToArray(date);
        try {
            dayList = getConnection().view("days/by_date")
                    .key(dateArray)
                    .includeDocs(true)
                    .query(Day.class);
        } catch (Exception e) {
            log.warning("No day info found for " + Arrays.toString(dateArray));
        } finally {
            closeConnection();
        }
        return dayList;
    }
}
