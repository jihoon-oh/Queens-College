#include <iostream>
#include <fstream>
using namespace std;

class BucketSort {
private:
    int min, max, bucketSize, offset;
    int* bucketAry;
    
public:
    BucketSort() {
        bucketSize = 0;
        min = 9999;
        max = -1;
    }
    
    void findMinMax(int data) {
        if (data < min) min = data;
        if (data > max) max = data;
    }
    
    void createArray() {
        offset = min;
        if(max < min) {
            bucketAry = new int[0];
        } else {
            bucketSize = max - min + 1;
            bucketAry = new int[bucketSize];
            for(int index=0; index<bucketSize; index++) {
                bucketAry[index]=0;
            }
        }
    }
    
    void getIndex(int index, ofstream &outDebugFile) {
        index -= offset;
        bucketAry[index]++;
    }
    
    void printBucketAry(ofstream &outDebugFile) {
        outDebugFile << "|";
        for(int index=0; index <= 18; index++) {
            outDebugFile << " " << index << " |";
        }
        outDebugFile << endl << "|";
        for(int index=0; index <= 18; index++) {
            if(index <10) outDebugFile << " " << bucketAry[index] << " |";
            else outDebugFile << "  " << bucketAry[index] << " |";
        }
        outDebugFile << endl;
    }
    
    void printSortedData(ofstream &outFile) {
        outFile << "Here is the sorted data, including duplicates: ";
        for(int index=0; index<bucketSize; index++) {
            while(bucketAry[index] > 0) {
                outFile << endl << index + offset;
                bucketAry[index]--;
            }
        }
    }
    
    ~BucketSort() {
        delete[] bucketAry;
    }
};

int main(int argc, const char * argv[]) {
    int datum;
    ifstream infile;
    ofstream outDebugFile;
    ofstream outFile;
    BucketSort myBucketSort;
    
    outFile.open(argv[2]);
    outDebugFile.open(argv[3]);
    
    // reading data, calculating size
    infile.open(argv[1]);
    if (infile.is_open())
    {
        outDebugFile << "Opening file and reading the data:" << endl;
        while (infile>>datum){
            outDebugFile << "Reading " << datum << " from file"<< endl;
            myBucketSort.findMinMax(datum);
        }
        outDebugFile << "Closing file" << endl;
        infile.close();
    } else {
        outDebugFile << "File is not open" << endl;
    }
    
    myBucketSort.createArray();
    
    infile.open(argv[1]);
    if (infile.is_open())
    {
        outDebugFile << "Opening file and reading the data:" << endl;
        while (infile>>datum){
            outDebugFile << "Inserting " << datum << " into bucket sort"<< endl;
            myBucketSort.getIndex(datum, outDebugFile);
        }
        outDebugFile << "Closing file" << endl;
        infile.close();
    } else {
        outDebugFile << "File is not open" << endl;
    }
    
    myBucketSort.printBucketAry(outDebugFile);
    myBucketSort.printSortedData(outFile);
    
    return 0;
}
