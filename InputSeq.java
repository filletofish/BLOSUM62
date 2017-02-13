
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InputSeq
{
    // instance variables 
    int numCols = 0, numSeqs = 0;
    char[][] sequences; // use CHAR instead of String for faster searches
    BufferedReader seqFile;

    /**
     * Constructor for objects of class InputSeq    */
    public InputSeq()
    {
        this.readSequences("group1.csv"); // This can be changed for any group, eg; group12.csv
        numSeqs = 20; // hard-code 20 for this exercise
    }

    /** public void readSequences()
     * reads the sequences1_20.csv file
     * stores the letters as chars in this.sequences[][]
     */
    public void readSequences(String filename)
    {
        String line = "";
        String[] valStrings;
        int i = 0;
        // open file
        try {
            seqFile = new BufferedReader(new FileReader(filename));
        }
        catch (IOException e) {
            System.out.println("Error in opening the file: " + filename);
            return;  
        }
        // read first line
        try {
                line = seqFile.readLine();
        }
        catch (IOException e) {
                System.out.println("Error reading the lines in file: " + filename);
                return;  
        }
        valStrings = line.split(",");
        numCols = valStrings.length;
        this.sequences = new char[20][numCols]; // there are 20 sequences with numCols each
        System.out.printf(" 1 ");
        for (int i2 = 0; i2 < numCols; i2++) {
            this.sequences[0][i2] = valStrings[i2].charAt(0);
            System.out.print(" " + this.sequences[0][i2]);
        }
        System.out.println();
        for (int i1 = 1; i1 < 20; i1++) {
             try {
                line = seqFile.readLine();
                valStrings = line.split(",");
                System.out.printf("%2d ", (i1 + 1));
                for (int i2 = 0; i2 < numCols; i2++)  {
                    this.sequences[i1][i2] = valStrings[i2].charAt(0);
                    System.out.print(" " + this.sequences[i1][i2]);
                }
                System.out.println();
            }
            catch (IOException e) {
                System.out.println("File not found: " + filename); //chang3 dis laturrr
                return;  
            }
        } // end for i1 loop reading sequence file
         // end loop to read
        if (seqFile != null) {
            try { seqFile.close(); }
            catch(IOException e) {
                System.out.println("This" + filename + "cannot be closed. "); //i feel like killing someone rn 
            }
        } // end if seqFile != null
    } // end readSequences()
}  // END class InputSeq_ID1_ID2
