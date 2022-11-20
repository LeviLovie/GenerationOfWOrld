import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class GenerateMethods {
    public Color[][] dayNightLandWater(Color[][] WorldArray) {
        Color[][] localWorldArray = WorldArray;

        Color tile;
        int days;
        int nightes;
        int height;
        int width;

        for (int t2 = 0; t2 < (localWorldArray.length * localWorldArray[0].length) / 2; t2++) {
            height = rand(0, localWorldArray.length);
            width = rand(0, localWorldArray[0].length);

            tile = localWorldArray[height][width];
            days = 0;
            nightes = 0;
//            if (Objects.equals(tile, new Color(0, 0, 0))) {
//                for (int i = -1; i < 1; i++) {
//                    for (int j = -1; j < 1; j++) {
//                        try {
//                            if (Objects.equals(
//                                    localWorldArray[height + i][width + j], new Color(255, 255, 255)
//                            )) {days++;}
//                        } catch (Exception ignored) {}
//                    }
//                }
//            } else if (Objects.equals(tile, new Color(255, 255, 255))) {
//                for (int i = -1; i < 1; i++) {
//                    for (int j = -1; j < 1; j++) {
//                        try {
//                            if (Objects.equals(localWorldArray[height + i][width + j], new Color(0, 0, 0)
//                            )) {nightes++;}
//                        } catch (Exception ignored) {}
//                    }
//                }
//            }
            if (Objects.equals(tile, new Color(0, 0, 0))) {
                try {
                    if (Objects.equals(localWorldArray[height - 1][width - 1], new Color(255, 255, 255))) {days++;}
                    if (Objects.equals(localWorldArray[height - 1][width], new Color(255, 255, 255))) {days++;}
                    if (Objects.equals(localWorldArray[height - 1][width + 1], new Color(255, 255, 255))) {days++;}

                    if (Objects.equals(localWorldArray[height][width - 1], new Color(255, 255, 255))) {days++;}
                    if (Objects.equals(localWorldArray[height][width], new Color(255, 255, 255))) {days++;}
                    if (Objects.equals(localWorldArray[height][width + 1], new Color(255, 255, 255))) {days++;}

                    if (Objects.equals(localWorldArray[height + 1][width], new Color(255, 255, 255))) {days++;}
                    if (Objects.equals(localWorldArray[height + 1][width], new Color(255, 255, 255))) {days++;}
                    if (Objects.equals(localWorldArray[height + 1][width + 1], new Color(255, 255, 255))) {days++;}

                    if (days == 3 || days == 6 || days == 7 || days == 8) {
                        localWorldArray[height][width] = new Color(0, 0, 0);
                    } else {
                        localWorldArray[height][width] = new Color(255, 255, 255);
                    }
                } catch (Exception ignored) {}
            } else if (Objects.equals(tile, new Color(255, 255, 255))) {
                try {
                    if (Objects.equals(localWorldArray[height - 1][width - 1], new Color(0, 0, 0))) {nightes++;}
                    if (Objects.equals(localWorldArray[height - 1][width], new Color(0, 0, 0))) {nightes++;}
                    if (Objects.equals(localWorldArray[height - 1][width + 1], new Color(0, 0, 0))) {nightes++;}

                    if (Objects.equals(localWorldArray[height][width - 1], new Color(0, 0, 0))) {nightes++;}
                    if (Objects.equals(localWorldArray[height][width], new Color(0, 0, 0))) {nightes++;}
                    if (Objects.equals(localWorldArray[height][width + 1], new Color(0, 0, 0))) {nightes++;}

                    if (Objects.equals(localWorldArray[height + 1][width], new Color(0, 0, 0))) {nightes++;}
                    if (Objects.equals(localWorldArray[height + 1][width], new Color(0, 0, 0))) {nightes++;}
                    if (Objects.equals(localWorldArray[height + 1][width + 1], new Color(0, 0, 0))) {nightes++;}

                    if (nightes == 3 || nightes == 6 || nightes == 7 || nightes == 8) {
                        localWorldArray[height][width] = new Color(0, 0, 0);
                    } else {
                        localWorldArray[height][width] = new Color(255, 255, 255);
                    }
                } catch (Exception e) {

                }
            }

            System.out.println("tile: " + tile + " days: " + days + " nightes: " + nightes);
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