package ie.futurecollars.invoicing.db.mongo;

import com.mongodb.client.MongoCollection;
import ie.futurecollars.invoicing.db.Database;
import ie.futurecollars.invoicing.model.Invoice;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.bson.Document;

@AllArgsConstructor
public class MongoBasedDatabase implements Database {

  private final MongoCollection<Invoice> invoices;
  private final MongoIdProvider idProvider;

  @Override
  public long save(Invoice invoice) {
    invoice.setId(idProvider.getNextIdAndIncrement());
    invoices.insertOne(invoice);

    return invoice.getId();
  }

  @Override
  public Optional<Invoice> getById(long id) {
    return Optional.ofNullable(
        invoices.find(idFilter(id)).first()
    );
  }

  @Override
  public List<Invoice> getAll() {
    return StreamSupport
        .stream(invoices.find().spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Invoice> update(long id, Invoice updatedInvoice) {
    updatedInvoice.setId(id);
    return Optional.ofNullable(
        invoices.findOneAndReplace(idFilter(id), updatedInvoice)
    );
  }

  @Override
  public Optional<Invoice> delete(long id) {
    return Optional.ofNullable(
        invoices.findOneAndDelete(idFilter(id))
    );
  }

  private Document idFilter(long id) {
    return new Document("_id", id);
  }
}
