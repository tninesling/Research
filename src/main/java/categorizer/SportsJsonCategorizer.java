package categorizer;

import serde.models.CategorizedSportsData;
import serde.models.PostUserTimeAndCategory;
import serde.models.SportsData;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Iterator;

public class SportsJsonCategorizer {
  public CategorizedSportsData[] mergeExistingSportsDataWithCategories(SportsData[] existingSportsData,
          Iterator<PostUserTimeAndCategory> categoryIterator) {
      CategorizedSportsData[] mergedSportsDataArray = new CategorizedSportsData[existingSportsData.length];

      for (int i = 0; i < mergedSportsDataArray.length; i++) {
        mergedSportsDataArray[i] = new CategorizedSportsData(existingSportsData[i]);
      }

      while (categoryIterator.hasNext()) {
        PostUserTimeAndCategory nextCategory = categoryIterator.next();
        for (CategorizedSportsData nextData: mergedSportsDataArray) {
          if (usersMatch(nextData, nextCategory) && timeStampsMatch(nextData, nextCategory)) {
            nextData.setCategory(nextCategory.getCategory());
          }
        }
      }

      return mergedSportsDataArray;
  }

  private boolean usersMatch(SportsData data, PostUserTimeAndCategory category) {
    String dataUser = data.getUser().trim().toLowerCase();
    String categoryUser = category.getUser().trim().toLowerCase();
    return dataUser.equals(categoryUser);
  }

  private boolean timeStampsMatch(SportsData data, PostUserTimeAndCategory category) {
    String dataTimeStamp = data.getTimeCode().replaceAll("\\s+", "").toLowerCase();
    String categoryTimeStamp = category.getTimeStamp().replaceAll("\\s+", "").toLowerCase();
    return dataTimeStamp.equals(categoryTimeStamp);
  }

  /** Posts of type "n" (new posts) or of type "r" (replies) should be categorized
    * in the Category_Coding.xlsx spreadsheet. If no category is assigned,
    * the category "0" will be assigned to distinguish it from votes which are
    * not categorized.
    *
    * @return The number of category corrections made
    */
  public int correctUncategorizedPostsAndReplies(CategorizedSportsData[] categorizedPosts) {
    int numberOfCategoryCorrections = 0;
    for (CategorizedSportsData post: categorizedPosts) {
      if ((post.getType().trim().equals("n") || post.getType().trim().equals("r")) && post.getCategory().isEmpty()) {
        post.setCategory("0");
        numberOfCategoryCorrections++;
      }
    }

    return numberOfCategoryCorrections;
  }
}
