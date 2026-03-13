public class ShakerSort {
    public static void sort(int[] array){
        boolean swapped = true;
        while (swapped){
            swapped = false;
            for(int i=0;i<array.length-1;i++){
                if (array[i]>array[i+1]){
                    int temp= array[i];
                    array[i]=array[i+1];
                    array[i+1]=temp;
                    swapped=true;
                }
            }
            if(!swapped){
                break;
            }
            swapped = false;
            for (int i=array.length-2;i>=0;i--){
                if (array[i]>array[i+1]){
                    int temp= array[i];
                    array[i]=array[i+1];
                    array[i+1]=temp;
                    swapped=true;
                }
            }
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
        System.out.printf("Average sorting time for ShakerSort with input size %d: %.2f ms%n", array.length, averageDuration);
        return  averageDuration;
    }
}
