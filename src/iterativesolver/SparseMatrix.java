package iterativesolver;

public class SparseMatrix {
	public static double[] value;
	public static int[] rowPtr;
	public static int[] colInd;
	public static int rank;
	
	/*Initialize the sparsematrix
	 */
	public SparseMatrix() {
		
	}
	
	/*Initialize the sparsematrix
	 */
	public SparseMatrix(double[] val, int[] rowPt, int[] colIn, int ran) {
		value = val;
		rowPtr = rowPt;
		colInd = colIn;
		rank = ran;
	}
	
	/*Initialize the sparsematrix
	 */
	public SparseMatrix (double[] x, int ran, int size) {
		double[] val = new double[size];
		int[] col = new int[size];
		int[] rowpt = new int[ran + 1];
		rowpt[0] = 0;
		int pointer = 0;
		int rowpointer = 0;
		for(int i = 0; i < ran; i++) {
			for(int j = 0; j < ran; j++) {
				if(x[ran * i + j] != 0) {
					val[pointer] = x[ran * i + j];
					col[pointer] = j;
					pointer++;
					rowpointer++;
				}
			}
			rowpt[i + 1] = rowpointer;
		}
		value = val;
		rowPtr = rowpt;
		rank = ran;
		colInd = col;
	}
	
	/*Converse the matrix to a full matrix. The reason why not using double[][]as the returned
	value is to save the cost of storage.
	*/
	public void toFullMatrix(double[][] A){
		if(A.length != rank || A[0].length != rank) {
			System.out.println("cannot converse");
			return;
		}
		for(int i = 0; i < rank; i++)
			for(int j = 0; j < rank; j++)
				A[i][j] = 0;
		int m = 1;
		int n = 1;
		while(n <= value.length) {
			while(n > rowPtr[m]) 
				m++;
			A[m - 1][colInd[n - 1]] = value[n - 1];
			n++;
		}
	}
	
	/*Retrieve the element whose coordinate is (m, n)
	 */
	public double retrieveElement(int m, int n) {
		double res = 0;
		for(int i = rowPtr[m]; i < rowPtr[m+1]; i++) {
			if(colInd[i] == n) {
				res = value[i];
			}
		}
		return res;
	}
	
	/*Permute ith row and jth row, return 0 if done successfully.
	 */
	public int permute(int i, int j) {
	   if(i > rank || j > rank) {
			System.out.println("Cannot permute");
			return -1;
		}
		if(i == j)
			return 1;
		if(i > j) {
			int m = i;
			i = j;
			j = m;
		}
		double[] newval = new double[value.length];
	    int[] newcolInd = new int[colInd.length];
		int[] newrowPtr = new int[rowPtr.length];
		for(int m = 0; m < rowPtr[i - 1]; m++) {
			newval[m] = value[m];
			newcolInd[m] = colInd[m];
		}
		for(int m = rowPtr[i - 1]; m < rowPtr[i - 1] + rowPtr[j] - rowPtr[j - 1]; m++) {
			newval[m] = value[m + rowPtr[j - 1] - rowPtr[i-1]];
			newcolInd[m] = colInd[m + rowPtr[j - 1] - rowPtr[i-1]];
		}
		for(int m = rowPtr[i - 1] + rowPtr[j] - rowPtr[j - 1]; m < rowPtr[j] - rowPtr[i] + rowPtr[i - 1]; m++) {
			newval[m] = value[m - rowPtr[j] + rowPtr[j-1] + rowPtr[i] - rowPtr[i - 1]];
			newcolInd[m] = colInd[m - rowPtr[j] + rowPtr[j-1] + rowPtr[i] - rowPtr[i - 1]];
		}
		for(int m = rowPtr[j] - rowPtr[i] + rowPtr[i - 1]; m < rowPtr[j]; m++) {
			newval[m] = value[m - rowPtr[j] + rowPtr[i]];
			newcolInd[m] = colInd[m - rowPtr[j] + rowPtr[i]];
		}
		for(int m = rowPtr[j]; m < value.length; m++) {
			newval[m] = value[m];
			newcolInd[m] = colInd[m];
		}
		value = newval;
		colInd = newcolInd;
		for(int m = 0; m < i; m++){
			newrowPtr[m] = rowPtr[m];
		}
		newrowPtr[i] = rowPtr[i - 1] + rowPtr[j] - rowPtr[j - 1];
		for(int m = i + 1; m < j; m++) {
			newrowPtr[m] = rowPtr[m] + rowPtr[j] - rowPtr[j - 1] - rowPtr[i] + rowPtr[i - 1];
		}
		newrowPtr[j] = newrowPtr[j - 1] + rowPtr[i] - rowPtr[i - 1];
		for(int m = j + 1; m <= rank; m++) {
			newrowPtr[m] = rowPtr[m];
		}
		rowPtr = newrowPtr;
		return 0;
	}
	
