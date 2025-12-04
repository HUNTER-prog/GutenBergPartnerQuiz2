import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Path odysseyPath = Paths.get("C:\\Users\\30001160\\Desktop\\KrackenDemo\\GutenBergPartnerQuiz2\\Odyssey.txt");
        Path kingPath = Paths.get("C:\\Users\\30001160\\Desktop\\KrackenDemo\\GutenBergPartnerQuiz2\\KingInYellow.txt");

        long start = System.currentTimeMillis();

        // Create an ExecutorService with 2 threads
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Submit both tasks as Callables
        Callable<Void> task1 = () -> {
            expensiveMatrixFromFile(odysseyPath, "Odyssey_processed.txt", 800, 25);
            return null;
        };
        Callable<Void> task2 = () -> {
            expensiveMatrixFromFile(kingPath, "KingInYellow_processed.txt", 800, 25);
            return null;
        };

        // Invoke both tasks in parallel and wait for completion
        Future<Void> future1 = executor.submit(task1);
        Future<Void> future2 = executor.submit(task2);

        future1.get(); // wait for task1
        future2.get(); // wait for task2

        executor.shutdown();

        long end = System.currentTimeMillis();
        System.out.println("Total time: " + (end - start) + " ms");
    }

    public static void expensiveMatrixFromFile(Path inputFile, String outputFileName, int matrixSize, int repetitions) throws Exception {
        byte[] fileBytes = Files.readAllBytes(inputFile);
        double[][] matrix = new double[matrixSize][matrixSize];

        // Seed the matrix deterministically from file content
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int index = (i * matrixSize + j) % fileBytes.length;
                matrix[i][j] = ((int) fileBytes[index] & 0xFF) / 255.0; // normalized byte
            }
        }

        double[][] result = matrixClone(matrix);

        // Perform repeated matrix multiplications to make it very expensive
        for (int r = 0; r < repetitions; r++) {
            result = multiplyMatrices(result, matrix);
        }

        // Compute a checksum of the resulting matrix
        double sum = 0;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                sum += result[i][j];
            }
        }

        // Write result to output file
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFileName))) {
            writer.write("Matrix sum: " + sum);
        }

        System.out.println(inputFile.getFileName() + " processed. Matrix sum: " + sum);
    }

    private static double[][] multiplyMatrices(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }
        return C;
    }

    private static double[][] matrixClone(double[][] mat) {
        int n = mat.length;
        double[][] clone = new double[n][n];
        for (int i = 0; i < n; i++) {
            clone[i] = Arrays.copyOf(mat[i], n);
        }
        return clone;
    }
}
