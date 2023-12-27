package ie.futurecollars.invoicing.db;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ie.futurecollars.invoicing.db.file.FileBasedDatabase;
import ie.futurecollars.invoicing.db.file.IdProvider;
import ie.futurecollars.invoicing.db.jpa.InvoiceRepository;
import ie.futurecollars.invoicing.db.jpa.JpaDatabase;
import ie.futurecollars.invoicing.db.memory.InMemoryDatabase;
import ie.futurecollars.invoicing.db.mongo.MongoBasedDatabase;
import ie.futurecollars.invoicing.db.mongo.MongoIdProvider;
import ie.futurecollars.invoicing.db.sql.SqlDatabase;
import ie.futurecollars.invoicing.model.Invoice;
import ie.futurecollars.invoicing.utils.FilesService;
import ie.futurecollars.invoicing.utils.JsonService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
public class DatabaseConfiguration {

  @Bean
  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
  public IdProvider idProvider(
      FilesService filesService,
      @Value("${invoicing-system.database.directory}") String databaseDirectory,
      @Value("${invoicing-system.database.id.file}") String idFile
  ) throws IOException {
    Path idFilePath = Files.createTempFile(databaseDirectory, idFile);
    return new IdProvider(idFilePath, filesService);
  }

  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
  @Bean
  public Database fileBasedDatabase(
      IdProvider idProvider,
      FilesService filesService,
      JsonService jsonService,
      @Value("${invoicing-system.database.directory}") String databaseDirectory,
      @Value("${invoicing-system.database.invoices.file}") String invoicesFile
  ) throws IOException {
    Path databaseFilePath = Files.createTempFile(databaseDirectory, invoicesFile);
    return new FileBasedDatabase(databaseFilePath, idProvider, filesService, jsonService);
  }

  @Bean
  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "sql")
  public Database sqlDatabase(JdbcTemplate jdbcTemplate) {
    return new SqlDatabase(jdbcTemplate);
  }

  @Bean
  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
  public Database inMemoryDatabase() {
    return new InMemoryDatabase();
  }

  @Bean
  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "jpa")
  public Database jpaDatabase(InvoiceRepository invoiceRepository) {
    return new JpaDatabase(invoiceRepository);
  }

  @Bean
  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
  public MongoDatabase mongoDb(
      @Value("${invoicing-system.database.name}") String databaseName
  ) {
    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    MongoClientSettings settings = MongoClientSettings.builder()
        .codecRegistry(pojoCodecRegistry)
        .build();

    MongoClient client = MongoClients.create(settings);
    return client.getDatabase(databaseName);
  }

  @Bean
  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
  public MongoIdProvider mongoIdProvider(
      @Value("${invoicing-system.database.counter.collection}") String collectionName,
      MongoDatabase mongoDb
  ) {
    MongoCollection<Document> collection = mongoDb.getCollection(collectionName);
    return new MongoIdProvider(collection);
  }

  @Bean
  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
  public Database mongoDatabase(
      @Value("${invoicing-system.database.collection}") String collectionName,
      MongoDatabase mongoDb,
      MongoIdProvider mongoIdProvider
  ) {
    MongoCollection<Invoice> collection = mongoDb.getCollection(collectionName, Invoice.class);
    return new MongoBasedDatabase(collection, mongoIdProvider);
  }

}
