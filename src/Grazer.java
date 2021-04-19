import com.sun.tools.javac.Main;

import java.awt.*;
import java.util.Random;

public class Grazer extends Actor  {
    int energy_input; //Amount of energy gained per minute while eating (is that really the best way to do that? Seems odd to be but idk)
    int energy_output; //How much energy a grazer uses moving 5 distance units
    int reproduce; //Energy level to reproduce
    float maintain; //How many minutes a grazer can maintain max speed
    float speed; //Max speed
    boolean danger;
    Plant food;
    int secondsFleeing = 0;

    public Grazer(float speed, float energy, int energy_input, int energy_output, int reproduce, float maintain, float x, float y) {
        this.energy = energy;
        this.energy_output = energy_output;
        this.energy_input = energy_input;
        this.reproduce = reproduce;
        this.maintain = maintain;
        this.speed = speed;
        this.x = x;
        this.y = y;
        danger = false;
        food = null;
    }

    //Move the grazer to the correct position, unless this takes them beyond the bounds of the map


    //Eats the plant, increases energy level accordingly
    void eat(MainGameModel model, Plant food) {
        //model.actorsToRemove.add(food);
        energy += (float)energy_input / 60.0;
    }

    //Update will determine the grazer's behavior per second of simulation time
    @Override
    public void Update(MainGameModel model) {
        if (danger) {
            //Do the thing
            return;
        }
        else if(energy >= reproduce) {
            return;
        }

        if (food == null) {
            Actor actor = model.findNearestActor(new char[]{'p'}, this);
            if (actor == null) {
                System.out.println("No food in sight");
                return;
            } else if (food != null && food.GetIntX() == GetIntX() && food.GetIntY() == GetIntY()) {
                eat(model, food);
            } else {
                food = (Plant) actor;
            }
        }

            Point destination = model.FindTileWithActor(food);
            Point motion = new Point(destination.x - GetIntX(), destination.y - GetIntY());
            model.moveActor(this, speed / 60, motion);

    }

    //Takes the direction to move in (int), the distance to move (float), and the game model
    //Returns whether or not a theoretical move is both within bounds and not blocked by an obstacle


    //This should be called by the predator when they pursue the grazer
    public void inDanger(int x, int y) {
        //danger = true;
        //The SimMap will activate this function when a grazer is within attack range of a hungry predator
        //x and y are the coordinates of the predator, which will (hopefully??) update every time it moves to a new square
        //So this has to be the logic for running away and hiding
        //Or running by another unsuspecting grazer so that it attacks them instead because that's the sort of behavior we're rewarding I guess
    }

    //Return the current energy level
    public float getEnergy() {
        return energy;
    }

}
