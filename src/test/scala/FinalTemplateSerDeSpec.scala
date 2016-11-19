package tests

import serde.FinalTemplateSerDe

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import java.io.File

class FinalTemplateSerDeSpec extends FlatSpec with Matchers {
  "The getSectionContent method" should "be successful for a valid group" in {
    val serde = new FinalTemplateSerDe()
    val fileLocation = "C:\\Users\\Taylor\\Research\\data\\FinalSportText\\Final_Sport_A1.txt"
    val text = serde.getDocumentText(fileLocation);
    val sections = Array("Sport Name:",
                         "Number of Players:",
                         "Objects used:",
                         "Location of Play:",
                         "Duration of Play (how long, minutes/hours/etc.):",
                         "Rules of Play (fouls, what is allowed, banned):",
                         "Point system used:",
                         "How do you earn points?:",
                         "How do you determine who wins?:",
                         "Extra Comments:")
    val sectionContent = serde.getSectionContent(text, sections)
    val finalString = serde.finalTemplateToJson(serde.buildTemplateFromContent(sectionContent))
  }
  "The parseFinalTemplatesFromDirectory" should "return a valid array of templates" in {
    val serde = new FinalTemplateSerDe()
    val directory = "C:\\Users\\Taylor\\Research\\data\\FinalSportText\\"
    val templateArray = serde.parseFinalTemplatesFromDirectory(directory, "A")
  }
}
