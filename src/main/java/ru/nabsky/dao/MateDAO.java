package ru.nabsky.dao;

import ru.nabsky.models.Mate;

import java.util.List;

public interface MateDAO extends CommonDAO<Mate>{

    List<Mate> findByUnitId(String unitId);

    List<Mate> findAllByName();

}
