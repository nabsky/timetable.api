package ru.nabsky.services;

import ru.nabsky.models.Mate;
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

    void deleteUnit(Team team, Unit unit);

    String createMate(Team team, Mate mate);

    List<Mate> getMates(Team team);

    List<Mate> getMates(Team team, Integer page, Integer perPage);

    Mate findMate(Team team, String mateId);

    Mate updateMate(Team team, Mate mate);

    void deleteMate(Team team, Mate mate);
}
