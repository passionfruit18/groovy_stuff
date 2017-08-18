package sqlStuff

import groovy.sql.Sql
import spock.lang.Specification

/**
 * Created by james on 27/07/2017.
 */
class PlayWithMcSpec extends Specification {
    Sql connection = Sql.newInstance("jdbc:mysql://localhost:3306/nhsref?autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8", "metadata", "metadata", "com.mysql.jdbc.Driver")
    //@Ignore
    def "spec as main: play with mc"() {
        when:
        PlayWithMc playWithMc = new PlayWithMc(connection)
        playWithMc.select(100)
        then:
        assert false
    }
}
