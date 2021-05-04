/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

/*************************************************
This is the abstract parent class for the actors.
**************************************************/
public abstract class Actor {
    float x, y;
    float energy;

    /*********************************************
    This function gets the X value as an integer.
     **********************************************/
    public int GetIntX()
    {
        return (int)Math.floor(x);
    }

    /*********************************************
    This function gets the Y value as an integer.
     *********************************************/
    public int GetIntY()
    {
        return (int)Math.floor(y);
    }

    /****************************************************************
    This function updates an actor, child classes must overwrite it.
    *****************************************************************/
    public abstract void Update(MainGameModel model);
}