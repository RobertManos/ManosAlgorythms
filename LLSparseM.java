
public class LLSparseM implements SparseM {

    private static class Node {

        private int rowId;
        private int colId;
        private int value;
        private Node colNext;
        private Node rowNext;

        public Node(int rid, int cid, int val, Node rnext, Node cnext) {
            rowId = rid;
            colId = cid;
            value = val;
            colNext = cnext;
            rowNext = rnext;

        }

        public int getColIndex() {
            return colId;
        }

        public int getRowIndex() {
            return rowId;
        }

        public int getValue() {
            return value;
        }

        public Node getRowNext() {
            return rowNext;
        }

        public Node getColNext() {
            return colNext;
        }

        public void setRowNext(Node n) {
            rowNext = n;
        }

        public void setColNext(Node n) {
            colNext = n;
        }

        public void setValue(int val) {
            value = val;
        }
    }

    private static class HeadNode {

        private int id;
        private HeadNode nextHead;
        private Node firstElement;

        public HeadNode(int idx, HeadNode nhead, Node felement) {
            id = idx;

            nextHead = nhead;
            firstElement = felement;

        }

        public int getID() {
            return id;
        }

        public HeadNode getNextHead() {
            return nextHead;
        }

        public Node getFirstElement() {
            return firstElement;
        }

        public void setHeadNext(HeadNode n) {
            nextHead = n;

        }

        public void setFirstElement(Node n) {
            firstElement = n;
        }

    }

    private int nrows, ncols; 	// number of rows and columns of the matrix
    private int nelements = 0; 	// number of nonzero elements, initialized to be zero
    private HeadNode colHead = null;
    private HeadNode rowHead = null;

    public LLSparseM(int nr, int nc) {
        if (nr <= 0) {
            nr = 1;	// if zero or negative nr, set nr = 1;
        }
        if (nc <= 0) {
            nc = 1;	// if zero or negative nc, set nc = 1;	
        }
        this.nrows = nr;
        this.ncols = nc;

    }

    public HeadNode getColhead() {
        return colHead;
    }

    public HeadNode getRowhead() {
        return rowHead;
    }

    public void setColhead(HeadNode e) {
        colHead = e;
    }

    public void setRowhead(HeadNode e) {
        rowHead = e;
    }

    @Override
    public int nrows() {

        // TODO Auto-generated method stub
        return nrows;
    }

    @Override
    public int ncols() {
        // TODO Auto-generated method stub
        return ncols;
    }

    @Override
    public int numElements() {
        // TODO Auto-generated method stub
        return nelements;
    }

    @Override
    public int getElement(int ridx, int cidx) {
        HeadNode currentCol = colHead;
        while (currentCol != null) {
            if (currentCol.getID() == cidx) {
                Node current = currentCol.getFirstElement();
                while (current != null) {
                    if (current.getRowIndex() == ridx) {
                        return current.getValue();
                    }
                    current = current.getRowNext();
                }
            }

            currentCol = currentCol.getNextHead();
        }
        return 0;
    }

    @Override
    public void clearElement(int ridx, int cidx) {
        //if empty return
        if (nelements == 0) {
            return;
        }
        //if out of bounds return
        if (ridx >= this.nrows() || ridx < 0 || cidx >= this.ncols || cidx < 0) {
            return;
        }
//        if (nelements == 1) {
//            colHead = null;
//            rowHead = null;
//            nelements = 0;
//            return;
//        }
        //if delete rowHead
        if (rowHead.getFirstElement().getColIndex() == cidx
                && rowHead.getFirstElement().getRowIndex() == ridx
                && rowHead.getFirstElement().getRowNext() == null) {
            rowHead = rowHead.getNextHead();
            System.out.println("clear rowhead");

        }
        //if delete colHead
        if (colHead.getFirstElement().getColIndex() == cidx
                && colHead.getFirstElement().getRowIndex() == ridx
                && colHead.getFirstElement().getColNext() == null) {
            colHead = colHead.getNextHead();
            System.out.println("clearColHead");
            nelements--;

        }
        //if first row  
        if (rowHead.getID() == ridx) {
            Node currNode = rowHead.getFirstElement();
            if (currNode.getColIndex() == cidx) {
                rowHead.setFirstElement(currNode.getRowNext());

            } else {
                while (currNode.getRowNext() != null) {
                    if (currNode.getRowNext().getColIndex() == cidx) {
                        currNode.setRowNext(currNode.getRowNext().getRowNext());
                        System.out.println("firstROw");
                        break;
                    }

                    currNode = currNode.getRowNext();
                }
            }
        }
        //if first column
        if (colHead.getID() == cidx) {
            Node currNode = colHead.getFirstElement();
            if (currNode.getRowIndex() == ridx) {
                colHead.setFirstElement(currNode.getColNext());
            } else {
                while (currNode.getColNext() != null) {
                    if (currNode.getColNext().getRowIndex() == ridx) {
                        currNode.setColNext(currNode.getColNext().getColNext());
                        System.out.println("FirstCOlmunu");
                        break;
                    }
                    currNode = currNode.getRowNext();
                }
            }
        }
        //find row 
        HeadNode currHead = rowHead;
        while (currHead.getNextHead() != null) {
            //if next row is correct row
            if (currHead.getNextHead().getID() == ridx) {
                //if first element in row
                if (currHead.getNextHead().getFirstElement().getColIndex() == cidx) {
                    //if the next row has one element delete whole row

                    if (currHead.getNextHead().getFirstElement().getRowNext() == null) {
                        currHead.setHeadNext(currHead.getNextHead().getNextHead());
                        System.out.println("deleted a rowhead");
                        break;
                    }
                    //set first element to next element
                    currHead.getNextHead().setFirstElement(currHead.getNextHead().getFirstElement().getRowNext());
                    System.out.println("deleted a row first element");
                    break;
                } else {
                    Node currNode = currHead.getNextHead().getFirstElement();
                    while (currNode.getRowNext() != null) {
                        if (currNode.getRowNext().getColIndex() == cidx) {
                            currNode.setRowNext(currNode.getRowNext().getRowNext());
                            System.out.println("deleted in a row place");
                            break;
                        }
                        currNode = currNode.getRowNext();

                    }

                }

            }
            currHead = currHead.getNextHead();
        }

        currHead = colHead;
        while (currHead.getNextHead() != null) {
            if (currHead.getNextHead().getID() == cidx) {
                //if first element in row
                if (currHead.getNextHead().getFirstElement().getRowIndex() == ridx) {
                    //if the next col has one element delete whole row
                    if (currHead.getNextHead().getFirstElement().getColNext() == null) {
                        currHead.setHeadNext(currHead.getNextHead().getNextHead());
                        System.out.println("colhead");
                        nelements--;
                        return;
                    }
                    //set first element to next element
                    currHead.getNextHead().setFirstElement(currHead.getNextHead().getFirstElement().getColNext());
                    System.out.println("firstrow");
                    nelements--;
                    return;
                } else {
                    Node currNode = currHead.getNextHead().getFirstElement();
                    while (currNode.getColNext() != null) {
                        if (currNode.getColNext().getRowIndex() == ridx) {
                            currNode.setColNext(currNode.getColNext().getColNext());
                            System.out.println("overrrr heaarrr");
                            nelements--;
                            return;
                        }
                        currNode = currNode.getColNext();
                        //if row has one element -> delete row

                    }
                }
            }
            currHead = currHead.getNextHead();
        }

        System.out.println("got here");
    }

