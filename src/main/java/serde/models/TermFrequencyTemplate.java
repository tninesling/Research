package serde.models;

import java.util.ArrayList;
import java.util.List;

public class TermFrequencyTemplate extends LemmatizedFinalTemplate implements TermFrequency {
  protected List<CountWord> termFrequencyList;

  public TermFrequencyTemplate() {}

  public TermFrequencyTemplate(LemmatizedFinalTemplate existingTemplate) {
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
    this.taggedUnigramTokens = existingTemplate.getTaggedUnigramTokens();
    this.taggedMultigramTokens = existingTemplate.getTaggedMultigramTokens();
    this.termFrequencyList = new ArrayList<CountWord>();
  }

  public List<CountWord> getTermFrequencyList() {
    return termFrequencyList;
  }

  public void setTermFrequencyList(List<CountWord> termFrequencyList) {
    this.termFrequencyList = termFrequencyList;
  }

  public int alpha(String term) {
    if (sportName.toLowerCase().equals(term.toLowerCase())) {
      return 10;
    }
    else if (locationOfPlay.contains(term) ||
             objectsUsed.contains(term)) {
      return 2;
    } else {
      return 1;
    }
  }
}
