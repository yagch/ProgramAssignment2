package iterativesolver;

public class SOR {
	public SOR() {
		
	}
	public double[] sparseSOR(SparseMatrix A, double[] b, double omega) {
		int rank = A.rank;
		double[] xk = new double[rank];    // the solution in each iteration
        double[] xk1 = new double[rank];    // the solution after each iteration
		double[] deltax = new double[rank];    // x(k+1)[] - x(k)[]
		/*Use D^-1 X b as the initial guess
		 */
		for(int i = 0; i < xk.length; i++) {
			xk[i] =  b[i] / A.retrieveElement(i, i);
		}
		deltax[0] = 20;
		int iter = 0;    // iteration times
		while(secondNorm(deltax) >= Math.pow(10, -7)) {
			for(int i = 0; i < rank; i++) {
				double xi = b[i];
				for(int j = 0; j < i; j++) {
					xi -= A.retrieveElement(i, j) * xk1[j];
				}
				for(int j = i+1; j < rank; j++) {
					xi -= A.retrieveElement(i, j) * xk[j];
				}
				xi *= omega / A.retrieveElement(i, i);
				xi += (1 - omega) * xk[i];
				xk1[i] = xi;
				deltax[i] = xk1[i] - xk[i];
			}
			for(int i = 0; i < rank; i++) {
				xk[i] = xk1[i];
			}
			System.out.println("Iteration " + iter + " ......");
			System.out.println("Second norm of deltax is: " + secondNorm(deltax));
		    iter++;
		}
		System.out.println("Converged in " + (iter) + " iterations");
		return xk;
	}
	
	/*Calculate secondNorm of a vector
	 */
	public static double secondNorm(double[] x) {
		double res = 0;
		for (int i = 0; i < x.length; i++) {
			res += x[i] * x[i];
		}
		return Math.sqrt(res);
	}
	
	/*Calculate normalized residual norm
	 */
	public double normalNorm(SparseMatrix A, double[] x, double[] b) {
		double denom = 0;     //denominator
		double numer = 0;     //numerator
		int rank = A.rank;
		double[] prod = new double[rank];     //production of Ax
		A.productAx(x, prod);
		for(int i = 0; i < rank; i++) {
			numer += (b[i] - prod[i]) * (b[i] - prod[i]);
			denom += b[i] * b[i];
		}
		return Math.sqrt(numer) / Math.sqrt(denom);
	}
}
