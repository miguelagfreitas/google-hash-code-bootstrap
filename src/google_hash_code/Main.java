package google_hash_code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author Miguel Freitas
 */
public class Main {

    public static final String EXAMPLE_FILE = "example.in";
    public static final String SMALL_FILE = "small.in";
    public static final String MEDIUM_FILE = "medium.in";
    public static final String BIG_FILE = "big.in";

    public static BufferedReader br = null;
    public static FileReader fr = null;

    public static String[] files = null;

    public static void main(String[] args) {

        files = new String[3];

        files[0] = SMALL_FILE;
        files[1] = MEDIUM_FILE;
        files[3] = BIG_FILE;

        for (String inputFile : files) {

            String currentLine = "";

            try {

                fr = new FileReader(inputFile);
                br = new BufferedReader(fr);

                currentLine = br.readLine();

                //Initialize data from first line here
                
                
                //Read individual lines here
                while ((currentLine = br.readLine()) != null) {
                    
                }
                
                Results results = new Results();
                
                Algorithm a = new Algorithm();
                a.algorithm(results);
                
                System.out.println(results.getScore());

            } catch (Exception e) {

            }
        }

    }

    /*Algorithm class*/
    public static class Algorithm implements HashCodeAlgorithm {

        @Override
        public  void algorithm(ResultList r) {
            throw new UnsupportedOperationException("Not supported yet."); 
        }
        
    }

    /*Result list class*/
    public static class Results extends ResultList {

        @Override
        public long getScore() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ArrayList getResults() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
