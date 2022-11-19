import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class Window extends JPanel {
    World world;
    public Window() {
        System.out.println(GetTime() + ": Created - Window");
//        create: true - auto generate world array, 100x100
//        print: true - print result
        world = new World(true, false);

        Timer timer;
        timer = new Timer(0, e -> {
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
    }
    private static String GetTime() {
        return String.valueOf(LocalDateTime.now());
    }
}

class World {
    private int[][] worldArray;
    public int[][] GetWorldArray() {return worldArray;}
    public World(boolean create, boolean printResultOfCreating) {
        System.out.println(GetTime() + ": Created - World");

        if (create) {
            Create(100, 100, printResultOfCreating);
        }
    }
    public void Create(int height, int width, boolean printResultOfCreating) {
        this.worldArray = new int[height][width];
        for (int y = 0; y < this.worldArray.length; y++) {
            for (int x = 0; x < this.worldArray[y].length; x++) {
                this.worldArray[y][x] = 0;
            }
        }
        Print();
    }
    public void Print() {
        for (int y = 0; y < this.worldArray.length; y++) {
            for (int x = 0; x < this.worldArray[y].length; x++) {
                if (x != worldArray.length - 1) {
                    System.out.print(worldArray[y][x]);
                } else {
                    System.out.println(worldArray[y][x]);
                }
            }
        }
    }
    private static String GetTime() {
        return String.valueOf(LocalDateTime.now());
    }
}