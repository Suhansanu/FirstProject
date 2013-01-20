
package baseline;

import edu.smu.tspell.wordnet.impl.file.PropertyNames;

public class Baseline {

    public static void main(String[] args) {
        System.setProperty(PropertyNames.DATABASE_DIRECTORY,"/usr/local/WordNet-3.0/dict/");
        String    input        = "pine cone";       
       
        String[] basic_tokens  = Central.basicTokens(input);
        String[] lemma_tokens  = Central.lemmaTokens(input);
        String[][]     senses  = Central.allSenses(basic_tokens);
        String[][][] sense_tokens= Central.senseTokens(basic_tokens , senses , basic_tokens.length);
//        for(int i=0 ; i < basic_tokens.length && basic_tokens[i]!=null; i++)
//        { 
//            System.out.println("\ntoken : " + basic_tokens[i] + " : "+ lemma_tokens[i]);
//            for(int j=0 ; j < 20 && senses[i][j] != null ; j++)
//            {
//                System.out.println("Sense " + j + " : " + senses[i][j] );
//            }
//        }
//         System.out.println("\n\n==========Simplified Lesk=========================\n");
//         simplifiedLesk(basic_tokens,senses, sense_tokens);
         System.out.println("\n\n==========Original Lesk=========================\n");
         originalLesk(basic_tokens,senses, sense_tokens);
    }

    private static void simplifiedLesk(String[]basic_tokens ,String[][] senses, String[][][] sense_tokens) {
        String[] context = new String[basic_tokens.length];
        System.arraycopy(basic_tokens, 0, context, 0, basic_tokens.length);
         for(int i=0; i < basic_tokens.length && basic_tokens[i]!= null ; i++)       // each term
                {
                    int best_sense   = 0;
                    int max_overlap  = 0;
                    
                    for(int j=0; j < senses[i].length && senses[i][j] !=null ; j++)  // sense j for each term i
                    {
                        //System.out.println("Overlapping for term "+ terms[i] +" :"+ senses[i][j]);
                        String[] overlap   = computeOverlap(sense_tokens[i][j], context);
                        int size           = numberOfTerms (overlap  );
                        if(size > max_overlap)
                        {
                            max_overlap = size;
                            best_sense = j;
                        }
                    }
                    System.out.println("Best sense for :"+ basic_tokens[i] +" = " + senses[i][best_sense] );
                }
            
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

    private static void originalLesk(String[] basic_tokens, String[][] senses, String[][][] sense_tokens) {
            int length = basic_tokens.length;
            String[]   temp           = new String[100];
            String[]   overlap        = new String[100];
            int   []   senses_count   = new    int[length];
            int   []   sense_id       = new    int[length];
            int   []   best_sense_id  = new    int[length];

            int        configurations = 1 , max_overlap = 0;
           
            int count=0;            // number of terms that we have to resolve senses for
            for(int i=0; i < length && basic_tokens[i] != null ; i++)
                {
                    int j=0;
                    while(senses[i][j]!= null) j++;
                    senses_count[i] = j;
                    configurations *= j;
                    count++;
                }
            // initial configuration of all the senses would be 0
            for(int i=0; i < length; i++)   { sense_id[i] =0; best_sense_id[i] = 0; max_overlap =0;}
            
            
            // we select the different configurations by following arithmetic increase approach
            
            for(int config=0; config<configurations ; config++)
            {
                
                // selecting the temporary configuration
                int iterator =0 , i =0;
                while(iterator ==0 && i<length)
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
                System.arraycopy(sense_tokens[0][sense_id[0]], 0, temp, 0, sense_tokens[0][0].length);
                System.arraycopy(sense_tokens[0][sense_id[0]], 0, overlap, 0, sense_tokens[0][0].length);
//                temp    = senses[0][sense_id[0]].split(" ");
//                overlap = senses[0][sense_id[0]].split(" ");
                // the configuration is now available in the sense_id array
                for(int j=1; j< count && numberOfTerms(overlap) != 0 ; j++)
                {
//                    System.out.println(senses[j][sense_id[j]] + " "+ i + " "+ j);
                    overlap   = computeOverlap(temp, sense_tokens[j][sense_id[j]] );
                    System.arraycopy(overlap, 0, temp, 0, temp.length);
                }
                
                if(max_overlap < numberOfTerms(overlap)) {
                    max_overlap = numberOfTerms(overlap);
                    System.arraycopy(sense_id, 0, best_sense_id, 0, length);
                    System.out.println("Overlap size"+numberOfTerms(overlap));
                }
                
            }
            for(int i=0; i < count ; i++)       // each term
                {
//                    correspondence[i][0] = terms[i]; 
//                    correspondence[i][1] = senses[i][best_sense_id[i]];
                    System.out.println("Best sense for :"+ basic_tokens[i]+" = " + senses[i][best_sense_id[i]]);
                }
                
    }
}
