package ru.nabsky.dao.impl;

import com.google.inject.Inject;
import com.sun.istack.internal.Nullable;
import lombok.extern.java.Log;
import ru.nabsky.dao.TeamDAO;
import ru.nabsky.models.Team;

import java.util.List;

@Log
public class TeamDAOCouchDBImpl extends CommonDAOCouchDBImpl<Team> implements TeamDAO {

    @Inject
    public TeamDAOCouchDBImpl() {
    }

    @Override
    @Nullable
    public Team findByName(String name) {
        List<Team> teams = getConnection().view("teams/by_name")
                .key(name)
                .includeDocs(true)
                .query(Team.class);
        closeConnection();
        if(teams.size() == 0){
            return null;
        } else {
            return teams.get(0);
        }
    }

}
