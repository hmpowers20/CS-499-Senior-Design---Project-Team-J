import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

class MainGameModel {
    public Tile[][] map;
    public int speed = 1;
    public boolean active = false;
    public int numSeconds;

    MainGameModel() {
        initialize();
    }

    public Duration GetTimeElapsed()
    {
        return Duration.ofSeconds(numSeconds);
    }

    public Point FindTileWithActor(Actor actor)
    {
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (map[i][j].occupier == actor)
                {
                    return new Point(i, j);
                }
            }
        }

        return null;
    }

    public void addActor(Actor actor, int x, int y) {
        map[x][y].occupier = actor;
    }

    public void removeActor(Actor actor) {
        Point coordinates = FindTileWithActor(actor);
        map[coordinates.x][coordinates.y].occupier = null;
    }

    //Plug in two points and see if there is an obstacle between them
    public boolean obstacleBetween(int x1, int y1, int x2, int y2) {
        if (x1 == x2) {
           if (y1 >= y2) {
               for (int i = y2; i < y1; i++) {
                   if (map[x1][i].occupier instanceof Obstacle) {
                       return true;
                   }
               }
           }
           else {
               for (int i = y1; i < y2; i++) {
                   if (map[x1][i].occupier instanceof Obstacle) {
                       return true;
                   }
               }
           }
        }
        else {
            if (x1 >= x2) {
                for (int i = x2; i < x1; i++) {
                    if (map[i][y1].occupier instanceof Obstacle) {
                        return true;
                    }
                }
            }
            else {
                for (int i = x1; i < x2; i++) {
                    if (map[i][y1].occupier instanceof Obstacle) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Calculate distance between two points, we can rewrite this if we upgrade from rooks to queens
    public int findDistance(Point first, Point second) {
        int x1 = first.x;
        int x2 = second.x;
        int y1 = first.y;
        int y2 = second.y;

        int x = x1 - x2;
        int y = y1 - y2;
        int dist = Math.abs(x) + Math.abs(y);
        return dist;
    }

    //Return location of nearest specified type of actor
    public Point findNearestActor(char finding, Actor actor) {
        Point location = FindTileWithActor(actor);
        int x = location.x;
        int y = location.y;
        Actor nearest = null;
        int min_distance = 150;
        int startx, starty, s_height, s_width;
        if(x >= 150) {
            startx = x - 150;
        }
        else {
            startx = 0;
        }

        if (x + 125 < map.length) {
            s_width = x + 125;
        }
        else {
            s_width = map.length - 1;
        }
        if (y > 150) {
            starty = y - 150;
        }
        else {
            starty = 0;
        }
        if (y + 150 > map[0].length) {
            s_height = map[0].length - 1;
        }
        else {
            s_height = y + 150;
        }
        //Right now that's out of the way and we have the range we're searching
        Point source = new Point();
        source.x = x - startx;
        source.y = y - starty;
        for (int i = startx; i < s_width; i++) {
            for (int j = starty; j < s_height; j++) {
                if ((finding == 'p' && map[i][j].occupier instanceof Plant) || (finding == 'g' && map[i][j].occupier instanceof Grazer) || (finding == 'P' && map[i][j].occupier instanceof Predator)) {
                    Point target = new Point();
                    target.x = i - startx;
                    target.y = j - starty;

                    if (finding == 'P') {
                        Predator pred = (Predator) actor;
                        Predator meal = (Predator) map[i][j].occupier;
                        meal.inDanger(pred);
                    }
                    else if (finding == 'g') {
                        Grazer grazer = (Grazer) map[i][j].occupier;
                        grazer.inDanger(x,y);
                    }

                    int distance = findDistance(source, target);
                    //Make sure target is actually visible to actor; accounting for Predators' sense of smell
                    if (distance < min_distance && !obstacleBetween(x,y,i,j) || (actor instanceof Predator && distance >= 25)) {
                        distance = min_distance;
                        nearest = map[i][j].occupier;
                    }
                }
            }
        }
        if (nearest != null) {
            Point closest = FindTileWithActor(nearest);
            return closest;
        }
        else {
            return null;
        }

    }


    public void save(SaveSession save) {

    }

    public void start() {
        active = true;
    }

    public int getNumGrazers() {
        int numGrazers = 0;

        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (map[i][j].occupier instanceof Grazer)
                {
                    numGrazers++;
                }
            }
        }

        return numGrazers;
    }

    public int getNumPredators() {
        int numPredators = 0;

        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (map[i][j].occupier instanceof Predator)
                {
                    numPredators++;
                }
            }
        }

        return numPredators;
    }

    public int getNumPlants() {
        int numPlants = 0;

        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[i].length; j++)
            {
                if (map[i][j].occupier instanceof Plant)
                {
                    numPlants++;
                }
            }
        }

        return numPlants;
    }

    public void initialize() {

        //JavatPoint code here we goooo
        File inputFile = new File("LifeSimulation01.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

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

        String expression = "/LIFE_SIMULATION/LAND_BOUNDS";
        NodeList nodeList = null;
        try {
            nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        node = nodeList.item(0);
        NodeList children = node.getChildNodes();

        int width = 0, height = 0;

        for (int i = 0; i < children.getLength(); i++) {
            Node tempNode = children.item(i);
            if (tempNode.getNodeName() == "WIDTH") {
                String value = tempNode.getTextContent();
                float conversion = Float.parseFloat(value);
                width = (int) conversion;
            }
            else if (tempNode.getNodeName() == "HEIGHT") {
                String value = tempNode.getTextContent();
                float conversion = Float.parseFloat(value);
                height = (int) conversion;
            }
        }

        map = new Tile[width][height];
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[0].length; j++)
            {
                map[i][j] = new Tile();
            }
        }

        try {
            nodeList = (NodeList) xPath.compile("/LIFE_SIMULATION/PLANTS").evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        node = nodeList.item(0);

        children = node.getChildNodes();
        float growth_rate = 0, viability = 0;
        int max_size, seed_distance = 0, seedcount = 0, x = 0, y = 0, diameter = 0;

        for (int i = 0; i < children.getLength(); i++) {
            Node tempNode = children.item(i);
            String name = tempNode.getNodeName();
            if (name == "GROWTH_RATE") {
                growth_rate = Float.parseFloat(tempNode.getTextContent());
            }
            else if(name == "MAX_SIZE") {
                //max_size = Integer.parseInt(tempNode.getTextContent());
            }
            else if (name == "MAX_SEED_CAST_DISTANCE") {
                String value = tempNode.getTextContent();
                seed_distance = Integer.parseInt(value.trim());
            }
            else if (name == "MAX_SEED_NUMBER") {
                String value = tempNode.getTextContent();
                seedcount = Integer.parseInt(value.trim());
            }
            else if (name == "SEED_VIABILITY") {
                String value = tempNode.getTextContent();
                viability = Float.parseFloat(value.trim());
            }
            else if (name == "PLANT") {
                NodeList plantList = tempNode.getChildNodes();
                for (int j = 0; j < plantList.getLength(); j++) {
                    Node plantNode = plantList.item(j);
                    String subName = plantNode.getNodeName();
                    if (subName == "X_POS") {
                        String value = plantNode.getTextContent();
                        x = Integer.parseInt(value.trim());
                    }
                    else if (subName == "Y_POS") {
                        String value = plantNode.getTextContent();
                        y = Integer.parseInt(value.trim());
                    }
                    else if (subName == "P_DIAMETER") {
                        String value = plantNode.getTextContent();
                        diameter = Integer.parseInt(value.trim());
                    }
                }
                Plant tempPlant = new Plant(growth_rate, diameter, seedcount, seed_distance, viability);
                map[x][y].occupier = tempPlant;
            }
        }

        try {
            nodeList = (NodeList) xPath.compile("/LIFE_SIMULATION/GRAZERS").evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        node = nodeList.item(0);
        nodeList = node.getChildNodes();
        int energy_In = 0, energy_Out = 0, g_reproduce = 0;
        float maintain = 0, g_max = 0;

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);
            String name = tempNode.getNodeName();

            if (name == "G_ENERGY_INPUT") {
                String value = tempNode.getTextContent();
                energy_In = Integer.parseInt(value.trim());
            }
            else if (name == "G_ENERGY_OUTPUT") {
                String value = tempNode.getTextContent();
                energy_Out = Integer.parseInt(value.trim());
            }
            else if (name == "G_ENERGY_TO_REPRODUCE") {
                String value = tempNode.getTextContent();
                g_reproduce = Integer.parseInt(value.trim());
            }
            else if (name == "G_MAINTAIN_SPEED") {
                String value = tempNode.getTextContent();
                maintain = Float.parseFloat(value.trim());
            }
            else if (name == "G_MAX_SPEED") {
                String value = tempNode.getTextContent();
                g_max = Float.parseFloat(value.trim());
            }
            else if (name == "GRAZER") {
                int init_energy = 0;
                NodeList subList = tempNode.getChildNodes();
                for (int j = 0; j < subList.getLength(); j++) {
                    Node thisNode = subList.item(j);
                    String subName = thisNode.getNodeName();

                    if (subName == "X_POS") {
                        String value = thisNode.getTextContent();
                        x = Integer.parseInt(value.trim());
                    }
                    else if (subName == "Y_POS") {
                        String value = thisNode.getTextContent();
                        y = Integer.parseInt(value.trim());
                    }
                    else if (subName == "G_ENERGY_LEVEL") {
                        String value = thisNode.getTextContent();
                        init_energy = Integer.parseInt(value.trim());
                    }

                }
                Grazer tempGrazer = new Grazer(g_max, init_energy, energy_In, energy_Out, g_reproduce, maintain);
                map[x][y].occupier = tempGrazer;
            }
        }

        try {
            nodeList = (NodeList) xPath.compile("/LIFE_SIMULATION/PREDATORS").evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        node = nodeList.item(0);
        nodeList = node.getChildNodes();
        float speed_hod = 0, speed_hed = 0, speed_hor = 0, p_maintain = 0, gestation = 0;
        int p_energy_out = 0, p_reproduce = 0, p_offspring = 0, e_offspring = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);
            String name = tempNode.getNodeName();
            if (name == "MAX_SPEED_HOD") {
                String value = tempNode.getTextContent();
                speed_hod = Float.parseFloat(value.trim());
            }
            else if (name == "MAX_SPEED_HED") {
                String value = tempNode.getTextContent();
                speed_hed = Float.parseFloat(value.trim());
            }
            else if (name == "MAX_SPEED_HOR") {
                String value = tempNode.getTextContent();
                speed_hor = Float.parseFloat(value.trim());
            }
            else if (name == "P_MAINTAIN_SPEED") {
                String value = tempNode.getTextContent();
                p_maintain = Float.parseFloat(value.trim());
            }
            else if (name == "P_GESTATION") {
                String value = tempNode.getTextContent();
                gestation = Float.parseFloat(value.trim());
            }
            else if (name == "P_ENERGY_OUTPUT") {
                String value = tempNode.getTextContent();
                p_energy_out = Integer.parseInt(value.trim());
            }
            else if (name == "P_ENERGY_TO_REPRODUCE") {
                String value = tempNode.getTextContent();
                p_reproduce = Integer.parseInt(value.trim());
            }
            else if (name == "P_MAX_OFFSPRING") {
                String value = tempNode.getTextContent();
                p_offspring = Integer.parseInt(value.trim());
            }
            else if (name == "P_OFFSPRING_ENERGY") {
                String value = tempNode.getTextContent();
                e_offspring = Integer.parseInt(value.trim());
            }
            else if (name == "PREDATOR") {
                int p_energy = 0;
                String genotype = "";
                NodeList subList = tempNode.getChildNodes();
                for (int j = 0; j < subList.getLength(); j++) {
                    Node thisNode = subList.item(j);
                    String subName = thisNode.getNodeName();
                    if (subName == "X_POS") {
                        String value = thisNode.getTextContent();
                        x = Integer.parseInt(value.trim());
                    }
                    else if (subName == "Y_POS") {
                        String value = thisNode.getTextContent();
                        y = Integer.parseInt(value.trim());
                    }
                    else if (subName == "P_ENERGY_LEVEL") {
                        String value = thisNode.getTextContent();
                        p_energy = Integer.parseInt(value.trim());
                    }
                    else if (subName == "GENOTYPE") {
                        genotype = thisNode.getTextContent();
                    }
                }
                Predator tempPredator = new Predator(speed_hod, speed_hed, speed_hor, p_energy, p_energy_out, gestation, genotype, p_maintain, p_reproduce, p_offspring, e_offspring);
                map[x][y].occupier = tempPredator;
            }
        }

        try {
            nodeList = (NodeList) xPath.compile("/LIFE_SIMULATION/OBSTACLES").evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        node = nodeList.item(0);
        nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);
            String name = tempNode.getNodeName();
            if (name == "OBSTACLE") {
                int o_diameter = 0, o_height = 0;
                NodeList obstacleList = tempNode.getChildNodes();
                for (int j = 0; j < obstacleList.getLength(); j++) {
                    Node obstNode = obstacleList.item(j);
                    String value = obstNode.getNodeName();
                    if (value == "X_POS") {
                        String rwhitespace = obstNode.getTextContent();//Wow way to use a consistent name scheme Wren
                        x = Integer.parseInt(rwhitespace.trim());
                    }
                    else if (value == "Y_POS") {
                        String rwhitespace = obstNode.getTextContent();
                        y = Integer.parseInt(rwhitespace.trim());
                    }
                    else if ( value =="O_DIAMETER") {
                        String rwhitespace = obstNode.getTextContent();
                        o_diameter = Integer.parseInt(rwhitespace.trim());
                    }
                    else if (value == "O_HEIGHT") {
                        String rwhitespace = obstNode.getTextContent();
                        o_height = Integer.parseInt(rwhitespace.trim());
                    }
                }
                Obstacle tempObstacle = new Obstacle(o_diameter, o_height);
                map[x][y].occupier = tempObstacle;
                //May need to expand here so that obstacle can take multiple tiles (depending on implementation)
            }
        }
    }

    public int getMapHeight() {
        return map.length;
    }

    public int getMapWidth() {
        return map[0].length;
    }
}