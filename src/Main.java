import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
    private static final long MEGABYTE = 1024L * 1024L;

    public static void main(String[] args) throws FileNotFoundException {
        BDD bdd = new BDD();
        String bFunc = "ABC+!ABC";
        String order = "ABCDEFGHIJKLMNXYZT";
        bdd.BDD_create(bFunc, order);
        System.out.println(bdd.count(bdd.root));
        BDDPrint binaryTreePrinter = new BDDPrint(bdd.root);
        binaryTreePrinter.print(new PrintStream("./data.txt"));
//
//        int numVariables = 13;
//        int maxVariables = 20;
//        int numFunctions = 100;
//        String order = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//
//        Runtime runtime = Runtime.getRuntime();
//        System.out.println("CREATE");
//        runtime.gc();
//        long startTime = System.currentTimeMillis();
//        while (numVariables <= maxVariables) {
//            for (int i = 0; i < numFunctions; i++) {
//                String bFuncion = BDD.generateBooleanFunction(numVariables);
//                bdd.BDD_create(bFuncion, order);
//            }
//            numVariables++;
//        }
//        long endTime = System.currentTimeMillis();
//        long duration = (endTime - startTime);
//        long memory = runtime.totalMemory() - runtime.freeMemory();
//        System.out.println("Time " + duration + " ms");
//        System.out.println("Used memory is bytes: " + memory);
//        System.out.println("Used memory is megabytes: "
//                + bytesToMegabytes(memory));
//



    }


    private static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }
}
