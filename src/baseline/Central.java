package baseline;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Central {

    public static String[] basicTokens(String input) {
        int length            = input.split(" ").length;
        String[]        basic = new String[length];
        String[] basic_tokens = input.split(" ");
        int count =0 ;
        int[] tokens = computeValidTokens(input);
        for(int i=0 ; i < length ; i++)
        {
            if(tokens[i] == -1 ){continue;}
            int tok = tokens[i];
            while(i < length && tok == tokens[i] )
            {
                basic[count] = basic_tokens[i] + " ";
                i++;
            }
            basic[count] = basic[count].trim();
            count++;
            i--;
        }
        return basic;
    }

    public static String[] lemmaTokens(String input) {
        int length            = input.split(" ").length;
        String[]        basic = new String[length];
        Lemmatizer lem = new Lemmatizer();
        String[] lemmatized_basic_tokens = lem.lemmatize(input);
        int count =0 ;
        int[] tokens = computeValidTokens(input);
        for(int i=0 ; i < length ; i++)
        {
            if(tokens[i] == -1 ){continue;}
            int tok = tokens[i];
            while(i < length && tok == tokens[i] )
            {
                basic[count] = lemmatized_basic_tokens[i] + " ";
                i++;
            }
            basic[count] = basic[count].trim();
            count++;
            i--;
        }
        return basic;
    }

    static String[][] allSenses(String[] basic_tokens) {
        int length          = basic_tokens.length;
        String[][] senses   = new String[length][20];
        Central tester      = new Central();
        for(int i=0; i < length && basic_tokens[i] != null ; i++)
            {
                int j=0;
                List<Synset> synset_list = tester.getSynsets(basic_tokens[i]);
                for (Synset synset : synset_list)
                {
                    senses[i][j] = synset.getDefinition().replaceAll("[^a-z A-Z]+","") ;
                    j++;
                }
            }
         return senses;
    }

    public static String[][][] senseTokens(String[] basic_tokens , String[][] senses , int length) {
        String[][][] sense_tokens = new String[length][20][100];
         for(int i=0 ; i < basic_tokens.length && basic_tokens[i]!=null; i++)
        { 
            for(int j=0 ; j < 20 && senses[i][j] != null ; j++)
            {
                sense_tokens[i][j]  = lemmaTokens(senses[i][j]);
            }
        }
        return sense_tokens;
    }
    
     public WordNetDatabase database = WordNetDatabase.getFileInstance();
        public Central() {
		database =WordNetDatabase.getFileInstance();
	}
	
	public List<Synset> getSynsets(String word) {
		return Arrays.asList(database.getSynsets(word));
	}
	

    private static void tokenEntry(int[] token_id, int j, int i, int i0) {
        for(int k=j ; k < i ; k++) {
            if(token_id[k] == -1)
                    token_id[k] = i0;
//            System.out.println("token#"+ i0 + " range :"+ j +" to" + i);
        }
    }

    private static int checkSuperset(int[] token_id, int i, int i0) {
        int token = token_id[i];
        for(int k=i ; k < i0 ; k++)
        {
            if(token_id[k] == -1) { // token yet not found
                return 1;
            }      
            if(token_id[k] != token) {  // new token overlaps two different tokens
                return 1;
            }      
        }
        return 0;
    }

    
    
       
	
        public static int[] computeValidTokens(String input) {
           
            String[] basic_tokens = input.split(" ");
            int      length       = basic_tokens.length;
            int[]    second_filter= new int[length];
            int[]    first_filter = existent_tokens(basic_tokens , length);
                    try { 
            second_filter= Tagger.filterTermsbyPOStagger(input, first_filter, length);
                    } catch (IOException ex) {
                        Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
                    }
//            Lemmatizer lem = new Lemmatizer();
//            String[] lemmatized_basic_tokens = lem.lemmatize(input);
//            for(int i=0; i< length ; i++)
//            {
//                System.out.println(basic_tokens[i] + " : " + lemmatized_basic_tokens[i] +":"+ first_filter[i] + " : " + second_filter[i] );
//            }
                    
                    
                    
                    
                    
             return second_filter;       
                    
        }
        
        private static int[] existent_tokens(String[] basic_tokens, int length) {
            int[] token_id = new int[length];
            for(int i=0 ; i<length; i++)
                    token_id[i] = -1; 
            
            int token_number = 0;
            
            
            
            int    no_valid_terms = 0;
            String check= null;
            // checking for subsets of size 9 to 1 and stopping whenever found substring
            String []valid_terms = new String[length];

                for(int i = 9 ; i >= 1 ; i--)
                {
                    for(int j= 0; j<=length - i ;j++ )
                    {
                        check = "";
                        for(int k=j; k <i+j ; k++ )
                        {
                            check =check+ basic_tokens[k]+ " ";
                            
                        }
                        //System.out.println(check);
                        check = check.trim();
                        int status = checkTermInWordnet(check);
                        if(status !=0 ) {
                            //System.out.println("valid term: "+ check );
                            if(no_valid_terms== 0) {
                                valid_terms[no_valid_terms] = check;no_valid_terms++;
                                tokenEntry(token_id , j , i+j , token_number);
                                token_number++;
//                                System.out.println("checking :::"+ check + token_number);
                            }
                            else{
                                int contain = checkSuperset(token_id , i , i+j);
                                if(contain == 1)
                                {    valid_terms[no_valid_terms] = check;no_valid_terms++;
                                     tokenEntry(token_id , j , i+j , token_number);
                                     token_number++;
//                                     System.out.println("checking :::"+ check + token_number);
                                }
                            }
                            
                        }
                    }
                }
                
		return token_id;
        }
        
        public static int checkTermInWordnet(String check) {
                
                Central tester = new Central();		
		List<Synset> synset_list = tester.getSynsets(check);
                //System.out.println(synset_list.size());
                return (synset_list.size());
                
        }

}
