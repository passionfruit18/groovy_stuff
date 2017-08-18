package gebStuff
import geb.Browser
import geb.Page

class PlayWithNHSDD {
    static void main(String... args) {
        Browser browser = new Browser()
        browser.go 'http://www.datadictionary.nhs.uk/data_dictionary/attributes/c/cou/critical_care_unit_function_de.asp?shownav=1'
        Page page = browser.page
        /**
         * A way to get text in #main-copy .fullWidth that's not in a subelement– have to delete everything else. But what about that thing that had a table that wasn't an enumeration?
         */
        println page.$('#main-copy .fullWidth').text().readLines().minus(page.$('#main-copy .fullWidth table, .titlebarwrapper, .specialisttoolbartop')*.text().join('\n').readLines())
        //System.sleep(2500)
        browser.go  'http://www.datadictionary.nhs.uk/data_dictionary/attributes/l/le/live_or_still_birth_de.asp?shownav=1'
        Page page2 = browser.page
        println page2.$('#main-copy .fullWidth p')*.text()

        browser.go 'http://www.datadictionary.nhs.uk/data_dictionary/attributes/c/cons/consultant_code_de.asp?shownav=1'
        Page page3 = browser.page
        println page3.$('#main-copy .fullWidth ul, #main-copy .fullWidth ol')*.text()

        browser.go 'http://www.datadictionary.nhs.uk/data_dictionary/attributes/i/int/international_classification_for_intraocular_retinoblastoma_de.asp?shownav=1'
        Page page4 = browser.page
        println page4.$('#main-copy .fullWidth ul')*.text()
    }
}
//File input = new File(args[0])
//File output = new File(args[1])

//output.getParentFile().mkdirs()
//output.write("")
/*

input.withReader { reader ->
    while (line = reader.readLine()) {
	//output << line
	Browser.drive {
	    go line
	    Thread.sleep(300)
	}
    }
 }
*/
/*
// Get Level 1 Urls: Urls of pages listing elements
List<String> urls1 = []
Browser.drive {
    go "http://www.datadictionary.nhs.uk/data_dictionary/data_field_notes/data_field_notes_A_child.asp"
    urls1 =  $("a", class:"leftmenuglossarylinkbold")*.attr("href") +
	$("a", class:"leftmenuglossarylink")*.attr("href")

}  
urls1.sort(true) //mutate in place
println urls1

// Get Level 2 Urls: Urls of pages of elements themselves, from Level 1 Url pages.
List<String> urls2 = [] 
urls1.forEach { url1 ->
    Browser.drive {
	go url1
	urls2 += $("a", class:"dd_data_element")*.attr("href")
    }	
}

println urls2

*/
/*
Browser.drive {

    go "http://gebish.org"
    Thread.sleep(400)
    assert title == "Geb - Very Groovy Browser Automation"
    $("a", class: "apis item").click()
    Thread.sleep(400)
    $("a", href: "manual/current/api/").click()
    Thread.sleep(400)
    go "http://gebish.org/manual/current/api/overview-summary.html"
    Thread.sleep(400)
    $("a", href: "geb/navigator/package-summary.html").click()
    Thread.sleep(400)
    List<String> captions = $("caption span")*.text()
    assert captions == ["Interface Summary", " ", "Class Summary", " ", "Exception Summary", " "]
    println captions
    println "Hello World!"

    
}

*/