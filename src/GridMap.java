import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.*;
import java.util.Scanner;

public class GridMap extends JComponent {
    int rows;
    int columns;

    GridMap(int R, int C) throws FileNotFoundException {
        rows = R;
        columns = C;
        int tileSize = 32;
        setPreferredSize(new Dimension(R * tileSize + 1, C * tileSize + 1));

        Scanner scanner = new Scanner(new File("resources/mapdesign.csv"));
        scanner.useDelimiter(",|\\r\\n|\\n");

        int[][] values = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(scanner.hasNextInt()){
                    values[i][j] = scanner.nextInt();
                }
            }
        }

        scanner.close();

        ImageIcon grassTileImage = new ImageIcon(new ImageIcon("images/grass.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));
        ImageIcon dirtTileImage = new ImageIcon(new ImageIcon("images/dirt.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));
        ImageIcon sandTileImage = new ImageIcon(new ImageIcon("images/sand.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));

        ImageIcon tiles[] = { grassTileImage, dirtTileImage, sandTileImage };

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                JLabel tile = new JLabel();
                tile.setIcon(tiles[values[i][j]]);
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