	/*Multiply the elements in ith row with a and add to corresponding elements in jth row.
	 * Return 0 if done successfully.
	 */
	public int scaling(int i, int j, double a) {
		if(i > rank || j > rank) {
			System.out.println("Cannot scale");
			return -1;
			}
		double[] newval = new double[value.length + rank + 1];
	    int[] newcolInd = new int[colInd.length + rank + 1];
	    int[] colInd1 = new int[value.length + rank];
	    double[] value1 = new double[value.length + rank];
	    for(int m = 0; m < value.length; m++) {
	    	    colInd1[m] = colInd[m];
	    	    value1[m] = value[m];
	    }
	    for(int m = value.length; m < value1.length; m++) {
	    	    colInd1[m] = rank + 1;
	    	    value1[m] = 0;
	    }
		for(int m = 0; m < rowPtr[j - 1]; m++) {
			newcolInd[m] = colInd[m];
			newval[m] = value[m];
		}
		for(int m = value.length; m < newval.length; m++ ) {
			newval[m] = 0;
			newcolInd[m] = rank;
		}
		int count = 0;
		int ptr1 = rowPtr[i - 1];
		int ptr2 = rowPtr[j - 1];
		int ptr = rowPtr[j - 1];
		while((ptr1 < rowPtr[i] || ptr2 < rowPtr[j]) && ptr < rowPtr[j] + rank) {
			if(ptr1 < rowPtr[i] && ptr2 < rowPtr[j]) {
				if(colInd1[ptr1] == colInd1[ptr2]) {
					newval[ptr] = value[ptr2] + a * value[ptr1];
					newcolInd[ptr] = colInd[ptr1];
					ptr1++;
					ptr2++;
					ptr++;
				}
				else if(colInd1[ptr1] < colInd1[ptr2]) {
					newval[ptr] = a * value[ptr1];
					newcolInd[ptr] = colInd[ptr1];
					ptr1++;
					ptr++;
					count++;
				}
				else {
					newval[ptr] = value[ptr2];
					newcolInd[ptr] = colInd[ptr2];
					ptr2++;
					ptr++;
				}
			}
			else if(ptr1 < rowPtr[i]) {
				newval[ptr] = a * value[ptr1];
				newcolInd[ptr] = colInd[ptr1];
				ptr1++;
				ptr++;
				count++;
			}
			else {
				newval[ptr] = value[ptr2];
				newcolInd[ptr] = colInd[ptr2];
				ptr2++;
				ptr++;
			}
		}
		for(int m = rowPtr[j] + count; m < value.length + count; m++) {
			newval[m] = value[m - count];
			newcolInd[m] = colInd[m - count];
		}
		double[] newval1 = new double[value.length + count];
	    int[] newcolInd1 = new int[colInd.length + count];
	    for(int m = 0; m < newval1.length; m++) {
	    	    newval1[m] = newval[m];
	    	    newcolInd1[m] = newcolInd[m];
	    }
	    value = newval1;
	    colInd = newcolInd1;
	    int[] newrowPtr = new int[rank + 1];
	    for(int m = 0; m < j; m++) {
	    	    newrowPtr[m] = rowPtr[m];
	    }
	    for(int m = j; m < rank + 1; m++) {
	    	    newrowPtr[m] = rowPtr[m] + count;
	    }
	    rowPtr = newrowPtr;
	    return 0;
	}
	
	/*Product the matrix with vector A, get the result b. Return 0 if product is done successfully
	 */
	public  int productAx(double[] x, double[] b) {
		if(x.length != rank || b.length != rank) {
			System.out.println("Cannot product");
			return -1;
		}
		for(int i = 0; i < rank; i++) {
			double val = 0;
			for(int j = rowPtr[i]; j < rowPtr[i + 1]; j++) {
				val += value[j] * x[colInd[j]];
			}
			b[i] = val;
		}
		return 0;
	}
	
	/*Output the matrix in full matrix format
	 */
	public void output() {
		System.out.println("The matrix is: " );
		for(int i = 0; i < rank; i++) {
			for(int j = 0; j < rank; j++) {
				System.out.print(retrieveElement(i, j) + " ");
				if(j == rank - 1)
					System.out.println("");
			}
		}
	}
}


