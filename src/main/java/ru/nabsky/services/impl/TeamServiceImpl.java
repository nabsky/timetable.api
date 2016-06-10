package ru.nabsky.services.impl;

import com.google.inject.Inject;
import ru.nabsky.dao.TeamDAO;
import ru.nabsky.helper.DatabaseHelper;
import ru.nabsky.helper.SecurityHelper;
import ru.nabsky.models.Team;
import ru.nabsky.services.TeamService;

public class TeamServiceImpl implements TeamService{

    @Inject
    private
    TeamDAO teamDAO;

    @Override
    public String create(Team team) {
        team.setMatePassword(SecurityHelper.getSecret(team.getMatePassword()));
        team.setLeadPassword(SecurityHelper.getSecret(team.getLeadPassword()));
        return teamDAO.insert(team);
    }

    @Override
    public boolean exists(String name) {
        Team team = teamDAO.findByName(name);
        return team != null;
    }
}
