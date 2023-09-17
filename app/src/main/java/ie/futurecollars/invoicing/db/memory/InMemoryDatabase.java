package ie.futurecollars.invoicing.db.memory;

import ie.futurecollars.invoicing.db.Database;
import ie.futurecollars.invoicing.model.Invoice;
import java.util.List;
import java.util.Optional;


public class InMemoryDatabase implements Database {

  @Override
  public int save(Invoice invoice) {
    return 0;
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return Optional.empty();
  }

  @Override
  public List<Invoice> getAll() {
    return null;
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {

  }

  @Override
  public void delete(int id) {

  }
}


