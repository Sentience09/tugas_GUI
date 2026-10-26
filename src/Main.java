import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Habit_Frame frame = new Habit_Frame();
            frame.setVisible(true);
        });
    }
}