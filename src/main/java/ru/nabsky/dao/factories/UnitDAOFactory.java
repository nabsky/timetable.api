package ru.nabsky.dao.factories;

import ru.nabsky.dao.UnitDAO;

public interface UnitDAOFactory {
    UnitDAO create(String teamName);
}
