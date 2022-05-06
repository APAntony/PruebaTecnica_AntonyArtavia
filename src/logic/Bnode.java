package logic;

import java.util.ArrayList;

public class Bnode {
	int keys[];
	Bnode subtrees[];
	int orderM;
	int occupiedKeys;
	boolean isLeaf;
	
	public Bnode(int pOrderM, boolean pIsLeaf) {
		this.orderM = pOrderM;
		this.isLeaf = pIsLeaf;
		this.occupiedKeys = 0;
		
		//Establish the maximum size for keys in the root
		this.keys = new int[2 * this.orderM - 1];
		
		//Establish the maximum size of sub-trees for the root size
		this.subtrees = new Bnode[2 * this.orderM];
	}
	
	/**
	 * Function to split a subtree in case this one is full.
	 * @param index is the index of the subtree in its father's subtrees array
	 * @param node is the instance of the node itself
	 */
	public void splitSubtree(int index, Bnode node) {
		
		// I need a new node to store some of the old node keys
		Bnode newNode = new Bnode(node.orderM, node.isLeaf);
		newNode.occupiedKeys = this.orderM - 1;
		
		// Now I have to fill the new node with some of the old node keys
		for (int index2 = 0; index2 < this.orderM-1; index2++) {
			newNode.keys[index2] = node.keys[index2 + this.orderM];
		}
		
		if (!node.isLeaf) {
			for (int index2 = 0; index2 < this.orderM; index2++) {
				newNode.subtrees[index2] = node.subtrees[index2 + this.orderM];
			}
		}
		
		// Reduce the amount of occupied keys in the old node
		node.occupiedKeys = this.orderM - 1;
		
		// Creation of space for the new subtree
		for (int index2 = this.occupiedKeys; index2 >= index+1; index2--) {
			this.subtrees[index2 + 1] = this.subtrees[index2];
		}
		
		// Now put the new node
		this.subtrees[index + 1] = newNode;
		
		// One older node's key that will move to this new node I have to find it and move greater keys
		for (int index2 = this.occupiedKeys-1; index2 >= index; index2--) {
			this.keys[index2+1] = this.keys[index2];
		}
		
		// I have to copy the middle key of the old node
		this.keys[index] = node.keys[this.orderM - 1];
		this.occupiedKeys = this.occupiedKeys + 1;
	}
	
	/**
	 * Function for the insertion on the root, when this isn't full.
	 * @param key data to be inserted in the tree.
	 */
	public void insertWhenRootNotFull(int key) {
		int index = this.occupiedKeys - 1; //
		
		if (this.isLeaf) {
			
			//The new key could provoke a relocation so I have to move the keys in that case
			while (index >= 0 && this.keys[index] > key) {
				this.keys[index + 1] = this.keys[index];
				index--;
			}
			
			this.keys[index + 1] = key; //insertion of the new key
			this.occupiedKeys = this.occupiedKeys + 1;
		} else {
			
			//I have to find the subtree for the new key
			while (index >= 0 && this.keys[index] > key) {
				index--;
			}
			
			// In case the subtree is already full
			if (this.subtrees[index + 1].occupiedKeys == 2*this.orderM-1) {
				// Split the full subtree
				splitSubtree(index+1, this.subtrees[index + 1]);
				
				//Now I need to know where is going to be the new key in new subtrees
				if (this.keys[index + 1] < key) {
					index++;
				}
			}
			this.subtrees[index+1].insertWhenRootNotFull(key);
		}
	}
	
	/**
	 * Function to print the keys of the tree
	 */
	public void trasverse() {
		
		int index = 0;
		for (index = 0; index < this.occupiedKeys; index++) {
			
			//Check if there is no leaf
			if (!this.isLeaf) {
				this.subtrees[index].trasverse();
			}
			System.out.println(this.keys[index] + " ");
		}
		
		//Print subtree for last subtree
		if (!this.isLeaf) {
			this.subtrees[index].trasverse();
		}
	}
	
	/**
	 * Function to find the node of an specific key
	 *  this function finds the first appear of the key.
	 * @param key key being searched
	 * @return the node of the specific key
	 */
	public Bnode search(int key) {
		
		int index = 0;
		while (index < this.occupiedKeys && key > this.keys[index]) {
			index++;
		}
		
		// Check if the found key is actually the searched key
		if (this.keys[index] == key) {
			return this;
		}
		
		// If the key wasn't found here and the actual node is a leaf
		if (this.isLeaf) {
			return null;
		}
		
		return this.subtrees[index].search(key);
	}
	
	/**
	 * Function to get the left node of a key
	 * @param key key being searched
	 * @return the Bnode of the left subtree of the specific key
	 */
	public Bnode getLeftNode(int key) {
		
		int index = 0;
		while (index < this.occupiedKeys && key > this.keys[index]) {
			index++;
		}
		
		// If the key wasn't found here and the actual node is a leaf
		if (this.isLeaf) {
			return null;
		}
		
		// Check if the found key is actually the searched key
		if (this.keys[index] == key) {
			return this.subtrees[index];
		}
		
		return this.subtrees[index].getLeftNode(key);
	}
	
	/**
	 * Function to get the right node of a key
	 * @param key key being searched
	 * @return the Bnode of the left subtree of the specific key
	 */
	public Bnode getRightNode(int key) {	
		int index = 0;
		while (index < this.occupiedKeys && key > this.keys[index]) {
			index++;
		}
		
		// If the key wasn't found here and the actual node is a leaf
		if (this.isLeaf) {
			return null;
		}
		
		// Check if the found key is actually the searched key
		if (this.keys[index] == key) {
			return this.subtrees[index+1];
		}
		
		return this.subtrees[index].getLeftNode(key);
	}
	
	/**
	 * Function to get the left tree keys of a determinate key based on its
	 * 	left subtree
	 * @param key is a key to check until where left keys are
	 * @return 
	 */
	public int[] getLeftKeys(int key) {
		int[] result;
		result = new int[this.occupiedKeys];
		
		
		int index = 0;
		for (index = 0; index < this.occupiedKeys; index++) {
			if (key <= this.keys[index]) {
				break;
			} else {
				result[index] = this.keys[index];
			}
		}
		
		return result;
	}
	
	/**
	 * Function to get the left tree keys of a determinate key based on its
	 * 	left subtree
	 * @param key is a key to check until where left keys are
	 * @return 
	 */
	public int[] getRightKeys(int key, int rightBrotherKey) {
		int[] result;
		
		result = new int[this.occupiedKeys];
		
		int index = 0;
		for (index = 0; index < this.occupiedKeys; index++) {
			if (rightBrotherKey != 0 && rightBrotherKey <= this.keys[index]) {
				break;
			} else {
				result[index] = this.keys[index];
			}
		}
		
		return result;
	}
	
	/**
	 * Function to get the brother keys of a particular key
	 * @param actualNode the node where the key is
	 * @return an array of brothers
	 */
	public int[] getBrothers(int searchedKey) {
		if (this.occupiedKeys == 1) {
			return null;
		}
		
		int resIndex = 0;
		
		int[] result = new int[this.occupiedKeys - 1];
		
		for (int index = 0; index < (this.occupiedKeys); index++) {
			if (this.keys[index] != searchedKey) {
				result[resIndex] = this.keys[index];
				resIndex++;
			}
		}
		
		return result;
	}
}
