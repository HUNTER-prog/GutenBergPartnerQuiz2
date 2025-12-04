import java.io.*;
import java.nio.file.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        expensiveMatrixTask("Odyssey_expensive.txt", 1200);
        expensiveMatrixTask("KingInYellow_expensive.txt", 1200);

        long end = System.currentTimeMillis();
        System.out.println("Total time: " + (end - start) + " ms");
    }

    public static void expensiveMatrixTask(String outputFileName, int size) throws IOException {
        System.out.println("Starting matrix computation for " + outputFileName);
        Random rand = new Random();
        double[][] A = new double[size][size];
        double[][] B = new double[size][size];
        double[][] C = new double[size][size];

        // Fill matrices with random numbers
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                A[i][j] = rand.nextDouble();
                B[i][j] = rand.nextDouble();
            }
        }

        // Multiply matrices (O(n^3)) - CPU heavy
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double sum = 0;
                for (int k = 0; k < size; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }

        // Compute sum of all elements as a "result"
        double total = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                total += C[i][j];
            }
        }

        // Write result to file
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFileName))) {
            writer.write("Matrix sum: " + total);
        }

        System.out.println(outputFileName + " done. Matrix sum: " + total);
    }
}
