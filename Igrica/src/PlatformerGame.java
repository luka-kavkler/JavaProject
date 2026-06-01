import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

public class PlatformerGame {
    
    private JFrame gameFrame;
    private JPanel gamePanel;

    public PlatformerGame() {
        // Initialize your game window
        gameFrame = new JFrame("My Platformer Game");
        gameFrame.setSize(new Dimension(1024, 768));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        gamePanel = new JPanel();
        Color skyBlue = new Color(135, 206, 235);
        gamePanel.setBackground(skyBlue); 
        
        gameFrame.add(gamePanel);
    }
    
    public void start() {
        // Show the game window
        gameFrame.setVisible(true);
        
        // TODO: Start your game loop (gravity, jumping, collision detection, etc.)
    }
}