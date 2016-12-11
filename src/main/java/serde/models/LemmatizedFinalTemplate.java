package serde.models;

import java.util.ArrayList;
import java.util.List;

public class LemmatizedFinalTemplate extends FinalTemplate implements LemmatizedData {
  protected List<List<String>> taggedUnigramTokens;
  protected List<List<String>> taggedMultigramTokens;

  public LemmatizedFinalTemplate() {}

  public LemmatizedFinalTemplate(FinalTemplate existingTemplate) {
    this.sportName = existingTemplate.getSportName();
    this.numberOfPlayers = existingTemplate.getNumberOfPlayers();
    this.objectsUsed = existingTemplate.getObjectsUsed();
    this.locationOfPlay = existingTemplate.getLocationOfPlay();
    this.durationOfPlay = existingTemplate.getDurationOfPlay();
    this.rulesOfPlay = existingTemplate.getRulesOfPlay();
    this.pointSystem = existingTemplate.getPointSystem();
    this.howToScore = existingTemplate.getHowToScore();
    this.howToWin = existingTemplate.getHowToWin();
    this.extraComments = existingTemplate.getExtraComments();
    this.taggedUnigramTokens = new ArrayList<List<String>>();
    this.taggedMultigramTokens = new ArrayList<List<String>>();
  }

  public List<List<String>> getTaggedUnigramTokens() {
    return taggedUnigramTokens;
  }

  public void setTaggedUnigramTokens(List<List<String>> taggedUnigramTokens) {
    this.taggedUnigramTokens = taggedUnigramTokens;
  }

  public List<List<String>> getTaggedMultigramTokens() {
    return taggedMultigramTokens;
  }

  public void setTaggedMultigramTokens(List<List<String>> taggedMultigramTokens) {
    this.taggedMultigramTokens = taggedMultigramTokens;
  }
}
