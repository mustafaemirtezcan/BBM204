import java.util.Arrays;

public class RadixSort {
    public static int[] sort(int[] array,int d) {
        for(int pos=1; pos<=d; pos++){
            array= countingSort(array, pos);
        }
        return array;
    }

    public static int[] countingSort(int[] array, int pos) {
        int[] count = new int[10];
        int[] output = new int[array.length];
        int size = array.length;

        for (int i = 0; i < size; i++) {
            int digit = getDigit(array[i], pos);
            count[digit]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = size - 1; i >= 0; i--) {
            int digit = getDigit(array[i], pos);
            count[digit]--;
            output[count[digit]] = array[i];
        }

        return output;
    }
    public static int getDigit(int number, int pos) {
        return (number / (int) Math.pow(10, pos - 1)) % 10;
    }

    public static int getMaxDigitCount(int[] array) {
        int maxNumber = Arrays.stream(array).max().orElse(0);
        return Integer.toString(maxNumber).length();
    }

    public static double calculateMilliseconds(int[] array){
        double totalDuration = 0;
        for (int i = 0; i < 10; i++) {
            double startTime = System.nanoTime();
            sort(array.clone(), getMaxDigitCount(array.clone()));
            double endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000.0;
            totalDuration += duration;
        }
        double averageDuration = totalDuration / 10;
        System.out.printf("Average sorting time for RadixSort with input size %d: %.2f ms%n", array.length, averageDuration);
        return  averageDuration;
    }

}
