#include <iostream>
#include <fstream>
using namespace std;

class Stack {
private:
    int top;
    int *array;
    
public:
    Stack(int size){
        array = new int[size];
        top = -1;
    }
    
    void push(int i){
        array[top+1] = i;
        top++;
    }
    int pop(){
        top--;
        return array[top+1];
    }
    
    bool isEmpty(){
        return top == -1;
    }
    
    ~Stack(){
        delete[] array;
    }
};

int main(int argc, const char * argv[]) {
    int count = 0;
    int datum;
    ifstream infile;
    
    // reading data, calculating size
    infile.open(argv[1]);
    if (infile.is_open())
    {
        cout << "Opening file and reading the data:" << endl;
        while (infile>>datum){
            cout << "Reading " << datum << " from file"<< endl;
            count++;
        }
        cout << "Closing file" << endl;
        infile.close();
    } else {
        cout << "File is not open" << endl;
    }
    
    // creating stack, pushing data
    Stack myStack(count);
    infile.open(argv[1]);
    if (infile.is_open())
    {
        cout << "Opening file and pushing onto stack:" << endl;
        while (infile>>datum){
            myStack.push(datum);
            cout << "Pushing " << datum << " onto the stack" << endl;
        }
        cout << "Closing file" << endl;
        infile.close();
    } else {
        cout << "File is not open" << endl;
    }
    
    //  popping the stack
    while(!myStack.isEmpty()){
        cout << "Popping " << myStack.pop() << " from the stack." << endl;
    }
    
    return 0;

}
