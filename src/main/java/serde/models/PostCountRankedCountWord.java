package serde.models;

public class PostCountRankedCountWord extends RankedCountWord {
  int numberOfPosts;

  public PostCountRankedCountWord() {}

  public PostCountRankedCountWord(RankedCountWord existingCountWord) {
    this.setWord(existingCountWord.getWord());
    this.setCount(existingCountWord.getCount());
    this.setRank(existingCountWord.getRank());
    numberOfPosts = 0;
  }

  public int getNumberOfPosts() {
    return numberOfPosts;
  }

  public void setNumberOfPosts(int numberOfPosts) {
    this.numberOfPosts = numberOfPosts;
  }
}
