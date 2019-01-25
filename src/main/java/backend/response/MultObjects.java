package backend.response;

import java.util.List;

public interface MultObjects<T> {

    public List<T> getResult ();

    public void setResult(List<T> result);
        
}