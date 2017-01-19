package termcounter;

import serde.CountWordSerDe;
import serde.SportsJsonSerDe;
import serde.models.*;
import termFilter.TermFilter;

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
  public static String getTermFrequencyData(String lemmatizedDataLocation, HashMap<String,Integer> globalHash) {
    SportsJsonSerDe sportsJsonSerDe = new SportsJsonSerDe();

    LemmatizedTokenData[] tokenArray = new LemmatizedTokenData[0];
    tokenArray = sportsJsonSerDe.parseSportsJson(lemmatizedDataLocation, tokenArray);
    TermFilter.filterLemmatizedData(tokenArray);

    TermFrequencyData[] frequencyArray = new TermFrequencyData[tokenArray.length];

    for (int i = 0; i < tokenArray.length; i++) {
      HashMap<String,Integer> hash = new HashMap<String,Integer>();

      countTerms(hash, tokenArray[i]);
      countTerms(globalHash, tokenArray[i]);
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

  public static List<RankedCountWord> rankCountWords(List<CountWord> countWords) {
    Collections.sort(countWords);
    Collections.reverse(countWords);
    ArrayList<RankedCountWord> rankedList = new ArrayList<RankedCountWord>();
    for (int i = 0; i < countWords.size(); i++) {
      RankedCountWord newRankedWord = new RankedCountWord(countWords.get(i));
      newRankedWord.setRank(i);
      rankedList.add(newRankedWord);
    }
    return rankedList;
  }

  public static List<PostCountRankedCountWord> updateRankedVocab(List<RankedCountWord> vocab, TermFrequencyMatrix matrix) {
    HashMap<Integer,Integer> rankedTermHash = new HashMap<Integer,Integer>();

    for (List<List<String>> row : matrix.getRows()) {
      // For each term in the post (row), increment the count in the HashMap
      for (List<String> entry : row) {
        Integer termRank = Integer.parseInt(entry.get(2));

        // Increment the count for number of posts the term appears in
        Integer numberOfPosts = rankedTermHash.get(termRank);
        if (numberOfPosts == null) {
          rankedTermHash.put(termRank, 1);
        } else {
          rankedTermHash.put(termRank, numberOfPosts + 1);
        }
      }
    }

    List<PostCountRankedCountWord> updatedVocab = new ArrayList<PostCountRankedCountWord>();

    for (RankedCountWord term : vocab) {
      PostCountRankedCountWord nextTerm = new PostCountRankedCountWord(term);
      nextTerm.setNumberOfPosts(rankedTermHash.get(term.getRank()));
      updatedVocab.add(nextTerm);
    }

    return updatedVocab;
  }

  public static String rankedListToJson(List<RankedCountWord> rankedList) {
    return CountWordSerDe.countWordsToJson(rankedList.toArray(new RankedCountWord[0]));
  }
}
