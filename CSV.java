/*
 * TITLE: CSV Parser
 * NAME: James Tung
 * DATE: 12/16/2023
 * DESCRIPTION: This class is used for parsing CSV files.
 */

import java.io.*;

public class CSV {
    private final String path;
    private final File file;
    private int line = 0;

    public static class Items {
        String first;
        String last;
        int num;
        int pin;
        double savings;
        double chequing;

        public void parse(String[] data) {
            first = data[0];
            last = data[1];
            num = Integer.parseInt(data[2]);
            pin = Integer.parseInt(data[3]);
            savings = Double.parseDouble(data[4]);
            chequing = Double.parseDouble(data[5]);
        }

        public String getFirst() {
            return first;
        }

        public String getLast() {
            return last;
        }

        public String getName() {
            return first + " " + last;
        }

        public int getNum() {
            return num;
        }

        public int getPin() {
            return pin;
        }

        public double getSavings() {
            return savings;
        }

        public double getChequing() {
            return chequing;
        }

        public String toString() {
            return String.format("%s,%s,%d,%d,%f,%f", first, last, num, pin, savings, chequing);
        }
    }

    public CSV(String path) {
        this.path = path;
        file = new File(path);
    }

    // Get file location
    public String getPath() {
        return path;
    }

    // Get line number
    public int getLine() { return line - 1; }


    // Read a specific line from the file
    public Items readLine(int line) {
        Items data = new Items();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            for (int i = 0; i < line; i++) {
                br.readLine();
            }

            data.parse(br.readLine().split(","));
        } catch (FileNotFoundException e) {
            System.out.println("The system could not find the specified file. Please ensure the file exists, then try again.");
            return null;
        } catch (IOException e) {
            System.out.println("There was a problem reading the file.");
        }

        return data;
    }

    // Overloaded method
    // Read the next line from the file
    public Items readLine() {
        Items data = new Items();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            for (int i = 0; i < line; i++) {
                br.readLine();
            }

            data.parse(br.readLine().split(","));
            line++;
        } catch (FileNotFoundException e) {
            System.out.println("The system could not find the specified file. Please ensure the file exists, then try again.");
            return null;
        } catch (IOException e) {
            System.out.println("There was a problem reading the file.");
        }

        return data;
    }

    // Set the line to read from
    public void setLine(int line) {
        this.line = line;
    }

    // Write to the file
    public void writeLine(Items data) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            for (int i = 0; i < line; i++) {
                bw.write(data.toString());
                bw.newLine();
            }
            bw.write(data.toString());
            bw.close();
        } catch (FileNotFoundException e) {
            System.out.println("The system could not find the specified file. Please ensure the file exists, then try again.");
        } catch (IOException e) {
            System.out.println("There was a problem writing to the file.");
        }
    }
}