    @Override
    public void setElement(int ridx, int cidx, int val) {
        //if val==0           
        if (val == 0) {
            return;
        }
        //if cidx or ridx is too large or small
        if (ridx >= this.nrows() || ridx < 0 || cidx >= this.ncols || cidx < 0) {
            return;
        }

        //the new Node is created here. Everything else deals with setting up the pointers correctly.
        Node newElement = new Node(ridx, cidx, val, null, null);

        //if  is empty        
        if (nelements == 0) {

            HeadNode coln = new HeadNode(cidx, null, newElement);    //creates the columen head 
            colHead = coln;
            //points colHead to the new columen head

            HeadNode rown = new HeadNode(ridx, null, newElement);  //creates the new row head and sets RowHead to point to it 
            rowHead = rown;

            nelements++;
            return;
        }

        //if the column cidx is less than column head
        //insert a column infront of colHead and set colHead to newCol
        if (cidx < colHead.getID()) {

            HeadNode newCol = new HeadNode(cidx, colHead, newElement);
            colHead = newCol;

        } else {

            //***The following while loop sets the Column pointers to the NewElement Node
            // check for the columm, cidx
            //if the column for cidx exists set the pointers in that column to include the new element
            //if the column cidx does not exist it is created and inserted into the Column HeadNode linked list
            HeadNode currentCol = colHead;
            while (currentCol != null) {

                //if column (cidx) exists loop through and insert
                if (currentCol.getID() == newElement.getColIndex()) {
                    //check if rowId is less than first rowid in this column
                    //if it is set it equal to this column.getFirstElement
                    if (newElement.getRowIndex() < currentCol.getFirstElement().getRowIndex()) {
                        newElement.setColNext(currentCol.getFirstElement());
                        currentCol.setFirstElement(newElement);

                        break;
                    }

                    //this part is entered if NewElement rowId is greater than the rowId of the first element
                    Node currentNode = currentCol.firstElement;
                    while (currentNode != null) {
                        //update an element
                        if (newElement.getRowIndex() == currentNode.getRowIndex()) {
                            currentCol.getFirstElement().setValue(val);
                            return;
                        }
                        //or check the row indexes of the elements in cidx column and
                        //if ridx is greater than current and less than next then insert.
                        //check if last iteration of loop..the end of the column
                        if (currentNode.getColNext() == null
                                || (newElement.getRowIndex() > currentNode.getRowIndex()
                                && newElement.getRowIndex() < currentNode.getColNext().getRowIndex())) {
                            newElement.setColNext(currentNode.getColNext());
                            currentNode.setColNext(newElement);
                            break;

                        }

                        currentNode = currentNode.getColNext();
                    }

                } //if the column does not exist b/c
                //a) loop is at the last iteration or
                //b) if the cidx is inbetween current and next column 
                else if (currentCol.getNextHead() == null && currentCol.getID() < cidx || cidx > currentCol.getID() && cidx < currentCol.getNextHead().getID()) {
                    HeadNode newCol = new HeadNode(cidx, currentCol.getNextHead(), newElement);
                    currentCol.setHeadNext(newCol);

                    break;
                }

                currentCol = currentCol.getNextHead();
            }
        }

        //checks if there needs to be a new row insrted as row head
        if (ridx < rowHead.getID()) {
            HeadNode newRowHead = new HeadNode(ridx, rowHead, newElement);
            rowHead = newRowHead;
            nelements++;
            return;
        } else {
            //***The following while loop sets the Row pointers to the NewElement Node
            // check for the row
            //if the index exists set the pointers in that row to include the new element
            //if the row cidx does not exist it is created and inserted into the row HeadNode linked list
            HeadNode currentRow = rowHead;
            while (currentRow != null) {

                //if row exists iterate through and insert
                if (currentRow.getID() == newElement.getRowIndex()) {
                    //check if colID is less than first ColID in this row
                    //if it is set it equal to this row.getFirstElement
                    if (newElement.getColIndex() < currentRow.getFirstElement().getColIndex()) {
                        newElement.setRowNext(currentRow.getFirstElement());
                        currentRow.setFirstElement(newElement);
                        nelements++;
                        return;
                    }
                    //if the New element will update a value of the first element in a row
                    if (newElement.getColIndex() == currentRow.getFirstElement().getColIndex()) {
                        currentRow.getFirstElement().setValue(val);
                        System.out.println("updating22");
                        return;
                    }

                    Node currentNode = currentRow.getFirstElement();
                    while (currentNode != null) {
                        //first check if last iteration of loop..the end of the row
                        //or check the col indexes of the elements in rowId row and
                        //if colid is greater than current and less than next then insert.
                        if (currentNode.getRowNext() == null || cidx > currentNode.getColIndex() && cidx < currentNode.getRowNext().getColIndex()) {

                            newElement.setRowNext(currentNode.getRowNext());
                            currentNode.setRowNext(newElement);
                            nelements++;
                            return;
                        }
                        //to update a value
                        if (currentNode.getColIndex() == cidx) {
                            currentNode.setValue(val);
                            return;
                        }
                        currentNode = currentNode.getRowNext();
                    }

                }//if the column does not exist b/c
                //a) loop is at the last iteration or
                //b) if the cidx is inbetween current and next column 
                else if (currentRow.getNextHead() == null || ridx > currentRow.getID() && ridx < currentRow.getNextHead().getID()) {
                    HeadNode newRow = new HeadNode(ridx, currentRow.getNextHead(), newElement);
                    currentRow.setHeadNext(newRow);

                    nelements++;
                    return;
                }

                currentRow = currentRow.getNextHead();
            }
        }
        System.out.println();
        int[] test = this.getRowIndices();
        for (int i = 0; i < test.length; i++) {
            System.out.println(test[i]);
        }

    }
//            

    @Override
    public int[] getRowIndices() {

        int counter = 0;
        HeadNode curr = rowHead;

        while (curr != null) {
            counter++;
            curr = curr.getNextHead();
        }

        int[] idxArray = new int[counter];
        int i = 0;
        curr = rowHead;
        while (curr != null) {
            idxArray[i] = curr.getID();

            i++;
            curr = curr.nextHead;
        }
        return idxArray;
    }

    @Override
    public int[] getColIndices() {

        int counter = 0;
        HeadNode curr = colHead;

        while (curr != null) {
            counter++;
            curr = curr.getNextHead();
        }
        int[] idxArray = new int[counter];
        int i = 0;
        curr = colHead;
        while (curr != null) {
            idxArray[i] = curr.getID();
            curr = curr.nextHead;
            i++;
        }
        return idxArray;
    }

