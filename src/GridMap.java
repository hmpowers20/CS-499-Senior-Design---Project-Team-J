import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.*;
import java.util.Scanner;

public class GridMap extends JComponent {
    int rows;
    int columns;

    Tile tiles[][];

    GridMap(int R, int C) throws FileNotFoundException {
        rows = R;
        columns = C;
        int tileSize = 32;
        setPreferredSize(new Dimension(R * tileSize + 1, C * tileSize + 1));

        // Initialize tiles
        tiles = new Tile[rows][columns];
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[0].length; j++)
            {
                tiles[i][j] = new Tile();
            }
        }

        Scanner scanner = new Scanner(new File("resources/mapdesign.csv"));
        scanner.useDelimiter(",|\\r\\n|\\n");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(scanner.hasNextInt()){
                    tiles[i][j].spriteType = scanner.nextInt();
                }
            }
        }

        scanner.close();

        // Add tiles actors
        ImageIcon grazer = new ImageIcon(new ImageIcon("images/grazer.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon AApredator = new ImageIcon(new ImageIcon("images/AAPredator.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon FFpredator = new ImageIcon(new ImageIcon("images/FFPredator.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon SSpredator = new ImageIcon(new ImageIcon("images/SSPredator.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon OtherPredator = new ImageIcon(new ImageIcon("images/OtherPredator.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon grazerOffspring = new ImageIcon(new ImageIcon("images/grazerOffspring.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon log = new ImageIcon(new ImageIcon("images/Log.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon boulder = new ImageIcon(new ImageIcon("images/boulder.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon pileOfRocks = new ImageIcon(new ImageIcon("images/RockPile.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon otherPredatorOffspring = new ImageIcon(new ImageIcon("images/OtherPredatorOffspring.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon plant1 = new ImageIcon(new ImageIcon("images/plant1.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon plant2 = new ImageIcon(new ImageIcon("images/plant2.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon plant3 = new ImageIcon(new ImageIcon("images/plant3.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));


        ImageIcon lifeFormSprites[] = { grazer, AApredator, FFpredator, SSpredator, OtherPredator, grazerOffspring, log, boulder, pileOfRocks, otherPredatorOffspring, plant1, plant2, plant3 };
        tiles[13][14].occupier = new Actor();
        tiles[13][14].occupier.spriteType = 1;

        tiles[13][3].occupier = new Actor();
        tiles[13][3].occupier.spriteType = 0;

        tiles[14][17].occupier = new Actor();
        tiles[14][17].occupier.spriteType = 3;

        tiles[12][10].occupier = new Actor();
        tiles[12][10].occupier.spriteType = 2;

        tiles[10][17].occupier = new Actor();
        tiles[10][17].occupier.spriteType = 4;

        tiles[13][5].occupier = new Actor();
        tiles[13][5].occupier.spriteType = 5;

        tiles[12][4].occupier = new Actor();
        tiles[12][4].occupier.spriteType = 6;

        tiles[11][3].occupier = new Actor();
        tiles[11][3].occupier.spriteType = 7;

        tiles[12][2].occupier = new Actor();
        tiles[12][2].occupier.spriteType = 8;

        tiles[14][8].occupier = new Actor();
        tiles[14][8].occupier.spriteType = 9;

        tiles[14][10].occupier = new Actor();
        tiles[14][10].occupier.spriteType = 10;

        tiles[15][5].occupier = new Actor();
        tiles[15][5].occupier.spriteType = 11;

        tiles[14][12].occupier = new Actor();
        tiles[14][12].occupier.spriteType = 12;

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                if (tiles[i][j].occupier != null && tiles[i][j].occupier.spriteType >= 0) {
                    JLabel actor = new JLabel();
                    actor.setIcon(lifeFormSprites[tiles[i][j].occupier.spriteType]);
                    actor.setBounds(j * tileSize, i * tileSize, tileSize, tileSize);
                    add(actor);
                }
            }
        }

        // Add tiles
        ImageIcon grassTileImage = new ImageIcon(new ImageIcon("images/grass.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));
        ImageIcon dirtTileImage = new ImageIcon(new ImageIcon("images/dirt.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));
        ImageIcon sandTileImage = new ImageIcon(new ImageIcon("images/sand.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));

        ImageIcon tileSprites[] = { grassTileImage, dirtTileImage, sandTileImage };

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                JLabel tile = new JLabel();
                tile.setIcon(tileSprites[tiles[i][j].spriteType]);
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
