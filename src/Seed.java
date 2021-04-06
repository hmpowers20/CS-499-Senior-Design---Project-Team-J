public class Seed extends Actor  {
    int radius, maxSeeds, maxSeedDistance;
    float rate, viability;

    public Seed(float rate, int radius, int maxSeeds, int maxSeedDistance, float viability) {
        //All of these are just to pass on to the plant offspring. Is there a better way to do that? Probably.
        this.rate = rate;
        this.radius = radius;
        this.maxSeeds = maxSeeds;
        this.viability = viability;
        this.maxSeedDistance = maxSeedDistance;
    }

    @Override
    public void Update(MainGameModel model) {

    }

    void generate() {
        //This should be called somehow after the correct amount of time or what have you
        //Plant plant = new Plant(rate, radius, max_size, maxSeeds,maxSeedDistance, viability, x, y);
        //Do whatever to make the seed self destruct now
    }
}
