import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GenerateMethods {
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
    public void Print2Int(int[][] Array) {
        System.out.println(GetTime() + ": Start - World.Print2()");
        for (int[] ints : Array) {
            for (int x = 0; x < ints.length; x++) {
                if (x != Array.length - 1) {
                    System.out.print(ints[x] + " ");
                } else {
                    System.out.println(ints[x]);
                }
            }
        }
    }
    public Color[][] dayNightLandWater(Color[][] WorldArray, int[] rules) {
//        rules = new int[] {3, 6, 7, 8};
//        Print2(WorldArray);
        Color[][] localWorldArray = WorldArray;
        int[][] localWorldArrayInt = new int[WorldArray.length][WorldArray[0].length];

        for (int y = 0; y < WorldArray.length; y++) {
            for (int x = 0; x < WorldArray[0].length; x++) {
                if (WorldArray[y][x].getRGB() == new Color(0, 0, 0).getRGB()) {
                    localWorldArrayInt[y][x] = 0;
//                    System.out.println(0);
                } else if (WorldArray[y][x].getRGB() == new Color(255, 255, 255).getRGB()) {
                    localWorldArrayInt[y][x] = 1;
//                    System.out.println(1);
                }
            }
        }
//        Print2Int(localWorldArrayInt);

//        System.out.println(Arrays.toString(localWorldArrayInt[0]));

        int tile;
        int night;
        int day;
        int height;
        int width;

        for (int t2 = 0; t2 < (localWorldArrayInt.length * localWorldArrayInt[0].length) / 2; t2++) {
            height = rand(0, localWorldArrayInt.length);
            width = rand(0, localWorldArrayInt[0].length);

            tile = localWorldArrayInt[height][width];
            night = 0;
            day = 0;
            if (tile == 0) {
                try {
                    if (localWorldArrayInt[height - 1][width - 1] == 1) {day++;}
                    if (localWorldArrayInt[height - 1][width] == 1) {day++;}
                    if (localWorldArrayInt[height - 1][width + 1] == 1) {day++;}

                    if (localWorldArrayInt[height][width - 1] == 1) {day++;}
                    if (localWorldArrayInt[height][width] == 1) {day++;}
                    if (localWorldArrayInt[height][width + 1] == 1) {day++;}

                    if (localWorldArrayInt[height + 1][width] == 1) {day++;}
                    if (localWorldArrayInt[height + 1][width] == 1) {day++;}
                    if (localWorldArrayInt[height + 1][width + 1] == 1) {day++;}
                } catch (Exception ignored) {}
            } else if (tile == 1) {
                try {
                    if (localWorldArrayInt[height - 1][width - 1] == 0) {night++;}
                    if (localWorldArrayInt[height - 1][width] == 0) {night++;}
                    if (localWorldArrayInt[height - 1][width + 1] == 0) {night++;}

                    if (localWorldArrayInt[height][width - 1] == 0) {night++;}
                    if (localWorldArrayInt[height][width] == 0) {night++;}
                    if (localWorldArrayInt[height][width + 1] == 0) {night++;}

                    if (localWorldArrayInt[height + 1][width] == 0) {night++;}
                    if (localWorldArrayInt[height + 1][width] == 0) {night++;}
                    if (localWorldArrayInt[height + 1][width + 1] == 0) {night++;}
                } catch (Exception ignored) {}
            }
            if (tile == 1) {
//                System.out.println(night + " - " + rules[0]);
                for (int i = 0; i < rules.length; i++) {
                    if (night == rules[i]) {
                        localWorldArrayInt[height][width] = 0;
                    }
                }
            } else if (tile == 0) {
                for (int i = 0; i < rules.length; i++) {
                    if (day == rules[i]) {
                        localWorldArrayInt[height][width] = 1;
                    }
                }
            }
        }

        for (int y = 0; y < localWorldArray.length; y++) {
            for (int x = 0; x < localWorldArray[y].length; x++) {
                if (localWorldArrayInt[y][x] == 1) {
                    localWorldArray[y][x] = new Color(255, 255, 255);
                } else if (localWorldArrayInt[y][x] == 0) {
                    localWorldArray[y][x] = new Color(0, 0, 0);
                }
            }
        }

        return localWorldArray;
    }
    public BufferedImage PerlinNoiseLandWater(Color[][] WorldArray) throws IOException {
        System.out.println(GetTime() + ": Start - PerlinNoiseLandWater()");
        BufferedImage image = new BufferedImage(WorldArray.length, WorldArray[0].length, BufferedImage.TYPE_INT_RGB);
        FractalNoise fractalNoise;
        if (WorldArray.length < 101) {
            fractalNoise = new FractalNoise(16,
                    new Random(image.getWidth(), 100000), 4);
        } else if (WorldArray.length < 151) {
            fractalNoise = new FractalNoise(32,
                    new Random(image.getWidth(), 100000), 5);
        } else if (WorldArray.length < 351) {
            fractalNoise = new FractalNoise(64,
                    new Random(image.getWidth(), 100000), 6);
        } else if (WorldArray.length < 501) {
            fractalNoise = new FractalNoise(128,
                    new Random(image.getWidth(), 100000), 7);
        } else if (WorldArray.length < 720) {
            fractalNoise = new FractalNoise(128,
                    new Random(image.getWidth(), 100000), 7);
        } else {
            fractalNoise = new FractalNoise(512,
                    new Random(image.getWidth(), 100000), 9);
        }
//        BufferedImage image = new BufferedImage(1028, 720, BufferedImage.TYPE_INT_RGB);

        int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        int pixelIndex = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int value = 0xff & (int)(fractalNoise.getValue(j, i) * 255);
                pixels[pixelIndex++] = 0xff000000 | value << 16 | value << 8 | value;
            }
        }

        return image;
    }
    private static String GetTime() {
        return String.valueOf(LocalDateTime.now());
    }
    public int rand(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }
}

