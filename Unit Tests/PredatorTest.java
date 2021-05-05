import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
/**************************************************************
 Unit Test file:
 It insures the methods in the predator class are all working
 This had been under re-construction as bugs were found while testing XMLs

 ***************************************************************/
class PredatorTest {
    MainGameModel m = mock (MainGameModel.class);
    Predator p = new Predator(1, 1, 1, 1, 1, 1, "Test", 1, 1,1, 1, 1, 1);


    @Test
    void getSpeed() {
        assertEquals(0, p.GetSpeed());

    }

    @Test
    void update() {
        MainGameModel m = mock (MainGameModel.class);
        p.Update(m);

        assertEquals(true,p.mating);
        
    }

    @Test
    void mendel() {


    }

    @Test
    void getTarget() {

        MainGameModel m = mock (MainGameModel.class);
    }

    @Test
    void moveToward() {
        MainGameModel m = mock (MainGameModel.class);
        Actor a = null;
        //p.MoveToward(m, a);



    }

    @Test
    void testMoveToward() {
    }

    @Test
    void getGenotype() {
    }

    @Test
    void eat() {
    }

    @Test
    void getEatChance() {
    }
}