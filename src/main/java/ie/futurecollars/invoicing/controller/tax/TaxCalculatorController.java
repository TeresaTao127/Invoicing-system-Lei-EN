package ie.futurecollars.invoicing.controller.tax;

import ie.futurecollars.invoicing.model.Company;
import ie.futurecollars.invoicing.service.TaxCalculatorResult;
import ie.futurecollars.invoicing.service.TaxCalculatorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TaxCalculatorController implements TaxCalculatorApi {

  private final TaxCalculatorService taxService;

  @Override
  public TaxCalculatorResult calculateTaxes(@RequestBody Company company) {
    return taxService.calculateTaxes(company);
  }
}
