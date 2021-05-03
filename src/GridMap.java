/*****************************************************
 CS 499-01 Senior Design
 Project Team J
 Anushka Bhattacharjee, Haley Powers, Wren Robertson
 Spring 2021
 Final Deliverable: May 4, 2021
 ****************************************************/

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.*;
import java.util.Scanner;

public class GridMap extends JComponent {
    private int tileSize = 32;
    private int zoomFactor = 1;

    private int rows;
    private int columns;
    ImageIcon tileSprites[];
    ImageIcon lifeFormSprites[];

    double tileValues[][];

    JScrollPane scrollPane;

    GridMap(MainGameModel model) throws FileNotFoundException {
        rows = model.getMapHeight();
        columns = model.getMapWidth();

        setPreferredSize(new Dimension(columns * tileSize / zoomFactor + 1, rows * tileSize / zoomFactor + 1));

        PerlinNoise perlin = new PerlinNoise(new java.util.Random().nextInt(),
                .5, 12.0, 1.0, 6);
        tileValues = new double[rows][columns];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                tileValues[i - 1][j - 1] = perlin.getHeight((double)i / rows, (double)j / columns);
            }
        }

        // Add tiles actors
        ImageIcon grazer = new ImageIcon(new ImageIcon("images/grazer.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon AApredator = new ImageIcon(new ImageIcon("images/AAPredator.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon FFpredator = new ImageIcon(new ImageIcon("images/FFPredator.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon SSpredator = new ImageIcon(new ImageIcon("images/SSPredator.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon grazerOffspring = new ImageIcon(new ImageIcon("images/grazerOffspring.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon boulder = new ImageIcon(new ImageIcon("images/boulder.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon otherPredatorOffspring = new ImageIcon(new ImageIcon("images/OtherPredatorOffspring.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon plant1 = new ImageIcon(new ImageIcon("images/plant1.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon plant2 = new ImageIcon(new ImageIcon("images/plant2.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon plant3 = new ImageIcon(new ImageIcon("images/plant3.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon seed = new ImageIcon(new ImageIcon("images/seed.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));

        lifeFormSprites = new ImageIcon[] { grazer, AApredator, FFpredator, SSpredator, grazerOffspring, boulder, otherPredatorOffspring, plant1, plant2, plant3, seed };

        // Add tiles
        ImageIcon grassTileImage = new ImageIcon(new ImageIcon("images/grass.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));
        ImageIcon dirtTileImage = new ImageIcon(new ImageIcon("images/dirt.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));
        ImageIcon sandTileImage = new ImageIcon(new ImageIcon("images/sand.png").getImage().getScaledInstance(tileSize, tileSize,  Image.SCALE_SMOOTH));

        tileSprites = new ImageIcon[] { grassTileImage, dirtTileImage, sandTileImage };
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
        if (zoomFactor != this.zoomFactor) {
            setPreferredSize(new Dimension(columns * tileSize / zoomFactor + 1, rows * tileSize / zoomFactor + 1));
            this.zoomFactor = zoomFactor;

            PaintTiles(model);
        }
    }

    void PaintTiles(MainGameModel model)
    {
        removeAll();

        Rectangle viewRect = scrollPane.getViewport().getViewRect();

        double startColExact = (double)viewRect.x / (tileSize / zoomFactor);
        double startRowExact = (double)viewRect.y / (tileSize / zoomFactor);
        double endColExact = (double)(viewRect.x + viewRect.width) / (tileSize / zoomFactor);
        double endRowExact = (double)(viewRect.y + viewRect.height) / (tileSize / zoomFactor);
        int startCol = (int)Math.floor(startColExact);
        int startRow = (int)Math.floor(startRowExact);
        int endCol = (int)Math.ceil(endColExact);
        int endRow = (int)Math.ceil(endRowExact);

        BufferedImage render = new BufferedImage(viewRect.width, viewRect.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D renderGraphics = render.createGraphics();
        for (int i = startRow; i < endRow && i < rows; i++)
        {
            for (int j = startCol; j < endCol && j < columns; j++)
            {
                renderGraphics.drawImage(tileSprites[GetTerrainTypeFromNoise(tileValues[i][j]).toInteger()].getImage(),
                        (int)((j - startColExact) * (tileSize / zoomFactor)),
                        (int)((i - startRowExact) * (tileSize / zoomFactor)),
                        tileSize / zoomFactor, tileSize / zoomFactor, null);
            }
        }

        for (Actor actor : model.actors)
        {
            if (actor.x >= startCol && actor.x <= endCol &&
                    actor.y >= startRow && actor.y <= endRow) {
                renderGraphics.drawImage(GetSprite(actor).getImage(),
                        (int)((actor.GetIntX() - startColExact) * (tileSize / zoomFactor)),
                        (int)((actor.GetIntY() - startRowExact) * (tileSize / zoomFactor)),
                        tileSize / zoomFactor, tileSize / zoomFactor, null);
            }
        }

        JLabel map = new JLabel(new ImageIcon(render));
        map.setBounds(viewRect.x, viewRect.y, viewRect.width, viewRect.height);
        add(map);
        repaint();
        revalidate();
    }

    public TerrainType GetTerrainTypeFromNoise(double noise)
    {
        if (noise < -0.6)
            return TerrainType.Dirt;
        else if (noise > 0.6)
            return TerrainType.Sand;
        else
            return TerrainType.Grass;
    }

    public ImageIcon GetSprite(Actor actor)
    {
        if (actor instanceof Predator)
        {
            Predator pred = (Predator)actor;
            if (pred.genotype.aggression == Predator.Aggression.Most)
                return lifeFormSprites[1];
            else if (pred.genotype.aggression == Predator.Aggression.Most)
                return lifeFormSprites[2];
            else
                return lifeFormSprites[3];
        }
        else if (actor instanceof Grazer)
            return lifeFormSprites[0];
        else if (actor instanceof Plant)
        {
            Plant plant = (Plant)actor;
            if (plant.radius < 20)
                return lifeFormSprites[7];
            else if (plant.radius < 100)
                return lifeFormSprites[8];
            else
                return lifeFormSprites[9];
        }
        else if (actor instanceof Seed)
            return lifeFormSprites[10];
        else
            return lifeFormSprites[5];
    }
}
