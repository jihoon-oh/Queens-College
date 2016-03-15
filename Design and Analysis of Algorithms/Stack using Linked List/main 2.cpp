#include <iostream>
#include <fstream>
using namespace std;

class ListNode {
private:
    int data;
    ListNode* next;
    friend class LinkedListStack;

public:
    ListNode(){
        next = nullptr;
    }
    
    ListNode(int d){
        data = d;
        next = new ListNode();
    }
    
    ~ListNode(){
    }
};


class LinkedListStack {
private:
    ListNode* top;
    
public:
    LinkedListStack(){
        top = nullptr;
    }
    
    void push(int data){
        ListNode* newNode = new ListNode(data);
        newNode->next = top;
        top = newNode;
        cout << "Pushing " << data << " onto the stack. The next memory address is " << newNode->next << endl;
    }
    
    int pop(){
        ListNode* temp = top;
        top = top->next;
        int tempData = temp->data;
        delete temp;
        return tempData;
    }
    
    bool isEmpty(){
        return top == nullptr;
    }
    
    ~LinkedListStack(){
        delete top;
    }
};

 
int main(int argc, const char * argv[]) {
    int datum;
    ifstream infile;
    
    // creating stack, pushing data
    LinkedListStack myStack;
    infile.open(argv[1]);
    if (infile.is_open())
    {
        cout << "Opening file and pushing onto stack:" << endl;
        while (infile>>datum){
            myStack.push(datum);
        }
        cout << "Closing file" << endl;
        infile.close();
    } else {
        cout << "File is not open" << endl;
    }
    
    //  popping the stack
    while(!myStack.isEmpty()) {
        cout << "Popping " << myStack.pop() << " from the stack." << endl;
    }
    
    return 0;
    
}
