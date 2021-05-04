import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;




class GrazerTest {

    private Grazer g = new Grazer(0, 0, 0, 0, 0, 0, 0, 0);
    MainGameModel m = mock (MainGameModel.class);

    @Test
    void eat() {
        when(m.obstacleBetween(1, 1, 1, 1)).thenReturn(false);
    }

    @Test
    void update() {
        MainGameModel m1 = new MainGameModel();
        Grazer g1 = new Grazer(0, 0, 0,0,0,0,0,0);
        g1.Update(m1);
        Actor sonActual = m1.actorsToAdd.get(0);
        Actor daughterActual = m1.actorsToAdd.get(1);
        sonActual.GetIntX();
        daughterActual.GetIntY();
        assertEquals(2, sonActual.GetIntX());

    }

    @Test
    void obstacleRouting() {
        when(m.obstacleBetween(1, 1, 1, 1)).thenReturn(false);
    }

    @Test
    void getEnergy() {

        assertEquals(0, g.getEnergy());

    }
}