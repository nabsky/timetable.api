package ru.nabsky.services.impl;

import com.google.inject.Inject;
import ru.nabsky.dao.TeamDAO;
import ru.nabsky.dao.TokenDAO;
import ru.nabsky.dao.UnitDAO;
import ru.nabsky.dao.factories.UnitDAOFactory;
import ru.nabsky.dao.impl.TeamDAOCouchDBImpl;
import ru.nabsky.helper.DatabaseHelper;
import ru.nabsky.helper.SecurityHelper;
import ru.nabsky.models.Team;
import ru.nabsky.models.Token;
import ru.nabsky.models.Unit;
import ru.nabsky.services.TeamService;

import java.util.List;

public class TeamServiceImpl implements TeamService{

    @Inject
    private TeamDAO teamDAO;
    @Inject
    private TokenDAO tokenDAO;
    @Inject
    private UnitDAOFactory unitDAOFactory;


    @Override
    public String createTeam(Team team) {
        team.setMatePassword(SecurityHelper.getSecret(team.getMatePassword()));
        team.setLeadPassword(SecurityHelper.getSecret(team.getLeadPassword()));
        return teamDAO.insert(team);
    }

    @Override
    public String createUnit(Team team, Unit unit) {
        UnitDAO unitDAO = unitDAOFactory.create(team.getName());
        String unitId = unitDAO.insert(unit);
        return unitId;
    }

    @Override
    public boolean isTeamExists(String name) {
        Team team = teamDAO.findByName(name);
        return team != null;
    }

    @Override
    public Team getTeamByTokenId(String tokenId) {
        Token token = tokenDAO.findById(tokenId);
        Team team = teamDAO.findById(token.getTeamId());
        return team;
    }

    @Override
    public List<Unit> getUnits(Team team) {
        UnitDAO unitDAO = unitDAOFactory.create(team.getName());
        List<Unit> units = unitDAO.findAll();
        return units;
    }
}
