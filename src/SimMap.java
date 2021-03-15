import java.awt.*;
import java.util.ArrayList;

class   SimMap {
    private static SimMap single_instance = null;
    DistanceUnit[][] map;
    int numPredators;
    int numGrazers;
    int numPlants;
    int speed;
    boolean active;
    int width;
    int height;
    ArrayList<Predator> predatorList;
    ArrayList<Grazer> grazerList;
    ArrayList<Plant> plantList;
    ArrayList<Seed> seedList; //This is only here so that we can pause seed growth

    private SimMap(int width, int height) {
        this. width = width;
        this.height = height;
        map = new DistanceUnit[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = new DistanceUnit();
            }
        }
        speed = 1;
        active = false;
        predatorList = new ArrayList<Predator>();
        grazerList = new ArrayList<Grazer>();
        plantList = new ArrayList<Plant>();
        seedList = new ArrayList<Seed>();
    }

    public static SimMap getInstance(int width, int height) {
        if (single_instance == null) {
            single_instance = new SimMap(width, height);
        }
       return single_instance;
    }

    public static SimMap getInstance() {
        if (single_instance == null) {
            //If this runs then something has gone very very wrong
            single_instance = new SimMap(0,0);
        }
        return single_instance;
    }

    public void addPredator(Predator predator) {
        int x = predator.getX();
        int y = predator.getY();
        DistanceUnit currentDU = map[x][y];
        currentDU.addPredator(predator);
        predatorList.add(predator);
    }

    public void addObstacle(Obstacle obstacle) {
        int x = obstacle.getX();
        int y = obstacle.getY();
        DistanceUnit du = map[x][y];
        du.addObstacle(obstacle);
    }

    public void addGrazer(Grazer grazer) {
        int x = grazer.getX();
        int y = grazer.getY();
        DistanceUnit currentDU = map[x][y];
        currentDU.addGrazer(grazer);
        grazerList.add(grazer);
    }

    public void removePredator(Predator predator) {
        int x = predator.getX();
        int y = predator.getY();
        DistanceUnit thisDU = map[x][y];
        thisDU.removePredator(predator);
        predatorList.remove(predator);
    }

    public void removeGrazer(Grazer grazer) {
        int x = grazer.getX();
        int y = grazer.getY();
        DistanceUnit thisDU = map[x][y];
        thisDU.removeGrazer(grazer);
        grazerList.remove(grazer);
    }

    public void addSeed(Seed seed) {
        seedList.add(seed);
        int x = seed.getX();
        int y = seed.getY();
        DistanceUnit thisDU = map[x][y];
        thisDU.addSeed(seed);
    }

    public void removeSeed(Seed seed) {
        seedList.remove(seed);
        int x = seed.getX();
        int y = seed.getY();
        DistanceUnit du = map[x][y];
        du.removeSeed(seed);
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
        DistanceUnit currentDU = map[x][y];
        currentDU.addPlant(plant);
        plantList.add(plant);
    }

    //Plug in two points and see if there is an obstacle between them
    public boolean obstacleBetween(int x1, int y1, int x2, int y2) {
        if (x1 == x2) {
           if (y1 >= y2) {
               for (int i = y2; i < y1; i++) {
                   DistanceUnit du = map[x1][i];
                   if (du.checkObstacle()) {
                       return true;
                   }
               }
           }
           else {
               for (int i = y1; i < y2; i++) {
                   DistanceUnit du = map[x1][i];
                   if (du.checkObstacle()) {
                       return true;
                   }
               }
           }
        }
        else {
            if (x1 >= x2) {
                for (int i = x2; i < x1; i++) {
                    DistanceUnit du = map[i][y1];
                    if (du.checkObstacle()) {
                        return true;
                    }
                }
            }
            else {
                for (int i = x1; i < x2; i++) {
                    DistanceUnit du = map[i][y1];
                    if (du.checkObstacle()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Point findPlant(Grazer grazer) {
        Point destination = new Point();
        int x = grazer.getX();
        int y = grazer.getY();

        return destination;
    }

    //To avoid errors, use this to check to see whether there is a plant visible to a grazer before running findPlant()
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

        if (x + 125 <= width) {
            highX = x + 125;
        }
        else {
            highX = width;
        }

        if (y + 125 <= height) {
            highY = y + 125;
        }

        else {
            highY = height;
        }

        for(int i = lowX; i < highX; i++) {
            for (int j = lowY; j < highY; j++) {
                DistanceUnit du = map[i][j];
                if (du.checkPlant()) {
                    if (!this.obstacleBetween(x,y,i,j)) {
                        return true;
                    }
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

    public int getNumGrazers() {
        numGrazers = grazerList.size();
        return numGrazers;
    }

    public int getNumPredators() {
        numPredators = predatorList.size();
        return numPredators;
    }

    public int getNumPlants() {
        numPlants = plantList.size();
        return numPlants;
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