public class Plant {
    int x, y, radius, maxSeeds, maxSeedDistance;
    int energy;
    int simSpeed;
    float viability, rate;

    public Plant(int x, int y, float rate, int radius, int maxSeeds, int maxSeedDistance, float viability) {
        this.x = x;
        this.y = y;
        this.rate = rate;
        this.radius = radius;
        this.maxSeeds = maxSeeds;
        this.maxSeedDistance = maxSeedDistance;
        this.viability = viability;
        simSpeed = 1;
    }

    void spawn() {
        Seed seed;
        //Do code here that generates a random number of seeds <= maxSeeds
        //They appear at a distance <= maxSeedDistance
        seed = new Seed(x, y, rate, radius, maxSeeds, maxSeedDistance, viability);
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getEnergy() {
        return energy;
    }

    public void pause() {

    }

    public void resume() {

    }
}
