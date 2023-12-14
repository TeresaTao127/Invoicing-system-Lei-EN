package ie.futurecollars.invoicing.db;

import ie.futurecollars.invoicing.db.file.FileBasedDatabase;
import ie.futurecollars.invoicing.db.file.IdProvider;
import ie.futurecollars.invoicing.db.jpa.InvoiceRepository;
import ie.futurecollars.invoicing.db.jpa.JpaDatabase;
import ie.futurecollars.invoicing.db.memory.InMemoryDatabase;
import ie.futurecollars.invoicing.db.sql.SqlDatabase;
import ie.futurecollars.invoicing.utils.FilesService;
import ie.futurecollars.invoicing.utils.JsonService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
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
}
