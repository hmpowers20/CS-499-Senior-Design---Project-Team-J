public class Plant extends Actor  {
    int maxSeeds, maxSeedDistance, max_size;
    float viability, rate;
    float radius;

    public Plant(float rate, int diameter, int max_size, int maxSeeds, int maxSeedDistance, float viability, float x, float y) {
        this.rate = rate;
        this.radius = diameter / 2;
        this.maxSeeds = maxSeeds;
        this.maxSeedDistance = maxSeedDistance;
        this.viability = viability;
        this.x = x;
        this.y = y;
        this.max_size = max_size;
    }

    @Override
    public void Update(MainGameModel model) {
        if (radius < max_size) {
            radius += (rate * max_size) / 60;
            radius = Math.min(radius, max_size);
        }
    }

    void spawn() {
        Seed seed;
        //Do code here that generates a random number of seeds <= maxSeeds
        //They appear at a distance <= maxSeedDistance
        //seed = new Seed(rate, radius, maxSeeds, maxSeedDistance, viability);
    }
}
