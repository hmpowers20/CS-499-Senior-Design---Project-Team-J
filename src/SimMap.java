import java.awt.*;
import java.util.ArrayList;

class SimMap {
    private static SimMap single_instance = null;
    Node[][] map;
    int numPredators;
    int numGrazers;
    int numPlants;
    int speed;
    boolean active;
    int size;
    ArrayList<Predator> predatorList;
    ArrayList<Grazer> grazerList;
    ArrayList<Plant> plantList;
    ArrayList<Seed> seedList;

    private SimMap(int size) {
        this.size = size;
        map = new Node[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = new Node();
            }
        }
        speed = 1;
        active = false;
        predatorList = new ArrayList<Predator>();
        grazerList = new ArrayList<Grazer>();
        plantList = new ArrayList<Plant>();
        seedList = new ArrayList<Seed>();
    }

    public static SimMap getInstance(int size) {
        if (single_instance == null) {
            single_instance = new SimMap(size);
        }
       return single_instance;
    }

    public static SimMap getInstance() {
        if (single_instance == null) {
            //If this runs then something has gone very very wrong
            single_instance = new SimMap(0);
        }
        return single_instance;
    }

    public void addPredator(Predator predator) {
        int x = predator.getX();
        int y = predator.getY();
        Node currentNode = map[x][y];
        currentNode.addPredator(predator);
        predatorList.add(predator);
    }

    public void addGrazer(Grazer grazer) {
        int x = grazer.getX();
        int y = grazer.getY();
        Node currentNode = map[x][y];
        currentNode.addGrazer(grazer);
        grazerList.add(grazer);
    }

    public void removePredator(Predator predator) {
        int x = predator.getX();
        int y = predator.getY();
        Node thisNode = map[x][y];
        thisNode.removePredator(predator);
        predatorList.remove(predator);
    }

    public void removeGrazer(Grazer grazer) {
        int x = grazer.getX();
        int y = grazer.getY();
        Node thisNode = map[x][y];
        grazerList.remove(grazer);
    }

    public int eatPredator(Predator predator) {
        int energy = predator.getEnergy();
        this.removePredator(predator);
        return energy;
    }

    public int eatGrazer(Grazer grazer) {
        int energy = grazer.getEnergy();
        this.removeGrazer(grazer);
        return energy;
    }

    public void addPlant(Plant plant) {
        int x = plant.getX();
        int y = plant.getY();
        Node currentNode = map[x][y];
        currentNode.addPlant(plant);
        plantList.add(plant);
    }

    public Point findPlant(Grazer grazer) {
        Point destination = new Point();
        int x = grazer.getX();
        int y = grazer.getY();

        return destination;
    }

    //Check to see whether there is a plant visible to a grazer before running findPlant() to avoid errors
    public boolean isPlantInRange(int x, int y) {
        int lowX;
        int lowY;
        int highX;
        int highY;

        if (x - 125 >= 0) {
            lowX = x - 125;
        }
        else {
            lowX = 0;
        }

        if (y - 125 >= 0) {
            lowY = y - 125;
        }
        else {
            lowY = 0;
        }

        if (x + 125 <= size) {
            highX = x + 125;
        }
        else {
            highX = size;
        }

        if (y + 125 <= size) {
            highY = y + 125;
        }

        else {
            highY = size;
        }

        for(int i = lowX; i < highX; i++) {
            for (int j = lowY; j < highY; j++) {
                Node node = map[i][j];
                if (node.checkPlant()) {
                    //Code to check whether there's an obstacle between the grazer and the plant
                    return true;
                }
            }
        }
        return false;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void save(SaveSession save) {

    }

    public void start() {
        active = true;
    }

    //Pause simulation it's running and vice versa, couldn't think of a less confusing name
    public void pauseResume() {
        if (active) {
            active = false;
            for (Predator predator : predatorList) {
                predator.pause();
            }

            for (Grazer grazer : grazerList) {
                grazer.pause();
            }

            for (Plant plant : plantList) {
                plant.pause();
            }

            for (Seed seed : seedList) {
                seed.pause();
            }
        }
        else {
            active = true;
            for (Predator predator : predatorList) {
                predator.resume();
            }

            for (Grazer grazer : grazerList) {
                grazer.resume();
            }

            for (Plant plant : plantList) {
                plant.resume();
            }

            for (Seed seed : seedList) {
                seed.resume();
            }
        }
    }
}