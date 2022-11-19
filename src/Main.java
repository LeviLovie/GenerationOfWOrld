import javax.swing.*;

public class Main extends JFrame {
    private JFrame frame;
    public Main() {
        frame = new JFrame();
        frame.setTitle("Generating of World");
        frame.setSize(1280, 720);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        System.out.println("App started");
        new Main();
    }
}