    @Override
    public int[] getOneRowColIndices(int ridx) {

        //first iterate through the rowHeads to find the corresponding ridx
        HeadNode currHead = rowHead;
        while (currHead != null) {
            if (currHead.getID() == ridx) {
                break;
            }
            currHead = currHead.getNextHead();
        }

        //make sure the above loop found the ridx or exit
        if (currHead == null) {
            return null;
        }
        //count the values in the row
        Node curr = currHead.getFirstElement();
        int counter = 0;
        while (curr != null) {
            counter++;
            curr = curr.getRowNext();
        }
        int[] idxArray = new int[counter];
        curr = currHead.getFirstElement();

        int i = 0;
        //get the column indexes of the given row
        while (curr != null) {
            idxArray[i] = curr.getColIndex();
            curr = curr.getRowNext();
            i++;
        }
        return idxArray;
    }

    @Override
    public int[] getOneRowValues(int ridx) {

        //first iterate through the rowHeads to find the corresponding ridx
        HeadNode currHead = rowHead;
        while (currHead != null) {
            if (currHead.getID() == ridx) {
                break;
            }
            currHead = currHead.getNextHead();
        }

        //make sure the above loop found the ridx or exit
        if (currHead == null) {
            return null;
        }
        //count the values in the row
        Node curr = currHead.getFirstElement();
        int counter = 0;
        while (curr != null) {
            counter++;
            curr = curr.getRowNext();
        }
        int[] valueArray = new int[counter];
        curr = currHead.getFirstElement();

        int i = 0;
        //get the column indexes of the given row
        while (curr != null) {
            valueArray[i] = curr.getValue();
            curr = curr.getRowNext();
            i++;
        }
        return valueArray;
    }

    @Override
    public int[] getOneColRowIndices(int cidx) {

        //first iterate through the colHeads to find the corresponding cidx
        HeadNode currHead = colHead;
        while (currHead != null) {
            if (currHead.getID() == cidx) {
                break;
            }
            currHead = currHead.getNextHead();
        }

        //make sure the above loop found the cidx or exit
        if (currHead == null) {
            return null;
        }
        //count the values in the row
        Node curr = currHead.getFirstElement();
        int counter = 0;
        while (curr != null) {
            counter++;
            curr = curr.getColNext();
        }
        int[] idxArray = new int[counter];
        curr = currHead.getFirstElement();

        int i = 0;
        //get the column indexes of the given row
        while (curr != null) {
            idxArray[i] = curr.getRowIndex();
            curr = curr.getColNext();
            i++;
        }
        return idxArray;
    }

    @Override
    public int[] getOneColValues(int cidx) {
        //first iterate through the colHeads to find the corresponding cidx
        HeadNode currHead = colHead;
        while (currHead != null) {
            if (currHead.getID() == cidx) {
                break;
            }
            currHead = currHead.getNextHead();
        }

        //make sure the above loop found the cidx or exit
        if (currHead == null) {
            return null;
        }
        //count the values in the row
        Node curr = currHead.getFirstElement();
        int counter = 0;
        while (curr != null) {
            counter++;
            curr = curr.getColNext();
        }
        int[] idxArray = new int[counter];
        curr = currHead.getFirstElement();

        int i = 0;
        //get the column indexes of the given row
        while (curr != null) {
            idxArray[i] = curr.getValue();
            curr = curr.getColNext();
            i++;
        }
        return idxArray;
    }

    public void append(int ridx, int cidx, int val) {
        if (val == 0) {
            return;
        }
        //if cidx or ridx is too large or small
        if (ridx >= this.nrows() || ridx < 0 || cidx >= this.ncols || cidx < 0) {
            return;
        }

        //the new Node is created here. Everything else deals with setting up the pointers correctly.
        Node newElement = new Node(ridx, cidx, val, null, null);

        //if  is empty        
        if (nelements == 0) {

            HeadNode coln = new HeadNode(cidx, null, newElement);    //creates the columen head 
            colHead = coln;
            //points colHead to the new columen head

            HeadNode rown = new HeadNode(ridx, null, newElement);  //creates the new row head and sets RowHead to point to it 
            rowHead = rown;

            nelements++;
            return;
        }
//           tail.setNext(e);
//                   tail=e;
//                    NumElements++;
//                    return;
//if row exists get last
//if row does not exist creat

    }

