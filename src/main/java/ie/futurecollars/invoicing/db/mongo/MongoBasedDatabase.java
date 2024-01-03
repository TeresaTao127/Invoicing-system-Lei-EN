package ie.futurecollars.invoicing.db.mongo;

import com.mongodb.client.MongoCollection;
import ie.futurecollars.invoicing.db.Database;
import ie.futurecollars.invoicing.model.WithId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.bson.Document;

@AllArgsConstructor
public class MongoBasedDatabase<T extends WithId> implements Database<T> {

  private final MongoCollection<T> items;
  private final MongoIdProvider idProvider;

  @Override
  public long save(T item) {
    item.setId(idProvider.getNextIdAndIncrement());
    items.insertOne(item);

    return item.getId();
  }

  @Override
  public Optional<T> getById(long id) {
    return Optional.ofNullable(
        items.find(idFilter(id)).first()
    );
  }

  @Override
  public List<T> getAll() {
    return StreamSupport
        .stream(items.find().spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<T> update(long id, T updatedItem) {
    updatedItem.setId(id);
    return Optional.ofNullable(
        items.findOneAndReplace(idFilter(id), updatedItem)
    );
  }

  @Override
  public Optional<T> delete(long id) {
    return Optional.ofNullable(
        items.findOneAndDelete(idFilter(id))
    );
  }

  private Document idFilter(long id) {
    return new Document("_id", id);
  }
}

