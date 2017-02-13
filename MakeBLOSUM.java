/** class MakeBLOSUM_ID1_ID2 
 * 
 * Naven Prasad
 * 
 */
public class MakeBLOSUM
{
    // instance variables 
    private InputSeq seqIn;
    private OutputMatrices outFile;
    private final int numAA = 20;
    private char[] aaList = {'A','C','D','E','F','G','H','I','K','L','M','N','P','Q','R','S','T','V','W','Y'}; // list of all AA
    private int[][] aaCountInCols; // aaCountInCols[20 = aaList][numCols in sequences]
    private int[] aaCountTotalInCols = new int[numAA]; // sum each row in aaCountInCols
    private double[] aaMargProb = new double[numAA]; // divide aaCountTotals by grand total
    private int[][] aaObservedSubstCount = new int[numAA][numAA]; // count all subst pairs
    private double[][] aaObservedSubstProb = new double[numAA][numAA]; // calc all subst probabilities
    private double[][] aaExpectSubst = new double[numAA][numAA]; // calc from aaMargProb
    private double[][] oddsRatio = new double[numAA][numAA]; // calc aaObs / aaExpect
    private double[][] logOddsRatio = new double[numAA][numAA]; // calc 2*log(oddsRatio)

    /**
     * Constructor for objects of class MakeBLOSUM_ID1_ID2     */
    public MakeBLOSUM()
    {
        // initialise instance variables
        seqIn = new InputSeq();
        outFile = new OutputMatrices();
        // initialize arrays
        for (int i1 = 0; i1 < numAA; i1++) {
            aaCountTotalInCols[i1] = 0;
            aaMargProb[i1] = 0.0;
            for (int i2 = 0; i2 < numAA; i2++) {
                aaObservedSubstCount[i1][i2] = 0;
                aaObservedSubstProb[i1][i2] = aaExpectSubst[i1][i2] = 0.0;
                oddsRatio[i1][i2] = logOddsRatio[i1][i2] = 0.0;
            }
        } // end for i1 loop to initialize
    }
    
    public void countAAinCols() {
        char res = ' '; // temporary char
        aaCountInCols = new int[numAA][seqIn.numCols]; // create mtx
        for (int c1 = 0; c1 < seqIn.numCols; c1++) {  // init to 0
            for (int s1 = 0; s1 < aaList.length; s1++) aaCountInCols[s1][c1] = 0;
        } // end for c1 loop thru cols
        // loop thru the columns of input sequences
        for (int c1 = 0; c1 < seqIn.numCols; c1++) {
            // loop from top to bottom row of each col
            for (int s1 = 0; s1 < seqIn.numSeqs; s1++) {
                res = seqIn.sequences[s1][c1];  // get the aa in col c1
                for (int a1 = 0; a1 < aaList.length; a1++) { // find the aa in aaList
                    if (res == aaList[a1]) {
                        aaCountInCols[a1][c1] ++;  // add 1 to that count
                        break;
                    } // end if found res in list
                } // end for a1 loop to find which aa we have
            } // end for s1 loop thru rows
        } // end for c1 loop thru cols
        outFile.write2DMatrix("aa Count in columns", aaList, aaList, aaCountInCols);
    } // END countAAinCols()
    
    /** public void calcObservedSubst()
     *   count all possible substitutions in each column in aaCountInCols[][]
     *   calc as probabilities = each cell count / total
     */
    public void calcObservedSubst() {
        int i1 = 0, i2 = 0, c1 = 0, totalI = 0;
        double totalD = 0.;
        for (i1 = 0; i1 < numAA; i1++) {
            for (i2 = 0; i2 <= i1; i2++) {
                if (i2 == i1) { // if same AA
                    for (c1 = 0; c1 < seqIn.numCols; c1++) {
                        aaObservedSubstCount[i1][i2] += (aaCountInCols[i1][c1] * (aaCountInCols[i2][c1] -1)) / 2;
                    }
                }
                else {  // if different AAs
                    for (c1 = 0; c1 < seqIn.numCols; c1++) {
                        aaObservedSubstCount[i1][i2] += aaCountInCols[i1][c1] * aaCountInCols[i2][c1];
                    }
                }
            } // end for i2 loop thru cols AA
        } // end for i1 loop thru rows AA
        outFile.write2DMatrix("aa Counted Subst rate", aaList, aaList, aaObservedSubstCount);
        // next find total subst
        for (i1 = 0; i1 < numAA; i1++) {
            for (i2 = 0; i2 <= i1; i2++) {
                totalI += aaObservedSubstCount[i1][i2];
            }
        }
        System.out.println("Total subst count = " + totalI);
        totalD = (double)totalI;
        if (totalD <= 0.) 
            return; // something wrong
        for (i1 = 0; i1 < numAA; i1++) {
            for (i2 = 0; i2 <= i1; i2++) {
                aaObservedSubstProb[i1][i2] = aaObservedSubstCount[i1][i2] / totalD;
            }
        } // end for i1 loop thru rows AA
        outFile.write2DMatrix("aa Observed Subst rate", aaList, aaList, aaObservedSubstProb);
    } // END calcObservedSubst()
    
