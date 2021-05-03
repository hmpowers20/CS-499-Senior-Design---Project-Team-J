/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

public class Obstacle extends Actor  {
    int height, diameter;
    public Obstacle(int diameter, int height, int x, int y) {
        this.diameter = diameter;
        this.height = height; //Why do these have height; I do not understand
        this.x = x;
        this.y = y;
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
