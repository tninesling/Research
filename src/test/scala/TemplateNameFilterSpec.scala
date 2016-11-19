package tests

import finalSportTemplate.TemplateNameFilter

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import java.io.File

class TemplateNameFilterSpec extends FlatSpec with Matchers {
  "The FinalSportText directory" should "contain between 1 and 4 files for group A" in {
    val filter = new TemplateNameFilter("A")
    val directory = new File("C:\\Users\\Taylor\\Research\\data\\FinalSportText")
    val validFileNames = directory.list(filter)
    validFileNames.length should be > 0
    validFileNames.length should be < 5
  }
}
