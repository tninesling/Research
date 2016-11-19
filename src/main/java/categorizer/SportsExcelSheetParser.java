package categorizer;

import serde.models.PostUserTimeAndCategory;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SportsExcelSheetParser {
  /*public static void main(String[] args) {
    SportsExcelSheetParser parser = new SportsExcelSheetParser();
    XSSFWorkbook wkbk = parser.getExcelWorkbookFromFileLocation("C:\\Users\\Taylor\\Research\\data\\Category_Coding.xlsx");
    XSSFSheet sheet = parser.getSheetForGroup(wkbk, "group H");
    System.out.println(parser.getTitleColumnNumber(sheet));
  }*/

  public XSSFWorkbook getExcelWorkbookFromFileLocation(String workbookLocation) {
    FileInputStream fis = null;
    XSSFWorkbook workbook = null;
    try {
      fis = new FileInputStream(new File(workbookLocation));
      workbook = new XSSFWorkbook(fis);
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return workbook;
  }

  public XSSFSheet getSheetForGroup(XSSFWorkbook workbook, String groupName) {
    int numberOfSheets = workbook.getNumberOfSheets();
    String sheetName = workbook.getSheetName(0);
    int index = 1;

    while (!sheetName.toLowerCase().equals(groupName.toLowerCase()) && index < numberOfSheets) {
      sheetName = workbook.getSheetName(index);
      index++;
    }
    return workbook.getSheetAt(index - 1);
  }

  public Iterator<PostUserTimeAndCategory> categorizedPostIterator(XSSFSheet groupSheet) {
    return buildListOfCategorizedPosts(groupSheet).iterator();
  }

  public List<PostUserTimeAndCategory> buildListOfCategorizedPosts(XSSFSheet groupSheet) {
    int rowNumber = 0;
    int titleColumnNumber = getTitleColumnNumber(groupSheet);
    // find the first title cell in the sheet
    rowNumber = getNextTitleCellRowNumber(groupSheet, rowNumber, titleColumnNumber);
    ArrayList<PostUserTimeAndCategory> categorizedPostList = new ArrayList<PostUserTimeAndCategory>();
    while (rowNumber >= 0) {
      int nextTitleCellRowNumber = getNextTitleCellRowNumber(groupSheet, rowNumber, titleColumnNumber);
      if (nextTitleCellRowNumber > 0) {
        PostUserTimeAndCategory nextPost = buildCategorizedPost(groupSheet,
                          rowNumber, nextTitleCellRowNumber, titleColumnNumber);
        if (nextPost.hasCategory()) {
          categorizedPostList.add(nextPost);
        }
      }
      rowNumber = nextTitleCellRowNumber;
    }

    return categorizedPostList;
  }

  // Checks the first 10 rows for a Title column number
  // Defaults to column 4 (Cell E)
  private int getTitleColumnNumber(XSSFSheet groupSheet) {
    int titleColumnNumber = 0;
    for (int i = 0; titleColumnNumber < 1 && i < 10; i++) {
      Row currentRow = groupSheet.getRow(i);
      for (int j = 0; titleColumnNumber < 1 && j < 10; j++) {
        Cell currentCell = currentRow.getCell(j);
        if (currentCell != null &&
            currentCell.getCellType() == Cell.CELL_TYPE_STRING &&
            currentCell.getStringCellValue().contains("Title")) {
          titleColumnNumber = j;
        }
      }
    }

    if (titleColumnNumber == 0) {
      titleColumnNumber = 4;
    }
    return titleColumnNumber;
  }

  /* returns the row number of the next title cell
   * returns -1 if there is no other title cell
   */
  private int getNextTitleCellRowNumber(XSSFSheet groupSheet,
              int currentRowNumber, int titleColumn) {
    int originalRowNumber = currentRowNumber;
    String cellString = new String();
    Row currentRow = groupSheet.getRow(currentRowNumber);

    while (currentRow != null && (cellString == null || !cellString.contains("Title"))) {
      currentRowNumber++;
      currentRow = groupSheet.getRow(currentRowNumber);
      if (currentRow != null) {
        Cell currentRowColumnECell = currentRow.getCell(titleColumn);
        if (currentRowColumnECell.getCellType() == Cell.CELL_TYPE_STRING) {
          cellString = currentRowColumnECell.getStringCellValue();
        }
      }
    }

    if (currentRow == null) {
      currentRowNumber = -1;
    }
    return currentRowNumber;
  }

  private PostUserTimeAndCategory buildCategorizedPost(XSSFSheet groupSheet,
          int currentRowNumber, int nextTitleCellRowNumber, int titleColumnNumber) {
    PostUserTimeAndCategory nextCategorizedPost = new PostUserTimeAndCategory();

    nextCategorizedPost.setUser(getPostUser(groupSheet, currentRowNumber, nextTitleCellRowNumber, titleColumnNumber));
    nextCategorizedPost.setTimeStamp(getPostTimeStamp(groupSheet, currentRowNumber, nextTitleCellRowNumber, titleColumnNumber));
    nextCategorizedPost.setCategory(getPostCategory(groupSheet, currentRowNumber, nextTitleCellRowNumber, titleColumnNumber));

    return nextCategorizedPost;
  }

  private String getPostUser(XSSFSheet groupSheet, int currentRowNumber,
            int nextTitleCellRowNumber, int titleColumnNumber) {
    Row postedByRow = groupSheet.getRow(currentRowNumber + 1);
    String postedByText = postedByRow.getCell(titleColumnNumber).getStringCellValue();
    String[] splitPostedByText = postedByText.split(" ");

    if (splitPostedByText.length > 2) {
      return splitPostedByText[2];
    } else {
      return "";
    }
  }

  private String getPostTimeStamp(XSSFSheet groupSheet, int currentRowNumber,
            int nextTitleCellRowNumber, int titleColumnNumber) {
    Row postedByRow = groupSheet.getRow(currentRowNumber + 1);
    String postedByText = postedByRow.getCell(titleColumnNumber).getStringCellValue();
    String[] splitPostedByText = postedByText.split(" on ");

    if (splitPostedByText.length > 1) {
      return splitPostedByText[1];
    } else {
      return "";
    }
  }

  private String getPostCategory(XSSFSheet groupSheet, int currentRowNumber,
            int nextTitleCellRowNumber, int titleColumnNumber) {
    Row maybeCategoryRow;
    Cell maybeCategoryCell;
    String category = "";
    while (currentRowNumber < nextTitleCellRowNumber && category.isEmpty()) {
      // update position to next row, cell A
      currentRowNumber++;
      maybeCategoryRow = groupSheet.getRow(currentRowNumber);
      for (int i = 0; category.isEmpty() && i < titleColumnNumber; i++) {
        maybeCategoryCell = maybeCategoryRow.getCell(i); // column A in sheet

        if (maybeCategoryCell.getCellType() == Cell.CELL_TYPE_STRING) {
          category = maybeCategoryCell.getStringCellValue();
        } else if (maybeCategoryCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
          category = getCategoryLetterFromNumeric(maybeCategoryCell.getNumericCellValue());
        }
      }

    }

    return category;
  }

  private String getCategoryLetterFromNumeric(double numericValue) {
    int intValue = (int) numericValue;
    switch (intValue) {
      case 1: return "a";
      case 2: return "b";
      case 3: return "c";
      default: return "";
    }
  }
}
