/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

public class Seed extends Actor  {
    int max_size, maxSeeds, maxSeedDistance;
    float rate, viability;

    int germinateTimer = 0;

    public Seed(float rate, int max_size, int maxSeeds, int maxSeedDistance, float viability, float x, float y) {
        //All of these are just to pass on to the plant offspring. Is there a better way to do that? Probably.
        this.rate = rate;
        this.max_size = max_size;
        this.maxSeeds = maxSeeds;
        this.viability = viability;
        this.maxSeedDistance = maxSeedDistance;
        this.x = x;
        this.y = y;
    }

    @Override
    public void Update(MainGameModel model) {
        germinateTimer++;
        if (germinateTimer >= 10)
        {
            model.actorsToRemove.add(this);
            if (Math.random() < viability)
            {
                Plant plant = new Plant(rate, .02f, max_size, maxSeeds, maxSeedDistance, viability, x, y);
                model.actorsToAdd.add(plant);
            }
        }
    }

    void generate() {
        //This should be called somehow after the correct amount of time or what have you
        //Plant plant = new Plant(rate, radius, max_size, maxSeeds,maxSeedDistance, viability, x, y);
        //Do whatever to make the seed self destruct now
    }
}
