/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

import java.awt.*;
import java.util.Random;

/*********************************************
This class handles the Grazer actor behavior
**********************************************/
public class Grazer extends Actor  {
    int energy_input; //Amount of energy gained per minute while eating (is that really the best way to do that? Seems odd to be but idk)
    int energy_output; //How much energy a grazer uses moving 5 distance units
    int reproduce; //Energy level to reproduce
    float maintain; //How many minutes a grazer can maintain max speed
    float max_speed; //Max speed
    float speed;
    boolean danger;
    Point dangerLoc;
    Point destination;
    Point previous;
    Plant food;
    int secondsFleeing = 0;
    int time_eating = 0;

    public Grazer(float max_speed, float energy, int energy_input, int energy_output, int reproduce, float maintain, float x, float y) {
        this.energy = energy;
        this.energy_output = energy_output;
        this.energy_input = energy_input;
        this.reproduce = reproduce;
        this.maintain = maintain;
        this.max_speed = max_speed;
        this.x = x;
        this.y = y;
        danger = false;
        food = null;
        dangerLoc = new Point();
        destination = new Point();
        speed = (float) (max_speed * .75);
        previous = new Point(0,0);

    }


    //Eats the plant, increases energy level accordingly
    void eat(MainGameModel model) {

        time_eating++;
        if (time_eating > 60) {
            model.actorsToRemove.add(food);
            food = null;
            time_eating = 0;
        }
    }

    //Update will determine the grazer's behavior per second of simulation time
    @Override
    public void Update(MainGameModel model) {

        if(energy >= reproduce) {
            float new_energy = energy / 2;
            energy = new_energy;
            Grazer son = new Grazer(max_speed, new_energy, energy_input,energy_output,reproduce,maintain,x + 2,y);
            Grazer daughter = new Grazer(max_speed, new_energy, energy_input,energy_output,reproduce,maintain,x - 2,y);
            model.actorsToAdd.add(son);
            model.actorsToAdd.add(daughter);
            return;
        }

        if (energy <= 0) {
            model.actorsToRemove.add(this);
            return;
        }
        if (destination != null) {
            if (destination.x == GetIntX() && destination.y == GetIntY()) {
                destination = null;
            }
        }

        Actor potential = model.findNearestActor(new char[] {'P'}, this);
        Predator predator = null;
        if (potential == null) {
            danger = false;
            secondsFleeing = 0;
            speed = (float) (max_speed * .75);
            destination = null;
        }
        else {
            danger = true;
            predator = (Predator) potential;
            dangerLoc.x = predator.GetIntX();
            dangerLoc.y = predator.GetIntY();
        }

        if (danger) {
            secondsFleeing++;
            if (secondsFleeing <= maintain * 60) {
                speed = max_speed;
            }
            else {
                speed = (float)(max_speed * .75); //Can't Maintain
            }
            //Generate a safe direction to move
            Point direction = new Point();
            direction.x = 0;
            direction.y = 0;
            if (dangerLoc.x < x) {
                direction.x =  1;
            }
            else if (dangerLoc.x > GetIntX()) {
                direction.x = - 1;
            }
            if (dangerLoc.y < GetIntY()) {
                direction.y = 1;
            }
            else if (dangerLoc.y > GetIntY()) {
                direction.y = -1;
            }

            float distance = speed / 60;

            float energy_per_unit = (float)energy_output / 5;
            energy -= energy_per_unit * distance;
            if (!model.checkObstacle(this, distance, direction)) {
                previous = model.FindTileWithActor(this);
                Point prev_motion = new Point(previous.x - GetIntX(), previous.y - GetIntY());
                if (prev_motion.x != direction.x || prev_motion.y != direction.y) {
                    model.moveActor(this, distance, direction);
                }
            }
            else {
                obstacleRouting(model, distance);
            }
            return;
        }

        else if (food != null && food.GetIntX() == GetIntX() && food.GetIntY() == GetIntY()) {
            energy += (float)energy_input / 60;
            eat(model);
            destination = null;
            return;
        }




        if (food == null) {
            Actor actor = model.findNearestActor(new char[]{'p'}, this);

            //If we found food that's not currently in reach, set destination for food
            if (actor != null){
                food = (Plant) actor;
                destination = model.FindTileWithActor(food);
            }

            if (actor == null && destination == null) {
                Random random = new Random();
                destination = new Point();
                destination.x = random.nextInt(model.getMapWidth());
                destination.y = random.nextInt(model.getMapHeight());

            }
        }
        //Move
        if (food != null) {
            destination = model.FindTileWithActor(food);
        }
        Point motion = new Point(destination.x - GetIntX(), destination.y - GetIntY());
        float distance = speed / 60;
        float energy_per_unit = (float)energy_output / 5;
        energy -= energy_per_unit * distance;


        if (!model.checkObstacle(this, distance, motion)) {
            previous = model.FindTileWithActor(this);
            Point prev_motion = new Point(previous.x - GetIntX(), previous.y - GetIntY());
            if (prev_motion.x != motion.x || prev_motion.y != motion.y) {
                model.moveActor(this, distance, motion);
            }
        }
        else {
            obstacleRouting(model, distance);
        }

    }

    //This function is called when there is an obstacle blocking the preferred path of motion
    //Takes the model in order to call moveActor, and the amount of distance to move
    //Check out adjacent units until we find an open one to move to
    void obstacleRouting(MainGameModel model, float distance) {
        Point prev_motion = new Point(previous.x - GetIntX(), previous.y - GetIntY());
        Point new_motion = new Point();

        new_motion.x = - 1;
        new_motion.y = 0;
        if (!model.checkObstacle(this, distance, new_motion) && (new_motion.x != prev_motion.x || new_motion.y != prev_motion.y)) {
            model.moveActor(this, distance, new_motion);
            return;
        }
        new_motion.y = 1;
        if (!model.checkObstacle(this, distance, new_motion) && (new_motion.x != prev_motion.x || new_motion.y != prev_motion.y)) {
            model.moveActor(this, distance, new_motion);
            return;
        }
        new_motion.x = 0;
        if (!model.checkObstacle(this, distance, new_motion) && (new_motion.x != prev_motion.x || new_motion.y != prev_motion.y)) {
            model.moveActor(this, distance, new_motion);
            return;
        }
        new_motion.x = -1;
        if (!model.checkObstacle(this, distance, new_motion) && (new_motion.x != prev_motion.x || new_motion.y != prev_motion.y)) {
            model.moveActor(this, distance, new_motion);
            return;
        }
        new_motion.y = 0;
        if (!model.checkObstacle(this, distance, new_motion) && (new_motion.x != prev_motion.x || new_motion.y != prev_motion.y)) {
            model.moveActor(this, distance, new_motion);
            return;
        }
        new_motion.y = -1;
        if (!model.checkObstacle(this, distance, new_motion) && (new_motion.x != prev_motion.x || new_motion.y != prev_motion.y)) {
            model.moveActor(this, distance, new_motion);
            return;
        }
        new_motion.x = 0;
        if (!model.checkObstacle(this, distance, new_motion) && (new_motion.x != prev_motion.x || new_motion.y != prev_motion.y)) {
            model.moveActor(this, distance, new_motion);
            return;
        }
        new_motion.x = 1;
        if (!model.checkObstacle(this, distance, new_motion) && (new_motion.x != prev_motion.x || new_motion.y != prev_motion.y)) {
            model.moveActor(this, distance, new_motion);
        }
    }

    //Return the current energy level
    public float getEnergy() {
        return energy;
    }

}