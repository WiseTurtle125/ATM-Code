/*
 * TITLE: CSV Parser
 * NAME: James Tung
 * DATE: 12/16/2023
 * DESCRIPTION: This class is used for parsing CSV files.
 */

import java.io.*;

public class CSV {
    private final String filePath;
    private final File file;

    public CSV(String path) {
        filePath = path;
        file = new File(path);
    }

    public String getPath() {
        return filePath;
    }


    public String[] readLine(int line) {
        String[] data = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            for (int i = 0; i < line; i++) {
                br.readLine();
            }

            data = br.readLine().split(",");
        } catch (FileNotFoundException e) {
            System.out.println("The system could not find the specified file. Please ensure the file exists, then try again.");
            return null;
        } catch (IOException e) {
            System.out.println("There was a problem reading the file.");
        }

        return data;
    }
}
