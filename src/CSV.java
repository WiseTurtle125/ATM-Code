package src;

import java.io.*;
import java.util.Objects;

public class CSV {
    private final File file;
    private int line = 0;

    public static class Items {
        String first;
        String last;
        String num;
        String pin;
        double chequing;
        double savings;

        public void parse(String[] data) {
            this.first = data[0];
            this.last = data[1];
            this.num = data[2];
            this.pin = data[3];
            if (data[4].isBlank()) this.chequing = -1;
            else                   this.chequing = Double.parseDouble(data[4]);
            if (data[5].isBlank()) this.savings = -1;
            else                   this.savings = Double.parseDouble(data[5]);
        }

        public String toString() {
            return String.format("%s,%s,%s,%s,%f,%f", first, last, num, pin, chequing, savings);
        }
    }

    public CSV(String path) {
        this.file = new File(path);
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

            for (int i = 0; i < this.line; i++) {
                br.readLine();
            }

            data.parse(br.readLine().split(","));
            this.line++;
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
    public int writeLine(Items data, int entry) {
        String[] lines = new String[1024];
        String l;
        int track = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw;

            while ((l = br.readLine()) != null) {
                if (track != entry) {
                    lines[track] = l;
                } else {
                    lines[track] = data.toString();
                }

                track++;
            }

            if (entry == -1) {
                lines[track] = data.toString();
            }

            bw = new BufferedWriter(new FileWriter(file));
            for (String a : lines) {
                if (Objects.nonNull(a)) {
                    System.out.println(a);
                    bw.write(a);
                    bw.newLine();
                }
            }

            br.close();
            bw.close();
        } catch (FileNotFoundException e) {
            System.out.println("The system could not find the specified file. Please ensure the file exists, then try again.");
        } catch (IOException e) {
            System.out.println("There was a problem writing to the file.");
        }

        return track;
    }

    public int writeLine(String data, int entry) {
        String[] lines = new String[1024];
        String l;
        int track = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw;

            while ((l = br.readLine()) != null) {
                if (track != entry) {
                    lines[track] = l;
                } else {
                    lines[track] = data;
                }

                track++;
            }

            if (entry == -1) {
                lines[track] = data;
            }

            bw = new BufferedWriter(new FileWriter(file));
            for (String a : lines) {
                if (Objects.nonNull(a)) {
                    bw.write(a);
                    bw.newLine();
                }
            }

            br.close();
            bw.close();
        } catch (FileNotFoundException e) {
            System.out.println("The system could not find the specified file. Please ensure the file exists, then try again.");
        } catch (IOException e) {
            System.out.println("There was a problem writing to the file.");
        }

        return track;
    }

    public boolean exists(String acc) {
        Items item;
        String line;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {
                item = new Items();
                item.parse(line.split(","));

                if (acc.equals(item.num)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("The system could not find the specified file. Please ensure the file exists, then try again.");
        } catch (IOException e) {
            System.out.println("There was a problem reading the file.");
        }

        return false;
    }
}
