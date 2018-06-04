
package ManosAlgorythms;

import java.util.Arrays;

/**
 *
 * @author Robert Manos
 */
public class RadixSort {

    public static int count = 0;

    public static ValueHolder RadixSort(int[] A, int d) {
        int digit = 1;
        while (digit <= d) {
            A = RadixCount(A, digit);
            digit++;
        }
        return new ValueHolder(A, count);
    }

    public static int[] RadixCount(int[] A, int d) {
        int mod = (int) Math.pow(10, d);
        int mod_1 = (int) Math.pow(10, d - 1);
        int[] sorted = new int[A.length];
        int[] C = new int[10];
        for (int j = 0; j < A.length; j++) {
            int digit = ((A[j] % mod) - (A[j] % mod_1)) / mod_1;
            C[digit]++;
        }
        for (int i = 1; i < C.length - 1; i++) {
            C[i] += C[i - 1];
        }
        for (int i = A.length - 1; i >= 0; i--) {
            int digit = ((A[i] % mod) - (A[i] % mod_1)) / mod_1;
            sorted[C[digit] - 1] = A[i];
            count++;
            C[digit]--;
        }
        return sorted;
    }

}
