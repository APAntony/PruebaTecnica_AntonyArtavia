package logic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Btree {
	private int orderM; // I used this like the degree for the tree
	private int height;
	private int keysAmount;
	private ArrayList<Integer> allKeys;
	
	Bnode root;
	
	public Btree(int pOrderM) {
		this.root = null;
		this.orderM = pOrderM;
		this.allKeys = new ArrayList<Integer>();
	}

	/**
	 * Method to transforms the current BTree to an NxM matrix representation.
	 * @return a matrix NxM
	 */
	public int[][] toMatrix() {
		// [row][column] format
		/*int[][] result = new int[2][3];
		result[0][0] = 1;
		result[0][1] = 1;
		result[0][2] = 1;
		result[1][0] = 1;
		result[1][1] = 1;
		result[1][2] = 1;
		
		System.out.println(printMatrix(result));
		*/
		
		// I create a matrix with columns based on the amount of keys of the tree
		// and each column with 4 row, the first for the key, second for brother keys
		// the third one for left subtree and fourth for right subtree
		int[][] result = new int[3][this.keysAmount];
		Bnode left;
		Bnode right;
		int[] brothers;
		int[] leftKeys;
		int[] rightKeys;
		int actualKey;
		
		for (int index = 0; index < allKeys.size(); index++) {
			actualKey = allKeys.get(index);
			
			left = this.root.getLeftNode(actualKey);
			right = this.root.getRightNode(actualKey);
			
			// I'll get all the necessary data (brother, left and right subtree keys)
			brothers = search(actualKey).getBrothers(actualKey);
			leftKeys = left.getLeftKeys(actualKey);
			
			if (brothers[brothers.length-1] > actualKey) {
				rightKeys = right.getRightKeys(actualKey, brothers[brothers.length-1]);
			} else {
				rightKeys = right.getRightKeys(actualKey, 0);
			}
			
			int newRowIdx = 0;
			int[] newRow = new int[brothers.length + leftKeys.length + rightKeys.length];
			
			// Add brothers
			for(int brother: brothers) {
				newRow[newRowIdx] = brother;
				newRowIdx++;
			}
			
			//Add leftkeys
			for (int key: leftKeys) {
				newRow[newRowIdx] = key;
				newRowIdx++;
			}
			
			// Add rigth keys
			for (int key: rightKeys) {
				newRow[newRowIdx] = key;
				newRowIdx++;
			}
			
			// Add the new row
			result[index] = newRow; 
		}
		
		return result;
	}

	public int getOrderM() {
		return orderM;
	}

	public void setOrderM(int orderM) {
		this.orderM = orderM;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public String printMatrix(int[][] matrix) {
		String res = "";
		
		for (int[] fila : matrix) {
			res = res + "[ ";
			for (int dato: fila) {
				res = res + dato + " ";
			}
			res = res + "]\n";
		}
		
		return res;
	}
	
	/**
	 * Function to insert new keys/nodes to the BTree
	 * @param key is the new key to be inserted
	 */
	public void insertKey(int key) {
		
		//The BTree has no nodes yet
		if (this.root == null) {
			root = new Bnode (this.orderM, true);
			this.root.keys[0] = key; 	//put the first key in the tree
			this.root.occupiedKeys = 1; // increase the amount of keys in the node
			this.keysAmount++;
			this.allKeys.add(key);
		} else {  //Enter if the tree already has at least one node/key 
			
			//First ask if the root of the tree is full to create new subtrees
			if (this.root.occupiedKeys == (2*this.orderM - 1)) {
				
				Bnode newNode = new Bnode(this.orderM, false); // Create the new node
				
				// Because the root is full I have to make an split in the tree
				// First I need the old root to be a child now, then the split will occurs
				newNode.subtrees[0] = this.root;
				
				//Split old root and move 1 key to the new one
				newNode.splitSubtree(0, this.root);
				
				// Decide where in new subtrees is going to be the new key
				int index = 0;
				if (newNode.keys[0] < key) {
					index++;
				}
				newNode.subtrees[index].insertWhenRootNotFull(key);
				
				//Put new root
				this.root = newNode;
				this.keysAmount++;
				this.allKeys.add(key);
			} else {
				this.root.insertWhenRootNotFull(key);
				this.keysAmount++;
				this.allKeys.add(key);
			}
		}
	}
	
	/**
	 * Function to print the ordered content of the tree if it is empty
	 *  print a tab.
	 */
	public void trasverse() {
		if (this.root != null) {
			this.root.trasverse();
		}
		System.out.println();
	}
	
	public Bnode search(int key) {
		if (this.root == null) {
			return null;
		} else {
			return this.root.search(key);
		}
	}
	
	public Bnode getLeftNode(int key) {
		if (this.root == null) {
			return null;
		} else {
			return this.root.getLeftNode(key);
		}
	}
	
	public Bnode getRightNode(int key) {
		if (this.root == null) {
			return null;
		} else {
			return this.root.getRightNode(key);
		}
	}
}
