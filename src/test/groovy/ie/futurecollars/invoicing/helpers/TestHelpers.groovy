package ie.futurecollars.invoicing.helpers

import ie.futurecollars.invoicing.model.Company
import ie.futurecollars.invoicing.model.Invoice
import ie.futurecollars.invoicing.model.InvoiceEntry
import ie.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(int id) {
        new Company(("$id").repeat(10),
                "ul, Polska",
                "iCode");
    }

    static product(int id) {
        new InvoiceEntry("Programming course $id", 1, BigDecimal.valueOf(id * 1000), BigDecimal.valueOf(id * 1000 * 0.08), Vat.VAT_8)
    }

    static invoice(int id) {
        new Invoice(LocalDate.now(), company(id), company(id), List.of(product(id)))
    }
}