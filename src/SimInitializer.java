import java.io.File;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class SimInitializer {
    int idNumber;
    File inputFile;
    public SimInitializer() {
        idNumber = 0;
    }

    public void initialize() throws ParserConfigurationException {

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

        NodeList plantList = doc.getElementsByTagName("Plant");
        NodeList predatorList = doc.getElementsByTagName("Predator");
        NodeList obstacleList = doc.getElementsByTagName("Obstacle");
        NodeList grazerList = doc.getElementsByTagName("Grazer");

        for (int i = 0; i < plantList.getLength(); i++) {
            Node plant = plantList.item(i);
        }
    }
}
