import java.util.Comparator;

/**
 * This class implements a method to compare two trees
 *
 * @author: Stuart Hayes and Jacob Fyda, Dartmouth CS 10, Winter 2020
 */

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
