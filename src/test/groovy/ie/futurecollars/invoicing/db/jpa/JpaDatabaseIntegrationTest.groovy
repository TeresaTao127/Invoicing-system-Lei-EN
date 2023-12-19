package ie.futurecollars.invoicing.db.jpa

import ie.futurecollars.invoicing.db.AbstractDatabaseTest
import ie.futurecollars.invoicing.db.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.IfProfileValue

@DataJpaTest
@IfProfileValue(name = "spring.profiles.active", value = "jpa")
class JpaDatabaseIntegrationTest extends AbstractDatabaseTest {

    @Autowired
    private InvoiceRepository invoiceRepository

    @Override
    Database getDatabaseInstance() {
        assert invoiceRepository != null
        new JpaDatabase(invoiceRepository)
    }

}