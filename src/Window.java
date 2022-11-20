import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Random;

public class Window extends JPanel {
    private World world;
    private int tileSize;
    Color[][] worldArray;
    private GenerateMethods generateMethods;

    public Window() {
        System.out.println(GetTime() + ": Created - Window");
        setLayout(null);

        world = new World(false, false);
        generateMethods = new GenerateMethods();
        creatingWorldArray(695, 695);
        calculateValuesForScreen();
        world.RandomArray();

        preferencesCreate();

        timer();
    }
    private void timer() {
        System.out.println(GetTime() + ": Start - timer()");
        Timer timer;
        timer = new Timer(0, e -> {
            revalidate();
            repaint();
        });
        timer.setRepeats(true);
        // Aprox. 60 FPS = 17
        // Aprox. 30 FPS = 33
        // Aprox. 10 FPS = 100
        // Aprox. 1  FPS = 1000
        timer.setDelay(17);
        timer.start();
    }
    private void preferencesCreate() {
        System.out.println(GetTime() + ": Start - preferencesCreate()");
        JPanel preferences = new JPanel();
        preferences.setBounds(720, 0, 1280 - 720, 695);


        JPanel panelDayNight = new JPanel();
        preferences.add(panelDayNight);

        JButton DayNightLandWaterGenerate = new JButton("Day-Night Land-Water Generate");
        DayNightLandWaterGenerate.addActionListener(e -> {
            Color[][] array = generateMethods.dayNightLandWater(this.world.GetWorldArray(), 500);
            this.world.SetWorldArray(array);
            calculateValuesForScreen();
            revalidate();
        });
        panelDayNight.add(DayNightLandWaterGenerate);

        JButton PerlinNoiseLandWaterGenerate = new JButton("Perlin-Noise Land-Water");
        PerlinNoiseLandWaterGenerate.addActionListener(e -> {
            BufferedImage result = generateMethods.PerlinNoiseLandWater(this.world.GetWorldArray());
//            showImageWindow(result, result.getWidth(), result.getHeight());
            Color[][] array = new Color[this.world.GetWorldArray().length][this.world.GetWorldArray()[0].length];
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getHeight(); x++) {
                    Color color = new Color(result.getRGB(x, y));
                    int grey = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                    if (grey < percentRGB(5)) {array[y][x] = new Color(2, 30, 40);}
                    else if (grey < percentRGB(10)) {array[y][x] = new Color(2, 30, 55);}
                    else if (grey < percentRGB(15)) {array[y][x] = new Color(3, 44, 80);}
                    else if (grey < percentRGB(20)) {array[y][x] = new Color(4, 60, 100);}
                    else if (grey < percentRGB(25)) {array[y][x] = new Color(5, 71, 124);}
                    else if (grey < percentRGB(30)) {array[y][x] = new Color(9, 95, 164);}
                    else if (grey < percentRGB(35)) {array[y][x] = new Color(20, 125, 200);}
                    else if (grey < percentRGB(45)) {array[y][x] = new Color(33, 150, 243);}
                    else if (grey < percentRGB(50)) {array[y][x] = new Color(0, 188, 212);}
                    else if (grey < percentRGB(55)) {array[y][x] = new Color(255, 239, 59);}
                    else if (grey < percentRGB(60)) {array[y][x] = new Color(139, 195, 74);}
                    else if (grey < percentRGB(65)) {array[y][x] = new Color(100, 150, 50);}
                    else if (grey < percentRGB(70)) {array[y][x] = new Color(75, 130, 40);}
                    else if (grey < percentRGB(75)) {array[y][x] = new Color(40, 100, 30);}
                    else if (grey < percentRGB(80)) {array[y][x] = new Color(20, 75, 25);}
                    else if (grey < percentRGB(85)) {array[y][x] = new Color(100, 100, 100);}
                    else if (grey < percentRGB(90)) {array[y][x] = new Color(150, 150, 150);}
                    else if (grey < percentRGB(95)) {array[y][x] = new Color(200, 200, 200);}
                    else if (grey < percentRGB(100)) {array[y][x] = new Color(255, 255, 255);}
                }
            }
            this.world.SetWorldArray(array);
            calculateValuesForScreen();
            revalidate();
        });
        panelDayNight.add(PerlinNoiseLandWaterGenerate);

        add(preferences, null);
    }
    private int percentRGB(int source) {
        return (source * 255) / 100;
    }
    public static void showImageWindow(Image image, int width, int height) {
        JFrame frame = new JFrame();
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel picLabel = new JLabel(new ImageIcon(image));

        BorderLayout borderLayout = new BorderLayout();
        frame.getContentPane().setLayout(borderLayout);
        frame.getContentPane().add(picLabel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
    private void calculateValuesForScreen() {
        System.out.println(GetTime() + ": Start - calculateValuesForScreen()");
        tileSize = (int) Math.floor(695 / this.world.GetWorldArray().length);
        this.worldArray = world.GetWorldArray();
    }
    private void creatingWorldArray(int Height, int Width) {
        System.out.println(GetTime() + ": Start - calculateValuesForScreen()");
//        create: true - auto generate world array, 100x100
//        print: true - print result
        world = new World(false, false);
//        print: true - print result
        world.Create(Height, Width, false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (int y = 0; y < this.worldArray.length; y++) {
            for (int x = 0; x < this.worldArray[y].length; x++) {
                g2d.setColor(this.worldArray[y][x]);
                g2d.fillRect(x * this.tileSize, y * this.tileSize, this.tileSize, this.tileSize);
            }
        }
    }
    private static String GetTime() {
        return String.valueOf(LocalDateTime.now());
    }
}

class World {
    private Color[][] worldArray;
    public Color[][] GetWorldArray() {return worldArray;}
    public void SetWorldArray(Color[][] WorldArray) {this.worldArray = WorldArray;}
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
        System.out.println(GetTime() + ": Start - World.Create()");
        this.worldArray = new Color[height][width];
        for (int y = 0; y < this.worldArray.length; y++) {
            for (int x = 0; x < this.worldArray[y].length; x++) {
                this.worldArray[y][x] = new Color(0, 0, 0);
            }
        }
        if (printResultOfCreating) {
            Print();
        }
    }
    public void Print() {
        System.out.println(GetTime() + ": Start - World.Print()");
        for (Color[] colors : this.worldArray) {
            for (int x = 0; x < colors.length; x++) {
                if (x != worldArray.length - 1) {
                    System.out.print(colors[x] + " ");
                } else {
                    System.out.println(colors[x]);
                }
            }
        }
    }
    public void Print2(Color[][] Array) {
        System.out.println(GetTime() + ": Start - World.Print2()");
        for (Color[] colors : Array) {
            for (int x = 0; x < colors.length; x++) {
                if (x != Array.length - 1) {
                    System.out.print(colors[x] + " ");
                } else {
                    System.out.println(colors[x]);
                }
            }
        }
    }
    public void RandomArray() {
        System.out.println(GetTime() + ": Start - World.RandomArray()");
        for (int y = 0; y < this.worldArray.length; y++) {
            for (int x = 0; x < this.worldArray[y].length; x++) {
                this.worldArray[y][x] = new Color(rand(0, 255), rand(0, 255), rand(0, 255));
//                this.worldArray[y][x] = y;
            }
        }
    }
    public int rand(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }
    private static String GetTime() {
        return String.valueOf(LocalDateTime.now());
    }
}