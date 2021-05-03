/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class SaveSession {

    public static void Save(MainGameModel model)
    {
        FileWriter saveFile = null;
        try {
            saveFile = new FileWriter("backup.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveTimer(saveFile, model);
    }

    public static void saveTimer(FileWriter saveFile, MainGameModel model) {
        long time = model.numSeconds;
        try {
            saveFile.write(":)\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            saveFile.write(String.valueOf(time)+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            saveFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
