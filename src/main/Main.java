package main;

import logic.Bnode;
import logic.Btree;

public class Main {

	public static void main(String[] args) {
		Btree t = new Btree(3);
		
		t.insertKey(10);
		t.insertKey(20);
		t.insertKey(30);
		t.insertKey(40);
		t.insertKey(50);
		t.insertKey(60);
		t.insertKey(70);
		t.insertKey(80);
		//t.insertKey(90);
		t.insertKey(25);
		t.insertKey(23);
		t.insertKey(35);
		/*
		t.insertKey(100);
		t.insertKey(110);
		t.insertKey(120);
		t.insertKey(130);
		t.insertKey(140);
		t.insertKey(150);
		t.insertKey(160);
		t.insertKey(170);
		*/
		
		t.trasverse();
		System.out.println();
		
		Bnode node = t.search(30);
		
		Bnode l = t.getLeftNode(30);
		Bnode r = t.getRightNode(30);
		
		int[] bros = (t.search(60)).getBrothers(60);
		
		int[] left = (t.getLeftNode(30)).getLeftKeys(30);
		int[] right = (t.getRightNode(30)).getRightKeys(30, 60);
				
		t.toMatrix();
	}
}
