package ru.nabsky.services;

import ru.nabsky.models.Team;
import ru.nabsky.models.Unit;

import java.util.List;

public interface TeamService {

    String createTeam(Team team);
    String createUnit(Team team, Unit unit);

    boolean isTeamExists(String name);

    Team getTeamByTokenId(String tokenId);

    List<Unit> getUnits(Team team);

    Unit findUnit(Team team, String id);

    Unit updateUnit(Team team, Unit unit);
}
