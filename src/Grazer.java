public class Grazer {
    int x;
    int y;
    static int id;
    static SimMap map;
    int simSpeed;
    int speed;
    int energy;
    public Grazer(int x, int y, int id, int speed) {
        this.x = x;
        this.y = y;
        this.id = id;
        map = SimMap.getInstance();
        simSpeed = 1;
        this.speed = speed;
    }
    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setSpeed(int simSpeed) {
        this.simSpeed = simSpeed;
    }

    public void inDanger(int x, int y) {
        //The SimMap will activate this function when a grazer is within attack range of a hungry predator
        //x and y are the coordinates of the predator, which will (hopefully??) update every time it moves to a new square
        //So this has to be the logic for running away and hiding
        //Or running by another unsuspecting grazer so that it attacks them instead because that's the sort of behavior we're rewarding I guess
    }

    public int getEnergy() {
        return energy;
    }

    public void pause() {

    }

    public void resume() {

    }
}
