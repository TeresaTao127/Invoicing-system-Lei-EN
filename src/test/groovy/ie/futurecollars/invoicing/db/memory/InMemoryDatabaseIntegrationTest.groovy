package ie.futurecollars.invoicing.db.memory

import ie.futurecollars.invoicing.db.AbstractDatabaseTest
import ie.futurecollars.invoicing.db.Database

class InMemoryDatabaseIntegrationTest extends AbstractDatabaseTest {

    @Override
    Database getDatabaseInstance() {
        return new InMemoryDatabase()
    }
}