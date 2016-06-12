package ru.nabsky.services.impl;

import com.google.inject.Inject;
import ru.nabsky.dao.MateDAO;
import ru.nabsky.dao.TeamDAO;
import ru.nabsky.dao.TokenDAO;
import ru.nabsky.dao.UnitDAO;
import ru.nabsky.dao.factories.MateDAOFactory;
import ru.nabsky.dao.factories.UnitDAOFactory;
import ru.nabsky.helper.SecurityHelper;
import ru.nabsky.models.Mate;
import ru.nabsky.models.Team;
import ru.nabsky.models.Token;
import ru.nabsky.models.Unit;
import ru.nabsky.services.TeamService;

import java.util.List;

public class TeamServiceImpl implements TeamService {

    @Inject
    private TeamDAO teamDAO;
    @Inject
    private TokenDAO tokenDAO;
    @Inject
    private UnitDAOFactory unitDAOFactory;
    @Inject
    private MateDAOFactory mateDAOFactory;


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

    @Override
    public Unit findUnit(Team team, String id) {
        UnitDAO unitDAO = unitDAOFactory.create(team.getName());
        Unit unit = unitDAO.findById(id);
        return unit;
    }

    @Override
    public Unit updateUnit(Team team, Unit unit) {
        UnitDAO unitDAO = unitDAOFactory.create(team.getName());
        unitDAO.update(unit);
        unit = unitDAO.findById(unit.get_id());
        return unit;
    }

    @Override
    public void deleteUnit(Team team, Unit unit) {
        UnitDAO unitDAO = unitDAOFactory.create(team.getName());
        unitDAO.delete(unit);
    }

    @Override
    public String createMate(Team team, Mate mate) {
        MateDAO mateDAO = mateDAOFactory.create(team.getName());
        String mateId = mateDAO.insert(mate);
        return mateId;
    }

    @Override
    public List<Mate> getMates(Team team) {
        MateDAO mateDAO = mateDAOFactory.create(team.getName());
        List<Mate> mates = mateDAO.findAll();
        return mates;
    }

    @Override
    public List<Mate> getMates(Team team, Integer page, Integer perPage) {
        MateDAO mateDAO = mateDAOFactory.create(team.getName());
        Integer limit = perPage != null ? perPage : 20;
        Integer skip = page != null ? page * limit : 0;
        List<Mate> mates = mateDAO.findAll(limit, skip);
        return mates;
    }

    @Override
    public Mate findMate(Team team, String mateId) {
        MateDAO mateDAO = mateDAOFactory.create(team.getName());
        Mate mate = mateDAO.findById(mateId);
        return mate;
    }

    @Override
    public Mate updateMate(Team team, Mate mate) {
        MateDAO mateDAO = mateDAOFactory.create(team.getName());
        mateDAO.update(mate);
        mate = mateDAO.findById(mate.get_id());
        return mate;
    }

    @Override
    public void deleteMate(Team team, Mate mate) {
        MateDAO mateDAO = mateDAOFactory.create(team.getName());
        mateDAO.delete(mate);
    }
}
