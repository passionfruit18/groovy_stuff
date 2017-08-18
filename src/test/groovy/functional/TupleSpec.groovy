package functional

import io.vavr.Tuple2 as VavrTuple2
import org.jooq.lambda.tuple.Tuple2 as JooqTuple2
import groovy.lang.Tuple2 as GroovyTuple2
import scala.Tuple2 as ScalaTuple2
import scala.Tuple5 as ScalaTuple5
import spock.lang.Specification

/**
 * Created by james on 08/08/2017.
 */
class TupleSpec extends Specification {
    def "try jooq tuple"() {
        // max 16
        setup:
        JooqTuple2<String, Integer> t = new JooqTuple2('Hello', 3)
        expect:
        t.v1() == 'Hello'
        t.v2() == 3

    }
    def "try groovy tuple"() {
        // max 2 !?
        setup:
        GroovyTuple2<String, Integer> t = new GroovyTuple2('Hello', 3)
        expect:
        t.first == 'Hello'
        t.second == 3
    }
    def "try scala tuple"() {
        //max 22
        setup:
        ScalaTuple2<String, Integer> t = new ScalaTuple2('Hello', 3)
        ScalaTuple5<String, Integer, String, Integer, String> tuple5 = new ScalaTuple5('H', 1, 'E', 3, 'L')
        expect:
        tuple5._3() == 'E'
        t._1() == 'Hello'
    }
    def "try vavr tuple"() {
        // max 8
        setup:
        VavrTuple2<String, Integer> t = new VavrTuple2('Hello', 3)
        expect:
        t.map1({x->'x'+x}) == new VavrTuple2('xHello', 3)
        t.apply({x,y -> "$x $y"}) == 'Hello 3'
        t._1 == 'Hello'

        when:
        t._1 = 'Blah'
        then:
        thrown(ReadOnlyPropertyException)

    }

    def "try Apache Commons Lang tuple"() {
    }
}
