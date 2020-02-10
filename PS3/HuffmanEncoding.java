
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.*;
import java.io.IOException;


public class HuffmanEncoding {

    private static final String fileName = "inputs/USConstitution.txt"; // where all the files are



    /**
     * frequencyTable() generates a map that maps each character in a document to the number of times it appears
     * in the document.
     */
    public Map <Character, Integer> frequencyTable () {
        String test = "We won't go to all that trouble here.  This string contains multiple words. And multiple copies of multiple words.  And multiple words with multiple copies";
        Map<Character,Integer> wordCounts = new TreeMap<Character,Integer>();
        BufferedReader input = null;
        int current = 0;

        try {
            input = new BufferedReader(new FileReader(fileName));
        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot open file.\n" + e.getMessage());
            return wordCounts;
        }
        try {
            current = input.read();
        }
        catch (IOException e1) {
            System.out.println("Empty File");
        }

        while (current != -1) {
            char character = (char) current;
            if (wordCounts.containsKey(character)){
                wordCounts.put(character, wordCounts.get(character) + 1);
            }
            else {
                wordCounts.put(character, 1);
            }
            try {
                current = input.read();
            }
            catch (IOException e1) {
                System.out.println("Empty File");
            }
        }
        return wordCounts;
    }

    /**
     * Priority queue
     */

    public PriorityQueue<TreeData> priorityQueue () {
        Comparator<TreeData> treeCompare = new TreeComparator();
        PriorityQueue<TreeData> pQueue = new PriorityQueue<TreeData>(frequencyTable().size(), treeCompare);
        for (Character k : frequencyTable().keySet()) {
            TreeData initTree = new TreeData(k, frequencyTable().get(k));
            pQueue.add(initTree);
        }

        while (pQueue.size() > 1) {
            TreeData T1 = pQueue.peek();
            pQueue.remove(T1);

            TreeData T2 = pQueue.peek();
            pQueue.remove(T2);

            Character root = '▒';

            TreeData newTree = new TreeData(root, (T1.getValue() + T2.getValue()), T1, T2);
            pQueue.add(newTree);

        }

        return pQueue;
    }

    /**
     * Code retrieval
     */

//    public Map<Character, Integer> codeTree () {
//        TreeData characterTree = priorityQueue().poll();
//
//
//        return;
//    }
//
////    public BinaryTree<E> copyToDepth(int d) {
////        if (d == 0) {
////            return new BinaryTree<E>(data);
////        }
////        BinaryTree<E> l = null;
////        BinaryTree<E> r = null;
////        if (hasLeft()) {
////            l = left.copyToDepth(d-1);
////        }
////        if (hasRight()) {
////            r = right.copyToDepth(d-1);
////        }
////        return new BinaryTree<E>(data, l, r);
////    }

    /**
     * Compression
     */


    /**
     * Decompression
     */

    public static void main(String[] args) {

    }

}