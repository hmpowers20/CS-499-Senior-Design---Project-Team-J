/*****************************************************
CS 499-01 Senior Design
Project Team J
Anushka Bhattacharjee, Haley Powers, Wren Robertson
Spring 2021
Final Deliverable: May 4, 2021
 ****************************************************/

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;


/******************************************
 Class which creates the GUI components.
 ******************************************/
public class MainGameView extends JComponent
{
    private static final int ZOOM_MIN = 0;
    private static final int ZOOM_MAX = 3;
    private static final int ZOOM_INIT = ZOOM_MAX;
    private JLabel life_stats;

    private String text = "View Instructions";

    GridMap map;
    JSlider zoomSlider;
    TimePanel timer;

    /********************************************************************************
    MainGameView defines the buttons, displays, sliders, and the simulation display.
     ********************************************************************************/
    public MainGameView(MainGameModel model) {
        JFrame window = new JFrame("A Day in the Life: Dinosaurs");   // create the window JFrame
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // ensure that the window closes completely when exited

        window.setResizable(false); // Resizable is set to false so the user is prevented from changing the size of the JFrame.
        window.setLayout(new FlowLayout());
        window.setVisible(true);

        JPanel guiPanel = new JPanel(); //Create the panel to contain all of the gui components
        guiPanel.setLayout(new BorderLayout()); //Create border layout to organize components

        /*-------------------------------------------------------------------------------------
        Start Grid Map Display
        ---------------------------------------------------------------------------------------*/
        JPanel simPanel = new JPanel();

        //Create the GridMap if the file is found
        map = new GridMap(model);
        simPanel.add(map);

        JScrollPane guiScrollPane = new JScrollPane(simPanel); //Create scroll bars on the grid map
        guiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        guiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        guiScrollPane.setPreferredSize(new Dimension(600, 500));

        guiPanel.add(guiScrollPane, BorderLayout.CENTER);
        map.scrollPane = guiScrollPane;
        guiScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> map.PaintTiles(model));
        guiScrollPane.getHorizontalScrollBar().addAdjustmentListener(e -> map.PaintTiles(model));

        /*------------------------------------------------------------------------------------------
        End Grid Map Display
        --------------------------------------------------------------------------------------------*/
        /*-------------------------------------------------------------
        Start Button Display
        ---------------------------------------------------------------*/
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton saveButton = new JButton("Save");
        saveButton.setVisible(true);
        timer = new TimePanel(model);
        saveButton.addActionListener(e -> SaveSession.Save(model));

        buttonPanel.add(timer);
        buttonPanel.add(saveButton);
        guiPanel.add(buttonPanel, BorderLayout.PAGE_END);
        /*--------------------------------------------------------------
        End Button Display
        ----------------------------------------------------------------*/

        /*-----------------------------------------------------------------------------------------------
        Start Report Button Display
        ------------------------------------------------------------------------------------------------*/
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.PAGE_AXIS));
        reportPanel.setPreferredSize(new Dimension(200, 575));

        //Functionality to open different XML files
        JButton openButton = new JButton("Open Another XML File");
        openButton.setVisible(true);
        openButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int result = chooser.showOpenDialog(getParent());
            if (result == JFileChooser.APPROVE_OPTION) {
                File input = chooser.getSelectedFile();
                model.openFile(input, true);

                Update(model);
            }
        });

        JButton reportButton = new JButton("REPORT");
        reportButton.setVisible(true);
        reportButton.addActionListener(e -> {
            try {
                model.report();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        JLabel reportLabel = new JLabel("<html>The button above generates a text file detailing the statistics of the virtual world to aid in creating a stable world.<br><br><br><br>If you want to know more about the simulation or need help, click the link below to access the simulation instructions!</html>");
        reportLabel.setVisible(true);

        JLabel instructions = new JLabel(text);

        //Functionality for the hyperlink to the instructions
        instructions.setForeground(Color.BLUE.darker());
        instructions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        instructions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                try{
                    Desktop.getDesktop().browse(new URI("https://adayinthelife-dinosaurs.github.io/"));//Add website url here
                } catch (IOException | URISyntaxException event1){
                    event1.printStackTrace();
                }
            }
            @Override
            public void mouseExited(MouseEvent event){
                instructions.setText(text);
            }
            @Override
            public void mouseEntered(MouseEvent event){
                instructions.setText("<html><a href=''>" + text + "</a></html>");
            }
        });

        //Life forms and world statistics labels
        JLabel worldStatsTitle = new JLabel("<html>Current Statistics: <br><br></html>");

        JLabel lifeStats = new JLabel("<html>Number of Life Forms: <br><br></html>");
        life_stats = new JLabel("<html>Plants: 0<br>Grazers: 0<br>Predators: 0</html>");

        JLabel dimensionsTitle = new JLabel("<html>Current Dimensions: <br><br></html>");
        JLabel dimensions = new JLabel("Width: "+model.getMapWidth()+"    Height: "+model.getMapHeight());

        JLabel space1 = new JLabel("<html><br><br></html>");
        JLabel space2 = new JLabel("<html><br><br></html>");
        JLabel space3 = new JLabel("<html><br><br></html>");
        JLabel space4 = new JLabel("<html><br><br></html>");
        JLabel space5 = new JLabel("<html><br><br></html>");

        reportPanel.add(openButton);
        reportPanel.add(space1);
        reportPanel.add(reportButton);
        reportPanel.add(space2);
        reportPanel.add(reportLabel);
        reportPanel.add(space3);
        reportPanel.add(instructions);
        reportPanel.add(space4);
        reportPanel.add(worldStatsTitle);
        reportPanel.add(lifeStats);
        reportPanel.add(life_stats);
        reportPanel.add(space5);
        reportPanel.add(dimensionsTitle);
        reportPanel.add(dimensions);
        guiPanel.add(reportPanel, BorderLayout.WEST);
        /*---------------------------------------------------------------------------------------------
        End Report Button Display
        -----------------------------------------------------------------------------------------------*/

        /*---------------------------------------------------------------------------------------------
        Start Zoom Slider Display
        ----------------------------------------------------------------------------------------------*/
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
        /*-------------------------------------------------------------------------------------------------
        End Zoom Slider Display
        --------------------------------------------------------------------------------------------------*/

        add(guiPanel);
        setLayout(new FlowLayout()); //Do not remove, necessary for GUI

        window.add(this); //Add MainGame to the window
        window.pack(); //Pack the window
    }

    /****************************************************************************
    The Update function updates the gui components based on data from the model.
     ****************************************************************************/
    public void Update(MainGameModel model)
    {
        map.PaintTiles(model);
        timer.Update(model);
        int pl_count = model.getNumPlants();
        int gr_count = model.getNumGrazers();
        int pr_count = model.getNumPredators();
        life_stats.setText("<html>Plants: "+pl_count+"<br>"+"Grazers: "+gr_count+"<br>"+"Predators: "+pr_count+"</html>");
    }
}