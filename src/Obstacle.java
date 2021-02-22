public class Obstacle {
    int x, y, h, d;
    public Obstacle(int x, int y, int d, int h) {
        this.x = x;
        this.y = y;
        this.d = d;
        this.h = h; //Why do these have height; I do not understand
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getH() {
        return h;
    }

    int getD() {
        return d;
    }
}
