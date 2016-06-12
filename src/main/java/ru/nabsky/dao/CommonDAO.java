package ru.nabsky.dao;

import java.util.List;

public interface CommonDAO<T> {

    String insert(T object);

    T findById(String id);

    void update(T object);

    void delete(T object);

    List<T> findAll(Integer limit, Integer skip);

    Integer getTotalCount();

}
