/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseline;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author suhansanu
 */
public class Lemmatizer {
    

    protected StanfordCoreNLP pipeline;

    public Lemmatizer() {
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        this.pipeline = new StanfordCoreNLP(props);
    }

    public  String[] lemmatize(String documentText)
    {
        List<String> lemmas = new LinkedList<String>();
        Annotation document = new Annotation(documentText);
        this.pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
           
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                lemmas.add(token.get(CoreAnnotations.LemmaAnnotation.class));
            }
        }
        String[] strarray = lemmas.toArray(new String[0]);
        return strarray;
    }
    
}