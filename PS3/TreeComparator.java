import java.util.Comparator;

public class TreeComparator implements Comparator<TreeData> {

    @Override
    public int compare(TreeData x, TreeData y){
        if (x.getValue() < y.getValue()){
            return -1;
        }
        else if (x.getValue() == y.getValue()){
            return 0;
        }
        else{
            return 1;
        }
    }

}
