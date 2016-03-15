import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Stack {
    private int top;
   	private int[] array;
    
    public Stack(int size) {
        array = new int[size];
        top = -1;
    }
    
    public void push(int i) {
        array[top+1] = i;
        top++;
    }
    
    public int pop() {
    	if(isEmpty()){
    		System.out.println("The stack is empty. Nothing to pop.");
    		return -1;
    	} else{
        top--;
        return array[top+1];
    	}
    }
    
    public boolean isEmpty(){
        return top == -1;
    }
}

class Main{
    public static void main(String[] args) {
    	int count = 0;
    	int temp = 0;
    	File infile = new File(args[0]);
		try {
			Scanner data = new Scanner(infile);
			System.out.println("Opening file and reading the data:" );
			while (data.hasNext() ) {
				System.out.println("Reading " + data.nextInt() + " from file");
	    		count++;
	    	}
			System.out.println("Closing the scanner");
			data.close();
			
			System.out.println("Opening the scanner");
			data = new Scanner(infile);
			Stack myStack = new Stack(count);
			while (data.hasNext() ) {
				temp = data.nextInt();
				System.out.println("Pushing " + temp + " onto the stack");
				myStack.push(temp);
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
    
