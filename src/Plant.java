public class Plant extends Actor  {
    int radius, maxSeeds, maxSeedDistance;
    float viability, rate;

    public Plant(float rate, int radius, int maxSeeds, int maxSeedDistance, float viability, float x, float y) {
        this.rate = rate;
        this.radius = radius;
        this.maxSeeds = maxSeeds;
        this.maxSeedDistance = maxSeedDistance;
        this.viability = viability;
        this.x = x;
        this.y = y;
    }

    @Override
    public void Update(MainGameModel model) {

    }

    void spawn() {
        Seed seed;
        //Do code here that generates a random number of seeds <= maxSeeds
        //They appear at a distance <= maxSeedDistance
        seed = new Seed(rate, radius, maxSeeds, maxSeedDistance, viability);
    }

    int getEnergy() {
        return energy;
    }
}
