/**
 * A singly-linked list implementation of the SimpleList interface
 * Now with tail pointer
 * Template for SA4
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012,
 * based on a more feature-ful version by Tom Cormen and Scot Drysdale
 * @author CBK, Spring 2016, cleaned up inner class, extended testing
 * @author Jacob Fyda, Dartmouth CS 10, Winter 2020
 */
public class SinglyLinkedHT<T> implements SimpleList<T> {
    private Element head;	// front of the linked list
    private Element tail;	// end
    private int size;		// # elements in the list

    /**
     * The linked elements in the list: each has a piece of data and a next pointer
     */

    private class Element {
        private T data;
        private Element next;

        private Element(T data, Element next) {
            this.data = data;
            this.next = next;
        }
    }

    public SinglyLinkedHT() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    /**
     * Helper function, advancing to the nth Element in the list and returning it
     * (exception if not that many elements)
     */
    private Element advance(int n) throws Exception {
        Element e = head;
        while (n > 0) {
            // Just follow the next pointers
            e = e.next;
            if (e == null) throw new Exception("invalid index");
            n--;
        }
        return e;
    }

    public void add(int idx, T item) throws Exception {
        if (idx == 0) {
            // Insert at head
            head = new Element(item, head);
            if (size == 0) {
                tail = head;
            }
        }else if (idx == this.size) {
            // Insert at tail
            tail.next = new Element(item, tail.next);
            tail = tail.next;

        }
        else {
            // It's the next thing after element # idx-1
            Element e = advance(idx-1);
            // Splice it in
            e.next = new Element(item, e.next);
        }
        size++;
    }

    public void remove(int idx) throws Exception {
        if (idx == 0) {
            // Just pop off the head
            if (head == null) throw new Exception("invalid index");
            head = head.next;
            if (size == 0) { tail = null; }
        }
        else if (idx == this.size) {
            // Just pop off the tail
            if (tail == null) throw new Exception("invalid index");
            Element e = advance(idx-1);
            tail = e;
        }
        else {
            // It's the next thing after element # idx-1
            Element e = advance(idx-1);
            if (e.next == null) throw new Exception("invalid index");
            // Splice it out
            e.next = e.next.next;
            if (e.next == null) { tail = e; }
        }
        size--;
    }

    public T get(int idx) throws Exception {
        Element e = advance(idx);
        return e.data;
    }

    public void set(int idx, T item) throws Exception {
        Element e = advance(idx);
        e.data = item;
    }

    /**
     * Appends other to the end of this in constant time, by manipulating head/tail pointers
     */

    public void append(SinglyLinkedHT<T> other) {
        if (size == 0 && other.size == 0) {
            System.out.println("both lists empty");
        }
        else if (size == 0 && other.size != 0) {
            head = other.head;
            tail = other.tail;
            size = other.size;
            System.out.println("first list empty");
        }
        else if (other.size == 0 && size != 0) {
            System.out.println("second list empty");
        }
        else {

            tail.next = other.head;
            tail = other.tail;
            size = size + other.size;
        }
    }

    public String toString() {
        String result = "";
        for (Element x = head; x != null; x = x.next)
            result += x.data + "->";
        result += "[/]";

        return result;
    }

    public static void main(String[] args) throws Exception {
        SinglyLinkedHT<String> list1 = new SinglyLinkedHT<String>();
        SinglyLinkedHT<String> list2 = new SinglyLinkedHT<String>();

        System.out.println(list1 + " + " + list2);
        list1.append(list2);
        System.out.println(" = " + list1);

        list2.add(0, "a");
        System.out.println(list1 + " + " + list2);
        list1.append(list2);
        System.out.println(" = " + list1);

        list1.add(1, "b");
        list1.add(2, "c");
        SinglyLinkedHT<String> list3 = new SinglyLinkedHT<String>();
        System.out.println(list1 + " + " + list3);
        list1.append(list3);
        System.out.println(" = " + list1);

        SinglyLinkedHT<String> list4 = new SinglyLinkedHT<String>();
        list4.add(0, "z");
        list4.add(0, "y");
        list4.add(0, "x");
        System.out.println(list1 + " + " + list4);
        list1.append(list4);
        System.out.println(" = " + list1);

        list1.remove(5);
        list1.remove(4);
        SinglyLinkedHT<String> list5 = new SinglyLinkedHT<String>();
        list5.add(0, "z");
        list5.add(0, "y");
        System.out.println(list1 + " + " + list5);
        list1.append(list5);
        System.out.println(" = " + list1);
    }
}