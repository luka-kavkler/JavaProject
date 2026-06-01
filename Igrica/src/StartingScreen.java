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
	public final Color DANGER_RED = new Color(255, 14, 14);
	public final Color D_GRAY = new Color(126,74,75);
	public JButton button;
	public JPanel panel;
	public volatile boolean menuRunning = true;
	 
	List<Square> squares = new ArrayList<Square>();
	public StartingScreen() {
		super();
		setTitle("Ime igre");
		setSize(new Dimension(1024, 768));
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
		
		button = new JButton("Start");
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);


        button.setFont(new Font("Sans Serif", Font.PLAIN, 14));
		button.setPreferredSize(new Dimension(96, 40));
		button.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	// 1. Stop the background animation loop
                menuRunning = false; 
                
                // 2. Hide and destroy the starting screen window
                dispose(); 
                
                // 3. Launch your actual platformer game
                // (Assuming you have a class called PlatformerGame)
                //PlatformerGame myGame = new PlatformerGame();
                //myGame.start();
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
		panel.add(button);
		
		
	}

	public static void main(String[] args) throws InterruptedException {
		StartingScreen GUI = new StartingScreen();
		GUI.setVisible(true);
		
		int i = 0;
		while (GUI.menuRunning) {
			
			if (GUI.squares.size()>120)
				GUI.squares.remove(0);
			
			if (i%2 == 0) {
				int radius = RADIUS;
  		  		double x = (double)Math.random(); //Želimo realno število med 0 in 1
  		  		double y = (double)Math.random();
  		 	
  		
  		  		Square newSquare = new Square(x,y,radius);
  		  		GUI.squares.add(newSquare);
			};
			
			for (Square square : GUI.squares) {
					square.waveRadius++;
			};
			GUI.repaint();
			Thread.sleep(35);
			i++;
		}
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
	

