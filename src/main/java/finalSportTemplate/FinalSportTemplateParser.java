package finalSportTemplate;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FinalSportTemplateParser {
  public void convertAllDocumentsToText(String inputLocation, String outputLocation) {
    FinalSportTemplateNameNormalizer normalizer = new FinalSportTemplateNameNormalizer();
    File directory = new File(inputLocation);
    String[] documentsAsText = getAllDocumentsAsTextFromDirectory(directory);
    String[] outFileNames = normalizer.getStandardizedFileNamesForDirectory(directory);
    writeDocumentsToLocation(outputLocation, documentsAsText, outFileNames);
  }

  public String[] getAllDocumentsAsTextFromDirectory(File documentDirectory) {
    String[] documentsAsText = null;
    if (documentDirectory.isDirectory()) {
      File[] files = documentDirectory.listFiles();
      documentsAsText = new String[files.length];
      for (int i = 0; i < files.length; i++) {
        documentsAsText[i] = getDocumentAsText(getDocumentFromFile(files[i]));
      }
    }
    return documentsAsText;
  }

  public XWPFDocument getDocumentFromFile(File docFile) {
    XWPFDocument document = null;
    try {
      document = new XWPFDocument(new FileInputStream(docFile));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return document;
  }

  public String getDocumentAsText(XWPFDocument doc) {
    if (doc == null) {
      return new String();
    } else {
      XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
      String documentText = extractor.getText();
      documentText = documentText.substring(documentText.indexOf("Sport Name"), documentText.length());
      return documentText;
    }
  }

  public void writeDocumentsToLocation(String location, String[] textDocs, String[] outFileNames) {
    for (int i = 0; i < textDocs.length; i++) {
      String currentLocation = location + outFileNames[i];
      try {
        PrintWriter outWriter = new PrintWriter(new BufferedWriter(new FileWriter(currentLocation)));
        outWriter.write(textDocs[i]);
        outWriter.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }
}
