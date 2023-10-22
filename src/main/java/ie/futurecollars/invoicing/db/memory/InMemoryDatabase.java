package ie.futurecollars.invoicing.db.memory;

import ie.futurecollars.invoicing.db.Database;
import ie.futurecollars.invoicing.model.Invoice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryDatabase implements Database {

  private final Map<Integer, Invoice> invoices = new HashMap<>();
  private int nextId = 1;

  @Override
  public int save(Invoice invoice) {
    invoice.setId(nextId);
    invoices.put(nextId, invoice);
    return nextId++;
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return Optional.ofNullable(invoices.get(id));
  }

  @Override
  public List<Invoice> getAll() {
    return new ArrayList<>(invoices.values());
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {
    if (!invoices.containsKey(id)) {
      throw new IllegalArgumentException("Id " + id + " does not exist");
    }
    updatedInvoice.setId(id);
    invoices.put(id, updatedInvoice);
  }

  @Override
  public void delete(int id) {
    invoices.remove(id);
  }
}
