package termcounter;

import serde.CountWordSerDe;
import serde.SportsJsonSerDe;
import serde.models.CountWord;
import serde.models.LemmatizedTokenData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TermCounter {
  public static String main(String[] args) {
    String lemmatizedDataLocation = args[0];
    File directory = new File(lemmatizedDataLocation);
    HashMap<String,Integer> hash = new HashMap<String,Integer>();

    for (String fileName : directory.list()) {
      SportsJsonSerDe sportsJsonSerDe = new SportsJsonSerDe();
      LemmatizedTokenData[] tokenArray = new LemmatizedTokenData[0];
      tokenArray = sportsJsonSerDe.parseSportsJson(lemmatizedDataLocation + fileName, tokenArray);

      for (LemmatizedTokenData datum : tokenArray) {
        countTerms(hash, datum);
      }
    }

    List<CountWord> countWordsList = buildCountWordListFromHashMap(hash);
    Collections.sort(countWordsList); // sorts in ascending order
    Collections.reverse(countWordsList);
    return CountWordSerDe.countWordsToJson(countWordsList.toArray(new CountWord[0]));
  }

  public static void countTerms(HashMap<String,Integer> countHash, LemmatizedTokenData data) {
    ArrayList<String> terms = new ArrayList<String>();
    for (List<String> lemmaTagPair : data.getTaggedUnigramTokens()) {
      terms.add(lemmaTagPair.get(0));
    }
    for (String term : terms) {
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
