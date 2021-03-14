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

    public TimePanel(long startTime, SimMap simMap) {
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
        start.addActionListener(new starts(timer, simMap));
        slide = new JSlider(0,100, 1);
        slide.setMajorTickSpacing(20);
        slide.setMinorTickSpacing(10);
        slide.setPaintTicks(true);
        slide.setPaintLabels(true);
        long sec = startTime % 60;
        long min = startTime / 60;
        long hrs = min / 60;
        long days = hrs / 24;

        String strSec = Long.toString(sec);
        String strMin = Long.toString(min);
        String strHrs = Long.toString(hrs);
        String strDays = Long.toString(days);

        time = new JLabel(strDays+" "+strHrs+":"+strMin+":"+strSec);

        JButton reset = new JButton("Reset");
        JLabel timeLabel = new JLabel("Speed: 1");
        slide.addChangeListener(new speed(timer, simMap, timeLabel));
        reset.addActionListener(new resetTime(slide, timeLabel));

        add(time);
        add(start);
    }


    public TimePanel(SimMap simMap) {
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


        start.addActionListener(new starts(timer, simMap));
        time = new JLabel("0 00:00:00");
        JButton reset = new JButton("Reset");
        JLabel timeLabel = new JLabel("Speed: 1");
        slide.addChangeListener(new speed(timer, simMap, timeLabel));
        reset.addActionListener(new resetTime(slide, timeLabel));
        add(start);
        add(time);
        add(slide);
        add(reset);
        add(timeLabel);
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
    SimMap simMap;
    public starts(SimTimer timer, SimMap simMap) {
        this.timer = timer;
        started = false;
        this.simMap = simMap;
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!started) {
            timer.startTimer();
            simMap.start();
        }
        else {
            timer.pause();
            simMap.pauseResume();
        }
    }
}

class speed implements ChangeListener {
     SimTimer timer;
     SimMap simMap;
     JLabel timeLabel;
    public speed(SimTimer timer, SimMap simMap, JLabel timeLabel) {
        this.timer = timer;
        this.simMap = simMap;
        this.timeLabel = timeLabel;
    }
    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        JSlider source = (JSlider)changeEvent.getSource();
        if (!source.getValueIsAdjusting()) {
            int fps = (int)source.getValue();
                timer.adjustSpeed(fps);
                simMap.setSpeed(fps);
                timeLabel.setText("Speed: "+fps);
        }
    }
}

class resetTime implements ActionListener {
    JSlider slider;
    JLabel timeLabel;
    public resetTime(JSlider slider, JLabel timeLabel) {
        this.slider = slider;
        this.timeLabel = timeLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        slider.setValue(1);
        timeLabel.setText("Speed: 1");
    }
}
