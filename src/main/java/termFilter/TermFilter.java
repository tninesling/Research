package termFilter;

import serde.SportsJsonSerDe;
import serde.models.LemmatizedTokenData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TermFilter {
  private static final List<String> stopWords = Arrays.asList(new String[] {
    "i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
    "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers",
    "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves",
    "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are",
    "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does",
    "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until",
    "while", "of", "at", "by", "for", "with", "about", "against", "between", "into",
    "through", "during", "before", "after", "above", "below", "to", "from", "up", "down",
    "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here",
    "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more",
    "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so",
    "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now",
    "also", "maybe", "instead", "allow", "certain", "most", "how", "so", "when", "as",
    "same", "just", "something", "next", "many", "thing", "such"
  });

  public static void filterLemmatizedData(LemmatizedTokenData[] lemmatizedData) {
    for (LemmatizedTokenData datum : lemmatizedData) {

      List<List<String>> filteredUnigrams = new ArrayList<List<String>>();
      for (List<String> unigram : datum.getTaggedUnigramTokens()) {
        // If the unigram term is not contained in stopWords, add it to the filtered List
        if(isValid(unigram)) {
          filteredUnigrams.add(unigram);
        }
      }
      datum.setTaggedUnigramTokens(filteredUnigrams);
    }
  }

  private static boolean isValid(List<String> unigram) {
    String lemma = unigram.get(0);
    String partOfSpeech = unigram.get(1);

    boolean notStopWord = !stopWords.contains(lemma);
    boolean validPOS = isNoun(partOfSpeech) || isVerb(partOfSpeech);

    return notStopWord && validPOS;
  }

  private static boolean isNoun(String tag) {
    // treats gerunds (VBG) as nouns
    return tag.contains("NN") || tag.equals("VBG");
  }

  private static boolean isVerb(String tag) {
    return tag.contains("VB");
  }
}
