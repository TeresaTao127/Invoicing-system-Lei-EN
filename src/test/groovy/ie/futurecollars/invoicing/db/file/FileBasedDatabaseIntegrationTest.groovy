package ie.futurecollars.invoicing.db.file

import ie.futurecollars.invoicing.db.AbstractDatabaseTest
import ie.futurecollars.invoicing.db.Database
import ie.futurecollars.invoicing.helpers.TestHelpers
import ie.futurecollars.invoicing.model.Invoice
import ie.futurecollars.invoicing.utils.FilesService
import ie.futurecollars.invoicing.utils.JsonService

import java.nio.file.Files
import java.nio.file.Path

class FileBasedDatabaseIntegrationTest extends AbstractDatabaseTest {

    def dbPath

    @Override
    Database getDatabaseInstance() {
        def filesService = new FilesService()

        def idPath = File.createTempFile('ids', '.txt').toPath()
        def idProvider = new IdProvider(idPath, filesService)

        dbPath = File.createTempFile('invoices', '.txt').toPath()
        return new FileBasedDatabase<>(dbPath, idProvider, filesService, new JsonService(), Invoice)
    }

    def "file based database writes invoices to correct file"() {
        given:
        def db = getDatabaseInstance()

        when:
        db.save(TestHelpers.invoice(4))

        then:
        Files.readAllLines(dbPath).size()==1

        when:
        db.save(TestHelpers.invoice(5))

        then:
        Files.readAllLines(dbPath).size()==2
    }
}