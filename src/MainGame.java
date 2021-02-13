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
        GridMap map = new GridMap(600, 600,20,20);

        JPanel timerPanel = new JPanel();

        Icon icon1 = new ImageIcon("play.png");
        JButton playButton = new JButton(icon1);
        playButton.setVisible(true);

        Dimension size = playButton.getPreferredSize();
        //playButton.setBounds(500, 180, size.width, size.height);
        playButton.setVisible(true);


        Icon icon2 = new ImageIcon("save.png");
        JButton saveButton = new JButton(icon2);
        saveButton.setVisible(true);

        //SimTimer gameTimer = new SimTimer();
        //gameTimer.startTimer();

        timerPanel.add(saveButton);
        timerPanel.add(playButton);
        //timerPanel.setLayout(null);
        //timerPanel.setLocation(150,10000);
        //timerPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
        //timerPanel.add(gameTimer);

        window.add(map);
        window.add(timerPanel);
        //window.add(playButton);
        //window.add(saveButton);
        window.pack();
    }
}

