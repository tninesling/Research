package serde;

import serde.models.TermFrequencyMatrix;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class TermFrequencyMatrixSerDe {
  private static ObjectMapper mapper = new ObjectMapper();

  public static TermFrequencyMatrix parseTermFrequencyMatrixJson(String jsonFileLocation) {
    File jsonFile = new File(jsonFileLocation);
    TermFrequencyMatrix outVal = new TermFrequencyMatrix();
    try {
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      outVal = mapper.readValue(jsonFile, outVal.getClass());
    } catch (JsonParseException jpe) {
      jpe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return outVal;
  }

  public static String termFrequencyMatrixToJson(TermFrequencyMatrix matrix) {
    String mappedJsonString = "";
    try {
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
      // Write matrix as indented JSON string value
      mappedJsonString = mapper.writeValueAsString(matrix);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return mappedJsonString;
  }
}
