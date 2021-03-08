public class SimTimer {

    boolean shouldCount=false;
    int int_sec=0;
    int int_min=0;
    int int_day=0;
    int int_hrs = 0;
    long resumeTime;
    long relTime;
    boolean running;
    int speed;
    private static SimTimer timerInstance = null;
    String timestamp;
    TimePanel tPanel;

    public SimTimer(TimePanel tPanel) {
        resumeTime = 0;
        this.tPanel = tPanel;
        running = true;
        speed = 1;
        relTime = 0;
    }

    public static SimTimer getInstance(TimePanel tPanel) {
        if (timerInstance == null) {
            timerInstance = new SimTimer(tPanel);
        }
        return timerInstance;
    }

    public static SimTimer getInstance() {
        if (timerInstance == null) {
            //Something has gone very wrong if this runs
        }
        return timerInstance;
    }

    public SimTimer(long resumeTime, TimePanel tPanel) {
        relTime = 0;
        this.resumeTime = resumeTime;
        this.tPanel = tPanel;
        running = true;
        speed = 1;
    }

    public void startTimer(){
        Thread thread = new Thread(new Runnable() {
            long startTime = System.currentTimeMillis() - resumeTime;
            @Override
            public void run() {
                try {
                    long prevTime = 0;
                    while (true) {
                        if (running) {
                            Thread.sleep(100);
                            long diff = (System.currentTimeMillis() - prevTime - startTime) * speed;
                            relTime = relTime + diff;
                            prevTime = System.currentTimeMillis() - startTime;
                            int total = (int) (relTime / 1000);
                            int_day = total / 86400;
                            total = total - int_day * 86400;
                            int_hrs = total / 3600;
                            total = total - int_hrs * 3600;
                            int_min = total / 60;
                            total = total - int_min * 60;
                            int_sec = total;

                            String strHours;
                            String strMins;
                            String strSecs;

                            if (int_hrs < 10) {
                                strHours = "0"+int_hrs;
                            }
                            else {
                                strHours = String.valueOf(int_hrs);
                            }
                            if (int_min < 10) {
                                strMins = "0"+int_min;
                            }
                            else {
                                strMins = String.valueOf(int_min);
                            }

                            if (int_sec < 10) {
                                strSecs = "0"+int_sec;
                            }
                            else {
                                strSecs = String.valueOf(int_sec);
                            }

                            timestamp = strHours+":"+strMins+":"+strSecs;

                            if (running) {
                                tPanel.update(int_sec, int_min, int_hrs, int_day);
                            }
                            else {
                                startTime += 100;
                            }
                        }
                        else {
                            Thread.sleep(100);
                            startTime += 100;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void pause() {
        if (this.running) {
            this.running = false;
        }
        else {
            this.running = true;
        }
    }

    public void adjustSpeed(int value) {
        this.speed = value;
    }

    public long getTime() {
        return relTime;
    }
}