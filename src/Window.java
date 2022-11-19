import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Random;

public class Window extends JPanel {
    World world;
    int tileSize;

    public Window() {
        System.out.println(GetTime() + ": Created - Window");
//        create: true - auto generate world array, 100x100
//        print: true - print result
        world = new World(false, false);
//        print: true - print result
        world.Create(200, 200, false);
        world.RandomArray();

        tileSize = (int) Math.floor(720 / world.GetWorldArray().length);
        System.out.println(tileSize);

        Timer timer;
        timer = new Timer(0, e -> {
            revalidate();
            repaint();
        });
        timer.setRepeats(true);
        // Aprox. 60 FPS
        timer.setDelay(17);
        timer.start();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int[][] worldArray = world.GetWorldArray();
        int color = 0;
        for (int y = 0; y < worldArray.length; y++) {
            for (int x = 0; x < worldArray[y].length; x++) {
                color = worldArray[y][x];
                g2d.setColor(new Color(color, color, color));
                g2d.fillRect(x * this.tileSize, y * this.tileSize, this.tileSize, this.tileSize);
            }
        }
    }
    private static String GetTime() {
        return String.valueOf(LocalDateTime.now());
    }
}

class World {
    private int[][] worldArray;
    public int[][] GetWorldArray() {return worldArray;}
    Random rand;
    public World(boolean create, boolean printResultOfCreating) {
        System.out.println(GetTime() + ": Created - World");
        rand = new Random();

        if (create) {
            Create(100, 100, printResultOfCreating);
            System.out.println(GetTime() + ": Created - World Array");
        }
    }
    public void Create(int height, int width, boolean printResultOfCreating) {
        this.worldArray = new int[height][width];
        for (int y = 0; y < this.worldArray.length; y++) {
            for (int x = 0; x < this.worldArray[y].length; x++) {
                this.worldArray[y][x] = 0;
            }
        }

        if (printResultOfCreating) {
            Print();
        }
    }
    public void Print() {
        for (int y = 0; y < this.worldArray.length; y++) {
            for (int x = 0; x < this.worldArray[y].length; x++) {
                if (x != worldArray.length - 1) {
                    System.out.print(worldArray[y][x] + " ");
                } else {
                    System.out.println(worldArray[y][x]);
                }
            }
        }
    }
    public void RandomArray() {
        for (int y = 0; y < this.worldArray.length; y++) {
            for (int x = 0; x < this.worldArray[y].length; x++) {
                this.worldArray[y][x] = rand(0, 255);
//                this.worldArray[y][x] = y;
            }
        }
    }
    private int rand(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }
    private static String GetTime() {
        return String.valueOf(LocalDateTime.now());
    }
}