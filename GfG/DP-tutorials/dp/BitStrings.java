package dp;


import java.util.*;


/**
 * Counts number of bit strings which satisfy some constraints.
 */
public class BitStrings {
  
  /**
   * Counts number of bit strings of length n, which don't have k consecutive 0s (i.e. has at most k-1 consecutive 0s).
   */
  public static long noKConsecutive0s(int n, int k, boolean bottomUp) {
    /*
     * Assume k = 3.
     * Let C(n) = number of bit strings of length n, which don't have 3 consecutive 0s.
     * Consider an arbitrary such bit string S.
     * Case 1: if S ends with '1' then C(n) = C(n-1).
     * Case 2: if S ends with '10' then C(n) = C(n-2).
     * Case 3: if S ends with '100' then C(n) = C(n-3).
     * Since these 3 cases are disjoint and cover all the possibilities we have C(n) = C(n-1) + C(n-2) + C(n-3).
     * Thus, generalization for k, we have C(n) = C(n-1) + C(n-2) + C(n-3) + ... + C(n-k).
     */
    if (n < 0) {
      throw new IllegalArgumentException(String.format("n must be a nonnegative integer: %d", n));
    }
    if (k < 0) {
      throw new IllegalArgumentException(String.format("k must be a nonnegative integer: %d", k));
    }

    if (bottomUp) {
      return noKConsecutive0sTabu(n, k);
    }

    long[] computedCounts = new long[n + 1];
    Arrays.fill(computedCounts, -1);
    return noKConsecutive0sMemo(n, k, computedCounts);
  }
  
  private static long noKConsecutive0sMemo(int n, int k, long[] computedCounts) {
    if (computedCounts[n] != -1) {
      return computedCounts[n];
    }
    
    // Base case
    if (n < k) {
      computedCounts[n] = 1L << n; // 2 power to n
      return computedCounts[n];
    }
    
    long count = 0L;
    for (int i = 1; i <= k; i++) {
      count += noKConsecutive0sMemo(n-i, k, computedCounts);
    }
    computedCounts[n] = count;
    return count;
  }
  
  private static long noKConsecutive0sTabu(int n, int k) {
    if (n < k) {
      return 1L << n;
    }
    // Convention, there is no bit string with at most -1 consecutive 0s
    if (k == 0) {
      return 0;
    }
    
    // The currently last k computed counts
    // Initially C[0..k-1] holds [C(0), .., C(k-1)]; Note that C(i) = 2^i for 0 <= i <= k-1
    long[] C = new long[k];
    for (int i = 0; i < k; i++) {
      C[i] = 1L << i;
    }
    
    long Cn;
    for (int i = k; i < n; i++) {
      // Compute next count by summing over the last k computed counts
      Cn = 0L;
      for (long prev: C) {
        Cn += prev;
      }

      // Make the next count as the last computed count by shifting each element to the left by one position
      for (int idx = 1; idx < k; idx++) {
        C[idx-1] = C[idx];
      }
      C[k-1] = Cn;
    }

    // Compute the final count by summing over the last k computed counts
    Cn = 0L;
    for (long prev: C) {
      Cn += prev;
    }
    
    return Cn;
  }
  
  /**
   * Counts number of bit strings of length n, which have k consecutive 0s (i.e. has at least k consecutive 0s).
   */
  public static long kConsecutive0s(int n, int k, boolean bottomUp) {
    /*
     * Let C(n) = number of bit strings of length n, which have at least k consecutive 0s.
     * Consider an arbitrary such bit string S.
     * Case 1: if S ends with '1' then C(n) = C(n-1).
     * Case 2: if S ends with '10' then C(n) = C(n-2).
     * Case 3: if S ends with '100' then C(n) = C(n-3).
     * ...
     * Case k: if S ends with '100...0' ( 1 followed by (k-1) 0s ) then C(n) = C(n-k).
     * Case k+1: if S ends with '000...0' ( k 0s ) then C(n) = 2^(n-k).
     * Since these k+1 cases are disjoint we have C(n) = C(n-1) + C(n-2) + C(n-3) + ... + C(n-k) + 2^(n-k).
     */
    if (n < 0) {
      throw new IllegalArgumentException(String.format("n must be a nonnegative integer: %d", n));
    }
    if (k < 0) {
      throw new IllegalArgumentException(String.format("k must be a nonnegative integer: %d", k));
    }

    if (bottomUp) {
      return kConsecutive0sTabu(n, k);
    }
    long[] computedCounts = new long[n+1];
    Arrays.fill(computedCounts, -1);
    return kConsecutive0sMemo(n, k, computedCounts);
  }
  
