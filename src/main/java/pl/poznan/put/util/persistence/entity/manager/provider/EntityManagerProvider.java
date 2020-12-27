package pl.poznan.put.util.persistence.entity.manager.provider;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class EntityManagerProvider {
    private static EntityManager entityManager;

    public static EntityManager getEntityManager() {
        var em = entityManager;
        if (em != null) return em;
        synchronized (EntityManagerProvider.class) {
            if (entityManager != null) return entityManager;
            var url = EntityManagerProvider.class.getResource("user.properties");
            try (var input = url.openStream()) {
                var properties = new Properties();
                properties.load(input);
                return entityManager = Persistence.createEntityManagerFactory("data", properties)
                                                  .createEntityManager();
            }
            catch (IOException e) {
                log.error("An error occurred", e);
                return null;
            }
        }
    }
}
