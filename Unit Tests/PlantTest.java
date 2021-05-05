/**************************************************************
 Unit Test file: To check if both plant methods are working properly
 ***************************************************************/

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class PlantTest {


    @Test
    void update() {
        MainGameModel m = mock (MainGameModel.class);
        Plant p = new Plant(24, 0, 5, 0, 0, 0, 0, 0);
        p.Update(m);

        assertEquals(2, p.radius);
    }

    @Test
    void spawn() {
        MainGameModel m = mock (MainGameModel.class);
        Plant p = new Plant(24, 0, 5, 0, 0, 0, 0, 0);
        p.spawn(m);

        Actor seed = m.actorsToAdd.get(0);

        assertEquals(-1, seed.x);


    }
}