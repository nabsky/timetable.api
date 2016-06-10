package ru.nabsky.helper;

import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.lightcouch.DesignDocument;

import java.util.Iterator;
import java.util.List;

public class DatabaseHelper {

    public static void updateDesignDocuments(){
        updateDesignDocuments(null);
    }

    public static void updateDesignDocuments(String database) {
        CouchDbClient couchDbClient = null;
        List<DesignDocument> designDocuments = null;
        if (database == null) {
            couchDbClient = new CouchDbClient();
            designDocuments = couchDbClient.design().getAllFromDesk();
            for (Iterator<DesignDocument> iterator = designDocuments.iterator(); iterator.hasNext(); ) {
                DesignDocument designDocument = iterator.next();
                if (!designDocument.getId().endsWith("teams") && !designDocument.getId().endsWith("tokens")) {
                    iterator.remove();
                }
            }
        } else {
            CouchDbProperties properties = new CouchDbProperties()
                    .setDbName(database)
                    .setCreateDbIfNotExist(true)
                    .setProtocol("http")
                    .setHost("127.0.0.1")
                    .setPort(5984)
                    .setMaxConnections(1000)
                    .setConnectionTimeout(0);
            couchDbClient = new CouchDbClient(properties);
            designDocuments = couchDbClient.design().getAllFromDesk();
            for (Iterator<DesignDocument> iterator = designDocuments.iterator(); iterator.hasNext(); ) {
                DesignDocument designDocument = iterator.next();
                if (designDocument.getId().endsWith("teams") || designDocument.getId().endsWith("tokens")) {
                    iterator.remove();
                }
            }
        }

        for (DesignDocument designDocument : designDocuments) {
            couchDbClient.design().synchronizeWithDb(designDocument);
        }
        couchDbClient.shutdown();
    }

}
