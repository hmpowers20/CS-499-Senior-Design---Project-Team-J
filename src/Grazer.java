import java.util.Random;

public class Grazer extends Actor  {
    int energy_input; //Amount of energy gained per minute while eating (is that really the best way to do that? Seems odd to be but idk)
    int energy_output; //How much energy a grazer uses moving 5 distance units
    int reproduce; //Energy level to reproduce
    float maintain; //How many minutes a grazer can maintain max speed
    float speed; //Max speed
    boolean danger;
    Plant food;

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
    void move(int direction, float distance, MainGameModel model) {
        //Move right
        if (direction == 1 && x + distance <= model.getMapWidth()) {
            x += distance;
        }
        //Moving down
        else if (direction == 2 && y + distance <= model.getMapHeight()) {
            y += distance;
        }
        //Move left
        else if (direction == 3 && x - distance >= 0) {
            x -= direction;
        }
        else if (direction == 4 && y - distance >= 0) {
            y -= distance;
        }

        float energy_expended = (float) ((float)energy_output / 5.0); //This looks like a mess to me too but it's the only way it works
        energy -= energy_expended;
        return;
    }

    void eat(Plant food) {
        food.eaten();
        energy += (float)energy_input / 60.0;

    }

    @Override
    public void Update(MainGameModel model) {

        if (energy <= 0) {
            //We die
            //model.removeActor(this);
            //Once we've got a sprite, remove sprite
            //return;
        }

        //If we're not in danger and don't have food already and aren't spawning offspring, find food


        if (!danger && food == null && energy < reproduce) {
            if (model.findNearestActor(new char[] {'p'},this) == null) {
                food = null;
            }
            else {
                food = (Plant) model.findNearestActor(new char[] {'p'},this);
            }

        }

        //If we're not in danger and already have access to food, eat the food
        else if (!danger && food != null && canEat()) {
            eat(food);
            if (!food.edible) {
                food = null;
            }
            return;
        }

        //If we are in danger, run
        else if (danger) {
            //Running away
            return;
        }

        //If we're not in danger and have the energy, reproduce
        else if (!danger && energy >= reproduce) {
            //Grazer offspring = new Grazer(speed, energy, energy_input, energy_output, reproduce, maintain, x, y);
            //model.addActor(offspring);
            return;
        }

        int dir1 = 0, dir2 = 0;
        float distance = speed / 60;

        if (food == null) { //Generate a random direction to move if we still don't have one
            Random rand = new Random();
            dir1 = rand.nextInt(4) + 1;

        }
        else {
            if (food.x > x) {
                dir1 = 1;
            }
            else if (food.x < x) {
                dir1 = 3;

            }
            if (food.y < y) {
                dir2 = 2;
            }
            else if (food.y > y) {
                dir2 = 4;
            }

        }

        // Motion logic
        if (dir1 > 0) {
            if (checkValidMove(dir1,distance,model)) {
                move(dir1, distance, model);
                return;
            }
        }
        else if (dir2 > 0) {
            if (checkValidMove(dir2, distance, model)) {
                move(dir2, distance, model);
                return;
            }
        }

        //We should only get here if we've attempted a move that was invalid, or I screwed something up
        //Gonna generate one more random move for us and if it doesn't work we just move on and our grazer sits this one out
        Random random = new Random();
        dir1 = random.nextInt(4) + 1;
        if (checkValidMove(dir1, distance, model)) {
            move(dir1, distance, model);
        }



        return;

    }

    boolean checkValidMove(int direction, float distance, MainGameModel model) {
        if (direction == 1) {
            float new_x = x + distance;
            if (new_x <= model.getMapWidth()) {
                return !model.checkObstacle(new_x, y);
            }
        }
        if (direction == 2) {
            float new_y = y + distance;
            if (new_y <= model.getMapHeight()) {
                return !model.checkObstacle(x, new_y);
            }
        }
        if (direction == 3) {
            float new_x = x - distance;
            if (new_x >= 0) {
                return !model.checkObstacle(new_x, y);
            }
        }
        if (direction == 4) {
            float new_y = y - distance;
            if (new_y >= 0) {
                return !model.checkObstacle(x, new_y);
            }
        }
        return false;
    }

    boolean canEat() {
        if (food == null) {
            return false;
        }
        float low_x, low_y, high_x, high_y;
        low_x = food.x - food.radius;
        high_x = food.x + food.radius;
        low_y = food.y - food.radius;
        high_y = food.y + food.radius;

        if (x >= low_x && x <= high_x && y >= low_y && y <= high_y) {
            return true;
        }
        return false;
    }


    public void inDanger(int x, int y) {
        danger = true;
        //The SimMap will activate this function when a grazer is within attack range of a hungry predator
        //x and y are the coordinates of the predator, which will (hopefully??) update every time it moves to a new square
        //So this has to be the logic for running away and hiding
        //Or running by another unsuspecting grazer so that it attacks them instead because that's the sort of behavior we're rewarding I guess
    }

    public float getEnergy() {
        return energy;
    }

}
