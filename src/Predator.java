import com.sun.tools.javac.Main;

import java.awt.*;
import java.util.List;

public class Predator extends Actor {
    public final int FoodRange = 150;

    final float speed_hod, speed_hed, speed_hor;
    final Genotype genotype;
    final float gestation; //In days
    final float maintain; //How many minutes predator can run at top speed
    final int reproduce; //Energy level necessary to start mating
    final int energy_output; //Amount of energy output per 5 distance units
    final int maxOffspring; //Maximum number of offspring
    final int in_energy; //Starting level of energy is inherited by offspring
    final int e_offspring; //Initial energy level for offspring

    Actor[] offspring; //Keep track of the offspring so that the predator knows to ignore them
    Actor pursuing = null;
    boolean hungry;
    boolean mating;
    Actor parent; //null for first-generation dinosaurs, be sure to update offspring with parent
    int timeSpentPursuing = 0;

    public enum Aggression { Most, Moderate, None }

    public enum Strength { Strong, Moderate, Weak }

    public enum Speed { Fast, Moderate, Slow }

    public class Genotype
    {
        public Aggression aggression;
        public Strength strength;
        public Speed speed;
    }

    public Predator(float speed_hod, float speed_hed, float speed_hor, int energy, int energy_output, float gestation, String genotype, float maintain, int reproduce, int maxOffspring, int e_offspring, float x, float y) {
        this.speed_hed = speed_hed;
        this.speed_hod = speed_hod;
        this.speed_hor = speed_hor;
        this.energy = energy;
        this.in_energy = energy;
        this.gestation = gestation;
        this.genotype = GetGenotype(genotype);
        this.maintain = maintain;
        this.reproduce = reproduce;
        this.maxOffspring = maxOffspring;
        this.energy_output = energy_output;
        this.e_offspring = e_offspring;
        this.x = x;
        this.y = y;
        mating = false;
        hungry = true;
    }

    public float GetSpeed()
    {
        float speed = 0;
        if (genotype.speed == Speed.Fast)
            speed = speed_hod;
        else if (genotype.speed == Speed.Moderate)
            speed = speed_hed;
        else if (genotype.speed == Speed.Slow)
            speed = speed_hor;

        speed /= 60; // per second
        speed -= Math.max(timeSpentPursuing - maintain * 60, 0) / 15;

        return speed;
    }

    @Override
    public void Update(MainGameModel model) {
        Point location = model.FindTileWithActor(this);

        if (pursuing == null)
        {
            if ((genotype.aggression == Aggression.Most && mating == false)) {
                pursuing = model.findNearestActor(new char[]{'P', 'g'}, location.x, location.y, FoodRange, this);
            }
            else if (genotype.aggression == Aggression.Moderate && hungry == true) {
                pursuing = model.findNearestActor(new char[] {'g'}, location.x, location.y, FoodRange, this);
                if (pursuing == null)
                    pursuing = model.findNearestActor(new char[] {'P'}, location.x, location.y, FoodRange, this);
            }
            else if (genotype.aggression == Aggression.None && hungry == true) {
                pursuing = model.findNearestActor(new char[] {'g'}, location.x, location.y, FoodRange, this);
            }
        }

        if (pursuing != null)
        {
            MoveToward(model, pursuing);
            timeSpentPursuing++;
        }
    }

    public void MoveToward(MainGameModel model, Actor actor)
    {
        Point location = model.FindTileWithActor(actor);
        MoveToward(model, location);
    }

    public void MoveToward(MainGameModel model, Point moveLoc)
    {
        Point direction = new Point(moveLoc.x - GetIntX(), moveLoc.y - GetIntY());
        model.moveActor(this, GetSpeed(), direction);
    }

    public Genotype GetGenotype(String genotypeString) {
        Genotype ret = new Genotype();
        String[] genotypes = genotypeString.split("\\s+");

        for (String genotype : genotypes)
        {
            switch(genotype)
            {
                case "AA":
                    ret.aggression = Aggression.Most;
                    break;
                case "Aa":
                case "aA":
                    ret.aggression = Aggression.Moderate;
                    break;
                case "aa":
                    ret.aggression = Aggression.None;
                    break;
                case "SS":
                    ret.strength = Strength.Strong;
                    break;
                case "Ss":
                case "sS":
                    ret.strength = Strength.Moderate;
                    break;
                case "ss":
                    ret.strength = Strength.Weak;
                    break;
                case "FF":
                    ret.speed = Speed.Fast;
                    break;
                case "Ff":
                case "fF":
                    ret.speed = Speed.Moderate;
                    break;
                case "ff":
                    ret.speed = Speed.Slow;
                    break;
            }
        }

        return ret;
    }

    public void inDanger(Predator attacker) {
        //The reason I'm passing in the predator rather than the coordinates is so that this function can check whether the other predator
        //is trying to eat them or mate with them
    }

    public float getEnergy() {
        return energy;
    }

    public void setParent(Actor parent) {
        this.parent = parent;
    }

    public void Eat(Actor actor)
    {
        if (actor instanceof Seed || actor instanceof Obstacle || actor instanceof Plant)
        {
            return;
        }

        energy += .9f * actor.energy;
    }
}
