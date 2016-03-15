import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class ListNode {
	private int data;
	private ListNode next;

	public ListNode(){
	        next = null;
	    }
	    
	public ListNode(int d){
	        data = d;
	        next = new ListNode();
	}
	public ListNode getNext(){
		return next;
	}
	public void setNext(ListNode item){
		next = item;
	}
	public int getData(){
		return data;
	}
	public void setData(int d){
		data = d;
	}
}

class LinkedListStack {
    private ListNode top = new ListNode();
    
    public LinkedListStack() {
        top = null;
    }
    
    public void push(int data) {
    	ListNode newNode = new ListNode(data);
        newNode.setNext(top);
        top = newNode;
    }
    
    public int pop() {
        int data = top.getData();
        top = top.getNext();
        return data;
    }
    
    public boolean isEmpty(){
        return top == null;
    }
    public ListNode getTop(){
    	return top;
    }
}

class Main{
    public static void main(String[] args) {
    	int temp;
    	File infile = new File(args[0]);
		try {
			System.out.println("Opening the scanner");
			Scanner data = new Scanner(infile);
			LinkedListStack myStack = new LinkedListStack();
			while (data.hasNext() ) {
				temp = data.nextInt();
				myStack.push(temp);
				System.out.println("Pushing " + temp + " onto the stack."
						+ " The next memory address is " + myStack.getTop().getNext());
	    	}
			System.out.println("Closing the scanner");
			data.close();
			
			while(!myStack.isEmpty()){
				System.out.println("Popping " + myStack.pop() + " from the stack");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
}
    
