//Author: Anushka Bhattacharjee

package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class statDisplay {
    private JPanel panelMain;
    private JButton currentStat;
    private JButton simulationData;


    public statDisplay() {
        currentStat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Game Stats: To be edited");


            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Current Stats: ");
        frame.setContentPane(new statDisplay().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
