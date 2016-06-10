package ru.nabsky.dao;

import ru.nabsky.models.Day;

import java.util.Date;
import java.util.List;

public interface DayDAO extends CommonDAO<Day>{
    List<Day> findByDate(Date date);
}
