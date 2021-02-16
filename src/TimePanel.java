import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class TimePanel extends JPanel {

    JToggleButton start;
    SimTimer timer;
    JLabel time;
    JSlider slide;
    boolean started;

    public TimePanel(long startTime) {
        timer = new SimTimer(startTime, this);
        started = false;
        Icon startIcon = new ImageIcon("play.png");
        Icon pauseIcon = new ImageIcon("pause.png");
        Dimension size = start.getPreferredSize();
        start = new JToggleButton();
        start.setIcon(startIcon);
        start.setSelectedIcon(pauseIcon);
        start.setBounds(500, 180, size.width, size.height);
        start.setVisible(true);
        start.addActionListener(new starts(timer));
        slide = new JSlider(0,100, 1);
        slide.setMajorTickSpacing(20);
        slide.setMinorTickSpacing(10);
        slide.setPaintTicks(true);
        slide.setPaintLabels(true);
        slide.addChangeListener(new speed(timer));
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
        Icon pauseIcon = new ImageIcon("pause.png");
        start = new JToggleButton();
        start.setIcon(startIcon);
        start.setSelectedIcon(pauseIcon);
        started = false;
        Dimension size = start.getPreferredSize();
        start.setBounds(500, 180, size.width, size.height);
        start.setVisible(true);

        slide = new JSlider(0,100, 1);
        slide.setMajorTickSpacing(20);
        slide.setMinorTickSpacing(10);
        slide.setPaintTicks(true);
        slide.setPaintLabels(true);
        slide.addChangeListener(new speed(timer));


        start.addActionListener(new starts(timer));
        time = new JLabel("0 00:00:00");
        add(start);
        add(time);
        add(slide);
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
        if (hrs < 10) {
            strHrs = "0"+Integer.toString(hrs);
        }
        else {
            strHrs = Integer.toString(hrs);
        }

        strDays = Integer.toString(days);

        time.setText(strDays+" "+strHrs+":"+strMin+":"+strSec);
    }
    public long getTime() {
        return timer.getTime();
    }

}

class starts implements ActionListener {
    boolean started;
    SimTimer timer;
    public starts(SimTimer timer) {
        this.timer = timer;
        started = false;
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!started) {
            timer.startTimer();
            started = true;
            Icon pause = new ImageIcon("pause.png");
            actionEvent.getSource();
        }
        else {
            timer.pause();
        }
    }
}

class speed implements ChangeListener {
     SimTimer timer;
    public speed(SimTimer timer) {
        this.timer = timer;
    }
    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        JSlider source = (JSlider)changeEvent.getSource();
        if (!source.getValueIsAdjusting()) {
            int fps = (int)source.getValue();
                timer.adjustSpeed(fps);
        }
    }
}
