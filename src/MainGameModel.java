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
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

class MainGameModel {
    int width, height;
    public java.util.List<Actor> actors = new ArrayList<>();
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
        return new Point((int)actor.x, (int)actor.y);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public void removeActor(Actor actor) {
        actors.remove(actor);
    }

    //Plug in two points and see if there is an obstacle between them
    public boolean obstacleBetween(int x1, int y1, int x2, int y2) {

        for (Actor actor : actors) {
            int dx = x2 - x1;
            int y = y1;
            int interval;
            if (dx != 0) {
                interval = (y2 - y1) / dx; //Finding slope
            }
            else {
                interval = 0;
            }

            if (actor instanceof Obstacle) {
                for (int i = x1; i < x2; i++) {
                    y += interval;
                    if (actor.x == i && actor.y == y) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void moveActor(Actor actor, float moveDistance, Point direction)
    {
        // normalize direction
        double directionMagnitude = Math.sqrt(direction.x*direction.x + direction.y*direction.y);
        Point2D.Double normalizedDirection = new Point2D.Double(direction.x / directionMagnitude,
                direction.y / directionMagnitude);

        // Get x and y to transform actor
        Point2D.Double movement = new Point2D.Double(normalizedDirection.x * moveDistance,
                normalizedDirection.y * moveDistance);

        actor.x += movement.x;
        actor.y += movement.y;
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
    public Actor findNearestActor(char[] finding, Actor actor) {
        return findNearestActor(finding, actor.GetIntX(), actor.GetIntY(), Integer.MAX_VALUE);
    }

    //Return location of nearest specified type of actor
    public Actor findNearestActor(char[] finding, Actor actor, Actor actorToExclude) {
        return findNearestActor(finding, actor.GetIntX(), actor.GetIntY(), Integer.MAX_VALUE, actorToExclude);
    }

    //Return location of nearest specified type of actor
    public Actor findNearestActor(char[] finding, Actor actor, int range) {
        return findNearestActor(finding, actor.GetIntX(), actor.GetIntY(), range);
    }

    //Return location of nearest specified type of actor
    public Actor findNearestActor(char[] finding, Actor actor, int range, Actor actorToExclude) {
        return findNearestActor(finding, actor.GetIntX(), actor.GetIntY(), range, actorToExclude);
    }

    //Return location of nearest specified type of actor
    public Actor findNearestActor(char[] finding, int x, int y) {
        return findNearestActor(finding, x, y, Integer.MAX_VALUE);
    }

    //Return location of nearest specified type of actor
    public Actor findNearestActor(char[] finding, int x, int y, Actor actorToExclude) {
        return findNearestActor(finding, x, y, Integer.MAX_VALUE, actorToExclude);
    }

    //Return location of nearest specified type of actor
    public Actor findNearestActor(char[] findings, int x, int y, int range) {
        return findNearestActor(findings, x, y, range, null);
    }

    //Return location of nearest specified type of actor
    public Actor findNearestActor(char[] findings, int x, int y, int range, Actor actorToExclude) {
        double minDist = Double.POSITIVE_INFINITY;
        Actor minDistActor = null;

        for (Actor otherActor : actors)
        {
            if (otherActor != actorToExclude)
            {
                for (char finding : findings) {
                    if (finding == 'p' && otherActor instanceof Plant ||
                            finding == 'g' && otherActor instanceof Grazer ||
                            finding == 'P' && otherActor instanceof Predator) {
                        double distance = Math.sqrt(Math.pow(x - otherActor.x, 2) + Math.pow(y - otherActor.y, 2));
                        if (distance < minDist && distance <= range && !obstacleBetween(x, y, otherActor.GetIntX(), otherActor.GetIntY())) {
                            minDist = distance;
                            minDistActor = otherActor;
                        }
                        break;
                    }
                }
            }
        }

        return minDistActor;
    }

    public boolean checkObstacle(float x, float y) {
        for (Actor actor : actors) {
            if (actor instanceof Obstacle) {
                Obstacle obstacle = (Obstacle) actor;
                float low_x = obstacle.x - obstacle.diameter;
                float high_x = obstacle.x + obstacle.diameter;
                float low_y = obstacle.y + obstacle.diameter;
                float high_y = obstacle.y + obstacle.diameter;

                //Check to see if our given point falls within an obstacle's diameter
                if ((low_x <= x) && (x <= high_x) && (low_y <= y) && (y <= high_y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void save(SaveSession save) {

    }

    public void start() {
        active = true;
    }

    public int getNumGrazers() {
        int numGrazers = 0;

        for (Actor actor : actors)
        {
            if (actor instanceof Grazer)
                numGrazers++;
        }

        return numGrazers;
    }

    public int getNumPredators() {
        int numPredators = 0;

        for (Actor actor : actors)
        {
            if (actor instanceof Predator)
                numPredators++;
        }

        return numPredators;
    }

    public int getNumPlants() {
        int numPlants = 0;

        for (Actor actor : actors)
        {
            if (actor instanceof Plant)
                numPlants++;
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

        try {
            nodeList = (NodeList) xPath.compile("/LIFE_SIMULATION/PLANTS").evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        node = nodeList.item(0);

        children = node.getChildNodes();
        float growth_rate = 0, viability = 0;
        int max_size = 0, seed_distance = 0, seedcount = 0, x = 0, y = 0, diameter = 0;

        for (int i = 0; i < children.getLength(); i++) {
            Node tempNode = children.item(i);
            String name = tempNode.getNodeName();
            if (name == "GROWTH_RATE") {
                growth_rate = Float.parseFloat(tempNode.getTextContent());
            }
            else if(name == "MAX_SIZE") {
                String value = tempNode.getTextContent();
                max_size = Integer.parseInt(value.trim());
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
                Plant tempPlant = new Plant(growth_rate, diameter, max_size, seedcount, seed_distance, viability, x, y);
                actors.add(tempPlant);
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
                Grazer tempGrazer = new Grazer(g_max, init_energy, energy_In, energy_Out, g_reproduce, maintain,x,y);
                actors.add(tempGrazer);
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
                Predator tempPredator = new Predator(speed_hod, speed_hed, speed_hor, p_energy, p_energy_out, gestation, genotype, p_maintain, p_reproduce, p_offspring, e_offspring, x, y);
                actors.add(tempPredator);
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
                Obstacle tempObstacle = new Obstacle(o_diameter, o_height, x, y);
                actors.add(tempObstacle);
                //May need to expand here so that obstacle can take multiple tiles (depending on implementation)
            }
        }
    }

    public int getMapHeight() {
        return height;
    }

    public int getMapWidth() {
        return width;
    }
}