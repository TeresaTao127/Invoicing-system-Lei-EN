package ie.futurecollars.invoicing.db.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.IfProfileValue
import ie.futurecollars.invoicing.db.AbstractDatabaseTest
import ie.futurecollars.invoicing.db.Database

@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "mongo")
class MongoDatabaseIntegrationTest extends AbstractDatabaseTest {

    @Autowired
    private MongoBasedDatabase mongoDatabase

    @Override
    Database getDatabaseInstance() {
        assert mongoDatabase != null
        mongoDatabase
    }

}
