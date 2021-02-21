public class Plant {
    int x, y, rate, radius, maxSeeds, maxSeedDistance, viability;
    int energy;
    int simSpeed;

    public Plant(int x, int y, int rate, int radius, int maxSeeds, int maxSeedDistance, int viability) {
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
