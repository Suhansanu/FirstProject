/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package central;

import edu.smu.tspell.wordnet.impl.file.PropertyNames;
import java.io.IOException;

/**
 *
 * @author suhansanu
 */
public class Main {
    public static void main(String[] args) throws IOException,
            ClassNotFoundException {
		// TODO Auto-generated method stub
		System.setProperty(PropertyNames.DATABASE_DIRECTORY,"/usr/local/WordNet-3.0/dict/");
//		String text = "In such times as this united States of america is a great nation";
//		String text = "pine cone";
                String text = "this united States of america is";
//                String text = "We like icecream cone";
                String [] original_terms              = Central.splitSentence(text);
		String [] valid_wordnet_terms         = Central.validTerms(original_terms);
                String [] terms                       = Tagger.filterTermsbyPOStagger(text , valid_wordnet_terms);
                Central.showInputMessage(text , terms);
                System.out.println("\n\n==========Original Lesk ==========================\n");
                String[][] terms_sense_original_lesk    = Central.originalLesk  (text , terms);
                System.out.println("\n\n==========Simplified Lesk=========================\n");
                 String[][] terms_sense_simplified_lesk = Central.simplifiedLesk(text , terms);

		
	}
    
}
