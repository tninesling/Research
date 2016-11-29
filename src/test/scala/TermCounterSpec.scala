package tests

import serde.CountWordSerDe
import serde.SportsJsonSerDe
import serde.models.CountWord
import serde.models.LemmatizedTokenData
import termcounter.TermCounter

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import java.util.HashMap

class TermCounterSpec extends FlatSpec with Matchers {
  "The list of counted terms" should "produce a string of JSON" in {
    val sportsJsonSerDe = new SportsJsonSerDe()
    val lemmatizedDataLocation = "C:\\Users\\Taylor\\Research\\data\\LemmatizedData\\A_lemmed.json"
    var lemmatizedArray = Array[LemmatizedTokenData]()
    lemmatizedArray = sportsJsonSerDe.parseSportsJson(lemmatizedDataLocation, lemmatizedArray)

    val countWordSerDe = new CountWordSerDe()
    val hash = new HashMap[String,Integer]()

    lemmatizedArray.foreach {
      TermCounter.countTerms(hash, _)
    }

    val countWordsList = TermCounter.buildCountWordListFromHashMap(hash)
    //println(countWordSerDe.countWordsToJson(countWordsList.toArray(Array[CountWord]())))
  }
}
