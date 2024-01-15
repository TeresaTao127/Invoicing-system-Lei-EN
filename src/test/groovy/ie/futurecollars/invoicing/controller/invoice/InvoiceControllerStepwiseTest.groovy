package ie.futurecollars.invoicing.controller.invoice

import ie.futurecollars.invoicing.db.Database
import ie.futurecollars.invoicing.helpers.TestHelpers
import ie.futurecollars.invoicing.model.Invoice
import ie.futurecollars.invoicing.utils.JsonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDate

import static ie.futurecollars.invoicing.helpers.TestHelpers.resetIds
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
@Stepwise
class InvoiceControllerStepwiseTest extends Specification {

    private final Invoice originalInvoice = TestHelpers.invoice(1)

    private final LocalDate updatedDate = LocalDate.of(2020, 02, 28)

    @Shared
    private int invoiceId

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Autowired
    private Database<Invoice> database

    def "database is reset to ensure clean state"() {
        expect:
        database != null

        when:
        database.reset()

        then:
        database.getAll().size() == 0
    }

    def "empty array is returned when no invoices were added"() {
        when:
        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        response == "[]"
    }

    def "add single invoice"() {
        given:
        def invoiceAsJson = jsonService.toJson(originalInvoice)

        when:
        invoiceId = Integer.valueOf(
                mockMvc.perform(
                        post("/invoices")
                                .content(invoiceAsJson)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn()
                        .response
                        .contentAsString
        )

        then:
        invoiceId > 0
    }

    def "one invoice is returned when getting all invoices"() {
        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.id = invoiceId

        when:
        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonService.toObject(response, Invoice[])

        then:
        invoices.size() == 1
        resetIds(invoices[0]) == resetIds(expectedInvoice)
    }

    def "invoice is returned correctly when getting by id"() {
        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.id = invoiceId

        when:
        def response = mockMvc.perform(get("/invoices/$invoiceId"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoice = jsonService.toObject(response, Invoice)

        then:
        resetIds(invoice) == resetIds(expectedInvoice)
    }

    def "invoice date can be modified"() {
        given:
        def modifiedInvoice = originalInvoice
        modifiedInvoice.date = updatedDate

        def invoiceAsJson = jsonService.toJson(modifiedInvoice)

        expect:
        mockMvc.perform(
                put("/invoices/$invoiceId")
                        .content(invoiceAsJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        )
                .andDo(print())
                .andExpect(status().isNoContent())
    }

    def "updated invoice is returned correctly when getting by id"() {
        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.id = invoiceId
        expectedInvoice.date = updatedDate

        when:
        def response = mockMvc.perform(get("/invoices/$invoiceId"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoice = jsonService.toObject(response, Invoice)

        then:
        resetIds(invoice) == resetIds(expectedInvoice)
    }

    def "invoice can be deleted"() {
        expect:
        mockMvc.perform(delete("/invoices/$invoiceId").with(csrf()))
                .andExpect(status().isNoContent())

        and:
        mockMvc.perform(delete("/invoices/$invoiceId").with(csrf()))
                .andExpect(status().isNotFound())

        and:
        mockMvc.perform(get("/invoices/$invoiceId").with(csrf()))
                .andExpect(status().isNotFound())
    }

}
