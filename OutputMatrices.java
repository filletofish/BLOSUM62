
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class OutputMatrices
{
    // instance variables - replace the example below with your own
    String[][] sequences;
    PrintWriter buffOutFile;
    String filename = "OutFile.txt";

    /**
     * Constructor for objects of class OutputMatrices
     */
    public OutputMatrices()
    {
        // initialise instance variables
        this.openOutFile(this.filename);
        
    }

    /** public void openOutFile()
     * reads the sequences1_20.csv file
     */
    public void openOutFile(String filename)
    {
        // open file
        try {
            buffOutFile = new PrintWriter(filename);
        }
        catch (IOException ioe) {
            System.out.println("I'm sorry sir we can't open the file named: " + filename );
            return;  
        }
    } // END openOutFile()
    
    public void write2DMatrix(String mtxName, char[] labelTop, char[] labelLeft, int[][] mtx)
    {
        int i1 = 0, i2 = 0;
        System.out.println(mtxName);
        buffOutFile.println(mtxName);
        for (int i3 = 0; i3 < labelTop.length; i3++) {
            System.out.printf("   %c  ", labelTop[i3]);
            buffOutFile.printf("   %c  ", labelTop[i3]);
        }
        System.out.println();
        buffOutFile.println();
        for (i1 = 0; i1 < mtx.length; i1++) { // outer loop thru rows
            System.out.printf("%c", labelLeft[i1]);
            buffOutFile.printf("%c", labelLeft[i1]);
            for (i2 = 0; i2 < mtx[i1].length; i2++) {
                System.out.printf("  %4d", mtx[i1][i2]);
                buffOutFile.printf("  %4d", mtx[i1][i2]);
            }
            System.out.println();
            buffOutFile.println();
        }
    } // END write2DMatrix()
    
    public void write2DMatrix(String mtxName, char[] labelTop, char[] labelLeft, double[][] mtx)
    {
        int i1 = 0, i2 = 0;
        System.out.println(mtxName);
        buffOutFile.println(mtxName);
        for (int i3 = 0; i3 < labelTop.length; i3++) {
            System.out.printf("     %c  ", labelTop[i3]);
            buffOutFile.printf("     %c  ", labelTop[i3]);
        }
        System.out.println();
        buffOutFile.println();
        for (i1 = 0; i1 < mtx.length; i1++) { // outer loop thru rows
            System.out.printf("%c", labelLeft[i1]);
            buffOutFile.printf("%c", labelLeft[i1]);
            for (i2 = 0; i2 < mtx[i1].length; i2++) {
                System.out.printf(" %7.4f", mtx[i1][i2]);
                buffOutFile.printf(" %7.4f", mtx[i1][i2]);
            }
            System.out.println();
            buffOutFile.println();
        }
    } // END write2DMatrix()
    
    public void write1DMatrix(String mtxName, char[] labelLeft, double[] mtx)
    {
        int i1 = 0;
        System.out.println(mtxName);
        buffOutFile.println(mtxName);
        for (i1 = 0; i1 < mtx.length; i1++) { // outer loop thru rows
            System.out.printf("%c %7.4f\n", labelLeft[i1], mtx[i1]);
            buffOutFile.printf("%c %7.4f\n", labelLeft[i1], mtx[i1]);
        }
    } // END write1DMatrix()
    
    public void closeOutFile()
    {
        if (buffOutFile != null) {
            buffOutFile.close();
        } // end if seqFile != null
    } // end readSequences()
}
