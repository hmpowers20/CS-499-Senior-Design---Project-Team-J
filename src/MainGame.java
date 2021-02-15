import javax.swing.*;
import java.awt.Dimension;
import java.io.IOException;

/**
 * Class which contains the main method
 */
public class MainGame
{
    /**
     * @param args Command line arguments
     * @throws IOException Thrown if there is a problem with input.
     */
    public static void main (String[] args) throws IOException, InterruptedException {
        JFrame window = trial.createWindow();  // create the window JFrame
        GridMap map = new GridMap(30, 30);

        Icon icon1 = new ImageIcon("play.png");
        Icon icon2 = new ImageIcon("save.png");
        JButton saveButton = new JButton(icon2);
        saveButton.setVisible(true);
        TimePanel timer = new TimePanel();
      
        window.add(map);
        window.add(timer);
        window.add(saveButton);
        window.pack();
    }
}

