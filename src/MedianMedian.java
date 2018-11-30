import java.util.ArrayList;
import java.util.Random;

public class MedianMedian {

    public static int median(int a[], int groupSize) {
        ArrayList<Integer> medians = new ArrayList();
        int medOfMed = 0;
        if (a.length < groupSize) {
            medOfMed = subMedian(a, 0, a.length);
        } else {
          
            for (int i = 0; i < a.length;) {

                if ((i += groupSize) < a.length - groupSize) {
                    medians.add(subMedian(a, i, i + groupSize));
                } else if (a.length > i) {
                    medians.add(subMedian(a, i, a.length));
                    i = a.length;
                } else {
                    medians.add(a[a.length - 1]);
                    i = a.length;
                }
            }

            medians.sort(null);
            if (medians.size() == 2) {
                medOfMed = medians.get(medians.size() / 2);
            } else if (medians.size() % 2 == 0) {
                medOfMed = (medians.get(medians.size() / 2) + medians.get(medians.size() / 2) + 1) / 2;
            } else {
                medOfMed = medians.get(medians.size() / 2);
            }
        }
        return medOfMed;
    }

    public static int subMedian(int[] a, int begin, int end) {
        int[] b = new int[end - begin];
        //populate b
        int j = 0;
        for (int i = begin; i < end; i++) {
            b[j] = a[i];
            j++;
        }
        int length = b.length;
        int median = 0;
        insertionSort(b);

        if (length == 2) {
            median = b[(length / 2)];
        } else if (length % 2 == 0) {
            median = (b[((length / 2))] + b[(length / 2) + 1]) / 2;
        } else {
            median = b[(length / 2)];
        }
        return median;

    }

    //looking for i'th smallest element of the array
    public static int randomizedSelect(int[] a, int p, int r, int i) {
        if (p == r) {
            return a[p];
        }
        int q = randomizedPartition(a, p, r);
        int k = q - p + 1;
        if (i == k) {
            return a[p];
        }
        if (i < k) {
            return randomizedSelect(a, p, q - 1, i);
        } else {
            return randomizedSelect(a, q + 1, r, i - k);
        }
    }

    public static int randomizedPartition(int[] a, int p, int r) {
        int tmp;
        Random rand = new Random();
        int swap = rand.nextInt(r);
        tmp = a[p];
        a[p] = a[swap];
        a[swap] = tmp;
        int i = p, j = r;

        int pivot = a[(p + r) / 2];
        while (i <= j) {
            while (a[i] < pivot) {
                i++;
            }
            while (a[j] > pivot) {
                j--;
            }
            if (i <= j) {
                tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
                i++;
                j--;
            }
        }
        return i;
    }

    public static void insertionSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int min = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > min) {
                a[j + 1] = a[j];
                j = j - 1;
            }
            a[j + 1] = min;
        }
    }
}
