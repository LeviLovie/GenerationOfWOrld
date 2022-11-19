import javax.swing.*;
import java.time.LocalDateTime;

public class Main extends JFrame {
    public Main() {
        JFrame frame = new JFrame();
        frame.setTitle("Generating of World");
        frame.setSize(1280, 720);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        System.out.println(GetTime() + ": Created - frame");
        frame.add(new Window());
        frame.setVisible(true);
    }
    private static String GetTime() {
        return String.valueOf(LocalDateTime.now());
    }
    public static void main(String[] args) {
        System.out.println(GetTime() + ": Started - app");
        new Main();
    }
}