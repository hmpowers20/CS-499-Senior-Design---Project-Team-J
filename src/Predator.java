/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Predator extends Actor {
    public final int SightRange = 150;
    public final int SmellRange = 25;

    final float speed_hod, speed_hed, speed_hor;
    final Genotype genotype;
    final float gestation; //In days
    final float maintain; //How many minutes predator can run at top speed
    final int reproduce; //Energy level necessary to start mating
    final int energy_output; //Amount of energy output per 5 distance units
    final int maxOffspring; //Maximum number of offspring
    final int in_energy; //Starting level of energy is inherited by offspring
    final int e_offspring; //Initial energy level for offspring

    java.util.List<Actor> offspring = new ArrayList<>(); //Keep track of the offspring so that the predator knows to ignore them
    Actor pursuing = null;
    boolean hungry;
    boolean mating;
    Predator partner;
    Actor parent; //null for first-generation dinosaurs, be sure to update offspring with parent
    int timeSpentPursuing = 0;
    int currentGestation = 0;

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
        if (speed == 0)
            timeSpentPursuing = 0;

        return speed;
    }

    @Override
    public void Update(MainGameModel model) {
        if (partner != null)
        {
            currentGestation++;
            if (currentGestation >= gestation * 60)
            {
                int numOffspring = new Random().nextInt(maxOffspring) + 1;
                for (int i = 0; i < numOffspring; i++)
                {
                    Predator child = new Predator(speed_hod, speed_hed, speed_hor, e_offspring, energy_output,
                            gestation, Mendel(partner), maintain, reproduce, maxOffspring, e_offspring, x, y);
                    child.parent = this;
                    model.actorsToAdd.add(child);
                    offspring.add(child);
                }

                currentGestation = 0;
                partner = null;
            }
        }

        if (energy >= reproduce && partner == null)
        {
            mating = true;
        }

        if (pursuing == null)
        {
            pursuing = GetTarget(model);
        }

        if (pursuing != null)
        {
            MoveToward(model, pursuing);
            timeSpentPursuing++;

            if (GetIntX() == pursuing.GetIntX() && GetIntY() == pursuing.GetIntY())
            {
                if (mating && pursuing instanceof Predator)
                {
                    Predator pred = (Predator)pursuing;
                    partner = pred;
                    pred.partner = this;
                }
                else
                {
                    if (Eat(model, pursuing))
                        pursuing = null;
                }
            }
        }
    }

    public String Mendel(Predator other)
    {
        String ret = "";

        if (genotype.aggression == Aggression.Most)
            ret += "A";
        else if (genotype.aggression == Aggression.None)
            ret += "a";
        else
            ret += Math.random() < .5 ? "A" : "a";

        if (other.genotype.aggression == Aggression.Most)
            ret += "A";
        else if (other.genotype.aggression == Aggression.None)
            ret += "a";
        else
            ret += Math.random() < .5 ? "A" : "a";

        ret += " ";

        if (genotype.strength == Strength.Strong)
            ret += "S";
        else if (genotype.strength == Strength.Moderate)
            ret += "s";
        else
            ret += Math.random() < .5 ? "S" : "s";

        if (other.genotype.strength == Strength.Strong)
            ret += "S";
        else if (other.genotype.strength == Strength.Moderate)
            ret += "s";
        else
            ret += Math.random() < .5 ? "S" : "s";

        ret += " ";

        if (genotype.speed == Speed.Fast)
            ret += "F";
        else if (genotype.speed == Speed.Moderate)
            ret += "f";
        else
            ret += Math.random() < .5 ? "F" : "f";

        if (other.genotype.speed == Speed.Fast)
            ret += "F";
        else if (other.genotype.speed == Speed.Moderate)
            ret += "f";
        else
            ret += Math.random() < .5 ? "F" : "f";

        return ret;
    }

    public Actor GetTarget(MainGameModel model)
    {
        Actor ret = null;

        if (mating)
        {
            ret = model.findNearestActor(new char[]{'P'}, GetIntX(), GetIntY(), SightRange, this);
            if (ret == null)
                ret = model.findNearestActor(new char[]{'P'}, GetIntX(), GetIntY(), SmellRange, this, true);
        }
        else if ((genotype.aggression == Aggression.Most)) {
            ret = model.findNearestActor(new char[]{'P', 'g'}, GetIntX(), GetIntY(), SightRange, this);
            if (ret == null)
                ret = model.findNearestActor(new char[]{'P', 'g'}, GetIntX(), GetIntY(), SmellRange, this, true);
        }
        else if (genotype.aggression == Aggression.Moderate && hungry) {
            ret = model.findNearestActor(new char[] {'g'}, GetIntX(), GetIntY(), SightRange, this);
            if (ret == null)
                ret = model.findNearestActor(new char[]{'g'}, GetIntX(), GetIntY(), SmellRange, this, true);
            if (ret == null)
                ret = model.findNearestActor(new char[] {'P'}, GetIntX(), GetIntY(), SightRange, this);
            if (ret == null)
                ret = model.findNearestActor(new char[]{'P'}, GetIntX(), GetIntY(), SmellRange, this, true);
        }
        else if (genotype.aggression == Aggression.None && hungry) {
            ret = model.findNearestActor(new char[] {'g'}, GetIntX(), GetIntY(), SightRange, this);
            if (ret == null)
                ret = model.findNearestActor(new char[]{'g'}, GetIntX(), GetIntY(), SmellRange, this, true);
        }

        return ret;
    }

    public void MoveToward(MainGameModel model, Actor actor)
    {
        Point location = model.FindTileWithActor(actor);
        MoveToward(model, location);
    }

    public void MoveToward(MainGameModel model, Point moveLoc)
    {
        Point direction = new Point(moveLoc.x - GetIntX(), moveLoc.y - GetIntY());
        float speed = GetSpeed();
        energy -= energy_output * speed / 5;
        model.moveActor(this, speed, direction);
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

    public boolean Eat(MainGameModel model, Actor actor)
    {
        if (actor instanceof Seed || actor instanceof Obstacle || actor instanceof Plant)
        {
            return false;
        }

        if (Math.random() < GetEatChance(actor))
        {
            if (actor instanceof Predator)
            {
                Predator pred = (Predator)actor;
                if (genotype.strength == pred.genotype.strength && Math.random() < 0.5)
                {
                    //Other predator won the fight
                    pred.EatSuccess(model, this);
                    return false;
                }
            }

            EatSuccess(model, actor);
            return true;
        }
        return false;
    }

    private void EatSuccess(MainGameModel model, Actor actor)
    {
        energy += .9f * actor.energy;
        model.actorsToRemove.add(actor);
    }

    public double GetEatChance(Actor actor)
    {
        if (genotype.strength == Strength.Strong)
        {
            if (actor instanceof Grazer)
            {
                return 0.95;
            }
            else
            {
                Predator pred = (Predator)actor;
                if (pred.genotype.strength == Strength.Strong)
                    return 0.5;
                else if (pred.genotype.strength == Strength.Moderate)
                    return 0.75;
                else
                    return 0.95;
            }
        }
        else if (genotype.strength == Strength.Moderate)
        {
            if (actor instanceof Grazer)
            {
                return 0.75;
            }
            else
            {
                Predator pred = (Predator)actor;
                if (pred.genotype.strength == Strength.Strong)
                    return 0.25;
                else if (pred.genotype.strength == Strength.Moderate)
                    return 0.5;
                else
                    return 0.75;
            }
        }
        else
        {
            if (actor instanceof Grazer)
            {
                return 0.5;
            }
            else
            {
                Predator pred = (Predator)actor;
                if (pred.genotype.strength == Strength.Strong)
                    return 0.05;
                else if (pred.genotype.strength == Strength.Moderate)
                    return 0.25;
                else
                    return 0.5;
            }
        }
    }
}
