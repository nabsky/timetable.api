package ru.nabsky.dao.impl;

import lombok.extern.java.Log;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.NoDocumentException;
import ru.nabsky.dao.CommonDAO;

import java.lang.reflect.ParameterizedType;
import java.util.logging.Level;

@Log
public abstract class CommonDAOCouchDBImpl<T> implements CommonDAO<T> {

    protected CouchDbProperties databaseProperties = null;
    protected String databaseName;
    public final static String DEFAULT_DATABASE = "timetable";

    private CouchDbClient connection = null;

    public CommonDAOCouchDBImpl() {
        this(DEFAULT_DATABASE);
    }

    public CommonDAOCouchDBImpl(String databaseName) {
        this.databaseName = databaseName;
        databaseProperties = new CouchDbProperties()
                .setDbName(databaseName)
                .setCreateDbIfNotExist(true)
                .setProtocol("http")
                .setHost("127.0.0.1")
                .setPort(5984)
                .setMaxConnections(1000)
                .setConnectionTimeout(0);
    }

    public CouchDbClient getConnection() {
        if (connection == null) {
            connection = new CouchDbClient(databaseProperties);
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            connection.shutdown();
            connection = null;
        }
    }

    @SuppressWarnings("unchecked")
    private Class<T> getGenericTypeClass() {
        try {
            String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            Class<?> clazz = Class.forName(className);
            return (Class<T>) clazz;
        } catch (Exception e) {
            throw new IllegalStateException("Class is not parametrized with generic type!!! Please use extends <> ");
        }
    }

    @Override
    public String insert(T object) {
        String id = getConnection().save(object).getId();
        closeConnection();
        return id;
    }

    @Override
    public T findById(String id) {
        T object = null;
        try {
            object = getConnection().find(getGenericTypeClass(), id);
        } catch (NoDocumentException e) {
            log.log(Level.WARNING, String.format("%s with id = %s is not found", getGenericTypeClass().getCanonicalName(), id));
        } finally {
            closeConnection();
        }
        return object;
    }

    @Override
    public void update(T object) {
        getConnection().update(object);
        closeConnection();
    }

    @Override
    public void delete(T object) {
        getConnection().remove(object);
        closeConnection();
    }
}
