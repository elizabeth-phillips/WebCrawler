package median_median;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Jason
 */
public class Median_Median {

    public static void main(String[] args) {
        int arraySize;
        Random rand = new Random();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter an integer between 1 and 100,000: ");
        arraySize = input.nextInt();
        while (arraySize < 1 || arraySize > 100000) {
            System.out.println("Enter an integer between 1 and 100,000: ");
            arraySize = input.nextInt();

        }
        int demoArray[] = new int[arraySize];
        for (int i = 0; i < demoArray.length; i++) {
            demoArray[i] = rand.nextInt(arraySize); //random nums up to size of array
        }
        printArray(demoArray);
        System.out.println("The median of medians of your Array is: " + median(demoArray));
    }

    public static int median(int a[]) {
        ArrayList<Integer> medians = new ArrayList();
        int medOfMed = 0;
        int groupSize;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter 3, 5, or 7 for group size");
        groupSize = input.nextInt();
        while (!(groupSize == 3 || groupSize == 5 || groupSize == 7)) {
            System.out.println("Enter 3, 5, or 7 for group size");
            groupSize = input.nextInt();
        }
        //if total array length is less than group size then just find median
        if (a.length < groupSize) {
            medOfMed = subMedian(a, 0, a.length);
        }
        else{
            int sumMedians = 0;
            for(int i = 0; i < a.length;){
                
                if((i+=groupSize) < a.length){
                    medians.add(subMedian(a,i, i+groupSize));
                    i+=groupSize;
                }
                else if (a.length > i){
                    medians.add(subMedian(a, i, a.length));
                    i = a.length;
                }
                else{
                    medians.add(a[a.length-1]);
                }
            }
            for(int j = 0; j < medians.size();j++){
                sumMedians+=medians.get(j);
            }
            medOfMed = sumMedians/medians.size();
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
        //sort subArray (inserttion sort)
        insertionSort(b);
        printArray(b);
        if (length % 2 == 0) {
            median = (b[((length / 2))] + b[(length / 2) + 1]) / 2;
        } else {
            median = b[(length / 2)];
        }
        System.out.println("subMedian: " + median);
        return median;

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

    //print array set up to matrix print values up to 100,000
    public static void printArray(int[] a) {
        for (int i = 0; i < a.length; i++) {
            switch (Integer.toString(a[i]).length()) {
                case 6:
                    System.out.print(a[i] + " ");
                    break;
                case 5:
                    System.out.print(a[i] + "  ");
                    break;
                case 4:
                    System.out.print(a[i] + "   ");
                    break;
                case 3:
                    System.out.print(a[i] + "    ");
                    break;
                case 2:
                    System.out.print(a[i] + "     ");
                    break;
                case 1:
                    System.out.print(a[i] + "      ");
                    break;
                default:
                    break;
            }

            if ((i > 0) && (i % 10 == 0)) {
                System.out.println();
            }
        }
        System.out.println();

    }
}
