public class Predator extends Actor {
    float speed_hod, speed_hed, speed_hor;
    Actor[] offspring; //Keep track of the offspring so that the predator knows to ignore them
    boolean pursuing;
    boolean hungry;
    boolean mating;
    float gestation; //In days
    String genotype;
    float maintain; //How many minutes predator can run at top speed
    int reproduce; //Energy level necessary to start mating
    int energy_output; //Amount of energy output per 5 distance units
    int maxOffspring; //Maximum number of offspring
    static int in_energy; //Starting level of energy is inherited by offspring
    Actor parent; //null for first-generation dinosaurs, be sure to update offspring with parent
    int e_offspring; //Initial energy level for offspring

    public Predator(float speed_hod, float speed_hed, float speed_hor, int energy, int energy_output, float gestation, String genotype, float maintain, int reproduce, int maxOffspring, int e_offspring) {
        this.speed_hed = speed_hed;
        this.speed_hod = speed_hod;
        this.speed_hor = speed_hor;
        this.energy = energy;
        this.in_energy = energy;
        this.gestation = gestation;
        this.genotype = genotype;
        this.maintain = maintain;
        this.reproduce = reproduce;
        this.maxOffspring = maxOffspring;
        this.energy_output = energy_output;
        this.e_offspring = e_offspring;
        mating = false;
        hungry = true;
        pursuing = false;
    }

    @Override
    public void Update(MainGameModel model) {

    }

    public int getEnergy() {
        return energy;
    }

    public void setParent(Actor parent) {
        this.parent = parent;
    }

    public void Eat(Actor actor)
    {
        if (actor instanceof Seed || actor instanceof Obstacle || actor instanceof Plant)
        {
            return;
        }

        energy += .9f * actor.energy;
    }
}
