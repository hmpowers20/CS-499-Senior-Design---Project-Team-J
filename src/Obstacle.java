/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

/**************************************************************
This class handles all of the behavior for the Obstacle actor.
***************************************************************/
public class Obstacle extends Actor  {
    int height, diameter;

    /************************************************
    This is the constructor for the Obstacle actor.
    *************************************************/
    public Obstacle(int diameter, int height, int x, int y) {
        this.diameter = diameter;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    /****************************************************************
     This is an empty override since the Obstacle does not
    have any behavior.
    *****************************************************************/
    @Override
    public void Update(MainGameModel model) {
    }
}
