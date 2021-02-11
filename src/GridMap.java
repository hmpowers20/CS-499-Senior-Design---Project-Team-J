import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.*;

public class GridMap extends JComponent {
    int rows;
    int columns;

    GridMap(int W, int H, int R, int C) {
        setPreferredSize(new Dimension(W + 1, H + 1));
        rows = R;
        columns = C;
        int tileSize = 32;

        ImageIcon grassTileImage = new ImageIcon(new ImageIcon("images/grass.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));
        ImageIcon dirtTileImage = new ImageIcon(new ImageIcon("images/dirt.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));
        ImageIcon sandTileImage = new ImageIcon(new ImageIcon("images/sand.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));

        ImageIcon tiles[] = { grassTileImage, dirtTileImage, sandTileImage };

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                JLabel tile = new JLabel();
                tile.setIcon(tiles[ThreadLocalRandom.current().nextInt(0, tiles.length)]);
                tile.setBounds(j * tileSize, i * tileSize, tileSize, tileSize);
                add(tile);
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        int width = getSize().width;
        int height = getSize().height;

        g.setColor(Color.BLACK);

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
