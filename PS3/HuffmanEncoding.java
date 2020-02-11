
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

    public Map<Character, String> codeTree () {
        TreeData characterTree = priorityQueue().poll();
        Map<Character, String> codeMap = new TreeMap<Character, String>();
        traverser(codeMap, characterTree, "");

        return codeMap;
    }

    public void traverser (Map<Character, String> map, TreeData tree, String pathSoFar) {
        if (tree.isLeaf()) {
            map.put((tree.getKey()), pathSoFar);
            return;
        }

        if (tree.hasLeft()) {
            traverser(map, tree.getLeft(), pathSoFar + '0' );
        }

        if (tree.hasRight()) {
            traverser(map, tree.getRight(), pathSoFar + '1' );
        }
    }


    /**
     * Compression
     */

    public void compress (Map<Character, String> map) throws IOException{
        BufferedReader input = new BufferedReader(new FileReader(fileName));
        BufferedBitWriter bitOutput = new BufferedBitWriter("file_compressed.txt");
        int current = input.read();
        while (current != -1){
            String data = map.get((char) current);
                for (int i = 0; i < data.length(); i++){
                    char c = data.charAt(i);
                    if (c == '0'){
                        bitOutput.writeBit(false);
                    }
                    else if (c == '1'){
                        bitOutput.writeBit(true);
                    }
                }
            current = input.read();
        }
        input.close();
        bitOutput.close();
    }


    /**
     * Decompression
     */

    public void decompress (TreeData tree) throws IOException{
        BufferedWriter output = new BufferedWriter(new FileWriter("file_decompressed.txt"));
        BufferedBitReader bitInput = new BufferedBitReader("file_compressed.txt");

        while (bitInput.hasNext()) {
            TreeData copyTree = tree;
            boolean bit = bitInput.readBit();
            while (!tree.isLeaf()){
                if (bit == true){
                    copyTree = tree.getRight();
                }
                else {
                    copyTree = tree.getLeft();
                }
            }
        }
    }




    public static void main(String[] args) {

    }

}