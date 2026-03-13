import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class Main {
    public static double[][] insertionSort = new double[3][10];
    public static double[][] shakerSort = new double[3][10];
    public static double[][] radixSort = new double[3][10];
    public static double[][] shellSort = new double[3][10];
    public static double[][] combSort = new double[3][10];

    public static void main(String args[]) throws IOException {

        int[] inputSizes = {500,500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};
        List<Integer> columnData = FileIO.readThirdColumn("TrafficFlowDataset.csv", 250000);
        int[] array = columnData.stream().mapToInt(i -> i).toArray();


        System.out.println("Average sorting times for random data:");
        compareSortingTimesForSortingTypes(inputSizes, array, false, false);
        System.out.println("Average sorting times for sorted data:");
        compareSortingTimesForSortingTypes(inputSizes, array,  true, false);
        System.out.println("Average sorting times for reverse sorted data:");
        compareSortingTimesForSortingTypes(inputSizes, array, true, true);
        plotCharts();
    }

    public static void showAndSaveChart(String title, int[] xAxis, double[][] yAxis) throws IOException {
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Milliseconds").xAxisTitle("Input Size").build();

        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setYAxisDecimalPattern("#0.0");

        if( title.equals("Random Data for Fast Sorting") || title.equals("Sorted Data for Fast Sorting") || title.equals("Reverse Sorted Data for Fast Sorting")){
            chart.addSeries("Radix Sort", doubleX, yAxis[2]);
            chart.addSeries("Shell Sort", doubleX, yAxis[1]);
            chart.addSeries("Comb Sort", doubleX, yAxis[0]);
        }
        else if(title.equals("Random Data for Slow Sorting") || title.equals("Sorted Data for Slow Sorting") || title.equals("Reverse Sorted Data for Slow Sorting")){
            chart.addSeries("Insertion Sort", doubleX, yAxis[0]);
            chart.addSeries("Shaker Sort", doubleX, yAxis[1]);
        }
        else{
            chart.addSeries("Random Data", doubleX, yAxis[0]);
            chart.addSeries("Sorted Data", doubleX, yAxis[1]);
            chart.addSeries("Reverse Sorted Data", doubleX, yAxis[2]);
        }

        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        new SwingWrapper(chart).displayChart();
    }

    public static void makeAllSorts(int[] array,int index, int type) {
        insertionSort[type][index]=InsertionSort.calculateMilliseconds(array);
        shakerSort[type][index]=ShakerSort.calculateMilliseconds(array);
        radixSort[type][index]=RadixSort.calculateMilliseconds(array);
        shellSort[type][index]=ShellSort.calculateMilliseconds(array);
        combSort[type][index]=CombSort.calculateMilliseconds(array);
    }
    

    public static void reverse(int[] array) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }

    public static void compareSortingTimesForSortingTypes(int[] inputSizes, int[] array, boolean sortFirst, boolean reverse) {
        for (int i = 0; i < inputSizes.length; i++) {
            int[] arrayCopy = Arrays.copyOfRange(array, 0, inputSizes[i]);
            if(!sortFirst){
                if(i==0){ // This sorting is ignored to warm up the JavaVM.
                    InsertionSort.calculateMilliseconds(arrayCopy);
                    ShakerSort.calculateMilliseconds(arrayCopy);
                    RadixSort.calculateMilliseconds(arrayCopy);
                    ShellSort.calculateMilliseconds(arrayCopy);
                    CombSort.calculateMilliseconds(arrayCopy);
                    continue;
                }
            }
            if(i!=0) {
                if (sortFirst) {
                    CombSort.sort(arrayCopy);
                }
                if (reverse) {
                    reverse(arrayCopy);
                }
                if(!sortFirst){
                    makeAllSorts(arrayCopy,i-1,0);
                }
                else{
                    if(!reverse){
                        makeAllSorts(arrayCopy,i-1,1);
                    }else {
                        makeAllSorts(arrayCopy,i-1,2);
                    }
                }
            }
        }
    }

    public  static void plotCharts() throws IOException {
        int[] inputAxis = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};

        double[][] yAxisRandomFast = new double[3][10];
        double[][] yAxisSortedFast = new double[3][10];
        double[][] yAxisReverseSortedFast = new double[3][10];
        double[][] yAxisRandomSlow = new double[2][10];
        double[][] yAxisSortedSlow = new double[2][10];
        double[][] yAxisReverseSortedSlow = new double[2][10];


        yAxisRandomFast[0] = combSort[0];
        yAxisRandomFast[1] = shellSort[0];
        yAxisRandomFast[2] = radixSort[0];

        yAxisSortedFast[0] = combSort[1];
        yAxisSortedFast[1] = shellSort[1];
        yAxisSortedFast[2] = radixSort[1];

        yAxisReverseSortedFast[0] = combSort[2];
        yAxisReverseSortedFast[1] = shellSort[2];
        yAxisReverseSortedFast[2] = radixSort[2];

        yAxisRandomSlow[0] = insertionSort[0];
        yAxisRandomSlow[1] = shakerSort[0];

        yAxisSortedSlow[0] = insertionSort[1];
        yAxisSortedSlow[1] = shakerSort[1];

        yAxisReverseSortedSlow[0] = insertionSort[2];
        yAxisReverseSortedSlow[1] = shakerSort[2];

        showAndSaveChart("Random Data for Fast Sorting", inputAxis, yAxisRandomFast);
        showAndSaveChart("Sorted Data for Fast Sorting", inputAxis, yAxisSortedFast);
        showAndSaveChart("Reverse Sorted Data for Fast Sorting", inputAxis, yAxisReverseSortedFast);
        showAndSaveChart("Random Data for Slow Sorting", inputAxis, yAxisRandomSlow);
        showAndSaveChart("Sorted Data for Slow Sorting", inputAxis, yAxisSortedSlow);
        showAndSaveChart("Reverse Sorted Data for Slow Sorting", inputAxis, yAxisReverseSortedSlow);

        showAndSaveChart("Radix Sorting", inputAxis, radixSort);
        showAndSaveChart("Shell Sorting", inputAxis, shellSort);
        showAndSaveChart("Comb Sorting", inputAxis, combSort);
        showAndSaveChart("Insertion Sorting", inputAxis, insertionSort);
        showAndSaveChart("Shaker Sorting", inputAxis, shakerSort);
    }


}