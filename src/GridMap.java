import java.awt.*;
import javax.swing.JComponent;

public class GridMap extends JComponent {
    int rows;
    int columns;

    GridMap(int W, int H, int R, int C) {
        setPreferredSize(new Dimension(W + 1, H + 1));
        rows = R;
        columns = C;
    }

    public void paint(Graphics g) {
        int width = getSize().width;
        int height = getSize().height;

        int rowHeight = height / rows;
        for (int i = 0; i <= rows; i++){
            g.drawLine(0, i * rowHeight, width, i * rowHeight);
        }

        int rowWidth = width / columns;
        for (int i = 0; i <= columns; i++){
            g.drawLine(i * rowWidth, 0, i * rowWidth, height);
        }
    }
}
