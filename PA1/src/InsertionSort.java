public class InsertionSort {
    public static void sort(int[] array){
        for (int j=1;j<array.length;j++){
            int key = array[j];
            int i = j-1;
            while (i>=0&&array[i]>key){
                array[i+1]=array[i];
                i--;
            }
            array[i+1]=key;
        }
    }
    public static double calculateMilliseconds(int[] array){
        double totalDuration = 0;
        for (int i = 0; i < 10; i++) {
            double startTime = System.nanoTime();
            sort(array.clone());
            double endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000.0;
            totalDuration += duration;
        }
        double averageDuration = totalDuration / 10;
        System.out.printf("Average sorting time for InsertionSort with input size %d: %.2f ms%n", array.length, averageDuration);
        return  averageDuration;
    }
}
