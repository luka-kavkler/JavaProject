import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlatformerGame {
    
    private JFrame gameFrame;
    private JPanel gamePanel;
    private final Color BACKGROUND_GREY = new Color(40,50,50);
    
    // Game Entities
    private Player player;
    private Rectangle ground;
    
    // Movement Flags 
    private boolean moveLeft = false;
    private boolean moveRight = false;
    
    // Physics constants
    private final double GRAVITY = 0.8;
    private final int JUMP_STRENGTH = -20;

    public PlatformerGame() {
    	
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        player = new Player(100, 400);
        ground = new Rectangle(0, screenSize.height - 151, screenSize.width, 150); 
        
        
        
        // Initialize your game window
        gameFrame = new JFrame("Mario 10");
        gameFrame.setSize(screenSize);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        gamePanel = new JPanel() {
        	@Override 
        	protected void paintComponent(Graphics g) {
        		super.paintComponent(g);
        		render(g);
        	}
        };
        
        
        
        gamePanel.setBackground(BACKGROUND_GREY); 
        gamePanel.setFocusable(true);
        
        setupControls();
        gameFrame.add(gamePanel);
        gameFrame.setVisible(true);
        gameFrame.setExtendedState(gameFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        
    }
    
    public void start() {
        // Show the game window
        gameFrame.setVisible(true);
        gamePanel.requestFocusInWindow();
        Thread gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                gameLoop();
            }
        });
        gameThread.start();
        }
    
    public void gameLoop() {
        while (true) {
            update();  // Update game state
            gamePanel.repaint();
            
            try {
                Thread.sleep(16);  // Roughly 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void update() {
        // Horizontal Movement
        if (moveLeft) 
        	player.move(-7, 0);
        if (moveRight) 
        	player.move(7, 0);
        
        // Apply Gravity
        player.velY += GRAVITY;
        player.move(0, (int) player.velY);
        
        // Collision Detection with Ground
        if (checkCollision(player.getBounds(), ground)) {
            // If falling down into the ground, snap to top of ground
            if (player.velY > 0) {
                player.y = ground.y - player.height;
                player.velY = 0;
                player.jumping = false;
            }
        }
    }
    
    
    private void render(Graphics g) {
        // Draw the Floor
        g.setColor(Color.BLACK);
        g.fillRect(ground.x, ground.y, ground.width, ground.height);
        
        // Draw the Player
        g.setColor(StartingScreen.DANGER_RED);
        g.fillRect(player.x, player.y, player.width, player.height);
    }
    
    
    private void setupControls() {
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) 
                	moveLeft = true;
                if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) 
                	moveRight = true;
                
                // Jump condition: Must be pressing Jump key AND not already jumping
                if ((key == KeyEvent.VK_W || key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) && !player.jumping) {
                    player.velY = JUMP_STRENGTH;
                    player.jumping = true;
                    
                //meč
                if (key == KeyEvent.VK_Q) 
                	player.attacking = true;
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) 
                	moveLeft = false;
                if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) 
                	moveRight = false;
            }
        });
    }
    
    public boolean checkCollision(Rectangle player, Rectangle platform) {
        return player.intersects(platform);
    }
    
    
    
    public class Sword {
    	private int x, y;
        final private int width, height;
        public Sword(int x1, int y1, boolean directionRight) {
        	x = x1;
        	if (directionRight)
        		width = 100; // zamah v desno
        	else 
        		width = -100; // zamah v levo
        	height = 20;
        	//if (player.directionRight)
        	//	x2 = x1 + width;
        	//else
        	//	x2 = x1 - width;
        	y = y1;
        	
        }
    }
    
    public class Player {
        private int x, y;
        private int width, height;
        private boolean jumping;
        private double velY;
        private boolean directionRight;
        private boolean attacking;

        public Player(int startX, int startY) {
            x = startX;
            y = startY;
            directionRight = true; // doloca v katero smer bo udarjal meč
            width = 50;
            height = 50;
            jumping = false;
            attacking = false;
            velY = 0;
        }

        public void move(int deltaX, int deltaY) {
            if (deltaX > 0)
            	directionRight = true;
            if (deltaX < 0)
            	directionRight = false;
        	x += deltaX;
            y += deltaY;
        }
        
        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }
}