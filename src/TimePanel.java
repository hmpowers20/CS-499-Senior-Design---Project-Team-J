import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class TimePanel extends JPanel{
    JLabel displayTime;
    SimTimer timer;
    JButton start;
    JLabel time;
    boolean started;

    public TimePanel(long startTime) {
        timer = new SimTimer(startTime, this);
        started = false;
        Icon startIcon = new ImageIcon("play.png");
        start = new JButton(startIcon);

        Dimension size = start.getPreferredSize();
        start.setBounds(500, 180, size.width, size.height);
        start.setVisible(true);
        start.addActionListener(new starts(timer));
        long sec = startTime % 60;
        long min = startTime / 60;
        long hrs = min / 60;
        long days = hrs / 24;

        String strSec = Long.toString(sec);
        String strMin = Long.toString(min);
        String strHrs = Long.toString(hrs);
        String strDays = Long.toString(days);

        time = new JLabel(strDays+" "+strHrs+":"+strMin+":"+strSec);

        add(time);
        add(start);
    }

    public TimePanel() {
        timer = new SimTimer(this);
        Icon startIcon = new ImageIcon("play.png");
        start = new JButton(startIcon);
        started = false;

        Dimension size = start.getPreferredSize();
        start.setBounds(500, 180, size.width, size.height);
        start.setVisible(true);

        start.addActionListener(new starts(timer));
        time = new JLabel("0 00:00:00");
        add(start);
        add(time);
}

    public void update(int sec, int min, int hrs, int days) {
        String strDays, strHrs, strMin, strSec;
        if (sec < 10) {
            strSec = "0"+Integer.toString(sec);
        }
        else {
            strSec = Integer.toString(sec);
        }

        if (min < 10) {
            strMin = "0"+Integer.toString(min);
        }
        else {
            strMin = Integer.toString(min);
        }
        if (hrs > 10) {
            strHrs = "0"+Integer.toString(hrs);
        }
        else {
            strHrs = Integer.toString(hrs);
        }

        strDays = Integer.toString(days);

        time.setText(strDays+" "+strHrs+":"+strMin+":"+strSec);
    }

}

class starts implements ActionListener {
    boolean started = false;
    SimTimer timer;
    public starts(SimTimer timer) {
        this.timer = timer;
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!started) {
            timer.startTimer();
            started = true;
        }
        else {
            timer.pause();
        }
    }
}
