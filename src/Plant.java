/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class Plant extends Actor  {
    int maxSeeds, maxSeedDistance, max_size;
    float viability, rate;
    float radius;

    int seedTimer = 0;

    public Plant(float rate, float diameter, int max_size, int maxSeeds, int maxSeedDistance, float viability, float x, float y) {
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
        else
        {
            seedTimer++;
            if (seedTimer >= 3600) // once an hour
            {
                spawn(model);
                seedTimer = 0;
            }
        }
    }

    void spawn(MainGameModel model) {
        Random random = new Random();
        int numSeeds = random.nextInt(maxSeeds);

        for (int i = 0; i < numSeeds; i++) {
            Point2D.Double transform = MainGameModel.GetTransform(random.nextFloat() * maxSeedDistance,
                    new Point(random.nextInt(), random.nextInt()));
            Seed seed = new Seed(rate, max_size, maxSeeds, maxSeedDistance, viability,
                    (float)(x + transform.x), (float)(y + transform.y));
            seed.x = Math.min(Math.max(seed.x, 0), model.width - 1);
            seed.y = Math.min(Math.max(seed.y, 0), model.height - 1);

            model.actorsToAdd.add(seed);
        }
    }
}
