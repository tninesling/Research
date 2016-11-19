package finalSportTemplate;

import java.io.File;

public class FinalSportTemplateNameNormalizer {
  public String[] getStandardizedFileNamesForDirectory(File templateDirectory) {
    String[] newFileNames = null;
    if (templateDirectory.isDirectory()) {
      String[] fileNames = templateDirectory.list();
      newFileNames = getStandardizedFileNames(fileNames);
    }

    return newFileNames;
  }

  public String[] getStandardizedFileNames(String[] fileNames) {
    String[] newFileNames = new String[fileNames.length];
    for (int i = 0; i < fileNames.length; i++) {
      newFileNames[i] = getStandardizedFileName(fileNames[i]);
    }
    return newFileNames;
  }

  public String getStandardizedFileName(String fileName) {
    String groupNameAndSectionNumber = getGroupNameAndSectionNumber(fileName);
    if (groupNameAndSectionNumber.isEmpty()) {
      return fileName.substring(0, fileName.length()-5) + ".txt";
    }
    return "Final_Sport_" + groupNameAndSectionNumber + ".txt";
  }

  public String getGroupNameAndSectionNumber(String fileName) {
    String groupNameStart = getSubstringStartingWithGroupName(fileName);
    return getSubstringEndingWithSectionNumber(groupNameStart);
  }

  public String getSubstringStartingWithGroupName(String str) {
    // group name will have either one or two captial letter(s) followed by a number
    String regex = "[A-Z]?[A-Z]\\d(\\S||\\s)*";
    while (!str.matches(regex) && !str.isEmpty()) {
      str = str.substring(1);
    }
    return str;
  }

  public String getSubstringEndingWithSectionNumber(String str) {
    // cut off the end of the string until it matches GroupName Section number regex
    String regex = "(\\S||\\s)*[A-Z]?[A-Z]\\d";
    while(!str.matches(regex) && str.length() > 1) {
      str = str.substring(0, str.length() - 1);
    }
    return str;
  }
}
