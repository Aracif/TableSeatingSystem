//please note that this code is different from the textbook code, because the data is encapsulated!

public class Node {
    private Object item;
    private Node next;
    private Node back;


    public Node(Object newItem) {
        item = newItem;
        next = this;
        back = this;
    } // end constructor

    public Node(Object newItem, Node nextNode) {
        item = newItem;
        next = nextNode;

    } // end constructor

    public Node(Object newItem, Node next, Node back) {
        item = newItem;
        this.next = next;
        this.back = back;
    }


    public Object getItem() {
        return item;
    } // end getItem

    public void setNext(Node nextNode) {
        next = nextNode;
    } // end setNext

    public Node getNext() {
        return next;
    } // end getNext

    public Node getPrevious() {
        return back;
    }

    public void setPrevious(Node previousNode) {
        back = previousNode;
    }
} // end class Node