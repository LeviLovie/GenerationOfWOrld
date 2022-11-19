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
    private Color[][] DayNightLandWater() {
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
    private Color[][] PerlinNoiseLandWater() {
        System.out.println(GetTime() + ": Start - PerlinNoiseLandWater()");

        BilinearInterpolation bilinearInterpolation = new BilinearInterpolation();

        Color[][] localWorldArrayColor = this.world.GetWorldArray();
        int[][] localWorldArray = new int[localWorldArrayColor.length][localWorldArrayColor[0].length];

        BufferedImage noise10 = new BufferedImage(
                localWorldArray[0].length, localWorldArray.length, BufferedImage.TYPE_INT_RGB
        );
        BufferedImage noise5 = new BufferedImage(
                localWorldArray[0].length, localWorldArray.length, BufferedImage.TYPE_INT_RGB
        );
        BufferedImage noise2 = new BufferedImage(
                localWorldArray[0].length, localWorldArray.length, BufferedImage.TYPE_INT_RGB
        );
        BufferedImage noise1 = new BufferedImage(
                localWorldArray[0].length, localWorldArray.length, BufferedImage.TYPE_INT_RGB
        );
        BufferedImage noiseResult = new BufferedImage(
                localWorldArray[0].length, localWorldArray.length, BufferedImage.TYPE_INT_RGB
        );

        int size;
        size = 10;
        for (int y = 0; y < Math.floor(localWorldArray.length / size); y++) {
            for (int x = 0; x < Math.floor(localWorldArray[y].length / size); x++) {
                for (int y10 = 0; y10 < Math.floor(localWorldArray.length / size); y10++) {
                    for (int x10 = 0; x10 < Math.floor(localWorldArray[y].length / size); x10++) {
                        int rand = this.world.rand(0, 255);
                        try {
                            noise10.setRGB(x * size + x10, y * size + y10, rand);
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
        size = 5;
        for (int y = 0; y < Math.floor(localWorldArray.length / size); y++) {
            for (int x = 0; x < Math.floor(localWorldArray[y].length / size); x++) {
                for (int y5 = 0; y5 < Math.floor(localWorldArray.length / size); y5++) {
                    for (int x5 = 0; x5 < Math.floor(localWorldArray[y].length / size); x5++) {
                        int rand = this.world.rand(0, 255);
                        try {
                            noise5.setRGB(x * size + x5, y * size + y5, rand);
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
//        size = 2;
//        for (int y = 0; y < Math.floor(localWorldArray.length / size); y++) {
//            for (int x = 0; x < Math.floor(localWorldArray[y].length / size); x++) {
//                for (int y2 = 0; y2 < Math.floor(localWorldArray.length / size); y2++) {
//                    for (int x2 = 0; x2 < Math.floor(localWorldArray[y].length / size); x2++) {
//                        int rand = this.world.rand(0, 255);
//                        try {
//                            noise2.setRGB(x * size + x2, y * size + y2, rand);
//                        } catch (Exception ignored) {}
//                    }
//                }
//            }
//        }
//        size = 1;
//        for (int y = 0; y < Math.floor(localWorldArray.length / size); y++) {
//            for (int x = 0; x < Math.floor(localWorldArray[y].length / size); x++) {
//                for (int y1 = 0; y1 < Math.floor(localWorldArray.length / size); y1++) {
//                    for (int x1 = 0; x1 < Math.floor(localWorldArray[y].length / size); x1++) {
//                        int rand = this.world.rand(0, 255);
//                        try {
//                            noise1.setRGB(x * size + x1, y * size + y1, rand);
//                        } catch (Exception ignored) {}
//                    }
//                }
//            }
//        }

//        noise10 = bilinearInterpolation.Interpolation(noise10);
//        noise5 = bilinearInterpolation.Interpolation(noise5);
//        noise2 = bilinearInterpolation.Interpolation(noise2);
//        noise1 = bilinearInterpolation.Interpolation(noise1);

        for (int y = 0; y < noiseResult.getHeight(); y++) {
            for (int x = 0; x < noiseResult.getWidth(); x++) {
                int noise10Color = noise10.getRGB(x, y) / 4;
                int noise5Color = noise5.getRGB(x, y) / 4;
//                int noise2Color = noise2.getRGB(x, y) / 4;
//                int noise1Color = noise1.getRGB(x, y) / 4;

//                noiseResult.setRGB(x, y, (noise10Color + noise5Color + noise2Color + noise1Color));
                noiseResult.setRGB(x, y, (noise10Color + noise5Color));
            }
        }

        for (int y = 0; y < localWorldArray.length; y++) {
            for (int x = 0; x < localWorldArray[y].length; x++) {
//                if (noiseResult.getRGB(x, y) > 0.5) {
//                    localWorldArrayColor[y][x] = new Color(139, 195, 74);
//                } else if (noiseResult.getRGB(x, y) < 0.5) {
//                    localWorldArrayColor[y][x] = new Color(33, 150, 243);
//                }
                localWorldArrayColor[y][x] = new Color(noiseResult.getRGB(x,y));
            }
        }

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

class BilinearInterpolation {
    /* gets the 'n'th byte of a 4-byte integer */
    private static int get(int self, int n) {
        return (self >> (n * 8)) & 0xFF;
    }

    private static float lerp(float s, float e, float t) {
        return s + (e - s) * t;
    }

    private static float blerp(final Float c00, float c10, float c01, float c11, float tx, float ty) {
        return lerp(lerp(c00, c10, tx), lerp(c01, c11, tx), ty);
    }

    private static BufferedImage scale(BufferedImage self, float scaleX, float scaleY) {
        int newWidth = (int) (self.getWidth() * scaleX);
        int newHeight = (int) (self.getHeight() * scaleY);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, self.getType());
        for (int x = 0; x < newWidth; ++x) {
            for (int y = 0; y < newHeight; ++y) {
                float gx = ((float) x) / newWidth * (self.getWidth() - 1);
                float gy = ((float) y) / newHeight * (self.getHeight() - 1);
                int gxi = (int) gx;
                int gyi = (int) gy;
                int rgb = 0;
                int c00 = self.getRGB(gxi, gyi);
                int c10 = self.getRGB(gxi + 1, gyi);
                int c01 = self.getRGB(gxi, gyi + 1);
                int c11 = self.getRGB(gxi + 1, gyi + 1);
                for (int i = 0; i <= 2; ++i) {
                    float b00 = get(c00, i);
                    float b10 = get(c10, i);
                    float b01 = get(c01, i);
                    float b11 = get(c11, i);
                    int ble = ((int) blerp(b00, b10, b01, b11, gx - gxi, gy - gyi)) << (8 * i);
                    rgb = rgb | ble;
                }
                newImage.setRGB(x, y, rgb);
            }
        }
        return newImage;
    }
    public BufferedImage Interpolation(BufferedImage source) {
        return scale(source, 1.0f, 1.0f);
    }
}