public class ShellSort {
    public static void sort(int[] array){
        int n= array.length;
        int gap= n/2;
        while (gap>0){
            for(int i=gap;i<n;i++){
                int temp= array[i];
                int j=i;
                while (j>=gap&&array[j-gap]>temp){
                    array[j]=array[j-gap];
                    j=j-gap;
                }
                array[j]=temp;
            }
        gap=gap/2;
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
        System.out.printf("Average sorting time for ShellSort with input size %d: %.2f ms%n", array.length, averageDuration);
        return  averageDuration;
    }
}
