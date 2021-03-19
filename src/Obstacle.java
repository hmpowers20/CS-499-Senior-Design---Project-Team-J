public class Obstacle extends Actor  {
    int height, diameter;
    public Obstacle(int diameter, int height) {
        this.diameter = diameter;
        this.height = height; //Why do these have height; I do not understand
    }

    @Override
    public void Update(MainGameModel model) {

    }

    int getHeight() {
        return height;
    }

    int getDiameter() {
        return diameter;
    }
}
