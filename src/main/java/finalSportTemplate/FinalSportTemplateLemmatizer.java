package finalSportTemplate;

import nlp.ContentProcessor;
import serde.FinalTemplateSerDe;
import serde.models.FinalTemplate;
import serde.models.LemmatizedFinalTemplate;

public class FinalSportTemplateLemmatizer {
  public static LemmatizedFinalTemplate[] lemmatizeTemplatesFromFile(String fileLocation) {
    FinalTemplateSerDe serde = new FinalTemplateSerDe();
    FinalTemplate[] templates = serde.parseFinalTemplateJsonArray(fileLocation);

    return lemmatizeTemplates(templates);
  }

  public static LemmatizedFinalTemplate[] lemmatizeTemplates(FinalTemplate[] templates) {
    ContentProcessor cp = new ContentProcessor();
    LemmatizedFinalTemplate[] lemmedTemplates = new LemmatizedFinalTemplate[templates.length];

    for (int i = 0; i < templates.length; i++) {
      LemmatizedFinalTemplate current = new LemmatizedFinalTemplate(templates[i]);

      current.setTaggedUnigramTokens(cp.getTaggedUnigramTokens(templates[i].content()));
      current.setTaggedMultigramTokens(cp.getTaggedMultigramTokens(templates[i].content()));

      lemmedTemplates[i] = current;
    }

    return lemmedTemplates;
  }
}
