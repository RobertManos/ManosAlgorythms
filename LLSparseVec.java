
public class LLSparseVec implements SparseVec {

    private static class Node {

        private int index;
        private int element;
        private Node next;

        public Node(int idx, int e, Node n) {
            index = idx;
            element = e;
            next = n;
        }

        public int getIndex() {
            return index;
        }

        public int getElement() {
            return element;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node n) {
            next = n;
        }

        public void setElement(int ele) {
            element = ele;
        }
    }
    public Node head = null;//me
    public Node tail=null;
    private int NumElements = 0;
    private int length;

    public LLSparseVec(int len) {
        this.length = len;
        return;
    }

//    public Node gethead() {
//        return this.head;
//    }
//public Node getTail(){
//    return this.tail;
//}


public void setTail(Node e){
    tail=e;
}
    @Override
    public int getLength() {
        // TODO Auto-generated meth
        return this.length;
    }

    @Override
    public int numElements() {
        return NumElements;
    }

    @Override
    public int getElement(int idx) {
        // TODO Auto-generated method stub
        //What to do if empty??    
        //if(NumElements==0) return ???;
        Node current = head;
        while (current != null) {
            if (current.getIndex() == idx) {
                return current.getElement();
            }
            current = current.getNext();
        }
        return 0;
    }

    @Override
    public void clearElement(int idx) {
        // TODO Auto-generated method stub
        //clear head
        if (idx == head.getIndex()) {
            head = head.getNext();
            NumElements--;
            return;
        }

        Node current = head;
        while (current.getNext() != null) {
            if (idx == current.getNext().getIndex()) {
                current.setNext(current.getNext().getNext());
                NumElements--;
                return;
            }
            current = current.getNext();

        }

    }

    @Override
    public void setElement(int idx, int val) {
        if (idx >= this.length || idx < 0) {
            return; //if idx is too large or small
        }       //if method call tries to set zero element
        if (val == 0) {
            return;
        }
        //if the ll is empty
        Node e = new Node(idx, val, null);
        if (NumElements == 0) {

            head = e;

            NumElements++;
            return;
        }

        //check if greater than last or less than first
        if (idx < head.getIndex()) {
            e.setNext(head);
            head = e;
            NumElements++;
            return;
        }
//        if (idx > tail.getIndex()) {
//            Node e = new Node(idx, val, null);
//            tail.setNext(e);
//            tail = e;
//             NumElements++;
//            return;
//        }

        // check to insert between current and current.next
        //or check to see if last iteration of loop and insert at the end
        Node curr = head;
        while (curr != null) {
            if (curr.getNext() == null || idx > curr.getIndex() && idx < curr.getNext().getIndex()) {
                e.setNext(curr.getNext());
                curr.setNext(e);
                NumElements++;
                return;
            }
            //update a value    
            if (curr.getIndex() == idx) {
                curr.setElement(val);
                return;
            }

            curr = curr.getNext();
        }
    }

    @Override
    public int[] getAllIndices() {
        // TODO Auto-generated method stub
        int[] idxArray = new int[this.NumElements];
        Node curr = head;
        for (int i = 0; i < NumElements; i++) {
            idxArray[i] = curr.getIndex();
            curr = curr.getNext();
        }
        return idxArray;
    }

