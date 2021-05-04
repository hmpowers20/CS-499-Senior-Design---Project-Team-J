/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

/*****************************************************************
This class handles the timekeeping for the simulation, and calls
the update functions for the actors.
******************************************************************/
public class SimTimer {
    boolean shouldCount=false;
    boolean terminated;

    /**************************************************
    This is the constructor for the Simulation Timer.
    ***************************************************/
    public SimTimer(MainGameModel model, MainGameController controller){
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    terminated = true;
                    if (model.active) {
                        model.numSeconds++;
                        long startTime = System.currentTimeMillis();
                        // Update all actors
                        int length = model.actors.size();
                        for (int i = 0; i < length; i++)
                        {
                            Actor actor = model.actors.get(i);
                            if (actor instanceof Plant || actor instanceof Grazer || actor instanceof Predator) {
                                terminated = false;
                            }
                            actor.Update(model);
                        }
                        if (terminated) {
                            model.active = false;
                        }
                        for (Actor actor : model.actorsToRemove)
                            model.actors.remove(actor);
                        model.actorsToRemove.clear();

                        for (Actor actor : model.actorsToAdd)
                            model.actors.add(actor);
                        model.actorsToAdd.clear();

                        controller.UpdateView();

                        long endTime = System.currentTimeMillis();
                        Thread.sleep((Math.max(1000 - (endTime - startTime), 0)) / model.speed);
                    }
                    else {
                        Thread.sleep(1000 / model.speed);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long now = System.currentTimeMillis();
            while(true){
                if(shouldCount){

                }
            }
        });
        thread.start();
    }
}