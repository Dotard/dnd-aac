package com.dnd.aac.suggest;

public class Node {
	 	char pictoID;
	    Node[] links;
	    
	    //We can use this but we can also just have the last element in the array store a mute reference
	    //representing the value for the node
	    boolean fullWord;
	  
	    Node(char letter, boolean fullWord)
	    {
	        this.pictoID = letter;
	        links = new Node[26];
	        this.fullWord = fullWord;
	    }
}
