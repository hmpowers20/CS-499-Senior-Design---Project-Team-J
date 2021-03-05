//import javafx.util.StringConverter;
import org.xml.sax.SAXException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;


/**
 * Class which contains the main method
 */
public class MainGame extends JComponent
{
    static final int ZOOM_MIN = 0;
    static final int ZOOM_MAX = 3;
    static final int ZOOM_INIT = ZOOM_MAX;

    GridMap map = null;
    JSlider zoomSlider = null;

    public MainGame() throws IOException {

        JPanel guiPanel = new JPanel(); //create the panel to contain all of the components
        guiPanel.setLayout(new BorderLayout()); //create border layout to organize components

        //***************************Start Map Display******************************************************************
        JPanel simPanel = new JPanel();

        SimInitializer initializer = new SimInitializer();
        try {
            try {
                map = initializer.initialize();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        SimMap posMap = SimMap.getInstance();

        simPanel.add(map);

        JScrollPane guiScrollPane = new JScrollPane(simPanel);
        guiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        guiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        guiScrollPane.setPreferredSize(new Dimension(600, 500));

        guiPanel.add(guiScrollPane, BorderLayout.CENTER);
        map.scrollPane = guiScrollPane;
        guiScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                map.paintTiles();
            }
        });
        guiScrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                map.paintTiles();
            }
        });

        //************************End Map GUI Display*******************************************************************

        //**********************Start Button Display********************************************************************
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        Icon play = new ImageIcon("play.png");
        Icon save = new ImageIcon("save.png");
        JButton saveButton = new JButton(save);
        saveButton.setVisible(true);
        TimePanel timer = new TimePanel(posMap);
        saveButton.addActionListener(new saveAll(timer,posMap));

        buttonPanel.add(timer);
        buttonPanel.add(saveButton);
        guiPanel.add(buttonPanel, BorderLayout.PAGE_END);
        //**********************************End Button Display**********************************************************

        //*********************************Report Button Display********************************************************
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.PAGE_AXIS));
        reportPanel.setPreferredSize(new Dimension(150, 500));

        JButton reportButton = new JButton("REPORT");
        //reportButton.addActionListener(this);
        reportButton.setVisible(true);

        JLabel reportLabel = new JLabel("<html>The button above generates a text file detailing the statistics of the virtual world to aid in creating a stable world.</html>");
        reportLabel.setVisible(true);

        reportPanel.add(reportButton);
        reportPanel.add(reportLabel);
        guiPanel.add(reportPanel, BorderLayout.WEST);
        //**********************************End Report Button Display***************************************************

        //**********************************Zoom Panel Display**********************************************************
        JPanel zoomPanel = new JPanel();
        zoomPanel.setLayout(new BoxLayout(zoomPanel, BoxLayout.PAGE_AXIS));
        zoomPanel.setPreferredSize(new Dimension(150, 500));

        JLabel zoomLabel = new JLabel("ZOOM IN/OUT");
        Font font = new Font("Serif", Font.BOLD, 15);
        zoomLabel.setFont(font);

        zoomSlider = new JSlider(JSlider.VERTICAL, ZOOM_MIN, ZOOM_MAX, ZOOM_INIT);

        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        for (int i = ZOOM_MIN; i <= ZOOM_MAX; i++)
        {
            labels.put(i, new JLabel(
                    (ZOOM_MAX - i != 0 ? "1 / " : "") + Integer.toString((int)Math.pow(2, ZOOM_MAX - i)) + "x"));
        }
        zoomSlider.setLabelTable(labels);
        zoomSlider.setMajorTickSpacing(1);
        zoomSlider.setMinorTickSpacing(0);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                map.zoom((int)Math.pow(2, ZOOM_MAX - zoomSlider.getValue()));
            }
        });

        zoomPanel.add(zoomSlider);
        zoomPanel.add(zoomLabel);
        guiPanel.add(zoomPanel, BorderLayout.EAST);
        //**********************************End Zoom Panel Display******************************************************

        add(guiPanel);
        setLayout(new FlowLayout()); // DO NOT REMOVE!!!! NECESSARY
    }
}

class saveAll implements ActionListener {
    TimePanel timer;
    SaveSession save;
    SimMap posMap;
    public saveAll(TimePanel timer, SimMap posMap) {
        this.timer = timer;
        this.save = new SaveSession(timer);
        this.posMap = posMap;
        posMap.save(save);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        save.saveTimer();
    }
}

