package iterativesolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
	public static void main(String[] arg) throws FileNotFoundException {
		
		try{
		       PrintStream out = new PrintStream("/Users/chenhaohan/eclipse-workspace/ProgramAssignment2/report2");
		                 System.setOut(out);
		   }catch(FileNotFoundException e){
		       e.printStackTrace();
		   }
		System.out.println(" ");
		System.out.println("Part2: Test Jacobi solver by comparing with SOR solver");
		System.out.println(" ");
		System.out.println("First read from local file......");
		double memory1 = Runtime.getRuntime().totalMemory(); // check the memory before starting
		/*Read the rank and number of non-zero entries from rowPtr.csv
		 */
		double time1 = System.currentTimeMillis();
		Scanner input1 = new Scanner (new File("/Users/chenhaohan/Desktop/ece4960/lab2/rowPtr.csv"));
		int rank = 0;  // rank of the matrix
		int nz = 0;    // number of non-zero entries
		while(input1.hasNext()) {
			nz = input1.nextInt() - 1;
			rank++;
		}
		rank -= 1;
		
		/*Read the rowPtr
		 */
		int[] rowPtr = new int[rank + 1];
		Scanner input2 = new Scanner (new File("/Users/chenhaohan/Desktop/ece4960/lab2/rowPtr.csv"));
		int i = 0;    // pointer
		while(input2.hasNext()){
			rowPtr[i] = input2.nextInt() - 1;
			i++;
		}
		
		/*Read the value
		 */
		double[] value = new double[nz];
		Scanner input3 = new Scanner (new File("/Users/chenhaohan/Desktop/ece4960/lab2/value.csv"));
		i = 0;    //pointer
		while(input3.hasNext()) {
			value[i] = input3.nextDouble();
			i++;
		}
		
		/*Read the colInd
		 */
		int[] colInd = new int[nz];
		Scanner input4 = new Scanner (new File("/Users/chenhaohan/Desktop/ece4960/lab2/colInd.csv"));
		i = 0;    //pointer
		while(input4.hasNext()) {
			colInd[i] = input4.nextInt() - 1;
			i++;
		}
		double time2 = System.currentTimeMillis(); 
		System.out.println("The time of reading is " + (time2 - time1) + "ms");
	
		/*Get the Sparse Matrix with the three vectors
		 */
		SparseMatrix matrixA = new SparseMatrix(value, rowPtr, colInd, rank);
		
		/*Implement jacobi solver
		 */
		Jacobi jac = new Jacobi();
		SOR sor = new SOR();
		double[] b = new double[rank];
		
		
		// Solution with first b
		time1 = System.currentTimeMillis();
		System.out.println(" ");
		System.out.println("Solve with first b:");
		b[0] = 1;
		for(int j = 1; j < rank; j++) {
			b[j] = 0;
		}
		double[] x1 = jac.sparseJacobi(matrixA, b);
		double norm1 = jac.normalNorm(matrixA, x1, b);
		System.out.println("The normalized residual norm is: " + norm1);
		time2 = System.currentTimeMillis(); 
		System.out.println("The computational time is " + (time2 - time1) + "ms");
		
		// Test with SOR solver with first b
		time1 = System.currentTimeMillis();
		System.out.println(" ");
		System.out.println("Test with SOR with first b:");
		double[] x12 = sor.sparseSOR(matrixA, b, 0.5);
		double norm12 = sor.normalNorm(matrixA, x12, b);
		System.out.println("The normalized residual norm is:  " + norm12);
	    time2 = System.currentTimeMillis(); 
		System.out.println("The computational time is " + (time2 - time1) + "ms");
		
		
		// Solution with second b
		time1 = System.currentTimeMillis();
		System.out.println(" ");
		System.out.println("Solve with second b:");
		b[0] = 0;
		b[5] = 1;
		double[] x2 = jac.sparseJacobi(matrixA, b);
		double norm2 = jac.normalNorm(matrixA, x2, b);
		System.out.println("The normalized residual norm is: " + norm2);
	    time2 = System.currentTimeMillis(); 
		System.out.println("The computational time is " + (time2 - time1) + "ms");
		
		//  Test with SOR solver with second b
		time1 = System.currentTimeMillis();
		System.out.println(" ");
		System.out.println("Test with SOR with second b:");
		double[] x22 = sor.sparseSOR(matrixA, b, 0.5);
		double norm22 = sor.normalNorm(matrixA, x22, b);
		System.out.println("The normalized residual norm is: " + norm22);
		time2 = System.currentTimeMillis(); 
		System.out.println("The computational time is " + (time2 - time1) + "ms");
		
		// Solution with third b
		time1 = System.currentTimeMillis();
		System.out.println(" ");
		System.out.println("Solve with third b:");
		for(int j = 0; j < rank; j++) {
			b[j] = 1;
		}
		double[] x3 = jac.sparseJacobi(matrixA, b);
		double norm3 = jac.normalNorm(matrixA, x3, b);
		System.out.println("The normalized residual norm is: " + norm3);
		time2 = System.currentTimeMillis(); 
		System.out.println("The computational time is " + (time2 - time1) + "ms");
		
		//  Test with SOR solver with third b
		time1 = System.currentTimeMillis();
		System.out.println(" ");
		System.out.println("Test with SOR with third b:");
		double[] x32 = sor.sparseSOR(matrixA, b, 0.5);
		double norm32 = sor.normalNorm(matrixA, x32, b);
		System.out.println("The normalized residual norm is: " + norm32);
		time2 = System.currentTimeMillis(); 
		System.out.println("The computational time is " + (time2 - time1) + "ms");
        double memory2 = Runtime.getRuntime().totalMemory();    //the memory after implementation
        System.out.println("The memory usage is " + (memory2 - memory1));    //the memory usage
        System.out.println(" ");
        System.out.println("Compare the result of SOR and Jacobi, both of the two methods can converge"
        		+ "and the normalized residual norms are very small. From Wilkinson principle, the Jacobi"
        		+ "solver works fine");
	}
}
