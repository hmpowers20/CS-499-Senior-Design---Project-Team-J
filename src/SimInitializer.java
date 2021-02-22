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
            Plant tempPlant = new Plant(x,y,growth_rate,diameter,seedcount,seed_distance,viability);
            simMap.addPlant(tempPlant);
        }


    }
}
