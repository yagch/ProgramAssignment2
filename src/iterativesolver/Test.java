package iterativesolver;
import java.io.*;

 /*Test the jacobi solver with 5X5 matrix
  */
public class Test {
	public static void main(String[] arg) {
		/*Initialize the matrix
		 */
		try{
		       PrintStream out = new PrintStream("/Users/chenhaohan/eclipse-workspace/ProgramAssignment2/report1");
		                 System.setOut(out);
		   }catch(FileNotFoundException e){
		       e.printStackTrace();
		   }
		System.out.println("Assignment 2");
		System.out.println(" ");
		System.out.println("@Author: Haohan Chen");
		System.out.println("@netID: hc937");
		System.out.println("@Teammate:Yiqi Yu, yy757");
		System.out.println("@Language: Java");
		System.out.println("@OS: MacOS");
		System.out.println("@Platform: eclipse-oxygen");
		System.out.println(" ");
		System.out.println("In this Assignment, we used two test method: the first is to use the 5X5 matrix to test the "
				+ "result. The second is to use SOR solver to solve the same matrix we have read and compare the result "
				+ "with Jacobi, from Wilkinson principle we know that if the two results are the same, both of the results"
				+ "are correct.");
		
		System.out.println(" ");
		System.out.println("Part1: Use 5X5 matrix to test Jacobi solver");
		double[] b = {1,0,0,0,0};
		int[] rowPtr = {0, 3, 6, 9, 12, 15};
		int[] colInd = {0, 1, 4, 0, 1, 2, 1, 2, 3, 2, 3, 4, 0, 3, 4};
		double[] val = {-4, 1, 1, 4, -4, 1, 1, -4, 1, 1, -4, 1, 1, 1, -4};
		Jacobi jac = new Jacobi();
		SOR sor = new SOR();
		SparseMatrix A = new SparseMatrix(val, rowPtr, colInd, 5);
		
		// Test with b
		System.out.println(" ");
		System.out.println("Test in jacobi with 5X5 matrix and {1,0,0,0,0}:");
		double[] x1 = jac.sparseJacobi(A, b);
		double norm1 = jac.normalNorm(A, x1, b);
		System.out.println("The normalized residual norm is: " + norm1);
        
		System.out.println(" ");
		System.out.println("Test in SOR with 5X5 matrix and {1,0,0,0,0}:");
		double[] x2 = sor.sparseSOR(A, b, 0.9);
		double norm2 = sor.normalNorm(A, x2, b);
		System.out.println("The normalized residual norm is: " + norm2);
	}
}
