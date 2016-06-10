package ru.nabsky.dao.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.extern.java.Log;
import ru.nabsky.dao.TimeDAO;
import ru.nabsky.models.Time;

import java.util.List;

@Log
public class TimeDAOCouchDBImpl extends CommonDAOCouchDBImpl<Time> implements TimeDAO {

    @Inject
    public TimeDAOCouchDBImpl(@Assisted String teamName) {
        super(teamName);
    }

    @Override
    public List<Time> findByDayId(String dayId) {
        List<Time> times = getConnection().view("times/by_day")
                .key(dayId)
                .includeDocs(true)
                .query(Time.class);
        closeConnection();
        return times;
    }

    @Override
    public List<Time> findByStart(Long from, Long to) {
        List<Time> times = getConnection().view("times/by_start")
                .startKey(from)
                .endKey(to)
                .includeDocs(true)
                .query(Time.class);
        closeConnection();
        return times;
    }


}
