public class Seed {
    int x, y, rate, radius, maxSeeds, maxSeedDistance, viability;
    int simSpeed;
    SimMap simMap;
    public Seed(int x, int y, int rate, int radius, int maxSeeds, int maxSeedDistance, int viability) {
        simSpeed = 1;
        this.x = x;
        this.y = y;

        //All of these are just to pass on to the plant offspring. Is there a better way to do that? Probably.
        this.rate = rate;
        this.radius = radius;
        this.maxSeeds = maxSeeds;
        this.viability = viability;
        simMap = SimMap.getInstance();
        simMap.addSeed(this);
    }
    public void pause() {

    }

    void generate() {
        //This should be called somehow after the correct amount of time or what have you
        Plant plant = new Plant(x, y, rate, radius, maxSeeds,maxSeedDistance, viability);
        simMap.removeSeed(this);
        //Do whatever to make the seed self destruct now
    }

    public void resume() {

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
