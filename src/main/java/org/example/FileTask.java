package org.example;

import java.io.*;

public class FileTask {
    private final String inputFilePath;
    private final String outputFilePath;
    private String content;

    public FileTask(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    // Synchronized method to read the file content
    public synchronized void readFile() {
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
                writer.write("This is some default text written to resource_file.txt.\n");
                System.out.println("Input file not found. Created and added default content.");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFilePath))) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                contentBuilder.append(new String(buffer, 0, bytesRead));
            }


            content = contentBuilder.toString();
            System.out.println("File reading completed by Thread: " + Thread.currentThread().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Synchronized block to modify content
    public void modifyContent() {
        synchronized (this) {
            if (content != null) {
                content = content.replace("default", "modified")
                        .concat("\nNew content appended to the file.");
                System.out.println("File modification completed by Thread: " + Thread.currentThread().getName());
            }
        }
    }

    // Static synchronized method to write modified content to the output file
    public static synchronized void writeFile(String outputFilePath, String content) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFilePath))) {
            outputStream.write(content.getBytes());
            outputStream.flush();
            System.out.println("File writing completed by Thread: " + Thread.currentThread().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getContent() {
        return content;
    }
}
