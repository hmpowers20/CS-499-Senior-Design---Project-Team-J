import java.util.ArrayList;
import java.util.Iterator;

public class SimTimer {
    boolean shouldCount=false;

    public SimTimer(MainGameModel model, MainGameController controller){
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    if (model.active) {
                        model.numSeconds++;
                        long startTime = System.currentTimeMillis();
                        // Update all actors
                        int length = model.actors.size();
                        for (int i = 0; i < length; i++)
                        {
                            Actor actor = model.actors.get(i);
                            actor.Update(model);
                        }

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