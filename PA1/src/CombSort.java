public class CombSort {
    public  static void sort(int[] array){
        int gap = array.length;
        double shrink = 1.3;
        boolean sorted=false;
        while(!sorted){
            gap= Math.max(1,(int) Math.floor(gap / shrink));
            sorted =(gap==1);
            for (int i=0; i< array.length-gap; i++){
                if(array[i]>array[i+gap]){
                    int temp = array[i];
                    array[i]=array[i+gap];
                    array[i+gap]=temp;
                    sorted=false;
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
        System.out.printf("Average sorting time for CombSort with input size %d: %.2f ms%n", array.length, averageDuration);
        return  averageDuration;
    }
}
