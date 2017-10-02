import com.fasterxml.uuid.EthernetAddress
import com.fasterxml.uuid.Generators
import com.fasterxml.uuid.UUIDType
import com.fasterxml.uuid.impl.TimeBasedGenerator
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
        Math.pow(10,4) == 10000.0
        10.4.intValue() == 10
        10.4 as Integer == 10
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
        for (Integer i: (1..4)) {println i}
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
        true && true
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
        ['a', 'b'].contains('a')
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
        '121.1'.find(/(.*)\./){match, firstSection -> firstSection} == '121'
    }
    def "split"() {
        expect:
        'A_B'.split(/_/)[0] == 'A'
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
    @Ignore // scanners just don't work in test
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
    def "collectMany"() {
        when:
        (1..15).each{benchmarkCollectMany(Math.pow(10,it) as Integer)}
        /*
        Collectmany of 10.0 items took 12 time
        Collectmany of 100.0 items took 2 time
        Collectmany of 1000.0 items took 5 time
        Collectmany of 10000.0 items took 16 time
        Collectmany of 100000.0 items took 58 time
        Collectmany of 1000000.0 items took 204 time
        Collectmany of 1.0E7 items took 1968 time
        Collectmany of 1.0E8 items took 49250 time

        java.lang.OutOfMemoryError: Java heap space
        seems to be quadratic.
         */
        then:
            true
        expect:
        [1,2].collectMany {i ->
            if (i==1) {
                return [i]
            }
            else {
                return []
            }

        } == [1]
    }
    void benchmarkCollectMany(Integer n) {
        long time = benchmark {
            (1..n).collectMany {[it]}
        }
        println "Collectmany of $n items took $time time"
    }
    long benchmark(Closure closure)  {
        long start = System.currentTimeMillis()
        closure.call()
        long now = System.currentTimeMillis()
        now - start
    }

    def "generate stuff for risk repository"() {

        ["region", "country", "ringfence", "typeOfBank", "departmentOfBank"].each {s ->
            println("result.put(\"$s\", riskInstance.get${s.capitalize()}());")
        }
        println ""
        ["preControlSeverity", "postControlSeverity"].each{s ->
            println("<p className=\"card-text\">$s: {riskType.${s}}</p>")
        }
        println ""
        ["region", "country", "ringfence", "typeOfBank", "departmentOfBank"].each {s ->
            println("<p className=\"card-text\">${s.capitalize()}: {riskInstance.${s}}</p>")
        }
        println ""
        TimeBasedGenerator gen = Generators.timeBasedGenerator(EthernetAddress.fromInterface())
        int n = 0
        ["Ringfenced", "Non-ringfenced"].each {ringfence ->
            ["Retail & Wealth Bank", "Private Bank", "Corporate Bank", "Banking & Markets"].each {typeOfBank ->
                ["HR", "Large Corporates", " Private Clients", " Trading Desk", " Central Functions", " Retail Clients", " Legal", " Wealth Clients", " Key Clients", "Small Corporates"].each {departmentOfBank ->
                    ["Turkey": "MENA", "China": "APAC", "UK": "EMEA", "Germany": "EMEA", "France": "EMEA", "Brazil": "LATAM", "Argentina": "LATAM", "USA": "USA"].each {country, region ->
                        String riskInstanceVar = "RiskInstance$n"
                        UUID uuid = gen.generate()
                        println """
($riskInstanceVar:RiskInstance {name: 'Mis-selling to Customers', preControlSeverity: 'High damage, unlikely', postControlSeverity: 'Low damage, unlikely', uuid: "${uuid.toString()}",reviewDate:'1-6 Months', region: '$region', country: '$country', ringfence: '$ringfence', typeOfBank: '$typeOfBank', departmentOfBank: '$departmentOfBank'}),
    ($riskInstanceVar)-[:INSTANCE_OF]->(Risk101),
    (Control1)-[:CONTROLS_RISK {effectiveness:'Needs Improvement'}]->($riskInstanceVar),
    (Control2)-[:CONTROLS_RISK {effectiveness:'Effective'}]->($riskInstanceVar),
    (Control3)-[:CONTROLS_RISK {effectiveness:'Ineffective'}]->($riskInstanceVar),
    (Control4)-[:CONTROLS_RISK {effectiveness:'Needs Improvement'}]->($riskInstanceVar),
    (Control5)-[:CONTROLS_RISK {effectiveness:'Effective'}]->($riskInstanceVar),"""
                        n += 1
                    }
            }

        }

        }
        expect:
        true
    }

    def "UUID"() {
        println Generators.timeBasedGenerator(EthernetAddress.fromInterface()).generate()
        expect:true
    }
    def "++"() {
        int n = 0
        n += 1
        expect:
        n == 1
    }


    public static void main(String... args) {

    }
}
