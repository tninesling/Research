package nlp;

import serde.models.CategorizedSportsData;
import serde.models.LemmatizedTokenData;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ContentProcessor {
  public LemmatizedTokenData[] lemmatizeAndTagCategorizedData(CategorizedSportsData[] input) {
    LemmatizedTokenData[] lemmatizedDataArray = new LemmatizedTokenData[input.length];
    for (int i = 0; i < input.length; i++) {
      LemmatizedTokenData current = new LemmatizedTokenData(input[i]);
      if (!current.getType().equals("v")) {
        current.setTaggedUnigramTokens(getTaggedUnigramTokens(current.getContent()));
        current.setTaggedMultigramTokens(getTaggedMultigramTokens(current.getContent()));
      } else {
        current.setTaggedUnigramTokens(new ArrayList<List<String>>());
        current.setTaggedMultigramTokens(new ArrayList<List<String>>());
      }
      lemmatizedDataArray[i] = current;
    }

    return lemmatizedDataArray;
  }

  public List<List<String>> getTaggedUnigramTokens(String str) {
    Document doc = new Document(str);

    List<String> lemmas = new ArrayList<String>();
    List<String> posTags = new ArrayList<String>();
    // extract the lemmas and POS tags from each sentence as lists
    for (Sentence sent : doc.sentences()) {
      lemmas.addAll(sent.lemmas());
      posTags.addAll(sent.posTags());
    }

    return buildTaggedUnigrams(lemmas, posTags);
  }

  // builds a list of String pairs
  // each pair is a list containing the lemmatized word and its part of speech
  private List<List<String>> buildTaggedUnigrams(List<String> lemmas, List<String> posTags) {
    List<List<String>> lemmaTagPairs = new ArrayList<List<String>>();
    for (int i = 0; i < lemmas.size(); i++) {
      String currentLemma = lemmas.get(i).toLowerCase();
      String currentTag = posTags.get(i);

      if (correctPartOfSpeech(currentTag)) {
        List<String> lemmaTagPair = Arrays.asList(currentLemma, currentTag);
        lemmaTagPairs.add(lemmaTagPair);
      }
    }
    return lemmaTagPairs;
  }

  private boolean correctPartOfSpeech(String tag) {
    return (isNoun(tag) || isAdjective(tag) || isAdverb(tag) || isVerb(tag));
  }

  public List<List<String>> getTaggedMultigramTokens(String str) {
    Document doc = new Document(str);

    List<String> lemmas = new ArrayList<String>();
    List<String> posTags = new ArrayList<String>();
    // extract the lemmas and POS tags from each sentence as lists
    for (Sentence sent : doc.sentences()) {
      lemmas.addAll(sent.lemmas());
      posTags.addAll(sent.posTags());
    }

    return buildTaggedMultigrams(lemmas, posTags);
  }

  // build List of Bigrams and Trigrams with Part of Speech Tags
  // the Bigrams and Trigrams will contain the lemmas of the original words
  // separated by spaces
  // the Part of Speech Tags for the words will be concatenated and separated
  // by /
  private List<List<String>> buildTaggedMultigrams(List<String> lemmas, List<String> posTags) {
    List<List<String>> multigramList = new ArrayList<List<String>>();
    for (int i = 0; i < lemmas.size() - 2; i++) {
      String[] currentLemmas = getNextThree(lemmas, i);
      String[] currentTags = getNextThree(posTags, i);

      String currentBigram = currentLemmas[0] + " " + currentLemmas[1];
      String currentBigramTags = currentTags[0] + "/" + currentTags[1];
      String currentTrigram = currentBigram + " " + currentLemmas[2];
      String currentTrigramTags = currentBigramTags + "/" + currentTags[2];

      if (correctPartOfSpeechPattern(currentTrigramTags)) {
        List<String> trigramTagPair = Arrays.asList(currentTrigram, currentTrigramTags);
        multigramList.add(trigramTagPair);
      } else if (correctPartOfSpeechPattern(currentBigramTags)) {
        List<String> bigramTagPair = Arrays.asList(currentBigram, currentBigramTags);
        multigramList.add(bigramTagPair);
      }
    }
    return multigramList;
  }

  private String[] getNextThree(List<String> list, int index) {
    return new String[] { list.get(index), list.get(index+1), list.get(index+2) };
  }

  private boolean correctPartOfSpeechPattern(String multigramTags) {
    String[] tagArray = multigramTags.split("/");
    if (tagArray.length == 3) {
      return correctTrigramPattern(tagArray);
    } else {
      return correctBigramPattern(tagArray);
    }
  }

  private boolean correctTrigramPattern(String[] tagArray) {
    boolean CNN = isNumber(tagArray[0]) && isNoun(tagArray[1]) && isNoun(tagArray[2]);
    boolean CAN = isNumber(tagArray[0]) && isAdjective(tagArray[1]) && isNoun(tagArray[2]);
    boolean AAN = isAdjective(tagArray[0]) && isAdjective(tagArray[1]) && isNoun(tagArray[2]);
    boolean ANN = isAdjective(tagArray[0]) && isNoun(tagArray[1]) && isNoun(tagArray[2]);
    boolean NAN = isNoun(tagArray[0]) && isAdjective(tagArray[1]) && isNoun(tagArray[2]);
    boolean NNN = isNoun(tagArray[0]) && isNoun(tagArray[1]) && isNoun(tagArray[2]);
    boolean NPN = isNoun(tagArray[0]) && isPronoun(tagArray[1]) && isNoun(tagArray[2]);

    return CNN || CAN || AAN || ANN || NAN || NNN || NPN;
  }

  private boolean correctBigramPattern(String[] tagArray) {
    boolean CN = isNumber(tagArray[0]) && isNoun(tagArray[1]);
    boolean NN = isNoun(tagArray[0]) && isNoun(tagArray[1]);
    boolean CV = isNumber(tagArray[0]) && isVerb(tagArray[1]);
    boolean AN = isAdjective(tagArray[0]) && isNoun(tagArray[1]);

    return CN || NN || CV || AN;
  }

  private boolean isNumber(String tag) {
    return tag.contains("CD");
  }

  private boolean isNoun(String tag) {
    // treats gerunds (VBG) as nouns
    return tag.contains("NN") || tag.equals("VBG");
  }

  private boolean isAdjective(String tag) {
    return tag.contains("JJ");
  }

  private boolean isAdverb(String tag) {
    // excludes -LRB- and -RRB- tags for parentheses
    return tag.contains("RB") && !tag.equals("-LRB-") && !tag.equals("-RRB-");
  }

  private boolean isVerb(String tag) {
    return tag.contains("VB");
  }

  private boolean isPronoun(String tag) {
    return tag.contains("PR");
  }

  /* Proof of concept for Open Information Extraction functionality
   * Can provide subject/object relationships
   * May use for future tests
  public void printOpenIeInfo(String testString) {
    Document doc = new Document(testString);
    for (Sentence sent : doc.sentences()) {
      System.out.println("Simple openIE: " + sent.openie());
      System.out.println("Triples: " + sent.openieTriples());
    }
  }*/
}
