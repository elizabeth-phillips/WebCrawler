import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class SorterQuick {

    public static void main(String[] args) {
        int arraySize;
        int groupSize;
        int menu;
        int findI;
        double runTime, timeStart, timeFinish;
        Random rand = new Random();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter an integer between 1 and 10,000,000: ");
        arraySize = input.nextInt();
        while (arraySize < 1 || arraySize > 10000000) {
            System.out.println("Enter an integer between 1 and 10,000,000: ");
            arraySize = input.nextInt();

        }
        int demoArray[] = new int[arraySize];
        for (int i = 0; i < demoArray.length; i++) {
            demoArray[i] = rand.nextInt(arraySize); //random nums up to size of array
        }

        printArray(demoArray);
        boolean runMe = true;
        while (runMe) {
            System.out.println("Choose a number from the following menu: ");
            System.out.println("(1) Quicksort with Median");
            System.out.println("(2) Quicksort with Random");
            System.out.println("(3) Quicksort with Middle Element [traditional]");
            System.out.println("(4) Quicksort with Order Statistic Randomized Selection");
            try {
                menu = input.nextInt();
                while (menu < 1 || menu > 4) {
                    System.out.println("Choose a number from the following menu: ");
                    System.out.println("(1) Quicksort with Median");
                    System.out.println("(2) Quicksort with Random");
                    System.out.println("(3) Quicksort with Middle Element [traditional]");
                    System.out.println("(4) Quicksort with Order Statistic Randomized Selection");
                    menu = input.nextInt();
                }
            } catch (InputMismatchException e) {
                System.out.println(e + " :That's not a number!");
                menu = 0;
            }
            switch (menu) {
                case 1:
                    System.out.println("Enter 3, 5, or 7 for group size");
                    groupSize = input.nextInt();
                    while (!(groupSize == 3 || groupSize == 5 || groupSize == 7)) {
                        System.out.println("Enter 3, 5, or 7 for group size");
                        groupSize = input.nextInt();
                    }
                    timeStart = System.currentTimeMillis();
                    quickSortMedian(demoArray, 0, demoArray.length - 1, groupSize);
                    timeFinish = System.currentTimeMillis();
                    runTime = timeFinish - timeStart;
                    printArray(demoArray);
                    System.out.println("Your Alg Quicksort with Median "
                            + "of a Median Groupsize " + groupSize + ""
                            + " ran in : " + runTime + " milliseconds!");

                    break;
                case 2:
                    timeStart = System.currentTimeMillis();
                    quickSortRandom(demoArray, 0, demoArray.length - 1);
                    timeFinish = System.currentTimeMillis();
                    runTime = timeFinish - timeStart;
                    printArray(demoArray);
                    System.out.println("Your Alg Quicksort with Random ran in : " + runTime + " milliseconds!");
                    break;
                case 3:
                    timeStart = System.currentTimeMillis();
                    quickSort(demoArray, 0, demoArray.length - 1);
                    timeFinish = System.currentTimeMillis();
                    runTime = timeFinish - timeStart;
                    printArray(demoArray);
                    System.out.println("Your Alg ran Quicksort with Heuristics in : " + runTime + " milliseconds!");
                    break;
                case 4:
                    System.out.println("Enter a number 'i' between 1 and " + (arraySize - 1) + " to find the i'th smallest"
                            + " number of array for Random Select");
                    findI = input.nextInt();
                    while (findI < 1 || findI > arraySize - 1) {
                        System.out.println("Enter a number 'i' between 1 and " + (arraySize - 1) + " to find the i'th smallest"
                                + "number of array for Random Select");
                        findI = input.nextInt();
                    }

                    int k = MedianMedian.randomizedSelect(demoArray, 0, demoArray.length - 1, findI);
//                  System.out.println("FINDI: " + i);
                    timeStart = System.currentTimeMillis();
                    quickSortRandomSelect(demoArray, 0, demoArray.length - 1, k);
                    timeFinish = System.currentTimeMillis();
                    runTime = timeFinish - timeStart;
                    System.out.println("Your Alg ran Quicksort with Random Select 'n/2' ran in : " + runTime + " milliseconds!");

                    break;
                default:
                    break;
            }

            try {
                System.out.println("Would you like to run again on this array?");
                System.out.println("Enter--> 1 for YES -- 2 for NO");
                menu = input.nextInt();
                while (menu < 1 || menu > 2) {
                    System.out.println("Enter --> 1 for YES -- 2 for NO");
                    menu = input.nextInt();
                }
                if (menu == 2) {
                    runMe = false;
                }
            } catch (InputMismatchException e) {
                System.out.println(e + " :That's not a number!");
                break;
            }
        }
    }

    public static void quickSortMedian(int[] a, int low, int high, int groupSize) {

        if (low < high) {
            int pi = partitionMedian(a, low, high, groupSize);
            quickSortMedian(a, low, pi - 1, groupSize);
            quickSortMedian(a, pi + 1, high, groupSize);
        }
    }

    public static int partitionMedian(int arr[], int left, int right, int groupSize) {
        int n = left;
        int i = left, j = right;
        int tmp;
        /**
         * feed the portion of the array passed into paritionMedium into median
         * method to get median of median of current sub-array
         */
        int brr[] = new int[right - left];
        System.out.println("BRR: " + brr.length);
        for (int m = 0; m < brr.length; m++) {
            brr[m] = arr[n];
            n++;
        }
        int pivot = MedianMedian.median(brr, groupSize);

        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
         
        }
        if(i < j){
          tmp = arr[i];
          arr[i] = arr[j];
          arr[j] = tmp;
        }
        return i;
    }

    public static int[] quickSortRandom(int[] a, int low, int high) {
        if (low < high) {
            int pi = partitionRandom(a, low, high);
            quickSortRandom(a, low, pi - 1);
            quickSortRandom(a, pi + 1, high);
        }
        return a;
    }

    public static int partitionRandom(int arr[], int left, int right) {
        int i = left, j = right;
        int tmp;
        Random rand = new Random();
        int pivotIndex = rand.nextInt(right + 1);
        //System.out.println(arr[pivotIndex]);
        int pivot = arr[pivotIndex];

        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        return i;
    }

    public static void quickSort(int arr[], int left, int right) {
        if (arr.length <= 7) {
            MedianMedian.insertionSort(arr);
        } else {
            int index = partition(arr, left, right);
            if (left < index - 1) {
                quickSort(arr, left, index - 1);
            }
            if (index < right) {
                quickSort(arr, index, right);
            }
        }
    }

    public static int partition(int arr[], int left, int right) {
        int i = left, j = right;
        int pivot, pivot1, pivot2, pivot3, length;
        int tmp;
        int middle = arr[(left + right) / 2];
        if (arr.length > 40) {
            length = arr.length / 8;
            pivot1 = median(left, left + length, left + 2 * length);
            pivot2 = median(right, right - length, right - 2 * length);
            pivot3 = median(middle, middle - length, middle + length);
            pivot = median(pivot1, pivot2, pivot3);
        } else {
            pivot = median(left, middle, right);
        }
        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        return i;
    }

    public static void quickSortRandomSelect(int arr[], int left, int right, int findI) {

        int index = partitionRandSel(arr, left, right, findI);
        if (left < index - 1) {
            quickSort(arr, left, index - 1);
        }
        if (index < right) {
            quickSort(arr, index, right);
        }

    }

    public static int partitionRandSel(int arr[], int left, int right, int findI) {
        int i = left, j = right;
        int pivot = findI;
        //MedianMedian.randomizedSelect(arr, left, right, findI);
        int tmp;

        while (i <= j) {
            while (arr[i] < pivot) {
                i++;
            }
            while (arr[j] > pivot) {
                j--;
            }
            if (i <= j) {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        return i;
    }

    public static void printArray(int[] a) {
        int user;
        Scanner input = new Scanner(System.in);
        try {
            System.out.println("Print Array?  1-->YES   2-->NO:");
            user = input.nextInt();
            if (user < 1 || user > 2) {
                System.out.println("Print Array?  1-->YES   2-->NO:");
                user = input.nextInt();

            }

        } catch (InputMismatchException e) {
            System.out.println(e + " :That's not a number! \n I'm not printing!");
            user = 2;
        }
        if (user == 1) {

            for (int i = 0; i < a.length; i++) {
                switch (Integer.toString(a[i]).length()) {
//                 FOR 100,000,000 
//                case 9:
//                    System.out.print(a[i] + " ");
//                    break;
                    case 8:
                        System.out.print(a[i] + "  ");
                        break;
                    case 7:
                        System.out.print(a[i] + "   ");
                        break;
                    case 6:
                        System.out.print(a[i] + "    ");
                        break;
                    case 5:
                        System.out.print(a[i] + "     ");
                        break;
                    case 4:
                        System.out.print(a[i] + "      ");
                        break;
                    case 3:
                        System.out.print(a[i] + "       ");
                        break;
                    case 2:
                        System.out.print(a[i] + "        ");
                        break;
                    case 1:
                        System.out.print(a[i] + "         ");
                        break;

                    default:
                        break;
                }

                if ((i > 0) && ((i + 1) % 10 == 0)) {
                    System.out.println();
                }
            }

        }
        System.out.println();
    }

    public static int median(int x, int y, int z) {
        int[] a = new int[3];
        a[0] = x;
        a[1] = y;
        a[2] = z;
        MedianMedian.insertionSort(a);
        return a[1];
    }
}
