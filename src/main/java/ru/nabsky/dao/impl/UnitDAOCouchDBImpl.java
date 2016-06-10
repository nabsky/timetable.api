package ru.nabsky.dao.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.extern.java.Log;
import ru.nabsky.dao.UnitDAO;
import ru.nabsky.models.Unit;

import java.util.List;

@Log
public class UnitDAOCouchDBImpl extends CommonDAOCouchDBImpl<Unit> implements UnitDAO {

    @Inject
    public UnitDAOCouchDBImpl(@Assisted String teamName) {
        super(teamName);
    }

    @Override
    public List<Unit> findAll() {
        List<Unit> units = getConnection().view("units/by_name")
                .includeDocs(true)
                .query(Unit.class);
        closeConnection();
        return units;
    }
}
