package ru.nabsky.dao.factories;

import ru.nabsky.dao.MateDAO;

public interface MateDAOFactory {
    MateDAO create(String teamName);
}
