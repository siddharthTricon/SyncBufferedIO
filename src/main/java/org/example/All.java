package org.example;

public class All {
    public static void main(String[] args) {
        String inputFilePath = "src/main/resources/resource_file.txt";
        String outputFilePath = "src/main/resources/resource_text.txt";

        FileTask fileTask = new FileTask(inputFilePath, outputFilePath);

        // Create threads for reading, modifying, and writing
//        Thread readThread = new Thread(() -> fileTask.readFile(), "Read-Thread");
        Thread readThread = new Thread(fileTask::readFile, "Read-Thread");
        Thread modifyThread = new Thread(() -> fileTask.modifyContent(), "Modify-Thread");
        Thread writeThread = new Thread(() -> FileTask.writeFile(outputFilePath, fileTask.getContent()), "Write-Thread");

        // Start threads in sequence
        try {
            readThread.start();
            readThread.join(); // Ensure read completes before modify

            modifyThread.start();
            modifyThread.join(); // Ensure modify completes before write

            writeThread.start();
            writeThread.join(); // Ensure write completes before ending

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All tasks completed.");
    }
}