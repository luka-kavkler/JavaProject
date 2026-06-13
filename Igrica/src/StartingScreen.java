import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StartingScreen extends JFrame {
	static final int RADIUS = 1; // polmer kvadratov
	public final static Color DANGER_RED = new Color(255, 14, 14);
	public final Color D_GRAY = new Color(126,74,75);
	public JButton button;
	public JPanel panel;
	public volatile boolean menuRunning = true;
	 
	List<Square> squares = new ArrayList<Square>();
	public StartingScreen(String buttonText) {
		super();
		setTitle("Mario 64");
		setSize(new Dimension(1024, 768));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		
		panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				for (Square square : squares) {
					g.setColor(DANGER_RED);
					//g.fillOval((int)(square.x * getWidth() - square.radius),(int)(square.y * getHeight() - square.radius), square.radius * 2, square.radius * 2);
					g.drawRect((int)(square.x * getWidth()) - square.waveRadius, (int)(square.y * getHeight()) - square.waveRadius, square.waveRadius * 2, square.waveRadius * 2);
				}
			}
		};
		panel.setBackground(Color.BLACK);
		panel.setLayout(new GridBagLayout());
		panel.setFocusable(true);
		
		
		
		
		
		panel.addMouseListener(new MouseAdapter() { 
		    
			@Override
			public void mouseClicked(MouseEvent event) {
				squares.clear();
		    	  }
			});
		
		
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				squares.clear();
			};
		}
		);
		
		button = new JButton(buttonText);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);

        
        
        button.setFont(new Font("Sans Serif", Font.PLAIN, 14));
		button.setPreferredSize(new Dimension(130, 40));
		button.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
                menuRunning = false; 
                
                //Hide and destroy the starting screen window
                dispose(); 
                
                PlatformerGame myGame = new PlatformerGame();
                myGame.start();
		    }
		});
		button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);

            }
            @Override
            public void mouseExited(MouseEvent e) {
            	button.setBackground(Color.DARK_GRAY);
                button.setForeground(Color.WHITE);
            }
        });
		
		
		add(panel);
		button.setFocusable(false);
		panel.add(button);
		
		
	}
	public void startAnimation() {
	    Thread animationThread = new Thread(new Runnable() {
	        @Override
	        public void run() {
	            int i = 0;
	            while (menuRunning) {
	                synchronized(squares) {
	                    if (squares.size() > 120) {
	                        squares.remove(0);
	                    }
	                    
	                    if (i % 2 == 0) {
	                        squares.add(new Square(Math.random(), Math.random(), RADIUS));
	                    }
	                    
	                    for (Square square : squares) {
	                        square.waveRadius++;
	                    }
	                }
	                
	                repaint();
	                
	                try {
	                    Thread.sleep(35);
	                } catch (InterruptedException e) {
	                    Thread.currentThread().interrupt();
	                    break;
	                }
	                i++;
	            }
	        }
	    });
	    animationThread.start();
	}
	public static void main(String[] args) throws InterruptedException {
		StartingScreen GUI = new StartingScreen("Start");
        GUI.setVisible(true);
        GUI.startAnimation();
	}
	}

class Square {
	
	double x;
	double y;
	int radius;
	int waveRadius;
	
	public Square(double x, double y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.waveRadius = radius;
		}
}
	