    @Override
    public int[] getAllValues() {
        // TODO Auto-generated method stub
        int[] valueArray = new int[this.NumElements];
        Node curr = head;
        for (int i = 0; i < NumElements; i++) {
            valueArray[i] = curr.element;
            curr = curr.getNext();
        }
        return valueArray;

    }
public void append(Node e){
     if (this.numElements() == 0) {
                    head = e;
                    tail=e;
                    NumElements++;
                    return;
                } 
                tail.setNext(e);
                   tail=e;
                    NumElements++;
                    
}
    @Override
    public SparseVec addition(SparseVec otherV) {

        if (this.getLength() != otherV.getLength()) {
            return null;//must be of equal length
        }
        LLSparseVec newVec = new LLSparseVec(this.getLength());
        int[] idx = this.getAllIndices();
        int[] value = this.getAllValues();
        int i = 0;

        int[] otherIdx = otherV.getAllIndices();
        int[] otherValue = otherV.getAllValues();
        int j = 0;

        while (i < idx.length && j < otherIdx.length) {
            if (idx[i] == otherIdx[j]) {
                int val = value[i] + otherValue[j];
                if (val == 0) {
                    i++;
                    j++;
                    continue;

                }
                //if the ll is empty
                Node e = new Node(idx[i], val, null);
                newVec.append(e);
                i++;
                    j++;
                continue;
                }
                //(idx[i], value[i]+otherValue[j]);

            
            if (idx[i] < otherIdx[j]) {
                // newVec.setElement(idx[i], value[i]);
                Node e = new Node(idx[i], value[i], null);
                if (newVec.numElements() == 0) {

                    newVec.append(e);
                    i++;
                    continue;
                }
            }
            if (idx[i] > otherIdx[j]) {
                // newVec.setElement(otherIdx[j], otherValue[j]);
                //j++;
                Node e = new Node(otherIdx[j], otherValue[j], null);
               newVec.append(e);
               j++;

                }
            }
        
            while (i < idx.length) {
//            newVec.setElement(idx[i], value[i]);
//            i++;
                Node e = new Node(idx[i], value[i], null);
                newVec.append(e);
                i++;
            }

            while (j < otherIdx.length) {
//            newVec.setElement(otherIdx[j], otherValue[j]);
//            j++;

                Node e = new Node(otherIdx[j], otherValue[j], null);
                
                   newVec.append(e);
                j++;
            }

            return newVec;
        }
    
        @Override
        public SparseVec substraction
        (SparseVec otherV
        
            ) {

        if (this.NumElements != otherV.numElements()) {
                return null;//must be of equal length
            }
            SparseVec newVec = new LLSparseVec(this.getLength());
            int[] idx = this.getAllIndices();
            int[] value = this.getAllValues();
            int i = 0;

            int[] otherIdx = otherV.getAllIndices();
            int[] otherValue = otherV.getAllValues();
            int j = 0;

            while (i < idx.length && j < otherIdx.length) {
                if (idx[i] == otherIdx[j]) {
                    newVec.setElement(idx[i], value[i] - otherValue[j]);
                    i++;
                    j++;
                    continue;
                }
                if (idx[i] < otherIdx[j]) {
                    newVec.setElement(idx[i], value[i]);
                    i++;
                    continue;
                }
                if (idx[i] > otherIdx[j]) {
                    newVec.setElement(otherIdx[j], otherValue[j]);
                    j++;

                }
            }
            while (i < idx.length) {
                newVec.setElement(idx[i], value[i]);
                i++;
            }
            while (j < otherIdx.length) {
                newVec.setElement(otherIdx[j], otherValue[j]);
                j++;
            }

            return newVec;
        }

        @Override
        public SparseVec multiplication
        (SparseVec otherV
        
            ) {

        if (this.NumElements != otherV.numElements()) {
                return null;//must be of equal length
            }
            SparseVec newVec = new LLSparseVec(this.getLength());
            int[] idx = this.getAllIndices();
            int[] value = this.getAllValues();
            int i = 0;

            int[] otherIdx = otherV.getAllIndices();
            int[] otherValue = otherV.getAllValues();
            int j = 0;

            while (i < idx.length && j < otherIdx.length) {
                if (idx[i] == otherIdx[j]) {
                    newVec.setElement(idx[i], value[i] * otherValue[j]);
                    i++;
                    j++;
                    continue;
                }
                if (idx[i] < otherIdx[j]) {
                    newVec.setElement(idx[i], value[i]);
                    i++;
                    continue;
                }
                if (idx[i] > otherIdx[j]) {
                    newVec.setElement(otherIdx[j], otherValue[j]);
                    j++;

                }
            }
            while (i < idx.length) {
                newVec.setElement(idx[i], value[i]);
                i++;
            }
            while (j < otherIdx.length) {
                newVec.setElement(otherIdx[j], otherValue[j]);
                j++;
            }

            return newVec;
        }
    }
