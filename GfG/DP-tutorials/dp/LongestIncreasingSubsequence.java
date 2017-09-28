package dp;


import java.util.Arrays;
import java.util.Random;


public class LongestIncreasingSubsequence {
  
  /**
   * Compute the length of a longest increasing subsequence (LIS) of the given sequence.
   * Let L(i) be the length of a LIS ending at index i such that A[i] is the last element of the LIS.
   * L(i) = 1 + Max( L(j) ) 0 <= j < i and A[j] < A[i].
   * Return Max( L(i) ) 0 <= i < n where n is the length of given sequence A.
   * 
   * Iteration with Tabulation (aka Bottom-Up).
   */
  public static int lis(int[] A) {
    final int n = A.length;
    if (n == 0) {
      return 0;
    }
    int[] L = new int[n];
    L[0] = 1;
    for (int i = 1; i < n; i++) {
      int maxLj = 0;
      for (int j = 0; j < i; j++) {
        if (A[j] < A[i]) {
          maxLj = Math.max(maxLj, L[j]);
        }
      }
      L[i] = 1 + maxLj;
    }
    return maxElement(L);
  }
  
  /*
   * Utility function to return max element in the given array.
   */
  private static int maxElement(int[] arr) {
    int res = arr[0];
    for (int i = 1; i < arr.length; i++) {
      res = Math.max(res, arr[i]);
    }
    return res;
  }
  
  /**
   * Recursion with Memozation (aka Top-Down).
   */
  public static int lisMemo(int[] A) {
    final int n = A.length;
    if (n == 0) {
      return 0;
    }
    int[] L = new int[n];
    Arrays.fill(L, -1);
    for (int i = n-1; i >= 0; i--) {
      lisMemo(A, L, i);
    }
    return maxElement(L);
  }
  /*
   * Helper function to compute the L[i].
   */
  private static void lisMemo(int[] A, int[] L, int i) {
    if (L[i] == -1) {
      if (i == 0) {
        // Base case
        L[0] = 1;
      } else {
        // Recursive case
        int maxLj = 0;
        for (int j = i-1; j >= 0; j--) {
          if (A[j] < A[i]) {
            lisMemo(A, L, j);
            maxLj = Math.max(maxLj, L[j]);
          }
        }
        L[i] = 1 + maxLj;
      }
    }
  }
  
  public static void shuffle(int[] arr) {
    Random rd = new Random();
    for (int i = 1; i < arr.length; i++) {
      swap(arr, i, rd.nextInt(i + 1));
    }
  }
  
  private static void swap(int[] a, int i, int j) {
    if (i != j) {
      a[i] = a[i] ^ a[j];
      a[j] = a[i] ^ a[j];
      a[i] = a[i] ^ a[j];
    }
  }
  
  public static void main(String[] args) {
    int[] A = {10, 60, 22, 50, 9, 33, 41, 21};
    for (int i = 0; i < 39; i++) {
      System.out.printf("Input sequence: %s%n", Arrays.toString(A));
      int lisTabu = lis(A), lisMemo = lisMemo(A);
      if (lisTabu != lisMemo) {
        throw new RuntimeException(String.format("lisTabu = %d Whereas lisMemo = %d", lisTabu, lisMemo));
      }
      System.out.printf("Length of LIS (Tabulation): %d%n%n", lisTabu);
      shuffle(A);
    }
  }
  
}
