#include <iostream>
#include <fstream>
using namespace std;

class HeapSort {
private:
    int* heapAry;
    int size;
    
public:
    HeapSort(int s){
        size = s+1;
        heapAry = new int[size];
        heapAry[0] = 0;
    }
    
    void buildHeap(int data, ofstream &outDebugFile){
        insertOneDataItem(data, outDebugFile);
        heapAry[0]++;
        printHeap(outDebugFile);
    }
    
    void insertOneDataItem(int data, ofstream &outDebugFile){
        if(!isHeapFull()){
            bubbleUp(data);
        } else {
            outDebugFile << "Heap Array is full" << endl;
        }
    }
    
    void bubbleUp(int data){
        int position = heapAry[0] + 1;
        heapAry[position] = data;
        while(position != 1 && data < heapAry[position/2]){
            int temp = heapAry[position/2];
            heapAry[position/2] = heapAry[position];
            heapAry[position] = temp;
            position = position/2;
            heapAry[position] = data;
        }
    }
    
    void deleteHeap(ofstream &outDebugFile, ofstream &outFile) {
        outFile << "Here are the roots before deletion:" << endl;
        while(!isHeapEmpty()){
            deleteRoot(outDebugFile, outFile);
        }
    }
    
    void deleteRoot(ofstream &outDebugFile, ofstream &outFile) {
        outFile << heapAry[1] << endl;
        heapAry[1] = heapAry[heapAry[0]];
        heapAry[0]--;
        bubbleDown();
        printHeap(outDebugFile);
    }
    
    void bubbleDown() {
        int position = 1;
        int smallKidPos;
        if(heapAry[position*2] < heapAry[position*2+1]) {
            smallKidPos = position*2;
        } else {
            smallKidPos = position*2+1;
        }
        while(position*2 <= heapAry[0] && heapAry[position] > heapAry[smallKidPos]) {
            int temp = heapAry[position];
            heapAry[position] = heapAry[smallKidPos];
            heapAry[smallKidPos] = temp;
            position = smallKidPos;
            if(heapAry[position*2] < heapAry[position*2+1]) {
                smallKidPos = position*2;
            } else {
                smallKidPos = position*2+1;
            }
        }
    }
    
    bool isHeapFull(){
        return heapAry[0] == size-1;
    }
    
    bool isHeapEmpty(){
        return heapAry[0] == 0;
    }
    
    void printHeap(ofstream &outDebugFile){
        outDebugFile << "The first ten contents of the array are: ";
        for(int index=0; index < 10; index++){
            outDebugFile << heapAry[index] << " ";
        }
        outDebugFile << endl;
    }
    
    ~HeapSort(){
        delete heapAry;
    }
};

int main(int argc, const char * argv[]) {
    int count = 0;
    int datum;
    ifstream infile;
    ofstream outDebugFile;
    ofstream outFile;
    
    outDebugFile.open(argv[2]);
    outFile.open(argv[3]);
    
    // reading data, calculating size
    infile.open(argv[1]);
    if (infile.is_open())
    {
        outDebugFile << "Opening file and reading the data:" << endl;
        while (infile>>datum){
            outDebugFile << "Reading " << datum << " from file"<< endl;
            count++;
        }
        outDebugFile << "There are " << count << " data items in the input file" << endl;
        outDebugFile << "Closing file" << endl;
        infile.close();
    } else {
        outDebugFile << "File is not open" << endl;
    }
    
    HeapSort myHeapAry(count);
    infile.open(argv[1]);
    if (infile.is_open())
    {
        outDebugFile << "Opening file and reading the data:" << endl;
        while (infile>>datum){
            outDebugFile << "Inserting " << datum << " into heap array"<< endl;
            myHeapAry.buildHeap(datum, outDebugFile);
        }
        outDebugFile << "Closing file" << endl;
        infile.close();
    } else {
        outDebugFile << "File is not open" << endl;
    }
    
    myHeapAry.deleteHeap(outDebugFile, outFile);
    
    
    return 0;
}
