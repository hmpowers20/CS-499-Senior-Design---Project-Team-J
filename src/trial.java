import javax.swing.*;
import java.awt.*;

public class trial {
    /**
     * Creates a window with desired attributes.
     * @return JFrame representing our window.
     */
    public static JFrame createWindow() {
        JFrame window = new JFrame("Life Simulator");   // create the window JFrame
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // ensure that the window closes completely when exited

        window.setResizable(false);     // Resizable is set to false so the user is prevented from changing the size of the JFrame.
        window.setLayout(new FlowLayout());
        window.setVisible(true);
        return window;
    }
}
