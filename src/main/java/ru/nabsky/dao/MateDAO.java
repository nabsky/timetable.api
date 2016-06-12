package ru.nabsky.dao;

import ru.nabsky.models.Mate;

import java.util.List;

public interface MateDAO extends CommonDAO<Mate>{

    List<Mate> findAll();

    List<Mate> findByUnitId(String unitId);

    List<Mate> findAll(Integer limit, Integer skip);

}
