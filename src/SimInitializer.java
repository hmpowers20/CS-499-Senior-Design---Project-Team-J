import java.io.File;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.channels.SelectableChannel;

public class SimInitializer {
    int idNumber;
    File inputFile;
    int width;
    int height;
    SimMap simMap;

    public SimInitializer() {
        idNumber = 0;
    }

    public void initialize() throws ParserConfigurationException, XPathExpressionException {

        //JavatPoint code here we goooo
        inputFile = new File("LifeSimulation01.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = null;
        try {
            doc = db.parse(inputFile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();

        XPath xPath = XPathFactory.newInstance().newXPath();
        Node node;


        node = (Node) xPath.compile("/LAND_BOUNDS[1]").evaluate(doc, XPathConstants.NODESET);
        width = Integer.parseInt(node.getAttributes().getNamedItem("WIDTH").getNodeValue());
        height = Integer.parseInt(node.getAttributes().getNamedItem("HEIGHT").getNodeValue());
        simMap = SimMap.getInstance(width, height);

        node = (Node) xPath.compile("/PLANTS[1]").evaluate(doc, XPathConstants.NODESET);

        float growth_rate = Float.parseFloat(node.getAttributes().getNamedItem("GROWTH_RATE").getNodeValue());
        int max_size = Integer.parseInt(node.getAttributes().getNamedItem("MAX_SIZE").getNodeValue());
        int seed_distance = Integer.parseInt(node.getAttributes().getNamedItem("MAX_SEED_CAST_DISTANCE").getNodeValue());
        int seedcount = Integer.parseInt(node.getAttributes().getNamedItem("MAX_SEED_NUMBER").getNodeValue());
        float viability = Float.parseFloat(node.getAttributes().getNamedItem("SEED_VIABILITY").getNodeValue());

        NodeList plantList = (NodeList) xPath.compile("/PLANTS/PLANT").evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < plantList.getLength(); i++) {
            Node plantNode = plantList.item(i);
            int x = Integer.parseInt(plantNode.getAttributes().getNamedItem("X_POS").getNodeValue());
            int y = Integer.parseInt(plantNode.getAttributes().getNamedItem("Y_POS").getNodeValue());
            int diameter = Integer.parseInt(plantNode.getAttributes().getNamedItem("P_DIAMETER").getNodeValue());
            Plant tempPlant = new Plant(x, y, growth_rate, diameter, seedcount, seed_distance, viability);
            simMap.addPlant(tempPlant);
        }

        node = (Node) xPath.compile("/GRAZERS[1]").evaluate(doc, XPathConstants.NODESET);
        int energy_In = Integer.parseInt(node.getAttributes().getNamedItem("G_ENERGY_INPUT").getNodeValue());
        int energy_Out = Integer.parseInt(node.getAttributes().getNamedItem("G_ENERGY_OUTPUT").getNodeValue());
        int g_reproduce = Integer.parseInt(node.getAttributes().getNamedItem("G_ENERGY_TO_REPRODUCE").getNodeValue());
        float maintain = Float.parseFloat(node.getAttributes().getNamedItem("G_MAINTAIN_SPEED").getNodeValue());
        float g_max = Float.parseFloat(node.getAttributes().getNamedItem("G_MAX_SPEED").getNodeValue());

        NodeList grazerList = (NodeList) xPath.compile("/GRAZERS/GRAZER").evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < grazerList.getLength(); i++) {
            Node grazerNode = grazerList.item(i);
            int x = Integer.parseInt(grazerNode.getAttributes().getNamedItem("X_POS").getNodeValue());
            int y = Integer.parseInt(grazerNode.getAttributes().getNamedItem("X_POS").getNodeValue());
            int init_energy = Integer.parseInt(grazerNode.getAttributes().getNamedItem("G_ENERGY_LEVEL").getNodeValue());
            Grazer tempGrazer = new Grazer(x, y, idNumber, g_max, init_energy, energy_In, energy_Out, g_reproduce, maintain);
            simMap.addGrazer(tempGrazer);
            idNumber++;
        }

        node = (Node) xPath.compile("/PREDATORS[1]").evaluate(doc, XPathConstants.NODESET);
        float speed_hod = Float.parseFloat(node.getAttributes().getNamedItem("MAX_SPEED_HOD").getNodeValue());
        float speed_hed = Float.parseFloat(node.getAttributes().getNamedItem("MAX_SPEED_HED").getNodeValue());
        float speed_hor = Float.parseFloat(node.getAttributes().getNamedItem("MAX_SPEED_HOR").getNodeValue());
        float p_maintain = Float.parseFloat(node.getAttributes().getNamedItem("P_MAINTAIN_SPEED").getNodeValue());
        int p_energy_out = Integer.parseInt(node.getAttributes().getNamedItem("P_ENERGY_OUTPUT").getNodeValue());
        int p_reproduce = Integer.parseInt(node.getAttributes().getNamedItem("P_ENERGY_TO_REPRODUCE").getNodeValue());
        int p_offspring = Integer.parseInt(node.getAttributes().getNamedItem("P_MAX_OFFSPRING").getNodeValue());
        float gestation = Float.parseFloat(node.getAttributes().getNamedItem("P_GESTATION").getNodeValue());
        int e_offspring = Integer.parseInt(node.getAttributes().getNamedItem("P_OFFSPRING_ENERGY").getNodeValue());

        NodeList predatorList = (NodeList) xPath.compile("/PREDATORS/PREDATOR").evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < predatorList.getLength(); i++) {
            Node predatorNode = predatorList.item(i);
            int x = Integer.parseInt(predatorNode.getAttributes().getNamedItem("X_POS").getNodeValue());
            int y = Integer.parseInt(predatorNode.getAttributes().getNamedItem("Y_POS").getNodeValue());
            int p_energy = Integer.parseInt(predatorNode.getAttributes().getNamedItem("P_ENERGY_LEVEL").getNodeValue());
            String genotype = predatorNode.getAttributes().getNamedItem("GENOTYPE").getNodeValue();

            Predator tempPredator = new Predator(x, y, idNumber, speed_hod, speed_hed, speed_hor, p_energy, p_energy_out, gestation, genotype, p_maintain, p_reproduce, p_offspring, e_offspring);
            simMap.addPredator(tempPredator);
            idNumber++;
        }

        NodeList obstacleList = (NodeList) xPath.compile("/OBSTACLES/OBSTACLE").evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < obstacleList.getLength(); i++) {
            Node obstacleNode = obstacleList.item(i);
            int x = Integer.parseInt(obstacleNode.getAttributes().getNamedItem("X_POS").getNodeValue());
            int y = Integer.parseInt(obstacleNode.getAttributes().getNamedItem("Y_POS").getNodeValue());
            int diameter = Integer.parseInt(obstacleNode.getAttributes().getNamedItem("O_DIAMETER").getNodeValue());
            int height = Integer.parseInt(obstacleNode.getAttributes().getNamedItem("O_HEIGHT").getNodeValue());
            Obstacle tempObstacle = new Obstacle(x, y, diameter, height);
            simMap.addObstacle(tempObstacle);
        }
    }
}
