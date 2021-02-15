public class SimTimer {

    boolean shouldCount=false;
    int int_sec=0;
    int int_min=0;
    int int_mil=0;

    public SimTimer() {
    }

    public static void main(String[] args) {
        TimeFrame t = new TimeFrame();
        JFrame f = new JFrame("Timer");
        f.setSize(300,200);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.getContentPane().add(new TimeFrame());
        f.setVisible(true);
    }

    public void count(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                while(true){
                    if(shouldCount){
                        if(System.currentTimeMillis()-now>=100){
                            now=System.currentTimeMillis();
                            int_mil++;
                            if(int_mil>9){
                                int_mil=0;
                                int_sec++;
                                if(int_sec>=60){
                                    int_sec=1;
                                    int_min++;
                                }
                            }
                        }
                    }
                }
            }
        });
        thread.start();
    }
}