package ru.nabsky.dao;

import ru.nabsky.models.Team;

public interface TeamDAO extends CommonDAO<Team>{

    Team findByName(String name);

}
