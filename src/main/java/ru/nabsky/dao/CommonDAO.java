package ru.nabsky.dao;

public interface CommonDAO<T> {

    String insert(T object);

    T findById(String id);

    void update(T object);

    void delete(T object);

}
