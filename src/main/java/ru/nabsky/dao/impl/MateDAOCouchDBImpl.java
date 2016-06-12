package ru.nabsky.dao.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import lombok.extern.java.Log;
import ru.nabsky.dao.MateDAO;
import ru.nabsky.models.Mate;

import java.util.List;

@Log
public class MateDAOCouchDBImpl extends CommonDAOCouchDBImpl<Mate> implements MateDAO {

    @Inject
    public MateDAOCouchDBImpl(@Assisted String teamName) {
        super(teamName);
    }

    @Override
    public List<Mate> findByUnitId(String unitId) {
        List<Mate> mates = getConnection().view("mates/by_unit")
                .key(unitId)
                .includeDocs(true)
                .query(Mate.class);
        closeConnection();
        return mates;
    }

    @Override
    public List<Mate> findAllByName() {
        List<Mate> mates = getConnection().view("mates/by_name")
                .includeDocs(true)
                .query(Mate.class);
        closeConnection();
        return mates;
    }
}
