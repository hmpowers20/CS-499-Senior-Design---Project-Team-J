import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Hashtable;


/**
 * Class which contains the main method
 */
public class MainGameView extends JComponent
{
    private static final int ZOOM_MIN = 0;
    private static final int ZOOM_MAX = 3;
    private static final int ZOOM_INIT = ZOOM_MAX;

    GridMap map;
    JSlider zoomSlider;
    TimePanel timer;

    public MainGameView(MainGameModel model) {
        JFrame window = new JFrame("Life Simulator");   // create the window JFrame
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // ensure that the window closes completely when exited

        window.setResizable(false);     // Resizable is set to false so the user is prevented from changing the size of the JFrame.
        window.setLayout(new FlowLayout());
        window.setVisible(true);

        JPanel guiPanel = new JPanel(); //create the panel to contain all of the components
        guiPanel.setLayout(new BorderLayout()); //create border layout to organize components

        //***************************Start Map Display******************************************************************
        JPanel simPanel = new JPanel();

        try {
            map = new GridMap(model);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        simPanel.add(map);

        JScrollPane guiScrollPane = new JScrollPane(simPanel);
        guiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        guiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        guiScrollPane.setPreferredSize(new Dimension(600, 500));

        guiPanel.add(guiScrollPane, BorderLayout.CENTER);
        map.scrollPane = guiScrollPane;
        guiScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> map.PaintTiles(model));
        guiScrollPane.getHorizontalScrollBar().addAdjustmentListener(e -> map.PaintTiles(model));

        //************************End Map GUI Display*******************************************************************

        //**********************Start Button Display********************************************************************
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        Icon save = new ImageIcon("save.png");
        JButton saveButton = new JButton(save);
        saveButton.setVisible(true);
        timer = new TimePanel(model);
        saveButton.addActionListener(e -> SaveSession.Save(model));

        buttonPanel.add(timer);
        buttonPanel.add(saveButton);
        guiPanel.add(buttonPanel, BorderLayout.PAGE_END);
        //**********************************End Button Display**********************************************************

        //*********************************Report Button Display********************************************************
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.PAGE_AXIS));
        reportPanel.setPreferredSize(new Dimension(150, 500));

        JButton reportButton = new JButton("REPORT");
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
        zoomSlider.addChangeListener(e -> map.Zoom((int)Math.pow(2, ZOOM_MAX - zoomSlider.getValue()), model));

        zoomPanel.add(zoomSlider);
        zoomPanel.add(zoomLabel);
        guiPanel.add(zoomPanel, BorderLayout.EAST);
        //**********************************End Zoom Panel Display******************************************************

        add(guiPanel);
        setLayout(new FlowLayout()); // DO NOT REMOVE!!!! NECESSARY

        window.add(this); //add MainGame to the window
        window.pack(); //pack the window
    }

    public void Update(MainGameModel model)
    {
        map.PaintTiles(model);
        timer.Update(model);
    }
}