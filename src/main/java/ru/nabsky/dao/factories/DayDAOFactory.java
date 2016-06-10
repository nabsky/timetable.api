package ru.nabsky.dao.factories;

import ru.nabsky.dao.DayDAO;

public interface DayDAOFactory {
    DayDAO create(String teamName);
}
