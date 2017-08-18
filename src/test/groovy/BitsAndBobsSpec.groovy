import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import java.nio.charset.StandardCharsets

/**
 * Created by james on 27/07/2017.
 */
class BitsAndBobsSpec extends Specification {
    def "numbers"() {
        expect:
        3%2 == 1
    }
    def "adding to list"() {
        when:
        def x = []
        x += [2,3,4]
        x += [5,6,7]
        then:
        assert x == [2,3,4,5,6,7]
        expect:
        [1] + [2] == [1,2]
    }
    public class Person {
        private String name
        public String blah(){"hello"}
    }
    def "public, private"() {
        when:
        def u = new Person(name:"Ron")
        then:
        assert u.name == 'Ron'
        assert u.blah() == 'hello'
    }
    @ToString(includes = ['id', 'title'])
    @EqualsAndHashCode(includes = 'id')
    @CompileStatic
    class Attribute {
        String prefix
        String id
        String url
        String title
        String firstLetter
    }
    def "some annotations"() {
        when:
        Attribute a = new Attribute()
        a.with {
            id = "3"
            title = "A"
            url = "a.com"
        }
        Attribute b = new Attribute()
        b.with {
            id = "3"
            title = "B"
            url = "b.com"
        }
        then:
        assert a==b
    }
    def "list stuff"() {

        expect:
        [].join('a') == ''
        [1,3,3,45,66].collate(2) == [[1, 3], [3, 45], [66]]

        [1,2,3].collectEntries{[blah:it+1]} == [blah:4]
        [1,2,3].collectEntries{[(it):it+1]} == [1:2, 2:3, 3:4]
        [1,2,3].collectEntries{[it:it+1]} == [it:4]

        [0,1].subList(0,1) == [0]
        (1..4) == [1,2,3,4]
        [[1,2], [1,3], [2,3]].groupBy({it[0]}) == [1: [[1,2], [1,3]], 2: [[2,3]]]
        [].isEmpty()
        [1].minus([1]).isEmpty()
    }
    class A {
        String toOverride() {"Hello"}
        String usingToOverride() {"${toOverride()}"+" World"}
    }
    class B extends A {
        @Override
        String toOverride() {"Goodbye"}
    }
    def "override"() {
        expect:
        new B().usingToOverride() == 'Goodbye World'
    }
    def "truth"() {
        expect:
        !(!true)
        !null
        !(0 as boolean)
        !([] as boolean)
        !(null as boolean)
    }
    class C {
        String blah(String x = "hello") { x }
    }
    def "default arg"() {
        expect:
        new C().blah() == "hello"
    }
    @Ignore
    def "read from console"() {
        when:
        /*def readln = javax.swing.JOptionPane.&showInputDialog
        def username = readln 'What is your name?'
        println "Hello $username."*/
        /*println "What is your name?"
        println "Your name is ${System.in.newReader().readLine()}"*/
        Scanner scanner = new Scanner(System.in);
//...
        String name = scanner.nextLine();
        println name
        then:
        assert true
    }
    def "string stuff"() {
        expect:
        "DATE".contains('DATE')
    }
    trait X {
        String x() {
            'Hello'
        }
    }
    trait Y extends X {
        String x() {
            super.x() + ' World'
        }
    }
    class Z implements Y {}
    def "trait stuff"() {
        expect:
        assert (new Z()).x() == 'Hello World'
    }
    class clA {
        String x() {
            'Hello'
        }
    }
    abstract class aclA extends clA {
        abstract String x()
    }
    class clB extends aclA {
        String x() {
            super.x() + ' World'
        }
    }
    def "class-> abstract class -> class"() {
        expect:
        (new clB()).x() == 'Hello World'
    }
    def "each map"() {
        setup:
        Closure c = {it}
        println c
        Map<String, Closure> m = ['Hello': {it} as Closure]
        m.each {k,v ->
            println k
        }
        println (m as String)
        expect:
        [:].collectEntries{k,v -> [(k), v]} == [:]
        ['hello':c] == ['hello':c]
        [1:2]+[2:3]==[1:2,2:3]
    }
    def "list minus"() {
        expect:
        ['a', 'b'] - ['a', 'b', 'c'] == []

        [1,2,3].dropWhile({it!=3}) == [3]
    }
    def "dropWhile"() {
        setup:
        String roles = ''
        String authority = 'CURATOR'
        if (authority in ['USER', 'CURATOR', 'ADMIN', 'SUPERVISOR']) {
            roles = ['ROLE_USER', 'ROLE_METADATA_CURATOR', 'ROLE_ADMIN', 'ROLE_SUPERVISOR'].dropWhile({
                !it.contains(authority)
            }).join(',')
        }
        expect:
        roles == "ROLE_METADATA_CURATOR,ROLE_ADMIN,ROLE_SUPERVISOR"
    }
    def "find"() {
        expect:
        'uclh_raw_import_from_cancer_v6.xls'.find(/(.*)_raw_import.*/) { match, firstWord -> firstWord } == 'uclh'
    }


    int sampleMethod() {
        return 3
    }
    boolean sampleField = true
    def "test spock stuff"() {
        sampleField = false
        expect:
        sampleMethod() == 3
        !sampleField
    }
    def "test sampleField not changed from previous"() {
        expect:
        sampleField
    }
    def "string as inputstream"() {
        expect:
        (new ByteArrayInputStream('e'.getBytes(StandardCharsets.UTF_8))).getText() == 'e'
    }
    class OuterClass {
        int x = 3
        class InnerClass {
            int y = 4
        }
    }
    def "inner class access"() {
        expect:
        (new OuterClass.InnerClass()).y == 4
    }
    String firstLowerCase(String s) {
        return (Character.toLowerCase(s.charAt(0)) as String) + s.substring(1)
    }
    def "some parsing"() {
        setup:
        def list1 = ['ID', 'Data Element', 'Multiplicity', 'Data Type',
         'Validation Rule', 'Business Rule', 'Related To', 'Source System', 'Previously In Source System']
        def list2 = ['Semantic Matching', 'Known Issue', 'Immediate Solution', 'Immediate Solution Owner',
         'Long Term Solution', 'Long Term Solution Owner', 'Data Item Unique Code',
         'Related To Metadata', 'Part Of Standard Data Set', 'Data Completeness',
         'Estimated Quality', 'Timely', 'Comments']
        list1.each{string ->
            //println "static String ${firstLowerCase(string.replace(' ', ''))} = '${string}'"
            print "${firstLowerCase(string.replace(' ', ''))}, "
        }
        expect:
        true
    }
    def "nextLine"() {
        Scanner scanner = new Scanner(System.in)
        scanner.nextLine()
        //System.in.read()
        //System.console().readLine()
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //br.readLine()
        expect:
        true
    }
    public static void main(String... args) {

    }
}
