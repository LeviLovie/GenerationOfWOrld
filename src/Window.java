import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

public class Window extends JPanel {
    private World world;
    private int tileSize;
    Color[][] worldArray;

    public Window() {
        System.out.println(GetTime() + ": Created - Window");
        setLayout(null);

        world = new World(false, false);
        creatingWorldArray();
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
            Color[][] array = DayNightLandWater();
            this.world.SetWorldArray(array);
            revalidate();
        });
        panelDayNight.add(DayNightLandWaterGenerate);

        JButton PerlinNoiseLandWaterGenerate = new JButton("Perlin-Noise Land-Water");
        PerlinNoiseLandWaterGenerate.addActionListener(e -> {
            Color[][] array = PerlinNoiseLandWater();
            this.world.SetWorldArray(array);
            revalidate();
        });
        panelDayNight.add(PerlinNoiseLandWaterGenerate);

        add(preferences, null);
    }
    private Color[][] dayNightLandWater() {
        System.out.println(GetTime() + ": Start - DayNightLandWater()");
        Color[][] localWorldArrayColor = this.world.GetWorldArray();
        int[][] localWorldArray = new int[localWorldArrayColor.length][localWorldArrayColor[0].length];

        for (int y = 0; y < localWorldArray.length; y++) {
            for (int x = 0; x < localWorldArray[y].length; x++) {
                int rand = this.world.rand(0, 2);
                if (rand == 0) {
                    localWorldArray[y][x] = 0;
                } else if (rand == 1) {
                    localWorldArray[y][x] = 1;
                }
            }
        }

        int tile;
        int friends;
        int animys;
        int height;
        int width;
        for (int t = 0; t < 100; t++) {
            for (int t2 = 0; t2 < (localWorldArray.length * localWorldArray[0].length) / 2; t2++) {
                height = this.world.rand(0, localWorldArray.length);
                width = this.world.rand(0, localWorldArray[0].length);

                tile = localWorldArray[height][width];
                friends = 0;
                animys = 0;
                if (tile == 0) {
                    try {
                        if (localWorldArray[height - 1][width - 1] == 1) {animys++;}
                        if (localWorldArray[height - 1][width] == 1) {animys++;}
                        if (localWorldArray[height - 1][width + 1] == 1) {animys++;}

                        if (localWorldArray[height][width - 1] == 1) {animys++;}
                        if (localWorldArray[height][width] == 1) {animys++;}
                        if (localWorldArray[height][width + 1] == 1) {animys++;}

                        if (localWorldArray[height + 1][width] == 1) {animys++;}
                        if (localWorldArray[height + 1][width] == 1) {animys++;}
                        if (localWorldArray[height + 1][width + 1] == 1) {animys++;}
                    } catch (Exception ignored) {}
                } else if (tile == 1) {
                    try {
                        if (localWorldArray[height - 1][width - 1] == 0) {friends++;}
                        if (localWorldArray[height - 1][width] == 0) {friends++;}
                        if (localWorldArray[height - 1][width + 1] == 0) {friends++;}

                        if (localWorldArray[height][width - 1] == 0) {friends++;}
                        if (localWorldArray[height][width] == 0) {friends++;}
                        if (localWorldArray[height][width + 1] == 0) {friends++;}

                        if (localWorldArray[height + 1][width] == 0) {friends++;}
                        if (localWorldArray[height + 1][width] == 0) {friends++;}
                        if (localWorldArray[height + 1][width + 1] == 0) {friends++;}
                    } catch (Exception ignored) {}
                }
                if (tile == 1) {
                    if (friends == 3 || friends == 6 || friends == 7 || friends == 8) {
                        localWorldArray[height][width] = 0;
                    }
                } else if (tile == 0) {
                     if (animys == 3 || animys == 6 || animys == 7 || animys == 8) {
                        localWorldArray[height][width] = 1;
                    }
                }
            }
        }

        for (int y = 0; y < localWorldArray.length; y++) {
            for (int x = 0; x < localWorldArray[y].length; x++) {
                if (localWorldArray[y][x] == 1) {
                    localWorldArrayColor[y][x] = new Color(139, 195, 74);
                } else if (localWorldArray[y][x] == 0) {
                    localWorldArrayColor[y][x] = new Color(33, 150, 243);
                }
            }
        }

        return localWorldArrayColor;
    }
    public int[][] createWorld(int width, int height) {
        return generateOctavedSimplexNoise(width, height);
    }

    private int[][] generateOctavedSimplexNoise(int width, int height) {
        double[][] totalNoise = new double[width][height];
        double layerFrequency = 1;
        double layerWeight = 1;
        double weightSum = 0;

        // Summing up all octaves, the whole expression makes up a weighted average
        // computation where the noise with the lowest frequencies have the least effect

        for (int octave = 0; octave < 2; octave++) {
            // Calculate single layer/octave of simplex noise, then add it to total noise
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    totalNoise[x][y] += SimplexNoise.noise(x * layerFrequency, y * layerFrequency) * layerWeight;
                }
            }

            // Increase variables with each incrementing octave
            layerFrequency *= 2;
            weightSum += layerWeight;
            layerWeight *= ROUGHNESS;

        }
        return totalNoise;
    }
    private void calculateValuesForScreen() {
        System.out.println(GetTime() + ": Start - calculateValuesForScreen()");
        tileSize = (int) Math.floor(695 / this.world.GetWorldArray().length);
        this.worldArray = world.GetWorldArray();
    }
    private void creatingWorldArray() {
        System.out.println(GetTime() + ": Start - calculateValuesForScreen()");
//        create: true - auto generate world array, 100x100
//        print: true - print result
        world = new World(false, false);
//        print: true - print result
        world.Create(100, 100, false);
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