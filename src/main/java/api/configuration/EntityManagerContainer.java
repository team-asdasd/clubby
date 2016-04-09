package api.configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;

public class EntityManagerContainer {
    private static HashMap<String, Object> properties;
    private static EntityManagerFactory emf;

    public static EntityManager getEntityManager() {
        initialize();

        return emf.createEntityManager();
    }

    public static void initialize() {
        if (properties == null) {
            properties = new HashMap<>();
            properties.put("hibernate.connection.username", System.getenv().get("OPENSHIFT_POSTGRESQL_DB_USERNAME"));
            properties.put("hibernate.connection.password", System.getenv().get("OPENSHIFT_POSTGRESQL_DB_PASSWORD"));
        }

        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("clubbyPersistenceUnit", properties);
        }
    }
}
