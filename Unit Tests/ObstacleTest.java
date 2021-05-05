import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**************************************************************
 Unit Test file changed and not used because the Obstacle class has no behaviour
 ***************************************************************/

class ObstacleTest {

    MainGameModel m = mock (MainGameModel.class);
    private Obstacle ob = new Obstacle(1, 1, 1, 1);

    @Test
    void update() {

        //This function has been tested in another test class

    }

    /*@Test
    void getHeight() {

        assertEquals(1, ob.getHeight());

    }

    @Test
    void getDiameter() {

        assertEquals(1, ob.getDiameter());
    }*/
}