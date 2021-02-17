import java.util.ArrayList;

public class Node {
    ArrayList<Predator> predators;
    ArrayList<Grazer> grazers;
    Obstacle obstacle;
    boolean hasPlant;
    boolean hasPredator;
    boolean hasGrazer;
    boolean hasObstacle;
    boolean hasSeed;

    public Node() {
        hasPlant = false;
        hasPredator = false;
        hasGrazer = false;
        hasObstacle = false;
        hasSeed = false;
        predators = new ArrayList<Predator>();
        grazers = new ArrayList<Grazer>();
    }

    public void addPredator(Predator predator) {
        predators.add(predator);
        hasPredator = true;
    }

    public void addPlant(Plant plant) {
        hasPlant = true;
    }

    public void addObstacle(Obstacle obstacle1) {
        hasObstacle = true;
    }

    public void addGrazer(Grazer grazer) {
        grazers.add(grazer);
        hasGrazer = true;
    }

    boolean checkPlant() {
        return hasPlant;
    }

    boolean checkPredator() {
        return hasPredator;
    }

    boolean checkObstacle() {
        return hasObstacle;
    }

    boolean checkGrazer() {
        return hasGrazer;
    }

    public void removePredator(Predator predator) {
        predators.remove(predator);
        if (predators.size() == 0) {
            hasPredator = false;
        }
    }

    public void removeGrazer(Grazer grazer) {
        grazers.remove(grazer);
        if (grazers.size() == 0) {
            hasGrazer = false;
        }
    }

    public void addSeed(Seed seed) {
        hasSeed = true;
    }

    public void removeSeed(Seed seed) {
        hasSeed = false;
    }

    public void removePlant() {
        hasPlant = false;
    }

}

