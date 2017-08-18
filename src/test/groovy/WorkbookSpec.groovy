import com.sun.xml.internal.xsom.visitor.XSSimpleTypeFunction
import org.apache.poi.ss.usermodel.Workbook
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * Created by james on 18/08/2017.
 */
class WorkbookSpec extends Specification {
    @Rule TemporaryFolder temporaryFolder = new TemporaryFolder()
    def "workbook creation"() {
        setup:
        File tempFile = temporaryFolder.newFile("tempFile${System.currentTimeMillis()}.xlsx")
        //Workbook workbook = new XSSFWorkbook
    }
}
