package ru.nabsky.dao.factories;

import ru.nabsky.dao.TimeDAO;

public interface TimeDAOFactory {
    TimeDAO create(String teamName);
}
