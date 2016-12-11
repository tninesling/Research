package serde;

import finalSportTemplate.TemplateNameFilter;
import serde.models.FinalTemplate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FinalTemplateSerDe {
  private static ObjectMapper mapper = new ObjectMapper();

  public FinalTemplate[] parseFinalTemplatesFromDirectory(String dirLocation, String groupName) {
    TemplateNameFilter filter = new TemplateNameFilter(groupName);
    File textDir = new File(dirLocation);
    String[] validFileNames = textDir.list(filter);

    FinalTemplate[] templates = new FinalTemplate[validFileNames.length];
    for (int i = 0; i < validFileNames.length; i++) {
      templates[i] = parseFinalTemplateFromText(dirLocation + validFileNames[i]);
    }
    return templates;
  }

  public FinalTemplate parseFinalTemplateFromText(String fileLocation) {
    String documentText = getDocumentText(fileLocation);
    String[] templateSections = { "Sport Name:",
                                  "Number of Players:",
                                  "Objects used:",
                                  "Location of Play:",
                                  "Duration of Play (how long, minutes/hours/etc.):",
                                  "Rules of Play (fouls, what is allowed, banned):",
                                  "Point system used:",
                                  "How do you earn points?:",
                                  "How do you determine who wins?:",
                                  "Extra Comments:" };
    String[] sectionContent = getSectionContent(documentText, templateSections);
    return buildTemplateFromContent(sectionContent);
  }

  public String getDocumentText(String fileLocation) {
    List<String> documentLines = new ArrayList<String>();
    try {
      Path path = (new File(fileLocation)).toPath();
      Charset charset = Charset.defaultCharset();
      documentLines = Files.readAllLines(path, charset);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return String.join(" ", documentLines);
  }

  public String[] getSectionContent(String documentText, String[] templateSections) {
    String[] sectionContent = new String[templateSections.length];
    for (int i = 0; i < templateSections.length - 1; i++) {
      String startSection = templateSections[i];
      String endSection = templateSections[i + 1];
      int startIndex = getStartIndex(startSection, documentText);
      int endIndex = getEndIndex(endSection, documentText);

      String currentSection = documentText.substring(startIndex, endIndex);
      sectionContent[i] = currentSection.trim();
    }
    String lastSection = templateSections[templateSections.length - 1];
    sectionContent[sectionContent.length - 1] = documentText.substring(getStartIndex(lastSection, documentText)).trim();
    return sectionContent;
  }

  // Determines the index of one of the variants of section title formats
  public int getStartIndex(String sectionTitle, String documentText) {
    String origSecTitle = sectionTitle;
    String origDocText = documentText;

    sectionTitle = sectionTitle.toLowerCase();
    documentText = documentText.toLowerCase();

    int index = documentText.indexOf(sectionTitle);
    if (index < 0) {
      sectionTitle = sectionTitle.substring(0, sectionTitle.length() - 1); // removes colon
      index = documentText.indexOf(sectionTitle);
    }
    if (index < 0) {
      sectionTitle = sectionTitle + " :";
      index = documentText.indexOf(sectionTitle);
    }
    if (index < 0) {
      System.out.println(origSecTitle + " not found in " + origDocText);
    }
    return index + sectionTitle.length();
  }

  public int getEndIndex(String sectionTitle, String documentText) {
    String origSecTitle = sectionTitle;
    String origDocText = documentText;

    sectionTitle = sectionTitle.toLowerCase();
    documentText = documentText.toLowerCase();

    int index = documentText.indexOf(sectionTitle);
    if (index < 0) {
      sectionTitle = sectionTitle.substring(0, sectionTitle.length() - 1); // removes colon
      index = documentText.indexOf(sectionTitle);
    }
    if (index < 0) {
      sectionTitle = sectionTitle + " :";
      index = documentText.indexOf(sectionTitle);
    }
    if (index < 0) {
      System.out.println(origSecTitle + " not found in " + origDocText);
    }
    return index;
  }

  public FinalTemplate buildTemplateFromContent(String[] sectionContent) {
    FinalTemplate template = new FinalTemplate();
    if (sectionContent.length == 10) {
      template.setSportName(sectionContent[0]);
      template.setNumberOfPlayers(sectionContent[1]);
      template.setObjectsUsed(sectionContent[2]);
      template.setLocationOfPlay(sectionContent[3]);
      template.setDurationOfPlay(sectionContent[4]);
      template.setRulesOfPlay(sectionContent[5]);
      template.setPointSystem(sectionContent[6]);
      template.setHowToScore(sectionContent[7]);
      template.setHowToWin(sectionContent[8]);
      template.setExtraComments(sectionContent[9]);
    }
    return template;
  }

  public FinalTemplate parseFinalTemplateJson(String jsonFileLocation) {
    FinalTemplate template = new FinalTemplate();
    File jsonFile = new File(jsonFileLocation);
    try {
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      template = mapper.readValue(jsonFile, FinalTemplate.class);
    } catch (JsonParseException jpe) {
      jpe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return template;
  }

  public FinalTemplate[] parseFinalTemplateJsonArray(String jsonFileLocation) {
    FinalTemplate[] templates = new FinalTemplate[0];
    File jsonFile = new File(jsonFileLocation);
    try {
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      templates = mapper.readValue(jsonFile, FinalTemplate[].class);
    } catch (JsonParseException jpe) {
      jpe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return templates;
  }

  public String finalTemplateToJson(FinalTemplate template) {
    String mappedJsonString = "";
    try {
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
      // Write array as indented JSON string value
      mappedJsonString = mapper.writeValueAsString(template);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return mappedJsonString;
  }

  public String finalTemplateArrayToJson(FinalTemplate[] templates) {
    String mappedJsonString = "";
    try {
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
      // Write array as indented JSON string value
      mappedJsonString = mapper.writeValueAsString(templates);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return mappedJsonString;
  }
}
