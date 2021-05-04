/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

/***************************************************
This class handles the behavior for the Seed actor.
****************************************************/
public class Seed extends Actor  {
    int max_size, maxSeeds, maxSeedDistance;
    float rate, viability;

    int germinateTimer = 0;

    /*******************************************
    This is the constructor for the Seed actor.
    ********************************************/
    public Seed(float rate, int max_size, int maxSeeds, int maxSeedDistance, float viability, float x, float y) {
        this.rate = rate;
        this.max_size = max_size;
        this.maxSeeds = maxSeeds;
        this.viability = viability;
        this.maxSeedDistance = maxSeedDistance;
        this.x = x;
        this.y = y;
    }

    /*************************************************************************
    This function calculates the behavior of the Seed each simulation second.
    **************************************************************************/
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
}
