public class Predator {
    int x;
    int y;
    SimMap map;
    static int id;
    int speed;
    int simSpeed;
    int energy;
    int[] offspring_id; //Keep track of the offspring so that the predator knows to ignore them
    boolean pursuing;
    boolean hungry;
    boolean mating;
    int offspringId;
    int gestation; //In days
    char[] genotype;
    int maintain; //How many minutes predator can run at top speed
    int reproduce; //Energy level necessary to start mating
    int energy_output; //Amount of energy output per 5 distance units
    int offspring; //Maximum number of offspring
    static int in_energy; //Starting level of energy is inherited by offspring
    int parentId; //0 for first-generation dinosaurs, be sure to update offspring with parent id

    public Predator(int x, int y, int id, int speed, int energy, int energy_output, int gestation, char[] genotype, int maintain, int reproduce, int offspring) {
        this.x = x;
        this.y = y;
        this.id = id;
        map = SimMap.getInstance();
        simSpeed = 1;
        this.speed = speed;
        offspringId = 0;
        this.energy = energy;
        this.in_energy = energy;
        this.gestation = gestation;
        this.genotype = genotype;
        this.maintain = maintain;
        this.reproduce = reproduce;
        this.offspring = offspring;
        this.energy_output = energy_output;
        this.parentId = 0;
        mating = false;
        hungry = true;
        pursuing = false;
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

    public void setParentId(int id) {
        this.parentId = id;
    }
}
