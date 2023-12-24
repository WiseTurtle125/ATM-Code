package src;

import java.io.*;

public class CSV {
    private final String path;
    private final File file;
    private int line = 0;

    public static class Items {
        String first;
        String last;
        String num;
        String pin;
        double savings;
        double chequing;

        public void parse(String[] data) {
            this.first = data[0];
            this.last = data[1];
            this.num = data[2];
            this.pin = data[3];
            if (data[4].isBlank()) this.savings = -1;
            else                   this.savings = Double.parseDouble(data[4]);
            if (data[5].isBlank()) this.chequing = -1;
            else                   this.chequing = Double.parseDouble(data[5]);
        }

        public String toString() {
            return String.format("%s,%s,%s,%s,%f,%f", first, last, num, pin, savings, chequing);
        }

        public void updateChequing(double amount) {
            this.chequing += amount;
        }

        public void updateSavings(double amount) {
            this.savings += amount;
        }
    }

    public CSV(String path) {
        this.path = path;
        file = new File(path);
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
        } catch (NullPointerException e) {
            return null;
        }

        return data;
    }

    // Overloaded method
    // Read the next line from the file
    public Items readLine() {
        Items data = new Items();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            System.out.println("Line = " + line);

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
        } catch (NullPointerException e) {
            return null;
        }

        return data;
    }

    // Set the line to read from
    public void setLine(int line) {
        this.line = line;
    }

    // Write to the file
    public void writeLine(Items data, int line) {
        String l;
        int track = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            while ((l = br.readLine()) != null) {
                if (track == line) {
                    System.out.println("Writing: " + data.toString());
                    bw.write(data.toString());
                } else {
                    System.out.println("Writing: " + l);
                    bw.write(l);
                }

                track++;
            }

            if (track == line) {
                System.out.println("Writing: " + data.toString());
                bw.write(data.toString());
            }

            br.close();
            bw.close();
        } catch (FileNotFoundException e) {
            System.out.println("The system could not find the specified file. Please ensure the file exists, then try again.");
        } catch (IOException e) {
            System.out.println("There was a problem writing to the file.");
        }
    }

    public void writeLine(String data, int line) {
        String l;
        int track = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            while ((l = br.readLine()) != null) {
                if (track == line) {
                    System.out.println("Writing: " + data);
                    bw.write(data);
                } else {
                    System.out.println("Writing: " + l);
                    bw.write(l);
                }

                track++;
            }

            if (track == line) {
                System.out.println("Writing: " + data);
                bw.write(data);
            }

            br.close();
            bw.close();
        } catch (FileNotFoundException e) {
            System.out.println("The system could not find the specified file. Please ensure the file exists, then try again.");
        } catch (IOException e) {
            System.out.println("There was a problem writing to the file.");
        }
    }
}