  private static long kConsecutive0sMemo(int n, int k, long[] computedCounts) {
    if (computedCounts[n] != -1) {
      return computedCounts[n];
    }
    
    // Base case
    if (n < k) {
      computedCounts[n] = 0;
      return 0;
    }
    
    long count = 1L << (n-k); // Case k+1
    for (int i = 1; i <= k; i++) {
      count += kConsecutive0sMemo(n-i, k, computedCounts);
    }
    computedCounts[n] = count;
    return count;
  }
  
  private static long kConsecutive0sTabu(int n, int k) {
    if (n < k) {
      return 0L;
    }
    
    // C[0..k-1] holds the currently last k computed counts; C[k] to hold next count
    // Initially C[0..k-1] holds [C(0), .., C(k-1)]; Note that C(i) = 0 for 0 <= i <= k-1.
    long[] C = new long[k+1];
    
    for (int i = k; i < n; i++) {
      // Compute next count by summing over the last k computed counts
      C[k] = 1L << (i-k); // Case k+1
      for (int idx = 0; idx < k; idx++) {
        C[k] += C[idx];
      }

      // Make the next count as the last computed count by shifting each element to the left by one position
      for (int idx = 0; idx < k; idx++) {
        C[idx] = C[idx+1];
      }
    }

    // Compute the final count by summing over the last k computed counts
    C[k] = 1L << (n-k); // Case k+1
    for (int idx = 0; idx < k; idx++) {
      C[k] += C[idx];
    }    

    return C[k];
  }
  
  

  public static void main(String[] args) {
    int n = 11, k = 3;
    if (args.length > 0) {
      n = Integer.valueOf(args[0]);
    }
    if (args.length > 1) {
      k = Integer.valueOf(args[1]);
    }
    
    long noKConsecutive0sMemo = noKConsecutive0s(n, k, false);
    long noKConsecutive0sTabu = noKConsecutive0s(n, k, true);
    if (noKConsecutive0sMemo != noKConsecutive0sTabu) {
      throw new RuntimeException(String.format("noKConsecutive0sTabu() -> %d OR noKConsecutive0sMemo() -> %d WRONG", noKConsecutive0sTabu, noKConsecutive0sMemo));
    }
    System.out.printf("# of bit strings of length %d Without %d consecutive 0s (i.e. with at most %d consecutive 0s): %d%n", n, k, k-1, noKConsecutive0sTabu);
    
    long kConsecutive0sMemo = kConsecutive0s(n, k, false);
    long kConsecutive0sTabu = kConsecutive0s(n, k, true);
    if (kConsecutive0sMemo != kConsecutive0sTabu) {
      throw new RuntimeException(String.format("kConsecutive0sTabu() -> %d OR kConsecutive0sMemo() -> %d WRONG", kConsecutive0sTabu, kConsecutive0sMemo));
    }
    System.out.printf("# of bit strings of length %d With %d consecutive 0s (i.e. with at least %d consecutive 0s): %d%n", n, k, k, kConsecutive0sTabu);

    if (noKConsecutive0sTabu + kConsecutive0sTabu != 1L << n) {
      throw new RuntimeException(String.format("noKConsecutive0s() -> %d OR kConsecutive0s() -> %d WRONG", noKConsecutive0sTabu, kConsecutive0sTabu));
    }
  }
  
}
