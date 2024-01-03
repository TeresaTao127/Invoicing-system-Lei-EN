package ie.futurecollars.invoicing.controller.tax;

import ie.futurecollars.invoicing.model.Company;
import ie.futurecollars.invoicing.service.tax.TaxCalculatorResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "tax", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"tax-controller"})
public interface TaxCalculatorApi {

  @ApiOperation(value = "Get incomes, costs, vat and taxes to pay")
  @PostMapping
  TaxCalculatorResult calculateTaxes(@RequestBody Company company);

}