    @Override
    public SparseM addition(SparseM otherM) {
        if (this.ncols() != otherM.ncols() || this.nrows() != otherM.nrows()) {
            return null;//must be of equal length
        }

        int[] thisrows = this.getRowIndices();
        int[] otherrows = otherM.getRowIndices();
        int i = 0;
        int j = 0;
        HeadNode currentRow = null;

        HeadNode currentCol = null;
        LLSparseM newMat = new LLSparseM(this.nrows(), ncols());
        while (i < thisrows.length && j < otherrows.length) {
            System.out.println(i + "  ");
            if (thisrows[i] == otherrows[j]) {
                //create new rowhead here
                Node prev = null;
                if (currentRow == null) {
                    newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                    //exit
                    currentRow = newMat.getRowhead();
                } else {
                    currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                    currentRow = currentRow.getNextHead();
                }

                int[] idx = this.getOneRowColIndices(thisrows[i]);
                int[] value = this.getOneRowValues(thisrows[i]);
                int[] otherIdx = otherM.getOneRowColIndices(otherrows[j]);
                int[] otherValue = otherM.getOneRowValues(otherrows[j]);
                int k = 0;
                int l = 0;
                while (k < idx.length && l < otherIdx.length) {

                    if (idx[k] == otherIdx[l]) {
                        //new element 
                        Node e = new Node(thisrows[i], idx[k], (value[k] + otherValue[l]), null, null);
                        //setColumnin row 
                        //first first in row
                        if (currentRow.getFirstElement() == null) {
                            currentRow.setFirstElement(e);
                            prev = e;
                        } else {
                            prev.setRowNext(e);
                            prev = e;
                        }
                        //set the column stuff
                        if (newMat.getColhead() == null) {
                            newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                            currentCol = newMat.getColhead();
                        }

                        if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                            HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                            newMat.setColhead(newColHead);
                            break;
                        } else {
                            HeadNode tmpCol = newMat.colHead;
                            while (tmpCol != null) {
                                if (tmpCol.getID() == e.getColIndex()) {
                                    //firstcheckfirst elemlent of coumn
                                    if (tmpCol.getFirstElement() == null) {
                                        tmpCol.setFirstElement(e);
                                        break;
                                    }
                                    Node tmpNode = tmpCol.firstElement;
                                    //get colmn tail
                                    while (tmpNode.getColNext() != null) {
                                        tmpNode = tmpNode.getColNext();
                                    }
                                    tmpNode.setColNext(e);
                                    break;
                                }
                                if (tmpCol.getNextHead().getID() > e.getColIndex()
                                        || tmpCol.getNextHead() == null) {
                                    HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                    tmpCol.setHeadNext(newCol);
                                    break;
                                }
                            }
                            tmpCol = tmpCol.getNextHead();

                        }

                        //newMat.setElement(thisrows[i], idx[k], (value[k] + otherValue[l]));
                        k++;
                        l++;
                        continue;
                    }
                    if (idx[k] < otherIdx[l]) {
//                        newMat.setElement(thisrows[i], idx[k], value[k]);
//                        k++;
//                        continue;
                        Node e = new Node(thisrows[i], idx[k], value[k], null, null);
                        //setColumnin row 
                        //first first in row
                        if (currentRow.getFirstElement() == null) {
                            currentRow.setFirstElement(e);
                            prev = e;
                        } else {
                            prev.setRowNext(e);
                            prev = e;
                        }
                        //set the column stuff
                        if (newMat.getColhead() == null) {
                            newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                            currentCol = newMat.getColhead();
                        }

                        if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                            HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                            newMat.setColhead(newColHead);
                            break;
                        } else {
                            HeadNode tmpCol = newMat.colHead;
                            while (tmpCol != null) {
                                if (tmpCol.getID() == e.getColIndex()) {
                                    //firstcheckfirst elemlent of coumn
                                    if (tmpCol.getFirstElement() == null) {
                                        tmpCol.setFirstElement(e);
                                        break;
                                    }
                                    Node tmpNode = tmpCol.firstElement;
                                    //get colmn tail
                                    while (tmpNode.getColNext() != null) {
                                        tmpNode = tmpNode.getColNext();
                                    }
                                    tmpNode.setColNext(e);
                                    break;
                                }
                                if (tmpCol.getNextHead().getID() > e.getColIndex()
                                        || tmpCol.getNextHead() == null) {
                                    HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                    tmpCol.setHeadNext(newCol);
                                    break;
                                }
                            }
                            tmpCol = tmpCol.getNextHead();

                        }

                        k++;
                        continue;

                    }

                    if (idx[l] > otherIdx[l]) {
//                        newMat.setElement(thisrows[i], otherIdx[l], otherValue[l]);
//                        l++;

                        Node e = new Node(thisrows[i], otherIdx[k], otherValue[l], null, null);
                        //setColumnin row 
                        //first first in row
                        if (currentRow.getFirstElement() == null) {
                            currentRow.setFirstElement(e);
                            prev = e;
                        } else {
                            prev.setRowNext(e);
                            prev = e;
                        }
                        //set the column stuff
                        if (newMat.getColhead() == null) {
                            newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                            currentCol = newMat.getColhead();
                        }

                        if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                            HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                            newMat.setColhead(newColHead);
                            break;
                        } else {
                            HeadNode tmpCol = newMat.colHead;
                            while (tmpCol != null) {
                                if (tmpCol.getID() == e.getColIndex()) {
                                    //firstcheckfirst elemlent of coumn
                                    if (tmpCol.getFirstElement() == null) {
                                        tmpCol.setFirstElement(e);
                                        break;
                                    }
                                    Node tmpNode = tmpCol.firstElement;
                                    //get colmn tail
                                    while (tmpNode.getColNext() != null) {
                                        tmpNode = tmpNode.getColNext();
                                    }
                                    tmpNode.setColNext(e);
                                    break;
                                }
                                if (tmpCol.getNextHead().getID() > e.getColIndex()
                                        || tmpCol.getNextHead() == null) {
                                    HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                    tmpCol.setHeadNext(newCol);
                                    break;
                                }
                            }
                            tmpCol = tmpCol.getNextHead();

                        }
                        l++;

                    }

                }
                while (k < idx.length) {
//                    
//                    newMat.setElement(thisrows[i], idx[k], value[k]);
//                    k++;
                    Node e = new Node(thisrows[i], idx[k], value[k], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                    //newMat.setElement(thisrows[i], idx[k], (value[k] + otherValue[l]));
                    k++;

                }
                while (l < otherIdx.length) {
//                    newMat.setElement(thisrows[i], otherIdx[l], otherValue[l]);
//                    l++;
                    Node e = new Node(thisrows[i], otherIdx[l], otherValue[l], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                    l++;

                }
                i++;
                j++;
                continue;
            }
//if not equal
            if (thisrows[i] < otherrows[j]) {
                //creat new row 

                Node prev = null;
                if (currentRow == null) {
                    newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                    //exit
                    currentRow = newMat.getRowhead();
                } else {
                    currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                    currentRow = currentRow.getNextHead();
                }
                int[] columnid = this.getOneRowColIndices(thisrows[i]);
                int[] value = this.getOneRowValues(thisrows[i]);

                for (int a = 0; a < columnid.length; a++) {
                    //    newMat.setElement(thisrows[i], columnid[a], value[a]);
                    Node e = new Node(thisrows[i], columnid[a], value[a], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                }
                i++;

                continue;
            }

            if (otherrows[j] < thisrows[i]) {

                Node prev = null;
                if (currentRow == null) {
                    newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                    //exit
                    currentRow = newMat.getRowhead();
                } else {
                    currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                    currentRow = currentRow.getNextHead();
                }
                int[] columnid = otherM.getOneRowColIndices(otherrows[j]);

                int[] value = otherM.getOneRowValues(otherrows[j]);

                for (int a = 0; a < value.length; a++) {
                    // newMat.setElement(otherrows[j], columnid[a], value[a]);
                    Node e = new Node(otherrows[j], columnid[a], value[a], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                }
                j++;
            }

        }

        while (i < thisrows.length) {
            int[] columnid = this.getOneColRowIndices(thisrows[i]);
            int[] value = this.getOneRowValues(thisrows[i]);
            Node prev = null;
            if (currentRow == null) {
                newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                //exit
                currentRow = newMat.getRowhead();
            } else {
                currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                currentRow = currentRow.getNextHead();
            }
            for (int a = 0; a < columnid.length; a++) {
                //     newMat.setElement(thisrows[i], columnid[a], value[a]);
                Node e = new Node(thisrows[i], columnid[a], value[a], null, null);
                //setColumnin row 
                //first first in row
                if (currentRow.getFirstElement() == null) {
                    currentRow.setFirstElement(e);
                    prev = e;
                } else {
                    prev.setRowNext(e);
                    prev = e;
                }
                //set the column stuff
                if (newMat.getColhead() == null) {
                    newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                    currentCol = newMat.getColhead();
                }

                if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                    HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                    newMat.setColhead(newColHead);
                    break;
                } else {
                    HeadNode tmpCol = newMat.colHead;
                    while (tmpCol != null) {
                        if (tmpCol.getID() == e.getColIndex()) {
                            //firstcheckfirst elemlent of coumn
                            if (tmpCol.getFirstElement() == null) {
                                tmpCol.setFirstElement(e);
                                break;
                            }
                            Node tmpNode = tmpCol.firstElement;
                            //get colmn tail
                            while (tmpNode.getColNext() != null) {
                                tmpNode = tmpNode.getColNext();
                            }
                            tmpNode.setColNext(e);
                            break;
                        }
                        if (tmpCol.getNextHead().getID() > e.getColIndex()
                                || tmpCol.getNextHead() == null) {
                            HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                            tmpCol.setHeadNext(newCol);
                            break;
                        }
                    }
                    tmpCol = tmpCol.getNextHead();

                }

            }
            i++;
        }
        while (j < otherrows.length) {
            Node prev = null;
            if (currentRow == null) {
                newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                //exit
                currentRow = newMat.getRowhead();
            } else {
                currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                currentRow = currentRow.getNextHead();
            }
            int[] columnid = otherM.getOneRowColIndices(otherrows[j]);
            int[] value = otherM.getOneRowValues(otherrows[j]);

            for (int a = 0; a < columnid.length; a++) {
                //newMat.setElement(otherrows[j], columnid[a], value[a]);
                Node e = new Node(thisrows[i], columnid[a], value[a], null, null);
                //setColumnin row 
                //first first in row
                if (currentRow.getFirstElement() == null) {
                    currentRow.setFirstElement(e);
                    prev = e;
                } else {
                    prev.setRowNext(e);
                    prev = e;
                }
                //set the column stuff
                if (newMat.getColhead() == null) {
                    newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                    currentCol = newMat.getColhead();
                }

                if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                    HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                    newMat.setColhead(newColHead);
                    break;
                } else {
                    HeadNode tmpCol = newMat.colHead;
                    while (tmpCol != null) {
                        if (tmpCol.getID() == e.getColIndex()) {
                            //firstcheckfirst elemlent of coumn
                            if (tmpCol.getFirstElement() == null) {
                                tmpCol.setFirstElement(e);
                                break;
                            }
                            Node tmpNode = tmpCol.firstElement;
                            //get colmn tail
                            while (tmpNode.getColNext() != null) {
                                tmpNode = tmpNode.getColNext();
                            }
                            tmpNode.setColNext(e);
                            break;
                        }
                        if (tmpCol.getNextHead().getID() > e.getColIndex()
                                || tmpCol.getNextHead() == null) {
                            HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                            tmpCol.setHeadNext(newCol);
                            break;
                        }
                    }
                    tmpCol = tmpCol.getNextHead();

                }

            }

            j++;
        }

