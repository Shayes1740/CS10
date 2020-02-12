import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.*;
import java.io.IOException;


public class HuffmanEncoding {

    private static final String fileName = "inputs/WarAndPeace.txt"; // file name to test

    /**
     * frequencyTable() generates a map that maps each character in a document to the number of times it appears
     * in the document.
     */
    public static Map <Character, Integer> frequencyTable () {
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
                System.out.println("Error Reading");
            }
        }
        return wordCounts;
    }

    /**
     * Priority queue
     */

    public static PriorityQueue<TreeData> priorityQueue (Map <Character, Integer> frequencyTable) throws IOException {
        Comparator<TreeData> treeCompare = new TreeComparator();
        if (frequencyTable.keySet().size() == 0) throw new IOException("No keys found (file is empty)." );
        PriorityQueue<TreeData> pQueue = new PriorityQueue<TreeData>(frequencyTable.size(), treeCompare);
        for (Character k : frequencyTable.keySet()) {
            TreeData initTree = new TreeData(k, frequencyTable.get(k));
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

    public static Map<Character, String> codeTree (TreeData tree) throws IOException{
        TreeData characterTree = tree;
        Map<Character, String> codeMap = new TreeMap<Character, String>();
        if (characterTree != null) {
            traverser(codeMap, characterTree, "");
        }
        return codeMap;
    }

    public static void traverser (Map<Character, String> map, TreeData tree, String pathSoFar) {
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

    public static void compress (Map<Character, String> map) throws IOException{
        BufferedReader input = new BufferedReader(new FileReader(fileName));
        BufferedBitWriter bitOutput = new BufferedBitWriter(fileName.substring(0, fileName.indexOf('.')) + "_compressed.txt");
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

    public static void decompress (TreeData tree) throws IOException{
        BufferedWriter output = new BufferedWriter(new FileWriter(fileName.substring(0, fileName.indexOf('.')) + "_decompressed.txt"));
        BufferedBitReader bitInput = new BufferedBitReader(fileName.substring(0, fileName.indexOf('.')) + "_compressed.txt");
        TreeData copyTree = tree;

        if (copyTree == null) throw new IOException();

        else if (copyTree != null && copyTree.isLeaf()){
            while (bitInput.hasNext()) {
                boolean bit = bitInput.readBit();
                output.write(copyTree.getKey());
                copyTree = tree;
            }
        }

        else if (copyTree != null && !copyTree.isLeaf()) {
            while (bitInput.hasNext()) {
                if (copyTree.isLeaf()){
                    output.write(copyTree.getKey());
                    copyTree = tree;
                }
                else {
                    boolean bit = bitInput.readBit();
                    if (bit == true) {
                        copyTree = copyTree.getRight();
                    } else {
                        copyTree = copyTree.getLeft();
                    }
                }
            }
            if (copyTree.isLeaf()){
                output.write(copyTree.getKey());
                copyTree = tree;
            }
            else {
                boolean bit = bitInput.readBit();
                if (bit == true) {
                    copyTree = copyTree.getRight();
                } else {
                    copyTree = copyTree.getLeft();
                }
            }
        }
        output.close();
        bitInput.close();
    }




    public static void main(String[] args) throws IOException{
        Map <Character, Integer> freqTable;
        TreeData treeData;
        Map <Character, String> codeTree;

        freqTable = frequencyTable();
        treeData = priorityQueue(freqTable).poll();
        codeTree = codeTree(treeData);

        for (Character c : codeTree.keySet()) {
            System.out.println(c + ":  " + freqTable.get(c));
        }

        compress(codeTree);
        decompress(treeData);

    }

}