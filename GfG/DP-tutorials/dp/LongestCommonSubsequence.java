package dp;


import java.util.Arrays;


public class LongestCommonSubsequence {
  
  /**
   * Compute the length of a longest common subsequence (LCS) of two given sequences.
   * Let L(i, j) be the length of LCS of the First i elements of L1 and the First j elements of L2.
   * Case 1: if L1[i] == L2[j] then L(i, j) = 1 + L(i-1, j-1) for 1 <= i <= m, 1 <= j <= n.
   * Case 2: if L1[i] != L2[j] then L(i, j) = Max { L(i-1, j), L(i, j-1) } for 1 <= i <= m, 1 <= j <= n.
   * Return L(m, n) where m and n are the length of A1 and A2 respectively.
   */
  public static int[] lcs(int[] A1, int[] A2) {
    final int m = A1.length, n = A2.length;
    final int[][] L = new int[m+1][n+1];
    // Explicitly initialize L[0][j] and L[i][0] for 0 <= j <= n, 1 <= i <= m.
    Arrays.fill(L[0], 0);
    for (int i = 1; i <= m; i++) {
      L[i][0] = 0;
    }
    final int[][][] P = new int[m][n][2];
    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        if (A1[i-1] == A2[j-1]) {
          // Case 1
          L[i][j] = 1 + L[i-1][j-1];
          P[i][j][0] = i-1;
          P[i][j][1] = j-1;
        } else {
          // Case 2
          //L[i][j] = Math.max(L[i-1][j], L[i][j-1]);
          if (L[i-1][j] >= L[i][j-1]) {
            L[i][j] = L[i-1][j];
            P[i][j][0] = i-1;
            P[i][j][1] = j;
          } else {
            L[i][j] = L[i][j-1];
            P[i][j][0] = i;
            P[i][j][1] = j-1;
          }
        }
      }
    }
    
    // Reconstruct the actual LCS
    int[] LCS = new int[L[m-1][n-1]];
    int i = m-1, j = n-1, k = L[m-1][n-1];
    while (i >= 1 && j >= 1) {
      //if (L[i][j] == 1 + L[i-1][j-1]) { WRONG
      if (A1[i] == A2[j]) {
        // Case 1
        LCS[--k] = A1[i];
        i--;
        j--;
      } else {
        // Case 2
        if (L[i][j] == L[i-1][j]) {
          i--;
        } else {
          j--;
        }
      }
    }
    if (A1[i] == A2[j]) {
      // Case 1
      LCS[--k] = A1[i];
    }
    
    // Reconstruct the actual LCS2
    int[] LCS2 = new int[L[m-1][n-1]];
    i = m-1; j = n-1; k = L[m-1][n-1];
    while (i >= 1 && j >= 1) {
      if (A1[i] == A2[j]) {
        LCS2[--k] = A1[i];
      }
      // Save i before update i
      int currI = i;
      i = P[i][j][0];
      j = P[currI][j][1];
    }
    if (A1[i] == A2[j]) {
      // Case 1
      LCS2[--k] = A1[i];
    }
    
    if (!Arrays.equals(LCS, LCS2)) {
      System.out.printf("%nLCS: %s%nLCS2:%s%n", Arrays.toString(LCS), Arrays.toString(LCS2));
      throw new RuntimeException("Reconstruction WRONG!");
    }
    
    return LCS;
  }
 
  public static void main(String[] args) {
    /*
    int[] A1 = {1,4,2,6,5,8,3,9,6,3,2,7};
    int[] A2 = {6,2,7,8,3,1,2,7,5,1,4};
    */
    int[] A1 = {6, 9, 7, 1, 6, 3, 2, 8, 2, 4, 3, 5};
    int[] A2 = {6, 2, 7, 8, 3, 1, 2, 7, 5, 1, 4};
    for (int i = 0; i < 13; i++) {
      System.out.printf("2 input sequences:%n%s%n%s%n", Arrays.toString(A1), Arrays.toString(A2));
      int[] lcs = lcs(A1, A2);
      System.out.printf("%nLCS: %s (length = %d)%n------------------------------------%n", Arrays.toString(lcs), lcs.length);
      LongestIncreasingSubsequence.shuffle(A1);
    }
  }
  
}
