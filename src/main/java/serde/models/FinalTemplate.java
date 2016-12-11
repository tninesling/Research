package serde.models;

public class FinalTemplate {
  protected String sportName;
  protected String numberOfPlayers;
  protected String objectsUsed;
  protected String locationOfPlay;
  protected String durationOfPlay;
  protected String rulesOfPlay;
  protected String pointSystem;
  protected String howToScore;
  protected String howToWin;
  protected String extraComments;

  public FinalTemplate() {}

  public String getSportName() {
    return sportName;
  }
  public void setSportName(String sportName) {
    this.sportName = sportName;
  }

  public String getNumberOfPlayers() {
    return numberOfPlayers;
  }
  public void setNumberOfPlayers(String numberOfPlayers) {
    this.numberOfPlayers = numberOfPlayers;
  }

  public String getObjectsUsed() {
    return objectsUsed;
  }
  public void setObjectsUsed(String objectsUsed) {
    this.objectsUsed = objectsUsed;
  }

  public String getLocationOfPlay() {
    return locationOfPlay;
  }
  public void setLocationOfPlay(String locationOfPlay) {
    this.locationOfPlay = locationOfPlay;
  }

  public String getDurationOfPlay() {
    return durationOfPlay;
  }
  public void setDurationOfPlay(String durationOfPlay) {
    this.durationOfPlay = durationOfPlay;
  }

  public String getRulesOfPlay() {
    return rulesOfPlay;
  }
  public void setRulesOfPlay(String rulesOfPlay) {
    this.rulesOfPlay = rulesOfPlay;
  }

  public String getPointSystem() {
    return pointSystem;
  }
  public void setPointSystem(String pointSystem) {
    this.pointSystem = pointSystem;
  }

  public String getHowToScore() {
    return howToScore;
  }
  public void setHowToScore(String howToScore) {
    this.howToScore = howToScore;
  }

  public String getHowToWin() {
    return howToWin;
  }
  public void setHowToWin(String howToWin) {
    this.howToWin = howToWin;
  }

  public String getExtraComments() {
    return extraComments;
  }
  public void setExtraComments(String extraComments) {
    this.extraComments = extraComments;
  }

  // Returns the text value of the entire document concatenated together
  public String content() {
    String content = sportName + " " + numberOfPlayers + " " + objectsUsed + " ";
    content += locationOfPlay + " " + durationOfPlay + " " + rulesOfPlay + " ";
    content += pointSystem + " " + howToScore + " " + howToWin + " ";
    content += extraComments;

    return content;
  }
}
