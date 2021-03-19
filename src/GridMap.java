import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.*;
import java.util.Scanner;

public class GridMap extends JComponent {
    int tileSize = 32;
    int zoomFactor = 1;

    int rows;
    int columns;
    ImageIcon tileSprites[];
    ImageIcon lifeFormSprites[];
    ImageIcon scaledTileSprites[];
    ImageIcon scaledLifeformSprites[];

    JScrollPane scrollPane;

    GridMap(MainGameModel model) throws FileNotFoundException {
        rows = model.getMapHeight();
        columns = model.getMapWidth();

        setPreferredSize(new Dimension(columns * tileSize / zoomFactor + 1, rows * tileSize / zoomFactor + 1));

        Scanner scanner = new Scanner(new File("resources/mapdesign.csv"));
        scanner.useDelimiter(",|\\r\\n|\\n");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(scanner.hasNextInt()){
                    model.map[i][j].terrainType = TerrainType.fromInteger(scanner.nextInt());
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

        lifeFormSprites = new ImageIcon[] { grazer, AApredator, FFpredator, SSpredator, OtherPredator, grazerOffspring, log, boulder, pileOfRocks, otherPredatorOffspring, plant1, plant2, plant3 };
        scaledLifeformSprites = lifeFormSprites;

        // Add tiles
        ImageIcon grassTileImage = new ImageIcon(new ImageIcon("images/grass.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));
        ImageIcon dirtTileImage = new ImageIcon(new ImageIcon("images/dirt.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));
        ImageIcon sandTileImage = new ImageIcon(new ImageIcon("images/sand.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));

        tileSprites = new ImageIcon[] { grassTileImage, dirtTileImage, sandTileImage };
        scaledTileSprites = tileSprites;
    }

    public void paint(Graphics g) {
        super.paint(g);

        int width = getSize().width;
        int height = getSize().height;

        g.setColor(Color.BLACK);

        int rowHeight = tileSize / zoomFactor;
        for (int i = 0; i <= rows; i++) {
            g.drawLine(0, i * rowHeight, width, i * rowHeight);
        }

        int rowWidth = tileSize / zoomFactor;
        for (int i = 0; i <= columns; i++) {
            g.drawLine(i * rowWidth, 0, i * rowWidth, height);
        }
    }

    void Zoom(int zoomFactor, MainGameModel model)
    {
        setPreferredSize(new Dimension(columns * tileSize / zoomFactor + 1, rows * tileSize / zoomFactor + 1));
        this.zoomFactor = zoomFactor;

        for (int i = 0; i < tileSprites.length; i++)
        {
            scaledTileSprites[i] = new ImageIcon(tileSprites[i].getImage().getScaledInstance(tileSize / zoomFactor, tileSize / zoomFactor, Image.SCALE_SMOOTH));
        }

        for (int i = 0; i < lifeFormSprites.length; i++)
        {
            scaledLifeformSprites[i] = new ImageIcon(lifeFormSprites[i].getImage().getScaledInstance(tileSize / zoomFactor, tileSize / zoomFactor, Image.SCALE_SMOOTH));
        }

        PaintTiles(model);
        repaint();
        revalidate();
    }

    void PaintTiles(MainGameModel model)
    {
        removeAll();

        Rectangle viewRect = scrollPane.getViewport().getViewRect();

        int startCol = (int)Math.floor((double)viewRect.x / (tileSize / zoomFactor));
        int startRow = (int)Math.floor((double)viewRect.y / (tileSize / zoomFactor));
        int endCol = (int)Math.ceil((double)(viewRect.x + viewRect.width) / (tileSize / zoomFactor));
        int endRow = (int)Math.ceil((double)(viewRect.y + viewRect.height) / (tileSize / zoomFactor));

        for (int i = startRow; i < endRow && i < rows; i++)
        {
            for (int j = startCol; j < endCol && j < columns; j++)
            {
                if (model.map[i][j].occupier != null) {
                    JLabel actor = new JLabel();
                    actor.setIcon(GetSprite(model.map[i][j].occupier));
                    actor.setBounds(j * (tileSize / zoomFactor), i * (tileSize / zoomFactor), (tileSize / zoomFactor), (tileSize / zoomFactor));
                    add(actor);
                }
            }
        }

        for (int i = startRow; i < endRow && i < rows; i++)
        {
            for (int j = startCol; j < endCol && j < columns; j++)
            {
                JLabel tile = new JLabel();
                TerrainType terrain = model.map[i][j].terrainType;
                if (terrain != null)
                    tile.setIcon(scaledTileSprites[model.map[i][j].terrainType.toInteger()]);
                else
                    tile.setIcon(scaledTileSprites[0]);
                tile.setBounds(j * (tileSize / zoomFactor), i * (tileSize / zoomFactor), (tileSize / zoomFactor), (tileSize / zoomFactor));
                add(tile);
            }
        }
    }

    public ImageIcon GetSprite(Actor actor)
    {
        return scaledLifeformSprites[(int)(Math.random() * (scaledLifeformSprites.length))];
    }
}
