package ie.futurecollars.invoicing.db.jpa;

import ie.futurecollars.invoicing.model.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {

}
