public class SimTimer {

    boolean shouldCount=false;
    int int_sec=0;
    int int_min=0;
    int int_day=0;
    int int_hrs = 0;
    long resumeTime;
    boolean running;
    TimePanel tPanel;

    public SimTimer(TimePanel tPanel) {
        resumeTime = 0;
        this.tPanel = tPanel;
        running = true;
    }

    public SimTimer(long resumeTime, TimePanel tPanel) {
        this.resumeTime = resumeTime;
        this.tPanel = tPanel;
        running = true;
    }

    public void startTimer(){
        Thread thread = new Thread(new Runnable() {
            long startTime = System.currentTimeMillis() - resumeTime;
            @Override
            public void run() {
                try {
                    while (true) {
                        if (running) {
                            Thread.sleep(1000);
                            long now = System.currentTimeMillis() - startTime;
                            int total = (int) (now / 1000);
                            int_day = total / 86400;
                            total = total - int_day * 86400;
                            int_hrs = total / 3600;
                            total = total - int_hrs * 3600;
                            int_min = total / 60;
                            total = total - int_min * 60;
                            int_sec = total;


                            tPanel.update(int_sec, int_min, int_hrs, int_day);
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
            }
        });
        thread.start();
    }
    public void pause() {
        if (running) {
            running = false;
        }
        else {
            running = true;
        }
    }
}