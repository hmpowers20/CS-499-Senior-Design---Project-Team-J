public class Grazer extends Actor  {
    int energy_input; //Amount of energy gained per minute while eating (is that really the best way to do that? Seems odd to be but idk)
    int energy_output; //How much energy a grazer uses moving 5 distance units
    int reproduce; //Energy level to reproduce
    float maintain; //How many minutes a grazer can maintain max speed
    float speed; //Max speed

    public Grazer(float speed, int energy, int energy_input, int energy_output, int reproduce, float maintain) {
        this.energy = energy;
        this.energy_output = energy_output;
        this.energy_input = energy_input;
        this.reproduce = reproduce;
        this.maintain = maintain;
        this.speed = speed;
    }

    @Override
    public void Update(MainGameModel model) {

    }

    public void inDanger(int x, int y) {
        //The SimMap will activate this function when a grazer is within attack range of a hungry predator
        //x and y are the coordinates of the predator, which will (hopefully??) update every time it moves to a new square
        //So this has to be the logic for running away and hiding
        //Or running by another unsuspecting grazer so that it attacks them instead because that's the sort of behavior we're rewarding I guess
    }

    public int getEnergy() {
        return energy;
    }

    public void Eat()
    {
        energy += energy_input / 60;
    }
}
