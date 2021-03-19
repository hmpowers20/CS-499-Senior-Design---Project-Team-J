import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.Duration;

public class TimePanel extends JPanel {

    JToggleButton start;
    JLabel time;
    JSlider slide;

    public TimePanel(MainGameModel model) {
        Icon startIcon = new ImageIcon("play.png");
        Icon pauseIcon = new ImageIcon("pause.png");
        start = new JToggleButton();
        start.setIcon(startIcon);
        start.setSelectedIcon(pauseIcon);
        start.setSelected(model.active);
        Dimension size = start.getPreferredSize();
        start.setBounds(500, 180, size.width, size.height);
        start.setVisible(true);

        slide = new JSlider(0,100, 1);
        slide.setMajorTickSpacing(20);
        slide.setMinorTickSpacing(10);
        slide.setPaintTicks(true);
        slide.setPaintLabels(true);
        slide.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                int fps = source.getValue();
                if (fps > 0) {
                    if (!model.active) {
                        model.active = true;
                    }
                    model.speed = fps;
                }
                else {
                    model.active = false;
                }
            }
        });

        start.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                model.active = true;
            }
            else if (e.getStateChange() == ItemEvent.DESELECTED)
            {
                model.active = false;
            }
        });

        time = new JLabel("0 00:00:00");
        add(start);
        add(time);
        add(slide);
}

    public void Update(MainGameModel model) {
        Duration duration = model.GetTimeElapsed();
        String strDays, strHrs, strMin, strSec;
        if (duration.toSecondsPart() < 10) {
            strSec = "0" + duration.toSecondsPart();
        }
        else {
            strSec = Long.toString(duration.toSecondsPart());
        }

        if (duration.toMinutesPart() < 10) {
            strMin = "0" + duration.toMinutesPart();
        }
        else {
            strMin = Integer.toString(duration.toMinutesPart());
        }

        if (duration.toHoursPart() < 10) {
            strHrs = "0" + duration.toHoursPart();
        }
        else {
            strHrs = Integer.toString(duration.toHoursPart());
        }

        strDays = Long.toString(duration.toDaysPart());

        time.setText(strDays+" "+strHrs+":"+strMin+":"+strSec);
    }
}