class FractalNoise {
    int baseScale;
    Random random;
    ArrayList<Noise> octaves = new ArrayList<>();

    public FractalNoise(int baseScale, Random random, int octavesCount) {
        this.baseScale = baseScale;
        this.random = random;

        int scale = baseScale;
        for (int i = 0; i < octavesCount; i++) {
            octaves.add(new Noise(scale, random));
            scale = scale / 2;
        }
    }
    public float getValue(int x, int y) {
        float sum = 0;
        float fraction = 0.5f;

        for(Noise noise: octaves) {
            sum += noise.getValue(x, y) * fraction;
            fraction *= 0.5;
        }
        return sum;
    }
}
class Random {
    int[] table;
    int lineWidth;

    public Random(int lineWidth, int size) {
        table = new int[size];
        for (int i = 0; i < size; i++) {
            table[i] = Math.random() >= 0.5 ? 1 : 0;
        }
        this.lineWidth = lineWidth;
    }
    public int getRandomValue(int x, int y) {
        return table[(x + y * lineWidth) % table.length];
    }
}
class Noise {
    int scale;
    Random random;

    public Noise(int scale, Random random) {
        this.scale = scale;
        this.random = random;
    }
    public float getValue(int x, int y) {
        int xgrid;
        int ygrid;
        try {
            xgrid = x / scale;
            ygrid = y / scale;
        } catch (Exception ignored) {
            xgrid = x;
            ygrid = y;
        }
        int xgridNext = xgrid + 1;
        int ygridNext = ygrid + 1;

        int xStart = xgrid * scale;
        int xEnd = xgridNext * scale;
        int yStart = ygrid * scale;
        int yEnd = ygridNext * scale;

        float value12 = random.getRandomValue(xgrid, ygrid);
        float value22 = random.getRandomValue(xgridNext, ygrid);
        float value21 = random.getRandomValue(xgridNext, ygridNext);
        float value11 = random.getRandomValue(xgrid, ygridNext);

        float w12 = ((float) (xEnd - x) * (yEnd - y)) / ((xEnd - xStart) * (yEnd - yStart));
        float w22 = ((float) (x - xStart) * (yEnd - y)) / ((xEnd - xStart) * (yEnd - yStart));
        float w21 = ((float) (x - xStart) * (y - yStart)) / ((xEnd - xStart) * (yEnd - yStart));
        float w11 = ((float) (xEnd - x) * (y - yStart)) / ((xEnd - xStart) * (yEnd - yStart));

        return value11 * w11 + value12 * w12 + value21 * w21 + value22 * w22;
    }
}