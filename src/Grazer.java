import com.sun.tools.javac.Main;

import java.awt.*;
import java.util.Random;

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
    Plant food;
    int secondsFleeing = 0;

    public Grazer(float speed, float energy, int energy_input, int energy_output, int reproduce, float maintain, float x, float y) {
        this.energy = energy;
        this.energy_output = energy_output;
        this.energy_input = energy_input;
        this.reproduce = reproduce;
        this.maintain = maintain;
        this.max_speed = speed;
        this.x = x;
        this.y = y;
        danger = false;
        food = null;
        dangerLoc = new Point();
        destination = new Point();
        speed = (float) (max_speed * .75);
    }


    //Eats the plant, increases energy level accordingly
    void eat(MainGameModel model, Plant food) {
        model.actorsToRemove.add(food);
        energy += (float)energy_input / 60.0;
    }

    //Update will determine the grazer's behavior per second of simulation time
    @Override
    public void Update(MainGameModel model) {
        if (energy <= 0) {
            model.actorsToRemove.add(this);
            return;
        }
        if (destination != null) {
            if (destination.x == GetIntX() && destination.y == GetIntY()) {
                destination = null;
            }
        }

        Predator predator = (Predator) model.findNearestActor(new char[] {'P'}, this);
        if (predator == null) {
            danger = false;
            secondsFleeing = 0;
            speed = (float) (max_speed * .75);
        }
        else {
            danger = true;
            dangerLoc.x = predator.GetIntX();
            dangerLoc.y = predator.GetIntY();
        }

        if (danger) {
            secondsFleeing++;
            if (secondsFleeing <= maintain) {
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
            energy -= distance / energy_per_unit;
            model.moveActor(this, distance, direction);
            return;
        }

        else if (food != null && food.GetIntX() == GetIntX() && food.GetIntY() == GetIntY()) {
            eat(model, food);
            destination = null;
            food = null;
            return;
        }

        else if(energy >= reproduce) {
            Grazer son = new Grazer(max_speed, energy / 2, energy_input,energy_output,reproduce,maintain,x,y);
            Grazer daughter = new Grazer(max_speed, energy / 2, energy_input,energy_output,reproduce,maintain,x,y);
            model.actorsToAdd.add(son);
            model.actorsToAdd.add(daughter);
            energy -= energy_output;
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
        Point motion = new Point(destination.x - GetIntX(), destination.y - GetIntY());
        float distance = speed / 60;
        float energy_per_unit = (float)energy_output / 5;
        energy -= distance / energy_per_unit;
        model.moveActor(this, distance, motion);

    }

    //Return the current energy level
    public float getEnergy() {
        return energy;
    }

}