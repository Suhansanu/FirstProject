/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseline;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.IOException;

/**
 *
 * @author suhansanu
 */
public class Tagger {

    public static int[]  filterTermsbyPOStagger(String input, int []token_id, int length ) throws IOException, ClassNotFoundException {
        String []tags = new String[length];  
        int    []temp = new int[length];
        for(int i =0 ; i < length ; i++) temp[i] =-1;
        int index, j, tok, valid;   // count denote the number of words in the term
        
        MaxentTagger tagger = new MaxentTagger("/Users/suhansanu/Downloads/stanford-postagger-2012-11-11/models/english-bidirectional-distsim.tagger");
        String tagged = tagger.tagString(input);
        
        String[] original_terms = tagged.split(" ");
        if(original_terms.length != length) System.err.println("String token by tagger not same as by tokenization via whitespace");
        
        
        for(int i=0; i< original_terms.length ; i++)
        {
            index = original_terms[i].indexOf("_");
            tags[i] = original_terms[i].substring(index+1);  
            //System.out.println(tags[i][0] + ":"+ tags[i][1]);
        }
        
        // we scan each valid term obatained by filtering the wordnet database to the new terms( noun, adjective , adverb and verb) 
        // obatined after running the Stanford Nlp POS tagger
        
        for(int i=0; i< length ; i++)
        {
            
            if(token_id[i]!= -1 && (tags[i].startsWith("NN")|| tags[i].startsWith("VB")||tags[i].startsWith("JJ")|| tags[i].startsWith("RB")) )
            {
                tok = token_id[i];
                for(j=0; j < length ; j++)
                {
                    if(token_id[j] == tok)
                        temp[j] = tok;
                }
            }
            
        }
        return temp;
        
    
    }
    
}
