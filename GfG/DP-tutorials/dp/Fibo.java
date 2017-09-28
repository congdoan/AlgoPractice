package dp;


import java.util.Arrays;


/**
 * Computes n-th Fibonacci number.
 */
public class Fibo {

  /**
   * Recursion only
   */
  public static long fibRec(int n) {
    // Base case
    if (n <= 1) {
      return n;
    }
    // Recursive case
    return fibRec(n - 1) + fibRec(n - 2);
  }
  
  /**
   * Recursion with Memozation (aka Top-Down)
   */
  //public static long fibMemo(int n, Map<Integer, Long> computedFibs) {
  public static long fibMemo(int n, long[] computedFibs) {
    if (computedFibs[n] == -1) {
      if (n <= 1) {
        // Base case
        computedFibs[n] = n;
      } else {
        // Recursive case
        computedFibs[n] = fibMemo(n - 1, computedFibs) + fibMemo(n - 2, computedFibs);
      }
    }    
    return computedFibs[n];
  }
  
  /**
   * Iteration with Tabulation (aka Bottom-Up)
   */
  public static long fibTabu(int n) {
    long a = 0, b = 1; // two previous Fibonacci numbers
    for (int i = 2; i <= n; i++) {
      long last = b;
      b += a;
      a = last;
    }
    return b;
  }
  
  public static void main(String[] args) {
    final int N = args.length > 0 ? Integer.valueOf(args[0]) : 40;
    
    if (N > 45) {
      System.out.printf("Recursion-Only version is Too SLOW to compute %d-th Fibonacci number%n%n", N);
    } else {
      long start = System.currentTimeMillis();
      final long fib = fibRec(N);
      System.out.printf("Running time of Recursion Only:            %d Milliseconds%n", (System.currentTimeMillis() - start));
      System.out.printf("%d-th Fibonacci number: %d%n%n", N, fib);
    }
    
    long start = System.currentTimeMillis();
    //long fib = fibMemo(N, new HashMap<Integer, Long>(N + 1));
    long[] memo = new long[N + 1];
    Arrays.fill(memo, -1L);
    long fib = fibMemo(N, memo);
    System.out.printf("Running time of Recursion with Memozation: %d Milliseconds%n", (System.currentTimeMillis() - start));
    System.out.printf("%d-th Fibonacci number: %d%n%n", N, fib);
    
    start = System.currentTimeMillis();
    if (fib != fibTabu(N)) {
      throw new RuntimeException("fibTabu WRONG!");
    }
    System.out.printf("Running time of Iteration with Tabulation: %d Milliseconds%n", (System.currentTimeMillis() - start));
    System.out.printf("%d-th Fibonacci number: %d%n", N, fib);
  }
  
}
