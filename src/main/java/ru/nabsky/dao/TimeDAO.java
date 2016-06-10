package ru.nabsky.dao;

import ru.nabsky.models.Time;

import java.util.List;

public interface TimeDAO extends CommonDAO<Time>{
    List<Time> findByDayId(String dayId);
    List<Time> findByStart(Long from, Long to);
}
