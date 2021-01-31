import javax.swing.JFrame;
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
    public static void main (String[] args) throws IOException {
        JFrame window = trial.createWindow();  // create the window JFrame
        GridMap map = new GridMap(600, 600,20,20);
        window.add(map);
        window.pack();
    }
}

