package ru.nabsky.services;

import ru.nabsky.models.Team;

public interface TeamService {

    String create(Team team);

    boolean exists(String name);
}
