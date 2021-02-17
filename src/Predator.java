public class Predator {
    int x;
    int y;
    SimMap map;
    static int id;
    int speed;
    int simSpeed;
    int energy;
    boolean pursuing;
    boolean hungry;
    boolean mating;
    int offspringId;

    public Predator(int x, int y, int id, int speed) {
        this.x = x;
        this.y = y;
        this.id = id;
        map = SimMap.getInstance();
        simSpeed = 1;
        this.speed = speed;
        offspringId = 0;
    }

    public void setSpeed(int speed) {
        this.simSpeed = speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getEnergy() {
        return energy;
    }

    public void pause() {

    }

    public void resume() {

    }
}
