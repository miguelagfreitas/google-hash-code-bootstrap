
package google_hash_code;

import java.util.ArrayList;
/**
 * 
 * @author Miguel Freitas
 * @param <R> Tipo de resultado
 */
public abstract class ResultList<R> {
    
    public ArrayList resultList;
    
    public ResultList(){
        this.resultList = new ArrayList<>();
    }
    
    public abstract long getScore();
    public abstract ArrayList getResults();
    
    public void addScore(R object){
        this.resultList.add(object);
    }
    
}
