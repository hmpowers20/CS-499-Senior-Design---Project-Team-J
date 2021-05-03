/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

public enum TerrainType
{
    Grass,
    Dirt,
    Sand;

    public static TerrainType fromInteger(int i)
    {
        switch(i)
        {
            case 0:
                return Grass;
            case 1:
                return Dirt;
            case 2:
                return Sand;
        }

        return null;
    }

    public int toInteger()
    {
        if (this == Grass)
            return 0;
        else if (this == Dirt)
            return 1;
        else
            return 2;
    }
}
