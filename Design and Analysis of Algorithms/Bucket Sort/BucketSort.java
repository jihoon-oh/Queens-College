import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class BucketSort {
    private int min, max, bucketSize, offset;
    private int[] bucketAry;
    
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
    
    void getIndex(int index) {
        index -= offset;
        bucketAry[index]++;
    }
    
    void printBucketAry(PrintWriter outDebugFile) {
        outDebugFile.print("|");
        for(int index=0; index <= 18; index++) {
            outDebugFile.print(" " + index + " |");
        }
        outDebugFile.println();
        outDebugFile.print("|");
        for(int index=0; index <= 18; index++) {
            if(index <10) outDebugFile.print(" " + bucketAry[index] + " |");
            else outDebugFile.print("  " + bucketAry[index] + " |");
        }
    }
    
    void printSortedData(PrintWriter outFile) {
        outFile.println("Here is the sorted data, including duplicates: ");
        for(int index=0; index<bucketSize; index++) {
        	while(bucketAry[index] > 0) {
                outFile.println(index + offset);
                bucketAry[index]--;
            }
        }
    }

	public static void main(String[] args) throws IOException {
	    int datum;
	    Scanner infile = new Scanner(new File(args[0]));
	    PrintWriter outDebugFile = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
	    PrintWriter outFile = new PrintWriter(new BufferedWriter(new FileWriter(args[2])));
	    BucketSort myBucketSort = new BucketSort();
	    
	    // reading data, calculating size
        outDebugFile.println("Opening file and reading the data:");
        while (infile.hasNext()) {
        	datum = infile.nextInt();
            outDebugFile.println("Reading " + datum + " from file");
            myBucketSort.findMinMax(datum);
        }
        outDebugFile.println("Closing file");
        infile.close();
	    
	    myBucketSort.createArray();
	    
	    infile = new Scanner(new File(args[0]));
	    outDebugFile.println("Opening file and reading the data:");
        while (infile.hasNext()){
        	datum = infile.nextInt();
            outDebugFile.println("Inserting " + datum + " into bucket sort");
            myBucketSort.getIndex(datum);
        }
        outDebugFile.println("Closing file");
        infile.close();
	    
	    myBucketSort.printBucketAry(outDebugFile);
	    myBucketSort.printSortedData(outFile);
	    
	    outFile.close();
	    outDebugFile.close();
	}
}