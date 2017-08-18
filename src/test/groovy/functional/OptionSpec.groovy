package functional
import io.vavr.control.Option
import spock.lang.Specification

class OptionSpec extends Specification {
    def "test option"() {
        when:
            Option<String> maybeFoo = Option.of('foo')
        then:
            assert maybeFoo.get() == 'foo'
        when:
            Option<String> maybeFooBar = maybeFoo.map({s -> (String)null})
            .flatMap({s -> Option.of(s)})
                .map({t -> t.toUpperCase() + "bar"})
        then:
            assert maybeFooBar.isEmpty()

    }
    def "test optional"() {
        when:
        Optional<String> x = Optional.of('Hello')
        then:
        assert x.flatMap({str -> Optional.of(str+' World')}).orElse('World') == 'Hello World'
    }
}
