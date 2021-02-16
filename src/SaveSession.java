import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class SaveSession {
    TimePanel timer;
    FileWriter saveFile;
    public SaveSession(TimePanel timer) {
        this.timer = timer;
        try {
            saveFile = new FileWriter("backup.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTimer() {
        long time = timer.getTime();
        try {
            saveFile.write(":)\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            saveFile.write(String.valueOf(time));
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
