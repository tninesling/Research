package finalSportTemplate;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Implements the FilenameFilter interface which can be used to filter out
 * names of files when listing fileNames in a directory.
 */
public class TemplateNameFilter implements FilenameFilter {
  private String groupName;

  public TemplateNameFilter(String groupName) {
    this.groupName = groupName;
  }

  // Filter accepts all names containing the groupName
  public boolean accept(File dir, String name) {
    String regex = "Final_Sport_" + groupName + "\\d.txt";
    return name.matches(regex);
  }
}
