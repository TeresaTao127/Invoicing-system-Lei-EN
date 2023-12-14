package ie.futurecollars.invoicing.db.jpa;

import ie.futurecollars.invoicing.model.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

}
