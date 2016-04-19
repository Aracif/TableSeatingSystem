public class ListCDLSBased implements ListInterface {
    // reference to linked list of items
    private Node head;
    private int numItems; // number of items in list

    public ListCDLSBased() {
        numItems = 0;
        head = null;
    }  // end default constructor

    public boolean isEmpty() {
        return numItems == 0;
    }  // end isEmpty

    public int size() {
        return numItems;
    }  // end size

    private Node findOld(int index) {
        // --------------------------------------------------
        // Locates a specified node in a linked list.
        // Precondition: index is the number of the desired
        // node. Assumes that 0 <= index <= numItems
        // Postcondition: Returns a reference to the desired
        // node.
        // --------------------------------------------------
        Node curr = head;
        for (int skip = 0; skip < index; skip++) {
            curr = curr.getNext();
        } // end for
        return curr;
    } // end find

    private Node findNew(int index) {
        Node curr = head;
        int secondHalf = numItems/2;


        if(index>=secondHalf) {
            for(int skip = numItems - 1; skip >= index; skip--) {
                curr = curr.getPrevious();
              }
        } else {
            for (int skip = 0; skip < index; skip++) {
                curr = curr.getNext();
               } // end for
        }
        return curr;
    } // end find

    public Object get(int index)
    throws ListIndexOutOfBoundsException {
        if (index >= 0 && index < numItems) {
            // get reference to node, then data in node
            Node curr = findNew(index);
            Object dataItem = curr.getItem();
            return dataItem;
        } else {
            throw new ListIndexOutOfBoundsException(
                "List index out of bounds exception on get");
        } // end if
    } // end get

    public void add(int index, Object item)
    throws ListIndexOutOfBoundsException {
        if (index >= 0 && index < numItems+1) {
            Node newNode = new Node(item);
            if(index!=0 &&numItems != 0) {
                Node prev = findNew(index-1);
                newNode.setNext(prev.getNext());
                prev.getNext().setPrevious(newNode);
                prev.setNext(newNode);
                newNode.setPrevious(prev);
            }

            else if (index == 0 && numItems != 0) {
                head.getPrevious().setNext(newNode);
                newNode.setPrevious(head.getPrevious());
                newNode.setNext(head);
                head.setPrevious(newNode);
                head = newNode;
            } else{
                head = newNode;
            }
            numItems++;
        } else {
            throw new ListIndexOutOfBoundsException(
                "List index out of bounds exception on add");
        } // end if
    }  // end add

    public void remove(int index)
    throws ListIndexOutOfBoundsException {
        if (index >= 0 && index < numItems) {
            if(index != 0 && numItems != 0) {
                Node prev = findNew(index-1);
                // delete the node after the node that prev
                // references, save reference to node
                Node curr = prev.getNext();
                prev.setNext(curr.getNext());
            } // end if
            else if (index == 0 && numItems >= 2) {
                // delete the first node from the list
                head.getNext().setPrevious(head.getPrevious());
                head.getPrevious().setNext(head.getNext());
                head = head.getNext();
            } else if ( index == 0 && numItems == 1) {
                head = null;
            }

            numItems--;
        } // end if
        else {
            throw new ListIndexOutOfBoundsException(
                "List index out of bounds exception on remove");
        } // end if
    }   // end remove

    public void removeAll() {
        // setting head to null causes list to be
        // unreachable and thus marked for garbage
        // collection
        head = null;
        numItems = 0;
    } // end removeAll

    public String toString() {
        String buildString = "";
        int index = 0;
        for(Node curr = head; index<numItems; curr = curr.getNext()) {
            String currentItem = ((String)curr.getItem());
            buildString += currentItem + " ";
            index++;
        }

        return buildString;
    }

} // end ListReferenceBased
