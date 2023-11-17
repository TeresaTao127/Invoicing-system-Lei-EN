package ie.futurecollars.invoicing.db;

import ie.futurecollars.invoicing.model.Invoice;
import java.util.List;
import java.util.Optional;

public interface Database {

  int save(Invoice invoice);

  Optional<Invoice> getById(int id);

  List<Invoice> getAll();

  Optional<Invoice> update(int id, Invoice updatedInvoice);

  Optional<Invoice> delete(int id);

}
