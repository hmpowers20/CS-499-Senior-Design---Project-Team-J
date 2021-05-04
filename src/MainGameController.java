/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

/*******************************
This is the controller class.
********************************/
public class MainGameController {
    private MainGameView view;
    private MainGameModel model;
    private SimTimer timer;

    /***********************************************************************
    This constructor creates an instance of the model, view, and the timer.
    ************************************************************************/
    public MainGameController()
    {
        model = new MainGameModel();
        view = new MainGameView(model);
        timer = new SimTimer(model, this);
    }

    /********************************
    This function updates the view.
    *********************************/
    public void UpdateView()
    {
        view.Update(model);
    }
}