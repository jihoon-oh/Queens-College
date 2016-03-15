#include <iostream>
#include <fstream>
using namespace std;

class ListNode {
private:
    int data;
    ListNode* next;
    friend class LinkedList;
    
public:
    ListNode(){
        next = NULL;
    }
    
    ListNode(int d){
        data = d;
        next = NULL;
    }
    
    ~ListNode(){
        delete next;
    }
};


class LinkedList {
private:
    ListNode* listHead;
    
public:
    LinkedList(){
        ListNode* dummyNode = new ListNode(-9999);
        listHead = dummyNode;
    }
    
    bool isEmpty(){
        return listHead->next == NULL;
    }
    
    ListNode* findSpot(int data){
        ListNode* spot = listHead;
        while(spot->next != NULL && spot->next->data < data) {
            spot = spot->next;
        }
        return spot;
    }
    
    void listInsert(int data, ofstream &outFile){
        ListNode* temp = findSpot(data);
        if(temp->next != NULL && temp->next->data == data){
            outFile << data << " is already in the list";
        } else {
            ListNode* newNode = new ListNode(data);
            newNode->next = temp->next;
            temp->next = newNode;
            output(outFile);
        }
    }
    
    void output(ofstream &outFile){
        ListNode* temp = listHead;
        outFile << "listHead";
        while(temp != NULL){
            outFile << " -> (" << temp->data << ",";
            if(temp->next == NULL){
                outFile << -1 << ")";
            } else {
                outFile << temp->next->data << ")";
            }
            temp = temp->next;
        }
    }

    void listDelete(int data, ofstream &outFile){
        if(isEmpty()){
            outFile << "The list is empty";
        } else {
            ListNode* temp = findSpot(data);
            temp->next = temp->next->next;
        }
    }
     
    ~LinkedList(){
        delete listHead;
    }
    
};


int main(int argc, const char * argv[]) {
    int data;
    ifstream inFile;
    ofstream outFile;
    
    // creating list, inserting data
    LinkedList myList = *new LinkedList();
    inFile.open(argv[1]);
    outFile.open(argv[2]);
    if (inFile.is_open() && outFile.is_open())
    {
        outFile << "Opening file and inserting into list:" << endl;
        while (inFile>>data){
            outFile << "Inserting " << data << ": ";
            myList.listInsert(data, outFile);
            outFile << endl;
        }
        outFile << "Closing file" << endl;
        inFile.close();
    } else {
        cout << "Files is not open" << endl;
    }
    
    return 0;
}