    public void calcAAMargProb() {
        // aaCountTotalInCols[] is the sum for each AA across all columns
        int aaGrandTotal = 0;
        double dblTotal = 1.;
        for (int a1 = 0; a1 < numAA; a1++) { // loop thru AAs
            for (int c1 = 0; c1 < seqIn.numCols; c1++) { // loop thru Cols
                aaCountTotalInCols[a1] += aaCountInCols[a1][c1];
                aaGrandTotal += aaCountInCols[a1][c1];
            } // end for c1 loop thru cols
            //System.out.println(aaList[a1] + " " + a1 + " total = " + aaCountTotalInCols[a1]);
        } // end for a1 loop thru AAs
        System.out.println("Grand Total AA = " + aaGrandTotal);
        dblTotal = (double)aaGrandTotal;
        for (int a1 = 0; a1 < numAA; a1++) { // loop thru AAs
            aaMargProb[a1] = (double)aaCountTotalInCols[a1] / dblTotal;
            //System.out.println(aaList[a1] + " " + a1 + " marg = " + aaMargProb[a1] + " = " + aaCountTotalInCols[a1] + "/" + aaGrandTotal);
        } // end for a1 loop thru AAs
        outFile.write1DMatrix("AA Marginal Prob", aaList, aaMargProb);
    } // END calcAAMargProb()
    
    /** public void calcExpectedSubst()
      *  multiply marginal probabilities to get Expected substitution rates
           if it were random */
    public void calcExpectedSubst() {
        int i1 = 0, i2 = 0;
        for (i1 = 0; i1 < numAA; i1++) {
            for (i2 = 0; i2 <= i1; i2++) {
                if (i2 == i1)
                    aaExpectSubst[i1][i2] = aaMargProb[i1] * aaMargProb[i2];
                else
                    aaExpectSubst[i1][i2] = 2. * aaMargProb[i1] * aaMargProb[i2];
            }
        } // end for i1 loop thru rows AA
        outFile.write2DMatrix("aa Expected Subst rate", aaList, aaList, aaExpectSubst);
    } // END calcExpectedSubst()

    /** public void calcOddsRatio()
      *  divide Observed substitution rate by Expected substitution rates */
    public void calcOddsRatio() {
        int i1 = 0, i2 = 0;
        for (i1 = 0; i1 < numAA; i1++) {
            for (i2 = 0; i2 <= i1; i2++) {
                if (aaExpectSubst[i1][i2] > 0.) 
                    oddsRatio[i1][i2] = aaObservedSubstProb[i1][i2] / aaExpectSubst[i1][i2];
            }
        } // end for i1 loop thru rows AA
        outFile.write2DMatrix("aa Odds Ratio", aaList, aaList, oddsRatio);
    } // END calcOddsRatio()

    /** public void calcLogOddsRatio()
      *  logOddsRatio = 2 * log2(oddsRatio) */
    public void calcLogOddsRatio() {
        int i1 = 0, i2 = 0;
        double c2_10 = 2. / Math.log10(2.); // convert log10 to log2
        for (i1 = 0; i1 < numAA; i1++) {
            for (i2 = 0; i2 <= i1; i2++) {
                if (oddsRatio[i1][i2] > 0.)
                    logOddsRatio[i1][i2] = c2_10 * Math.log10(oddsRatio[i1][i2]);
                else 
                    logOddsRatio[i1][i2] = -9.99;  // value for log(0)
            }
        } // end for i1 loop thru rows AA
        outFile.write2DMatrix("aa Log(Odds Ratio)", aaList, aaList, logOddsRatio);
    } // END calcLogOddsRatio()
    
    public static void main(String[] args) {
        // open and read input sequences, open output
        MakeBLOSUM main1 = new MakeBLOSUM();
        // count AAs in columns
        main1.countAAinCols();
        // calc observed (counted) substitution rates
        main1.calcObservedSubst();
        // calc AA marginal prob
        main1.calcAAMargProb();
        // calc Expected substitution rates
        main1.calcExpectedSubst();
        // calc odds ratio of observed / expected
        main1.calcOddsRatio();
        // calc log(odds ratio)
        main1.calcLogOddsRatio();
        // close output
        main1.outFile.closeOutFile();
    } // END main()
}  // END class MakeBLOSUM_ID1_ID2
