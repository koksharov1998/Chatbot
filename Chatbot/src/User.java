import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class User {
    User(){
        mark=0;
    }

    private HashSet<Integer> qestions;
    private int mark;

    public boolean contains(int ind){
        return qestions.contains(ind);
    }

    public void pushQestions(int ind){
        qestions.add(ind);
    }

    public int getMark() {
        return mark;
    }

    public void upMark(){
        mark++;
    }
}
