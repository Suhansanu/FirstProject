/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package central;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.IOException;

/**
 *
 * @author suhansanu
 */
public class Tagger {

    static String[] filterTermsbyPOStagger(String text, String[] terms) throws IOException, ClassNotFoundException {
        String []valid_terms = new String[terms.length];       // 0-th index denote the term and 1-th index denote its tag
        String [][]original_terms_with_tags = new String[text.split(" ").length][2];  
        int index, count, match, valid;   // count denote the number of words in the term
        
        MaxentTagger tagger = new MaxentTagger("/Users/suhansanu/Downloads/stanford-postagger-2012-11-11/models/english-bidirectional-distsim.tagger");
        String tagged = tagger.tagString(text);
        
        String[] original_terms = tagged.split(" ");
        
        for(int i=0; i< original_terms.length ; i++)
        {
            index = original_terms[i].indexOf("_");
            original_terms_with_tags[i][0] = original_terms[i].substring(0,index);
            original_terms_with_tags[i][1] = original_terms[i].substring(index+1);  
            //System.out.println(original_terms_with_tags[i][0] + ":"+ original_terms_with_tags[i][1]);
        }
        
        // we scan each valid term obatained by filtering the wordnet database to the new terms( noun, adjective , adverb and verb) 
        // obatined after running the Stanford Nlp POS tagger
        index =0;
        for(int i=0; i< terms.length && terms[i]!= null ; i++)
        {
            String[] temp = terms[i].split(" ");
            count =  temp.length;
            for(int k=0 ; k<original_terms_with_tags.length ; k++)
                    {
                        match =0; valid =1;
                        for(int j=0; j< count ; j++)
                        {
                            if(!temp[j].equals(original_terms_with_tags[k+j][0]))
                            {
                               valid =0;break;
                            }
                            if(original_terms_with_tags[k+j][1].startsWith("NN")|| original_terms_with_tags[k+j][1].startsWith("VB")||original_terms_with_tags[k+j][1].startsWith("JJ")|| original_terms_with_tags[k+j][1].startsWith("RB") )
                            {
                                match =1;
                            }
                        }
                        if(match == 1 && valid == 1)
                        {
                            valid_terms[index++] = terms[i]; //System.out.println("RESULT:" + terms[i]);
                        }
                     
                }
            //System.out.println(terms[i] + ":"+ count);
            
            
        }
        
        
        return valid_terms;
    
    }
    
}
