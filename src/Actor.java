public abstract class Actor {
    float x, y;
    int energy;

    public int GetIntX()
    {
        return (int)Math.floor(x);
    }

    public int GetIntY()
    {
        return (int)Math.floor(y);
    }

    public abstract void Update(MainGameModel model);
}