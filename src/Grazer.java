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
    float speed; //Current speed
    boolean danger; //Is the grazer currently fleeing?
    Point dangerLoc; //If the grazer is fleeing, where is the predator?
    Point destination; //The point the grazer is moving towards
    Point previous; //The previous location for the grazer; prevents getting stuck between two points
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


    /****************************************************************************************************************************
     * This function is called to increase a grazer's energy while they're eating, and remove the plant once it is fully consumed
     * @param model
     ****************************************************************************************************************************/
    void eat(MainGameModel model) {

        time_eating++;
        energy += (float)energy_input / 60;
        if (time_eating > 60) {
            model.actorsToRemove.add(food);
            food = null;
            time_eating = 0;
        }
    }

    /******************************************************************************
     * Update function called once per second in simulation time, controls behavior
     * @param model
     ******************************************************************************/
    @Override
    public void Update(MainGameModel model) {

        //If grazer has enough energy to reproduce, do so
        if(energy >= reproduce) {
            float new_energy = energy / 2;
            energy = new_energy;
            Grazer son = new Grazer(max_speed, new_energy, energy_input,energy_output,reproduce,maintain,x + 2,y);
            Grazer daughter = new Grazer(max_speed, new_energy, energy_input,energy_output,reproduce,maintain,x - 2,y);
            model.actorsToAdd.add(son);
            model.actorsToAdd.add(daughter);
            return;
        }

        //If grazer no longer has energy, remove self
        if (energy <= 0) {
            model.actorsToRemove.add(this);
            return;
        }

        //Set destination to null if we are currently at previous destination, prevent getting stuck
        if (destination != null) {
            if (destination.x == GetIntX() && destination.y == GetIntY()) {
                destination = null;
            }
        }

        //Search for danger: detect whether there's a predator in range and set danger and dangerLoc as needed
        Actor potential = model.findNearestActor(new char[] {'P'}, this);
        Predator predator = null;
        float p_distance = 160;
        float d_distance = 160;
        if (dangerLoc != null && potential != null) {
            d_distance = Math.abs(GetIntX() - potential.GetIntX()) * Math.abs(GetIntY() - potential.GetIntY());
            p_distance = Math.abs(GetIntX()-potential.GetIntX()) * Math.abs(GetIntY()-potential.GetIntY());
        }
        if (potential == null) {
            danger = false;
            secondsFleeing = 0;
            speed = (float) (max_speed * .75);
            destination = null;
        }
        else if(potential != null && danger && d_distance <= p_distance) {
            danger = false;
            secondsFleeing = 0;
            speed = (float) (max_speed * .75);
            destination = null;
        }
        else {
            if(Math.abs(dangerLoc.x - GetIntX()) < 50 && Math.abs(dangerLoc.y - GetIntY()) < 50) {
                danger = true;
                predator = (Predator) potential;
                dangerLoc.x = predator.GetIntX();
                dangerLoc.y = predator.GetIntY();
            }
        }

        //Flee from danger, adjust speed to max until the grazer can't maintain
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

        //If we've reached food, eat
        else if (food != null && food.GetIntX() == GetIntX() && food.GetIntY() == GetIntY()) {
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
        if (food != null) {
            destination = model.FindTileWithActor(food);
        }
        Point motion = new Point(destination.x - GetIntX(), destination.y - GetIntY());
        float distance = speed / 60;
        float energy_per_unit = (float)energy_output / 5;
        energy -= energy_per_unit * distance;


        //Check whether there's an obstacle in the way, proceed normally if not or call obstacleRouting if there is
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

    /***************************************************************
     * Call obstacleRouting when optimal path is blocked by obstacle
     * Finds alternative route if one exists
     * @param model
     * @param distance
     ***************************************************************/
    void obstacleRouting(MainGameModel model, float distance) {
        Point prev_motion = new Point(previous.x - GetIntX(), previous.y - GetIntY());
        Point new_motion = new Point();

        //Perform a clockwise series of checks to find an adjacent distance unit with no obstacle, move into it when it's found
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

    /**********************************
     * Returns the current energy level
     * @return energy level
     **********************************/
    public float getEnergy() {
        return energy;
    }

}