        return newMat;

    }

    @Override
    public SparseM substraction(SparseM otherM){
        if (this.ncols() != otherM.ncols() || this.nrows() != otherM.nrows()) {
            return null;//must be of equal length
        }

        int[] thisrows = this.getRowIndices();
        int[] otherrows = otherM.getRowIndices();
        int i = 0;
        int j = 0;
        HeadNode currentRow = null;

        HeadNode currentCol = null;
        LLSparseM newMat = new LLSparseM(this.nrows(), ncols());
        while (i < thisrows.length && j < otherrows.length) {
            System.out.println(i + "  ");
            if (thisrows[i] == otherrows[j]) {
                //create new rowhead here
                Node prev = null;
                if (currentRow == null) {
                    newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                    //exit
                    currentRow = newMat.getRowhead();
                } else {
                    currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                    currentRow = currentRow.getNextHead();
                }

                int[] idx = this.getOneRowColIndices(thisrows[i]);
                int[] value = this.getOneRowValues(thisrows[i]);
                int[] otherIdx = otherM.getOneRowColIndices(otherrows[j]);
                int[] otherValue = otherM.getOneRowValues(otherrows[j]);
                int k = 0;
                int l = 0;
                while (k < idx.length && l < otherIdx.length) {

                    if (idx[k] == otherIdx[l]) {
                        //new element 
                        Node e = new Node(thisrows[i], idx[k], (value[k] - otherValue[l]), null, null);
                        //setColumnin row 
                        //first first in row
                        if (currentRow.getFirstElement() == null) {
                            currentRow.setFirstElement(e);
                            prev = e;
                        } else {
                            prev.setRowNext(e);
                            prev = e;
                        }
                        //set the column stuff
                        if (newMat.getColhead() == null) {
                            newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                            currentCol = newMat.getColhead();
                        }

                        if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                            HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                            newMat.setColhead(newColHead);
                            break;
                        } else {
                            HeadNode tmpCol = newMat.colHead;
                            while (tmpCol != null) {
                                if (tmpCol.getID() == e.getColIndex()) {
                                    //firstcheckfirst elemlent of coumn
                                    if (tmpCol.getFirstElement() == null) {
                                        tmpCol.setFirstElement(e);
                                        break;
                                    }
                                    Node tmpNode = tmpCol.firstElement;
                                    //get colmn tail
                                    while (tmpNode.getColNext() != null) {
                                        tmpNode = tmpNode.getColNext();
                                    }
                                    tmpNode.setColNext(e);
                                    break;
                                }
                                if (tmpCol.getNextHead().getID() > e.getColIndex()
                                        || tmpCol.getNextHead() == null) {
                                    HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                    tmpCol.setHeadNext(newCol);
                                    break;
                                }
                            }
                            tmpCol = tmpCol.getNextHead();

                        }

                        //newMat.setElement(thisrows[i], idx[k], (value[k] + otherValue[l]));
                        k++;
                        l++;
                        continue;
                    }
                    if (idx[k] < otherIdx[l]) {
//                        newMat.setElement(thisrows[i], idx[k], value[k]);
//                        k++;
//                        continue;
                        Node e = new Node(thisrows[i], idx[k], value[k], null, null);
                        //setColumnin row 
                        //first first in row
                        if (currentRow.getFirstElement() == null) {
                            currentRow.setFirstElement(e);
                            prev = e;
                        } else {
                            prev.setRowNext(e);
                            prev = e;
                        }
                        //set the column stuff
                        if (newMat.getColhead() == null) {
                            newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                            currentCol = newMat.getColhead();
                        }

                        if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                            HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                            newMat.setColhead(newColHead);
                            break;
                        } else {
                            HeadNode tmpCol = newMat.colHead;
                            while (tmpCol != null) {
                                if (tmpCol.getID() == e.getColIndex()) {
                                    //firstcheckfirst elemlent of coumn
                                    if (tmpCol.getFirstElement() == null) {
                                        tmpCol.setFirstElement(e);
                                        break;
                                    }
                                    Node tmpNode = tmpCol.firstElement;
                                    //get colmn tail
                                    while (tmpNode.getColNext() != null) {
                                        tmpNode = tmpNode.getColNext();
                                    }
                                    tmpNode.setColNext(e);
                                    break;
                                }
                                if (tmpCol.getNextHead().getID() > e.getColIndex()
                                        || tmpCol.getNextHead() == null) {
                                    HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                    tmpCol.setHeadNext(newCol);
                                    break;
                                }
                            }
                            tmpCol = tmpCol.getNextHead();

                        }

                        k++;
                        continue;

                    }

                    if (idx[l] > otherIdx[l]) {
//                        newMat.setElement(thisrows[i], otherIdx[l], otherValue[l]);
//                        l++;

                        Node e = new Node(thisrows[i], otherIdx[k], otherValue[l], null, null);
                        //setColumnin row 
                        //first first in row
                        if (currentRow.getFirstElement() == null) {
                            currentRow.setFirstElement(e);
                            prev = e;
                        } else {
                            prev.setRowNext(e);
                            prev = e;
                        }
                        //set the column stuff
                        if (newMat.getColhead() == null) {
                            newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                            currentCol = newMat.getColhead();
                        }

                        if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                            HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                            newMat.setColhead(newColHead);
                            break;
                        } else {
                            HeadNode tmpCol = newMat.colHead;
                            while (tmpCol != null) {
                                if (tmpCol.getID() == e.getColIndex()) {
                                    //firstcheckfirst elemlent of coumn
                                    if (tmpCol.getFirstElement() == null) {
                                        tmpCol.setFirstElement(e);
                                        break;
                                    }
                                    Node tmpNode = tmpCol.firstElement;
                                    //get colmn tail
                                    while (tmpNode.getColNext() != null) {
                                        tmpNode = tmpNode.getColNext();
                                    }
                                    tmpNode.setColNext(e);
                                    break;
                                }
                                if (tmpCol.getNextHead().getID() > e.getColIndex()
                                        || tmpCol.getNextHead() == null) {
                                    HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                    tmpCol.setHeadNext(newCol);
                                    break;
                                }
                            }
                            tmpCol = tmpCol.getNextHead();

                        }
                        l++;

                    }

                }
                while (k < idx.length) {
//                    
//                    newMat.setElement(thisrows[i], idx[k], value[k]);
//                    k++;
                    Node e = new Node(thisrows[i], idx[k], value[k], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                    //newMat.setElement(thisrows[i], idx[k], (value[k] + otherValue[l]));
                    k++;

                }
                while (l < otherIdx.length) {
//                    newMat.setElement(thisrows[i], otherIdx[l], otherValue[l]);
//                    l++;
                    Node e = new Node(thisrows[i], otherIdx[l], otherValue[l], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                    l++;

                }
                i++;
                j++;
                continue;
            }
//if not equal
            if (thisrows[i] < otherrows[j]) {
                //creat new row 

                Node prev = null;
                if (currentRow == null) {
                    newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                    //exit
                    currentRow = newMat.getRowhead();
                } else {
                    currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                    currentRow = currentRow.getNextHead();
                }
                int[] columnid = this.getOneRowColIndices(thisrows[i]);
                int[] value = this.getOneRowValues(thisrows[i]);

                for (int a = 0; a < columnid.length; a++) {
                    //    newMat.setElement(thisrows[i], columnid[a], value[a]);
                    Node e = new Node(thisrows[i], columnid[a], value[a], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                }
                i++;

                continue;
            }

            if (otherrows[j] < thisrows[i]) {

                Node prev = null;
                if (currentRow == null) {
                    newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                    //exit
                    currentRow = newMat.getRowhead();
                } else {
                    currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                    currentRow = currentRow.getNextHead();
                }
                int[] columnid = otherM.getOneRowColIndices(otherrows[j]);

                int[] value = otherM.getOneRowValues(otherrows[j]);

                for (int a = 0; a < value.length; a++) {
                    // newMat.setElement(otherrows[j], columnid[a], value[a]);
                    Node e = new Node(otherrows[j], columnid[a], value[a], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                }
                j++;
            }

        }

        while (i < thisrows.length) {
            int[] columnid = this.getOneColRowIndices(thisrows[i]);
            int[] value = this.getOneRowValues(thisrows[i]);
            Node prev = null;
            if (currentRow == null) {
                newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                //exit
                currentRow = newMat.getRowhead();
            } else {
                currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                currentRow = currentRow.getNextHead();
            }
            for (int a = 0; a < columnid.length; a++) {
                //     newMat.setElement(thisrows[i], columnid[a], value[a]);
                Node e = new Node(thisrows[i], columnid[a], value[a], null, null);
                //setColumnin row 
                //first first in row
                if (currentRow.getFirstElement() == null) {
                    currentRow.setFirstElement(e);
                    prev = e;
                } else {
                    prev.setRowNext(e);
                    prev = e;
                }
                //set the column stuff
                if (newMat.getColhead() == null) {
                    newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                    currentCol = newMat.getColhead();
                }

                if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                    HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                    newMat.setColhead(newColHead);
                    break;
                } else {
                    HeadNode tmpCol = newMat.colHead;
                    while (tmpCol != null) {
                        if (tmpCol.getID() == e.getColIndex()) {
                            //firstcheckfirst elemlent of coumn
                            if (tmpCol.getFirstElement() == null) {
                                tmpCol.setFirstElement(e);
                                break;
                            }
                            Node tmpNode = tmpCol.firstElement;
                            //get colmn tail
                            while (tmpNode.getColNext() != null) {
                                tmpNode = tmpNode.getColNext();
                            }
                            tmpNode.setColNext(e);
                            break;
                        }
                        if (tmpCol.getNextHead().getID() > e.getColIndex()
                                || tmpCol.getNextHead() == null) {
                            HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                            tmpCol.setHeadNext(newCol);
                            break;
                        }
                    }
                    tmpCol = tmpCol.getNextHead();

                }

            }
            i++;
        }
        while (j < otherrows.length) {
            Node prev = null;
            if (currentRow == null) {
                newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                //exit
                currentRow = newMat.getRowhead();
            } else {
                currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                currentRow = currentRow.getNextHead();
            }
            int[] columnid = otherM.getOneRowColIndices(otherrows[j]);
            int[] value = otherM.getOneRowValues(otherrows[j]);

            for (int a = 0; a < columnid.length; a++) {
                //newMat.setElement(otherrows[j], columnid[a], value[a]);
                Node e = new Node(thisrows[i], columnid[a], value[a], null, null);
                //setColumnin row 
                //first first in row
                if (currentRow.getFirstElement() == null) {
                    currentRow.setFirstElement(e);
                    prev = e;
                } else {
                    prev.setRowNext(e);
                    prev = e;
                }
                //set the column stuff
                if (newMat.getColhead() == null) {
                    newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                    currentCol = newMat.getColhead();
                }

                if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                    HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                    newMat.setColhead(newColHead);
                    break;
                } else {
                    HeadNode tmpCol = newMat.colHead;
                    while (tmpCol != null) {
                        if (tmpCol.getID() == e.getColIndex()) {
                            //firstcheckfirst elemlent of coumn
                            if (tmpCol.getFirstElement() == null) {
                                tmpCol.setFirstElement(e);
                                break;
                            }
                            Node tmpNode = tmpCol.firstElement;
                            //get colmn tail
                            while (tmpNode.getColNext() != null) {
                                tmpNode = tmpNode.getColNext();
                            }
                            tmpNode.setColNext(e);
                            break;
                        }
                        if (tmpCol.getNextHead().getID() > e.getColIndex()
                                || tmpCol.getNextHead() == null) {
                            HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                            tmpCol.setHeadNext(newCol);
                            break;
                        }
                    }
                    tmpCol = tmpCol.getNextHead();

                }

            }

            j++;
        }

        return newMat;

    }


    @Override
    public SparseM multiplication(SparseM otherM) {    if (this.ncols() != otherM.ncols() || this.nrows() != otherM.nrows()) {
            return null;//must be of equal length
        }

        int[] thisrows = this.getRowIndices();
        int[] otherrows = otherM.getRowIndices();
        int i = 0;
        int j = 0;
        HeadNode currentRow = null;

        HeadNode currentCol = null;
        LLSparseM newMat = new LLSparseM(this.nrows(), ncols());
        while (i < thisrows.length && j < otherrows.length) {
            System.out.println(i + "  ");
            if (thisrows[i] == otherrows[j]) {
                //create new rowhead here
                Node prev = null;
                if (currentRow == null) {
                    newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                    //exit
                    currentRow = newMat.getRowhead();
                } else {
                    currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                    currentRow = currentRow.getNextHead();
                }

                int[] idx = this.getOneRowColIndices(thisrows[i]);
                int[] value = this.getOneRowValues(thisrows[i]);
                int[] otherIdx = otherM.getOneRowColIndices(otherrows[j]);
                int[] otherValue = otherM.getOneRowValues(otherrows[j]);
                int k = 0;
                int l = 0;
                while (k < idx.length && l < otherIdx.length) {

                    if (idx[k] == otherIdx[l]) {
                        //new element 
                        Node e = new Node(thisrows[i], idx[k], (value[k] * otherValue[l]), null, null);
                        //setColumnin row 
                        //first first in row
                        if (currentRow.getFirstElement() == null) {
                            currentRow.setFirstElement(e);
                            prev = e;
                        } else {
                            prev.setRowNext(e);
                            prev = e;
                        }
                        //set the column stuff
                        if (newMat.getColhead() == null) {
                            newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                            currentCol = newMat.getColhead();
                        }

                        if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                            HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                            newMat.setColhead(newColHead);
                            break;
                        } else {
                            HeadNode tmpCol = newMat.colHead;
                            while (tmpCol != null) {
                                if (tmpCol.getID() == e.getColIndex()) {
                                    //firstcheckfirst elemlent of coumn
                                    if (tmpCol.getFirstElement() == null) {
                                        tmpCol.setFirstElement(e);
                                        break;
                                    }
                                    Node tmpNode = tmpCol.firstElement;
                                    //get colmn tail
                                    while (tmpNode.getColNext() != null) {
                                        tmpNode = tmpNode.getColNext();
                                    }
                                    tmpNode.setColNext(e);
                                    break;
                                }
                                if (tmpCol.getNextHead().getID() > e.getColIndex()
                                        || tmpCol.getNextHead() == null) {
                                    HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                    tmpCol.setHeadNext(newCol);
                                    break;
                                }
                            }
                            tmpCol = tmpCol.getNextHead();

                        }

                        //newMat.setElement(thisrows[i], idx[k], (value[k] + otherValue[l]));
                        k++;
                        l++;
                        continue;
                    }
                    if (idx[k] < otherIdx[l]) {
//                        newMat.setElement(thisrows[i], idx[k], value[k]);
//                        k++;
//                        continue;
                        Node e = new Node(thisrows[i], idx[k], value[k], null, null);
                        //setColumnin row 
                        //first first in row
                        if (currentRow.getFirstElement() == null) {
                            currentRow.setFirstElement(e);
                            prev = e;
                        } else {
                            prev.setRowNext(e);
                            prev = e;
                        }
                        //set the column stuff
                        if (newMat.getColhead() == null) {
                            newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                            currentCol = newMat.getColhead();
                        }

                        if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                            HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                            newMat.setColhead(newColHead);
                            break;
                        } else {
                            HeadNode tmpCol = newMat.colHead;
                            while (tmpCol != null) {
                                if (tmpCol.getID() == e.getColIndex()) {
                                    //firstcheckfirst elemlent of coumn
                                    if (tmpCol.getFirstElement() == null) {
                                        tmpCol.setFirstElement(e);
                                        break;
                                    }
                                    Node tmpNode = tmpCol.firstElement;
                                    //get colmn tail
                                    while (tmpNode.getColNext() != null) {
                                        tmpNode = tmpNode.getColNext();
                                    }
                                    tmpNode.setColNext(e);
                                    break;
                                }
                                if (tmpCol.getNextHead().getID() > e.getColIndex()
                                        || tmpCol.getNextHead() == null) {
                                    HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                    tmpCol.setHeadNext(newCol);
                                    break;
                                }
                            }
                            tmpCol = tmpCol.getNextHead();

                        }

                        k++;
                        continue;

                    }

                    if (idx[l] > otherIdx[l]) {
//                        newMat.setElement(thisrows[i], otherIdx[l], otherValue[l]);
//                        l++;

                        Node e = new Node(thisrows[i], otherIdx[k], otherValue[l], null, null);
                        //setColumnin row 
                        //first first in row
                        if (currentRow.getFirstElement() == null) {
                            currentRow.setFirstElement(e);
                            prev = e;
                        } else {
                            prev.setRowNext(e);
                            prev = e;
                        }
                        //set the column stuff
                        if (newMat.getColhead() == null) {
                            newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                            currentCol = newMat.getColhead();
                        }

                        if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                            HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                            newMat.setColhead(newColHead);
                            break;
                        } else {
                            HeadNode tmpCol = newMat.colHead;
                            while (tmpCol != null) {
                                if (tmpCol.getID() == e.getColIndex()) {
                                    //firstcheckfirst elemlent of coumn
                                    if (tmpCol.getFirstElement() == null) {
                                        tmpCol.setFirstElement(e);
                                        break;
                                    }
                                    Node tmpNode = tmpCol.firstElement;
                                    //get colmn tail
                                    while (tmpNode.getColNext() != null) {
                                        tmpNode = tmpNode.getColNext();
                                    }
                                    tmpNode.setColNext(e);
                                    break;
                                }
                                if (tmpCol.getNextHead().getID() > e.getColIndex()
                                        || tmpCol.getNextHead() == null) {
                                    HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                    tmpCol.setHeadNext(newCol);
                                    break;
                                }
                            }
                            tmpCol = tmpCol.getNextHead();

                        }
                        l++;

                    }

                }
                while (k < idx.length) {
//                    
//                    newMat.setElement(thisrows[i], idx[k], value[k]);
//                    k++;
                    Node e = new Node(thisrows[i], idx[k], value[k], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                    //newMat.setElement(thisrows[i], idx[k], (value[k] + otherValue[l]));
                    k++;

                }
                while (l < otherIdx.length) {
//                    newMat.setElement(thisrows[i], otherIdx[l], otherValue[l]);
//                    l++;
                    Node e = new Node(thisrows[i], otherIdx[l], otherValue[l], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                    l++;

                }
                i++;
                j++;
                continue;
            }
//if not equal
            if (thisrows[i] < otherrows[j]) {
                //creat new row 

                Node prev = null;
                if (currentRow == null) {
                    newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                    //exit
                    currentRow = newMat.getRowhead();
                } else {
                    currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                    currentRow = currentRow.getNextHead();
                }
                int[] columnid = this.getOneRowColIndices(thisrows[i]);
                int[] value = this.getOneRowValues(thisrows[i]);

                for (int a = 0; a < columnid.length; a++) {
                    //    newMat.setElement(thisrows[i], columnid[a], value[a]);
                    Node e = new Node(thisrows[i], columnid[a], value[a], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                }
                i++;

                continue;
            }

            if (otherrows[j] < thisrows[i]) {

                Node prev = null;
                if (currentRow == null) {
                    newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                    //exit
                    currentRow = newMat.getRowhead();
                } else {
                    currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                    currentRow = currentRow.getNextHead();
                }
                int[] columnid = otherM.getOneRowColIndices(otherrows[j]);

                int[] value = otherM.getOneRowValues(otherrows[j]);

                for (int a = 0; a < value.length; a++) {
                    // newMat.setElement(otherrows[j], columnid[a], value[a]);
                    Node e = new Node(otherrows[j], columnid[a], value[a], null, null);
                    //setColumnin row 
                    //first first in row
                    if (currentRow.getFirstElement() == null) {
                        currentRow.setFirstElement(e);
                        prev = e;
                    } else {
                        prev.setRowNext(e);
                        prev = e;
                    }
                    //set the column stuff
                    if (newMat.getColhead() == null) {
                        newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                        currentCol = newMat.getColhead();
                    }

                    if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                        HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                        newMat.setColhead(newColHead);
                        break;
                    } else {
                        HeadNode tmpCol = newMat.colHead;
                        while (tmpCol != null) {
                            if (tmpCol.getID() == e.getColIndex()) {
                                //firstcheckfirst elemlent of coumn
                                if (tmpCol.getFirstElement() == null) {
                                    tmpCol.setFirstElement(e);
                                    break;
                                }
                                Node tmpNode = tmpCol.firstElement;
                                //get colmn tail
                                while (tmpNode.getColNext() != null) {
                                    tmpNode = tmpNode.getColNext();
                                }
                                tmpNode.setColNext(e);
                                break;
                            }
                            if (tmpCol.getNextHead().getID() > e.getColIndex()
                                    || tmpCol.getNextHead() == null) {
                                HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                                tmpCol.setHeadNext(newCol);
                                break;
                            }
                        }
                        tmpCol = tmpCol.getNextHead();

                    }

                }
                j++;
            }

        }

        while (i < thisrows.length) {
            int[] columnid = this.getOneColRowIndices(thisrows[i]);
            int[] value = this.getOneRowValues(thisrows[i]);
            Node prev = null;
            if (currentRow == null) {
                newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                //exit
                currentRow = newMat.getRowhead();
            } else {
                currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                currentRow = currentRow.getNextHead();
            }
            for (int a = 0; a < columnid.length; a++) {
                //     newMat.setElement(thisrows[i], columnid[a], value[a]);
                Node e = new Node(thisrows[i], columnid[a], value[a], null, null);
                //setColumnin row 
                //first first in row
                if (currentRow.getFirstElement() == null) {
                    currentRow.setFirstElement(e);
                    prev = e;
                } else {
                    prev.setRowNext(e);
                    prev = e;
                }
                //set the column stuff
                if (newMat.getColhead() == null) {
                    newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                    currentCol = newMat.getColhead();
                }

                if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                    HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                    newMat.setColhead(newColHead);
                    break;
                } else {
                    HeadNode tmpCol = newMat.colHead;
                    while (tmpCol != null) {
                        if (tmpCol.getID() == e.getColIndex()) {
                            //firstcheckfirst elemlent of coumn
                            if (tmpCol.getFirstElement() == null) {
                                tmpCol.setFirstElement(e);
                                break;
                            }
                            Node tmpNode = tmpCol.firstElement;
                            //get colmn tail
                            while (tmpNode.getColNext() != null) {
                                tmpNode = tmpNode.getColNext();
                            }
                            tmpNode.setColNext(e);
                            break;
                        }
                        if (tmpCol.getNextHead().getID() > e.getColIndex()
                                || tmpCol.getNextHead() == null) {
                            HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                            tmpCol.setHeadNext(newCol);
                            break;
                        }
                    }
                    tmpCol = tmpCol.getNextHead();

                }

            }
            i++;
        }
        while (j < otherrows.length) {
            Node prev = null;
            if (currentRow == null) {
                newMat.setRowhead(new HeadNode(thisrows[i], null, null));
                //exit
                currentRow = newMat.getRowhead();
            } else {
                currentRow.setHeadNext(new HeadNode(thisrows[i], null, null));
                currentRow = currentRow.getNextHead();
            }
            int[] columnid = otherM.getOneRowColIndices(otherrows[j]);
            int[] value = otherM.getOneRowValues(otherrows[j]);

            for (int a = 0; a < columnid.length; a++) {
                //newMat.setElement(otherrows[j], columnid[a], value[a]);
                Node e = new Node(thisrows[i], columnid[a], value[a], null, null);
                //setColumnin row 
                //first first in row
                if (currentRow.getFirstElement() == null) {
                    currentRow.setFirstElement(e);
                    prev = e;
                } else {
                    prev.setRowNext(e);
                    prev = e;
                }
                //set the column stuff
                if (newMat.getColhead() == null) {
                    newMat.setColhead(new HeadNode(e.getColIndex(), null, null));

                    currentCol = newMat.getColhead();
                }

                if (e.getColIndex() < newMat.colHead.getID()) {//must update head
                    HeadNode newColHead = new HeadNode(e.getColIndex(), newMat.colHead, e);
                    newMat.setColhead(newColHead);
                    break;
                } else {
                    HeadNode tmpCol = newMat.colHead;
                    while (tmpCol != null) {
                        if (tmpCol.getID() == e.getColIndex()) {
                            //firstcheckfirst elemlent of coumn
                            if (tmpCol.getFirstElement() == null) {
                                tmpCol.setFirstElement(e);
                                break;
                            }
                            Node tmpNode = tmpCol.firstElement;
                            //get colmn tail
                            while (tmpNode.getColNext() != null) {
                                tmpNode = tmpNode.getColNext();
                            }
                            tmpNode.setColNext(e);
                            break;
                        }
                        if (tmpCol.getNextHead().getID() > e.getColIndex()
                                || tmpCol.getNextHead() == null) {
                            HeadNode newCol = new HeadNode(e.getColIndex(), tmpCol.getNextHead(), e);
                            tmpCol.setHeadNext(newCol);
                            break;
                        }
                    }
                    tmpCol = tmpCol.getNextHead();

                }

            }

            j++;
        }

        return newMat;

    }

}
