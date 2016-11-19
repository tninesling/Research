package tests

import finalSportTemplate.FinalSportTemplateNameNormalizer

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class FinalSportTemplateNameNormalizerSpec extends FlatSpec with Matchers {
  "A group name with section number" should "match with [A-Z]?[A-Z]\\d" in {
    val regex = "[A-Z]?[A-Z]\\d"
    assert("AA3".matches(regex))
    assert("A2".matches(regex))
  }
  "The substring beginning with the group name" should "match [A-Z]?[A-Z]\\d(\\S||\\s)*" in {
    val regex = "[A-Z]?[A-Z]\\d(\\S||\\s)*"
    val fileName1 = "Sport_Template_A1.docx"
    val fileName2 = "Final_Sport_Group_AA2.docx"
    val fileName3 = "TeamSport.docxEY3.docx"

    val normalizer = new FinalSportTemplateNameNormalizer()

    assert(normalizer.getSubstringStartingWithGroupName(fileName1).matches(regex))
    assert(normalizer.getSubstringStartingWithGroupName(fileName2).matches(regex))
    assert(normalizer.getSubstringStartingWithGroupName(fileName3).matches(regex))
  }
  "The substring ending with the section number" should "match (\\S||\\s)*[A-Z]?[A-Z]\\d" in {
    val regex = "(\\S||\\s)*[A-Z]?[A-Z]\\d"
    val fileName1 = "Sport_Template_A1.docx"
    val fileName2 = "Final_Sport_Group_AA2.docx"
    val fileName3 = "Team Sport.docxEY3.docx"

    val normalizer = new FinalSportTemplateNameNormalizer()

    assert(normalizer.getSubstringEndingWithSectionNumber(fileName1).matches(regex))
    assert(normalizer.getSubstringEndingWithSectionNumber(fileName2).matches(regex))
    assert(normalizer.getSubstringEndingWithSectionNumber(fileName3).matches(regex))
  }
  "The final group name and number extraction" should "match [A-Z]?[A-Z]\\d" in {
    val regex = "[A-Z]?[A-Z]\\d"
    val fileName1 = "Sport_Template_A1.docx"
    val fileName2 = "Final_Sport_Group_AA2.docx"
    val fileName3 = "Team Sport.docxEY3.docx"

    val normalizer = new FinalSportTemplateNameNormalizer()

    assert(normalizer.getGroupNameAndSectionNumber(fileName1).matches(regex))
    assert(normalizer.getGroupNameAndSectionNumber(fileName2).matches(regex))
    assert(normalizer.getGroupNameAndSectionNumber(fileName3).matches(regex))
  }
}
