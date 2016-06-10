package ru.nabsky.dao;

import ru.nabsky.models.Unit;

import java.util.List;

public interface UnitDAO extends CommonDAO<Unit>{

    List<Unit> findAll();

}
