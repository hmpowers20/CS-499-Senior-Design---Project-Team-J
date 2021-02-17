public class Grazer {
    int x;
    int y;
    static int id;
    static SimMap map;
    int simSpeed;
    int energy; //Energy level
    int energy_input; //Amount of energy gained per minute while eating (is that really the best way to do that? Seems odd to be but idk)
    int energy_output; //How much energy a grazer uses moving 5 distance units
    int reproduce; //Energy level to reproduce
    int maintain; //How many minutes a grazer can maintain max speed
    int speed; //Max speed

    public Grazer(int x, int y, int id, int speed, int energy, int energy_input, int energy_output, int reproduce, int maintain) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.energy = energy;
        this.energy_output = energy_output;
        this.energy_input = energy_input;
        this.reproduce = reproduce;
        this.maintain = maintain;
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
