package pl.poznan.put.util.persistence.entity.manager.provider;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.Properties;

@Slf4j
@UtilityClass
public class EntityManagerProvider {
    private final String PROPERTIES  = "user.properties";
    private final String PERSISTENCE = "data";

    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        val em = entityManager;
        if (em != null) return em;
        synchronized (EntityManagerProvider.class) {
            if (entityManager != null) return entityManager;
            val url = EntityManagerProvider.class.getResource(PROPERTIES);
            try (val input = url.openStream()) {
                val properties = new Properties();
                properties.load(input);
                return entityManager = Persistence.createEntityManagerFactory(PERSISTENCE, properties)
                                                  .createEntityManager();
            }
            catch (IOException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
    }
}
