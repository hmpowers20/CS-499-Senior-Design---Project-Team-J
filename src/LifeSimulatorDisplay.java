import javax.swing.*;
import java.io.IOException;

public class LifeSimulatorDisplay {

    public static void main(String[] args) throws IOException, InterruptedException {
        JFrame window = Window.createWindow(); //create the window
        window.add(new MainGame()); //add MainGame to the window
        window.pack(); //pack the window
    }
}
