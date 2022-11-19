import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
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
    private JPanel preferences;
    private void preferencesCreate() {
        System.out.println(GetTime() + ": Start - preferencesCreate()");
        this.preferences = new JPanel();
        this.preferences.setBounds(720, 0, 1280 - 720, 695);


        JPanel panelDayNight = new JPanel();
        this.preferences.add(panelDayNight);

        JButton generate = new JButton("Generate");
        generate.addActionListener(e -> {
            Color[][] array = landWater();
//            this.world.Print2(array);
            this.world.SetWorldArray(array);

            revalidate();
        });
        panelDayNight.add(generate);

//        JPanel panelWorldPreferences = new JPanel();
//        panelWorldPreferences.setVisible(false);
//        setLayout(null);
//        this.preferences.setBounds(720, 20, 1280 - 720, 695);
//        this.preferences.add(panelWorldPreferences);
//
//        JTextField panelWorldWidth = new JTextField(10);
//        panelWorldPreferences.add(panelWorldWidth);
//
//        JButton buttonWorldPreferences = new JButton("World");
//        buttonWorldPreferences.addActionListener(e -> {
//            panelWorldPreferences.setVisible(true);
//        });
//        preferences.add(buttonWorldPreferences);


        add(this.preferences, null);
    }
    private Color[][] landWater() {
        System.out.println(GetTime() + ": Start - LandWater()");
        System.out.println();
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

//        this.world.Print2(localWorldArray);

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
                    } catch (Exception e) {}
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
                    } catch (Exception e) {}
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
//        for (int t = 0; t < 100; t++) {
//            for (int y = 0; y < localWorldArray.length; y++) {
//                for (int x = 0; x < localWorldArray[y].length; x++) {
//                    tile = localWorldArray[y][x];
//                    friends = 0;
//                    animys = 0;
//                    if (tile == 0) {
//                        try {
//                            if (localWorldArray[y - 1][x - 1] == 1) {animys++;}
//                            if (localWorldArray[y - 1][x] == 1) {animys++;}
//                            if (localWorldArray[y - 1][x + 1] == 1) {animys++;}
//
//                            if (localWorldArray[y][x - 1] == 1) {animys++;}
//                            if (localWorldArray[y][x] == 1) {animys++;}
//                            if (localWorldArray[y][x + 1] == 1) {animys++;}
//
//                            if (localWorldArray[y + 1][x] == 1) {animys++;}
//                            if (localWorldArray[y + 1][x] == 1) {animys++;}
//                            if (localWorldArray[y + 1][x + 1] == 1) {animys++;}
//                        } catch (Exception e) {}
//                    } else if (tile == 1) {
//                        try {
//                            if (localWorldArray[y - 1][x - 1] == 0) {friends++;}
//                            if (localWorldArray[y - 1][x] == 0) {friends++;}
//                            if (localWorldArray[y - 1][x + 1] == 0) {friends++;}
//
//                            if (localWorldArray[y][x - 1] == 0) {friends++;}
//                            if (localWorldArray[y][x] == 0) {friends++;}
//                            if (localWorldArray[y][x + 1] == 0) {friends++;}
//
//                            if (localWorldArray[y + 1][x] == 0) {friends++;}
//                            if (localWorldArray[y + 1][x] == 0) {friends++;}
//                            if (localWorldArray[y + 1][x + 1] == 0) {friends++;}
//                        } catch (Exception e) {}
//                    }
//                    if (tile == 1) {
//                        if (friends == 3 || friends == 6 || friends == 7 || friends == 8) {
//                            localWorldArray[y][x] = 0;
//                        }
//                    } else if (tile == 0) {
//                        if (animys == 3 || animys == 6 || animys == 7 || animys == 8) {
//                            localWorldArray[y][x] = 1;
//                        }
//                    }
//                }
//            }
//        }

        for (int y = 0; y < localWorldArray.length; y++) {
            for (int x = 0; x < localWorldArray[y].length; x++) {
                if (localWorldArray[y][x] == 1) {
                    localWorldArrayColor[y][x] = new Color(139, 195, 74);
                } else if (localWorldArray[y][x] == 0) {
                    localWorldArrayColor[y][x] = new Color(33, 150, 243);
                }
            }
        }

//        this.world.Print2(localWorldArray);
        return localWorldArrayColor;
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

        int color = 0;
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
    public void Print2(int[][] array) {
        for (int x = 0; x < array[0].length; x++) {
            if (x != array.length - 1) {
                System.out.print(array[0][x] + " ");
            } else {
                System.out.println(array[0][x]);
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