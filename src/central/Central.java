package central;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.util.Arrays;
import java.util.List;
import java.lang.Object.*;

public class Central {

    

    
    
     public WordNetDatabase database = WordNetDatabase.getFileInstance();
     
        public Central() {
		
		database =WordNetDatabase.getFileInstance();
	}
	
	
	public List<Synset> getSynsets(String word) {
		
		return Arrays.asList(database.getSynsets(word));
	
	}
	
	
	public static String[] validTerms(String[] text_terms) {   // assuming the terms are distinct
                int    no_valid_terms = 0;
                String check= null;
                // checking for subsets of size 9 to 1 and stopping whenever found substring
                int sentence_size = text_terms.length;
                String []valid_terms = new String[sentence_size];

                for(int i = 9 ; i >= 1 ; i--)
                {
                    for(int j= 0; j<=sentence_size - i ;j++ )
                    {
                        check = "";
                        for(int k=j; k <i+j ; k++ ) {
                            check =check+ text_terms[k]+ " ";
                            
                        }
                        //System.out.println(check);
                        check = check.trim();
                        int status = checkTermInWordnet(check);
                        if(status !=0 ) {
                            //System.out.println("valid term: "+ check );
                            if(no_valid_terms== 0) {
                                valid_terms[no_valid_terms] = check;no_valid_terms++;
                            }
                            else{
                                int contain =0;
                                for(int l=0; l< no_valid_terms ; l++){
                                       if(valid_terms[l].contains(check)){
                                           contain =1;
                                        }

                                }
                                if(contain == 0)
                                {    valid_terms[no_valid_terms] = check;no_valid_terms++;}
                            }
                            
                        }
                    }
                }
                
		return valid_terms;
	}




	public static String[] splitSentence( String  text){
		String text_terms[] = text.split(" ");
		return text_terms;
	}

        public static int checkTermInWordnet(String check) {
                
                Central tester = new Central();		
		List<Synset> synset_list = tester.getSynsets(check);
                //System.out.println(synset_list.size());
                return (synset_list.size());
                
        }
        
	public static void showInputMessage(String text, String[] terms) {
                int sense_id;
                System.out.println("Input :" + text + "\n");
                for(int i=0; i < terms.length && terms[i] != null ; i++)
                    {
                    System.out.println("Terms :" + terms[i] + "  ");
                    Central tester = new Central();		
                    String check = terms[i] ;
                    List<Synset> synset_list = tester.getSynsets(check);
                    sense_id = 1;
                    for (Synset synset : synset_list) {
                        System.out.println("\t\t Sense "+ (sense_id++) + " : " +synset.getDefinition());
                    }
                System.out.println();
                }
	}
        
	public static String[][] originalLesk(String text, String[] terms) {
            String[][] correspondence = new String[terms.length][2];
            String[][] senses         = new String[terms.length][20];
            String[]   temp           = new String[200];
            String[]   overlap        = new String[200];
            int   []   senses_count   = new    int[terms.length];
            int   []   sense_id       = new    int[terms.length];
            int   []   best_sense_id  = new    int[terms.length];

            int        configurations = 1 , max_overlap = 0;
            Central tester = new Central();
            int count=0;            // number of terms that we have to resolve senses for
            for(int i=0; i < terms.length && terms[i] != null ; i++)
                {
                    int j=0;
                    List<Synset> synset_list = tester.getSynsets(terms[i]);
                    for (Synset synset : synset_list)
                    {
                        senses[i][j] = synset.getDefinition();
                        j++;
                    }
                    senses_count[i] = j;
                    configurations *= j;
                    count++;
                }
            // initial configuration of all the senses would be 0
            for(int i=0; i < terms.length; i++)   { sense_id[i] =0; best_sense_id[i] = 0; max_overlap =0;}
            
            
            // we select the different configurations by following arithmetic increase approach
            
            for(int config=0; config<configurations ; config++)
            {
                
                // selecting the temporary configuration
                int iterator =0 , i =0;
                while(iterator ==0 && i<terms.length)
                {
                    if(sense_id[i] < (senses_count[i]-1) )
                    { 
                        sense_id[i]++;
                        iterator = 1;
                    }
                    else {
                        sense_id[i] = 0;
                    }
                    i++;
                }
                
                temp    = senses[0][sense_id[0]].split(" ");
                overlap = senses[0][sense_id[0]].split(" ");
                // the configuration is now available in the sense_id array
                for(int j=1; j< count && numberOfTerms(overlap) != 0 ; j++)
                {
                    System.out.println(senses[j][sense_id[j]] + " "+ i + " "+ j);
                    overlap   = computeOverlap(temp, senses[j][sense_id[j]].split(" ") );
                    System.arraycopy(overlap, 0, temp, 0, temp.length);
                }
                
                if(max_overlap < numberOfTerms(overlap)) {
                    max_overlap = numberOfTerms(overlap);
                    System.arraycopy(sense_id, 0, best_sense_id, 0, terms.length);
                    System.out.println("Overlap size"+numberOfTerms(overlap));
                }
                
            }
            for(int i=0; i < count ; i++)       // each term
                {
                    correspondence[i][0] = terms[i]; 
                    correspondence[i][1] = senses[i][best_sense_id[i]];
                    System.out.println("Best sense for :"+ terms[i]+" = " + senses[i][best_sense_id[i]]);
                }
                
            return correspondence;
        }
          
         public static String[][] simplifiedLesk(String text, String[] terms) {
            String[][] correspondence = new String[terms.length][2];
            String[][] senses         = new String[terms.length][20];
            Central tester = new Central();
            int count=0;            // number of terms that we have to resolve senses for
            for(int i=0; i < terms.length && terms[i] != null ; i++)
                {
                    int j=0;
                    List<Synset> synset_list = tester.getSynsets(terms[i]);
                    for (Synset synset : synset_list)
                    {
                        senses[i][j] = synset.getDefinition();
                        j++;
                    }
                    count++;
                }
            
            for(int i=0; i < count ; i++)       // each term
                {
                    int best_sense  = 0;
                    int max_overlap  = 0;
                    String[] context = text.split(" ");
                    for(int j=0; j < senses[i].length && senses[i][j] !=null ; j++)  // sense j for each term i
                    {
                        //System.out.println("Overlapping for term "+ terms[i] +" :"+ senses[i][j]);
                        String[] signature = senses[i][j].split(" ");
                        String[] overlap   = computeOverlap(signature, context);
                        int size           = numberOfTerms (overlap  );
                        if(size > max_overlap)
                        {
                            max_overlap = size;
                            best_sense = j;
                        }
                    }
                    correspondence[i][0] = terms[i]; 
                    correspondence[i][1] = senses[i][best_sense];
                    System.out.println("Best sense for :"+ terms[i]+" = " + senses[i][best_sense] );
                }
            
            return correspondence;
         }
         
         
         private static String[] computeOverlap(String[] signature, String[] context) {
            String[] overlap = new String[signature.length];
            int i = 0,j = 0, count = 0;
            while( i < signature.length && signature[i]!=null)
            {
                j=0;
                //System.out.println(signature[i] + "  =  "+ context[j] + "  "+ context.length);
                while(j < context.length && context[j]!=null )
                {
                    if(signature[i].equalsIgnoreCase(context[j]))
                    {
                        overlap[count++]= signature[i];
                        break;
                    }
                    
                    j++;
                }
                i++;
            }
            return overlap;
         }

        private static int numberOfTerms(String[] overlap) {
            int i=0;
            while( i < overlap.length && overlap[i]!= null ) {
                i++;
            }
            
            return i;
        
        }
         


}
