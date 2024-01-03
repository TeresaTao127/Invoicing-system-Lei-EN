package ie.futurecollars.invoicing.db.jpa;

import ie.futurecollars.invoicing.db.Database;
import ie.futurecollars.invoicing.model.Company;
import ie.futurecollars.invoicing.model.Invoice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "jpa")
public class JpaDatabaseConfiguration {

  @Bean
  public Database<Invoice> invoiceJpaDatabase(InvoiceRepository repository) {
    return new JpaDatabase<>(repository);
  }

  @Bean
  public Database<Company> companyJpaDatabase(CompanyRepository repository) {
    return new JpaDatabase<>(repository);
  }

}
