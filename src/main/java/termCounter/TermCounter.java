package termcounter;

import serde.CountWordSerDe;
import serde.SportsJsonSerDe;
import serde.models.CountWord;
import serde.models.LemmatizedData;
import serde.models.LemmatizedFinalTemplate;
import serde.models.LemmatizedTokenData;
import serde.models.TermFrequencyData;
import serde.models.TermFrequencyTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TermCounter {
  /**
   * Returns an array of TermFrequencyData as a JSON string. The array of
   * TermFrequencyData objects is built from an array of LemmatizedTokenData
   * which is parsed from the lemmatizedDataLocation
   */
  public static String getTermFrequencyData(String lemmatizedDataLocation) {
    SportsJsonSerDe sportsJsonSerDe = new SportsJsonSerDe();

    LemmatizedTokenData[] tokenArray = new LemmatizedTokenData[0];
    tokenArray = sportsJsonSerDe.parseSportsJson(lemmatizedDataLocation, tokenArray);

    TermFrequencyData[] frequencyArray = new TermFrequencyData[tokenArray.length];

    for (int i = 0; i < tokenArray.length; i++) {
      HashMap<String,Integer> hash = new HashMap<String,Integer>();

      countTerms(hash, tokenArray[i]);
      List<CountWord> countWordsList = buildCountWordListFromHashMap(hash);

      frequencyArray[i] = new TermFrequencyData(tokenArray[i]);
      frequencyArray[i].setTermFrequencyList(countWordsList);
    }

    return sportsJsonSerDe.sportsDataToJson(frequencyArray);
  }

  public static TermFrequencyTemplate[] getTermFrequencyTemplates(LemmatizedFinalTemplate[] templates) {
    TermFrequencyTemplate[] countedData = new TermFrequencyTemplate[templates.length];

    for (int i = 0; i < templates.length; i++) {
      HashMap<String,Integer> hash = new HashMap<String,Integer>();

      countTerms(hash, templates[i]);
      List<CountWord> countWordsList = buildCountWordListFromHashMap(hash);

      countedData[i] = new TermFrequencyTemplate(templates[i]);
      countedData[i].setTermFrequencyList(countWordsList);
    }

    return countedData;
  }

  public static void countTerms(HashMap<String,Integer> countHash, LemmatizedData data) {
    ArrayList<String> terms = new ArrayList<String>();

    for (List<String> lemmaTagPair : data.getTaggedUnigramTokens()) {
      terms.add(lemmaTagPair.get(0));
    }

    for (List<String> lemmaTagPair : data.getTaggedMultigramTokens()) {
      terms.add(lemmaTagPair.get(0));
    }

    countTermsInList(countHash, terms);
  }

  private static void countTermsInList(HashMap<String,Integer> countHash, List<String> list) {
    for (String term : list) {
      Integer count = countHash.get(term);
      if (count == null) {
        countHash.put(term, 1);
      } else {
        countHash.put(term, count + 1);
      }
    }
  }

  public static List<CountWord> buildCountWordListFromHashMap(HashMap<String,Integer> countHash) {
    List<CountWord> wordCountList = new ArrayList<CountWord>();
    for (String key : countHash.keySet()) {
      CountWord currentWord = new CountWord();
      currentWord.setWord(key);
      currentWord.setCount(countHash.get(key));
      wordCountList.add(currentWord);
    }
    return wordCountList;
  }
}
