import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlatformerGame {
    
    private JFrame gameFrame;
    private JPanel gamePanel;
    private final Color BACKGROUND_GREY = new Color(40,50,50);
    
    // Game Entities
    private Player player;
    private Enemy enemy;
    private Rectangle ground;
    private Sword sword;
    public ArrayList<Enemy> enemies; 
    
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
        sword = new Sword(player);
        
        int baseX = 800;
        int enemyMovementRange = 200;
        enemy = new Enemy(screenSize.width - baseX, screenSize.height - ground.height - 50, baseX, enemyMovementRange);
        
        
        enemies = new ArrayList<Enemy>();
        enemies.add(enemy);
        
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
        // Horizontal Player Movement
        if (moveLeft) 
            player.move(-7, 0);
        if (moveRight) 
            player.move(7, 0);
            
        // Enemy Horizontal Movement Logic
        for (Enemy enemy1 : enemies) {
            if (enemy1.directionRight)
                enemy1.move(5,0);
            else 
                enemy1.move(-5,0);
        }
        
        // Apply Gravity to Player
        player.velY += GRAVITY;
        player.move(0, (int) player.velY);
        
        // Collision Detection with Ground (Player)
        if (checkCollision(player.getBounds(), ground)) {
            if (player.velY > 0) {
                player.y = ground.y - player.height;
                player.velY = 0;
                player.jumping = false;
            }
        }
        
        
        ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();
        
        for (Enemy enemy1 : enemies) {
            enemy1.velY += GRAVITY;
            enemy1.move(0, (int) enemy1.velY);
            
            
            if (checkCollision(enemy1.getBounds(), ground)) {
                if (enemy1.velY > 0) {
                    enemy1.velY = 0;
                    enemy1.jumping = false;
                    enemy1.y = ground.y - enemy1.height;
                }
            }
      
            // Collision detection enemy touching player
            if (checkCollision(enemy1.getBounds(), player.getBounds())){
                // TODO: One hitpoint restart logic goes here
            }
        
            // Collision Detection Sword hitting Enemy
            if (player.attacking) {
                if (checkCollision(enemy1.getBounds(), sword.getBounds())) {
                    enemiesToRemove.add(enemy1);    
                }
            }
        }
        
        // Safe batch removal execution at the end of the frame step
        enemies.removeAll(enemiesToRemove);
    }
    
    private void render(Graphics g) {
        // Draw the Floor
        g.setColor(Color.BLACK);
        g.fillRect(ground.x, ground.y, ground.width, ground.height);
        
        // Draw the Player
        g.setColor(new Color(255, 14, 14)); // Uses standard Color matching StartingScreen.DANGER_RED
        g.fillRect(player.x, player.y, player.width, player.height);
        
        // FIX 4: Loop through and dynamically render all current elements in the enemy collection
        g.setColor(Color.GRAY);
        for (Enemy enemy1 : enemies) {
            g.fillRect(enemy1.x, enemy1.y, enemy1.width, enemy1.height);
        }
        
        // Draw sword
        if (player.attacking) {    
            g.setColor(Color.WHITE);
            if (player.directionRight)
                g.fillRect(player.x + player.width, player.y + player.height/2, sword.width, sword.height);
            else
                g.fillRect(player.x - sword.width, player.y + player.height/2, sword.width, sword.height);
        }    
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
                
                // Jump condition
                if ((key == KeyEvent.VK_W || key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) && !player.jumping) {
                    player.velY = JUMP_STRENGTH;
                    player.jumping = true;
                }
                    
                // Sword attack activation trigger
                if (key == KeyEvent.VK_Q && !player.attacking) { 
                    player.attacking = true;
                    
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            player.attacking = false;
                        }
                    };
                    timer.schedule(task, 600);    
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
    
    public boolean checkCollision(Rectangle obj1, Rectangle obj2) {
        return obj1.intersects(obj2);
    }
    
    public class Sword {
        public int width, height;
        private Player playerRef;
        
        public Sword(Player player1) {
            this.playerRef = player1;
            width = 90;
            height = 5;
        }
        
        public Rectangle getBounds() {
            if (playerRef.directionRight)    
                return new Rectangle(playerRef.x + playerRef.width, playerRef.y + playerRef.height/2, width, height);
            else
                return new Rectangle(playerRef.x - width, playerRef.y + playerRef.height/2, width, height);
        }
    }
    
    public class Player {
        public int x, y;
        public int width, height;
        boolean jumping;
        double velY;
        boolean directionRight;
        private boolean attacking;

        public Player(int startX, int startY) {
            x = startX;
            y = startY;
            directionRight = true; 
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
    
    public class Enemy extends Player {
        public int baseX;
        public int movementRange;
        
        public Enemy(int startX, int startY, int homeX, int enemyMovementRange) {
            
            super(startX, startY);
            baseX = homeX;
            movementRange = enemyMovementRange;
        }
        
        @Override
        public void move(int deltaX, int deltaY) {    
            if (x > baseX + movementRange) {
                directionRight = false;
            }
            else if (x < baseX - movementRange) {
                directionRight = true;
            }
            x += deltaX;
            y += deltaY;
        }
    }
}