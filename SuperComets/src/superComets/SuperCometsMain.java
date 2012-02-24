//Andrew Bertrand
//Assignment 3 EC
//Main class
// 3/15/10

package superComets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;


import javax.swing.*;
import java.util.*;
import java.io.*;

// This class is primarily responsible for organizing the game of Comets
public class SuperCometsMain implements KeyListener, ActionListener
{
	// GUI Data
	private JFrame frame; // The window itself
	private Canvas playArea;  // The area where the game takes place
	
	private final int playWidth = 500; // The width of the play area (in pixels)
	private final int playHeight = 500; // The height of the play area (in pixels)
	
	// Game Data
	private Ship ship; // The ship in play
	private Vector<Shot> shots; // The shots fired by the player
	private Vector<Comet> comets; // The comets floating around
	
	private boolean shipDead; // Whether or not the ship has been blown up
	
	
	// Keyboard data
	// Indicates whether the player is currently holding the accelerate, decelerate
	//turn left, or turn right buttons, respectively
	private boolean accelerateHeld = false;
	private boolean turnLeftHeld = false;
	private boolean turnRightHeld = false;
	private boolean decelerateHeld = false;
	
	// Indicates whether the player struck the fire key
	private boolean firing = false;
	
	//'score' stores the score during the game, 'lives' tracks how many
	//lives the player has, 'livesAdded' tracks how many lives the player
	//has earned in order to increment how many points are necessary for the
	//next life, 'gaveOver' tracks whether or not the player has ended the game,
	//'level' tracks which level the player is on, 'timeSpawn' tracks how many
	//frames have gone by since the player died to know when to respawn them,
	//'timeShot' tracks how many frames since the player last shot to know when
	//they can shoot again, 'paused' tracks whether or not the game is paused,
	//'topNames' stores the names of the top scores and 'topScores' stores tho
	int score = 0;
	short lives = 3;
	int livesAdded = 0;
	boolean gameOver = false;
	int level = 1;
	int timeSpawn = 0;
	int timeShot = 20;
	boolean paused = false;
	String[] topNames = new String[5];
	int[] topScores = new int[5];
	
	
	
	
	// Set up the game and play!
	public SuperCometsMain()
	{
		// Get everything set up
		configureGUI();
		configureGameData();
		
		// Display the window so play can begin
		frame.setVisible(true);
		
		// Start the gameplay
		do
		{
			playGame();
			
			
		}while(gameOver == false);
		
			return;
	}
	
	
	//after a game, this will check if the player has gotten a new high score
	//and, if so, update the high scores
	public void updateScores()
	{
		//this loop sees where the new score fits in the high scores, and sets
		//its spot to 5 if it's not in the high scores
		int place = 5;
		for(int i = 0; i < 5; i++)
		{
			if(i == 0 && score > topScores[i])
				place = 0;
			else if(i == 4 && score > topScores[i])
				place = 4;
			else if(i != 4 && score <= topScores[i] && score > topScores[i + 1])
				place = i + 1;
			
		}
		
		//ends the loop if it's not a new high score
		if(place == 5)
			return;
		
		//if it's a new high score, this moves all the scores to their
		//appropriate places
		else
		{
			for(int i = 4; i > place; i--)
			{
				topScores[i] = topScores[i - 1];
				topNames[i] = topNames[i - 1];
			}
			topScores[place] = score;
			topNames[place] = getName();
		}
		
		//prints the new scores to a file, and just skips it if it can't
		//open the file
		try
		{
			PrintStream fout = new PrintStream(new File("highScores.txt"));
			for(int i = 0; i < 5; i++)
			{
				fout.println(topNames[i] + " " + topScores[i]);
			}
			fout.close();
		}
		catch(FileNotFoundException e)
		{
			
		}
		
		
	}
	
	//if the player has a new high score, this box will let them
	//enter their name
	public String getName()
	{
		return JOptionPane.showInputDialog("You have made a new high score!\n" +
				"Please type in your name:", "Enter Name Here");
		
	}
	
	//used because of buttons
	public void actionPerformed(ActionEvent e)
	{
		
	}
	
	// Set up the initial positions of all space objects
	private void configureGameData()
	{
		// Configure the play area size
		SpaceObject.playfieldWidth = playWidth;
		SpaceObject.playfieldHeight = playHeight;
		
		// Create the ship
		ship = new Ship(playWidth/2, playHeight/2, 0, 0);
		
		// Create the shot vector (initially, there shouldn't be any shots on the screen)
		shots = new Vector<Shot>();
		
		// Read the comets from comets.cfg
		comets = new Vector<Comet>();
		
	}
	
	// Set up the game window
	private void configureGUI()
	{
		// Create the window object
		frame = new JFrame("Comets");
		frame.setSize(playWidth+20, playHeight+35);
		frame.setResizable(false);
		
		// The program should end when the window is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set the window's layout manager
		frame.setLayout(new FlowLayout());
		
		// Create the play area
		playArea = new Canvas();
		playArea.setSize(playWidth, playHeight);
		playArea.setBackground(Color.BLACK);
		playArea.setFocusable(false);
		frame.add(playArea);
		
		// Make the frame listen to keystrokes
		frame.addKeyListener(this);
	}
	
	// The main game loop. This method coordinates everything that happens in
	// the game
	private void playGame()
	{
		//this section initially draws the lives, level, and score
		Graphics g = playArea.getGraphics();
		g.setColor(Color.WHITE);
		g.drawString("Score: 0", playWidth / 2, 20);
		drawLives(g, lives - 1);
		
		//this sets up a background of 100 randomly placed white circles
		//and draws them all to the screen
		Point2D[] starPos = new Point2D[100];
		int[] starRadii = new int[100];
		for(int i = 0; i < 100; i++)
		{
			starPos[i] = new Point2D.Double(Math.random() * playWidth, 
					Math.random() * playHeight);
			starRadii[i] = (int)(Math.random() * 3 + 1);
			g.drawOval((int)starPos[i].getX(), (int)starPos[i].getY(), 
					starRadii[i], starRadii[i]);
			
		}
		
		//this section opens the high scores file and retrieves the data
		//and if it can't it sets 'topNames' and 'topScores' to the presets
		try
		{	
			Scanner fin = new Scanner(new File("highScores.txt"));
			for(int i = 0; i < 5; i++)
			{
				topNames[i] = fin.next();
				while(!fin.hasNextInt())
					topNames[i] += " " + fin.next();
				topScores[i] = fin.nextInt();
			}
			fin.close();
		}
		catch(FileNotFoundException e)
		{
				topNames[0] = "The Angel Sanguinius";
				topScores[0] = 15000;
				topNames[1] = "Mewtwo";
				topScores[1] = 14000;
				topNames[2] = "Perfect Cell";
				topScores[2] = 12000;
				topNames[3] = "Eric Cartman";
				topScores[3] = 10000;
				topNames[4] = "Bender";
				topScores[4] = 8000;
		}
		
		//this section creates the initial random comets
		for(int i = 0; i <= level; i++)
		{
			double x, y;
			
			do
			{
				x = Math.random() * playWidth;
				y = Math.random() * playHeight;
				
			}while(Math.sqrt(Math.pow(Math.abs(x - playWidth/2), 2) + Math.pow(Math.abs(y - playHeight/2), 2)) < 80);
			
			comets.add(new LargeComet( x, y, Math.random() * 5 - 2, Math.random() * 5 - 2));
		}
		
		
		//this goes through each frame of the game
		while(gameOver == false)
		{
			
			//only goes through if the game isn't paused
			if(!paused)
			{
				// Measure the current time in an effort to keep up a consistent
				// frame rate
				long time = System.currentTimeMillis();
				
				//redraws the score, lives, stars, and level every frame
				g.setColor(Color.WHITE);
				g.drawString("Score: " + score, playWidth / 2, 20); 
				g.drawString("Extra Lives: ", 10, 20);
				g.drawString("Level: " + level, 3 * playWidth / 4, 20);
				drawLives(g, lives - 1);
				for(int i = 0; i < 100; i++)
					g.drawOval((int)starPos[i].getX(), (int)starPos[i].getY(), starRadii[i], starRadii[i]);
				
				//increments the frames since last shot and since the player
				//died, if necessary
				timeShot++;
				if(shipDead)
					timeSpawn++;
								
				
				// If the ship has been dead for more than 3 seconds, revive it
				if(shipDead && timeSpawn > 93)
				{
					//if the ship has more lives, this doesn't spawn them if the
					//comets are too close, and lets them know why
					if(cometAtSpawn() == true && timeSpawn < 1000 && lives != 1)
					{
						g.setColor(Color.WHITE);
						g.drawString("Waiting to spawn", playWidth / 2, playHeight / 2);
					}
					
					//if the comets aren't too close to spawn it does this
					else
					{
						//erase the "waiting to spawn" message
						g.setColor(Color.BLACK);
						g.drawString("Waiting to spawn", playWidth / 2, playHeight / 2);
						
						//this updates the lives and 'timeSpawn'
						drawLives(g, lives - 1);
						lives--;
						g.setColor(Color.WHITE);
						drawLives(g, lives - 1);
						g.setColor(Color.BLACK);
						timeSpawn = 0;
						
						//if they have no more lives, this erases the
						//screen, updates the scores, and sees if the
						//player wants to continue
						if(lives == 0)
						{
							g.setColor(Color.BLACK);
							for(int i = comets.size() - 1; i >= 0; i--)
							{
								drawComet(g, comets.elementAt(i));
								comets.remove(i);
							}
							for(int i = shots.size() - 1; i >= 0; i--)
							{
								drawSpaceObject(g, shots.elementAt(i));
								shots.remove(i);
							}
							g.drawString("Extra Lives: ", 10, 20);
							g.drawString("Level: " + level, 3 * playWidth / 4, 20);
							g.drawString("Score: " + score, playWidth / 2, 20);
							g.setColor(Color.WHITE);
							g.drawString("Game Over!", playWidth / 2, playHeight / 2);
							updateScores();
							gameOver = gameOverButton();
							
							//if the players wants to continue, this resets 
							//all their info and uses 'return' to start the
							//loop again
							if(gameOver == false)
							{
								
								score = 0;
								lives = 3;
								level = 1;
								livesAdded = 0;
								shipDead = false;
								ship = new Ship(playWidth / 2, playHeight / 2, 0, 0);
								g.setColor(Color.BLACK);
								g.drawString("Game Over!", playWidth / 2, playHeight / 2);
								return;
							}
							
							//just uses 'return' to end the game if they want
							else
							{
								return;
							}
							
						}
					
						//the ship is recreated
						shipDead = false;
						ship = new Ship(playWidth/2, playHeight/2, 0, 0);
					}
				}
				
				// Process game events, move all the objects floating around,
				// and update the display
				if(!shipDead)
					handleKeyEntries();
				handleCollisions();
				moveSpaceObjects();
				
				// Sleep until it's time to draw the next frame 
				// (i.e. 32 ms after this frame started processing)
				try
				{
					long delay = Math.max(0, 32-(System.currentTimeMillis()-time));
					
					Thread.sleep(delay);
				}
				catch(InterruptedException e)
				{
				}
				
				//if there are no more comets, this updates the displayed
				//info, erases the current shots, and creates the new comets
				//and waits one second to start the next level
				if(comets.isEmpty())
				{
					g.setColor(Color.BLACK);
					g.drawString("Level: " + level, 3 * playWidth / 4, 20);
					level++;
					drawShip(g, ship);
					
					for(int i = shots.size() - 1; i >= 0; i--)
					{
						
						this.drawSpaceObject(g, shots.elementAt(i));
						shots.remove(i);
					}
					g.setColor(Color.WHITE);
					g.drawString("Next Level: " + level, playWidth/2, playHeight/2);
					try
					{
						long delay = Math.max(0, 1000-(System.currentTimeMillis()-time));
						
						Thread.sleep(delay);
					}
					catch(InterruptedException e)
					{
					}
					g.setColor(Color.BLACK);
					
					g.drawString("Next Level: " + level, playWidth/2, playHeight/2);
					ship = new Ship(playWidth/2, playHeight/2, 0, 0);
					for(int i = 0; i <= level; i++)
					{
						double x, y;
						do
						{
							x = Math.random() * playWidth;
							y = Math.random() * playHeight;
						}while(Math.sqrt(Math.pow(Math.abs(x - playWidth/2), 2) + Math.pow(Math.abs(y - playHeight/2), 2)) < 80);
						comets.add(new LargeComet( x, y, Math.random() * 5 - 2 , Math.random() * 5 - 2));
					}
					
					//this creates a new background for each level
					for(int i = 0; i < 100; i++)
					{
						g.drawOval((int)starPos[i].getX(), (int) starPos[i].getY(), (int)starRadii[i], (int)starRadii[i]);
						starPos[i] = new Point2D.Double(Math.random() * playWidth, Math.random() * playHeight);
						starRadii[i] = (int)(Math.random() * 3 + 1);
					}
				}
			}
		}
	}

	//Checks if a comet is too close to the spawn point
	boolean cometAtSpawn()
	{
		for(int i = 0; i < comets.size(); i++)
		{
			Comet c = comets.elementAt(i);
			if(Math.sqrt(Math.pow(Math.abs(c.getXPosition() - playWidth/2), 2)
					+ Math.pow(Math.abs(c.getYPosition() - playHeight/2), 2)) < 80)
			return true;
		}
		return false;
	}
	
	//creates the button to let the player decide if they want to continue
	//and displays the high scores
	public boolean gameOverButton()
	{
		int n = JOptionPane.showConfirmDialog(frame, "Your score was " + score + "\n" +
				"High Scores:\n" +
				"1. " + topNames[0] + ": " + topScores[0] + "\n" + 
				"2. " + topNames[1] + ": " + topScores[1] + "\n" +
				"3. " + topNames[2] + ": " + topScores[2] + "\n" +
				"4. " + topNames[3] + ": " + topScores[3] + "\n" +
				"5. " + topNames[4] + ": " + topScores[4] + "\n" +
				"Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
		if(n == JOptionPane.NO_OPTION)
			return true;
		return false;
	}
	
	// Deal with objects hitting each other
	private void handleCollisions()
	{
		// Anything that is destroyed should be erased, so get ready
		// to erase stuff
		Graphics g = playArea.getGraphics();
		g.setColor(Color.BLACK);
		
		// Deal with shots blowing up comets
		for(int i = 0; i < shots.size(); i++)
		{
			Shot s = shots.elementAt(i);
			for(int j = 0; j < comets.size(); j++)
			{
				Comet c = comets.elementAt(j);
				
				// If a shot has hit a comet, destroy both the shot and comet
				if(s.overlapping(c))
				{
					// Erase the bullet
					shots.remove(i);
					i--;
					this.drawSpaceObject(g, s);
					
					// If the comet was actually destroyed, replace the comet
					// with the new comets it spawned (if any)
					Vector<Comet> newComets = c.explode();					
					
					if(newComets != null)
					{
						//this updates the score if the comet is destroyed
						g.drawString("Score: " + score, playWidth / 2, 20);
						if(c instanceof LargeComet)
							score += 400;
						else if(c instanceof MediumComet)
							score += 200;
						else
							score += 100;
						g.setColor(Color.WHITE);
						g.drawString("Score: " + score, playWidth / 2, 20);
						
						//adds a life if the player earned it
						if(score >= 20000 * (livesAdded + 1))
						{
							livesAdded++;
							if(lives < 6)
								lives++;
						}
						drawLives(g, lives - 1);
						
						//removes the comet if it was destroyed
						g.setColor(Color.BLACK);
						this.drawComet(g, c);
						comets.remove(j);
						j--;
						comets.addAll(newComets);		
					}
					break;
				}
				
			}		
			
		}
		
		
		
		// Deal with comets blowing up the ship
		if(!shipDead)
		{
			for(Comet c : comets)
			{
				// If the ship hit a comet, kill the ship 
				if(ship.overlapping(c))
				{
					shipDead = true;
					drawShip(g, ship);
				}
			}
		}
	}
	
	
	
	// Check which keys have been pressed and respond accordingly
	private void handleKeyEntries()
	{
		// Ship movement keys
		if(accelerateHeld)
			ship.accelerate();
	
		// Shooting the cannon
		if(firing && timeShot >= 20)
		{
			firing = false;
			shots.add(ship.fire());
			timeShot = 0;
			
		}
		
		if(decelerateHeld)
			ship.decelerate();
	}
	
	// Deal with moving all the objects that are floating around
	private void moveSpaceObjects()
	{
		Graphics g = playArea.getGraphics();
		
		// Handle the movements of all objects in the field
		if(!shipDead)
			updateShip(g);
		updateShots(g);
		updateComets(g);		
	}
	
	// Move all comets and draw them to the screen
	private void updateComets(Graphics g)
	{
		for(Comet c : comets)
		{
			// Erase the comet at its old position
			g.setColor(Color.BLACK);
			drawComet(g, c);
			
			// Move the comet to its new position
			c.move();
			
			// Draw it at its new position
			g.setColor(Color.CYAN);
			drawComet(g, c);
			
		}
	}
	
	// Move all shots and draw them to the screen
	private void updateShots(Graphics g)
	{
		
		for(int i = 0; i < shots.size(); i++)
		{
			Shot s = shots.elementAt(i);
			
			// Erase the shot at its old position
			g.setColor(Color.BLACK);
			drawSpaceObject(g, s);
			
			// Move the shot to its new position
			s.move();
			
			// Remove the shot if it's too old
			if(s.getAge() > 90)
			{
				shots.remove(i);
				i--;
			}
			// Otherwise, draw it at its new position
			else
			{
				g.setColor(Color.RED);
				drawSpaceObject(g, s);
			}		
		}
	}
	
	//draws all the line segments of each comet
	private void drawComet(Graphics g, Comet s)
	{
		for(int i = 0; i < 12; i++)
			g.drawLine((int)s.outline[i].getX(), (int)s.outline[i].getY(), 
					(int)s.outline[(i + 1) % 12].getX(), (int)s.outline[(i + 1) % 12].getY());
		
	}
	
	// Draws the space object s to the the specified graphics context
	private void drawSpaceObject(Graphics g, SpaceObject s)
	{
			
		// Figure out where the object should be drawn
			int radius = (int)s.getRadius();
			int xCenter = (int)s.getXPosition();
			int yCenter = (int)s.getYPosition();
		
			// Draw the object
			g.drawOval(xCenter - radius, yCenter - radius, radius*2, radius*2);
		
	}
	
	// Moves the ship and draws it at its new position
	private void updateShip(Graphics g)
	{
		// Erase the ship at its old position
		g.setColor(Color.BLACK);
		drawShip(g, ship);

		// Ship rotation must be handled between erasing the ship at its old position
		// and drawing it at its new position so that artifacts aren't left on the screen
		if(turnLeftHeld)
			ship.rotateLeft();
		if(turnRightHeld)
			ship.rotateRight();
		ship.move();
		
		// Draw the ship at its new position
		g.setColor(Color.WHITE);
		drawShip(g, ship);
	}
	
	//this draws each of the player's extra lives, one less than their
	//total lives, as a small version of the ship
	private void drawLives(Graphics g, int lives)
	{
		for(int i = 0; i < lives; i++)
		{
			g.drawLine(76 + 20 * i, 20, 80 + 20 * i, 12);
			g.drawLine(80 + 20 * i, 12, 84 + 20 * i, 20);
			g.drawLine(84 + 20 * i, 20, 80 + 20 * i, 17);
			g.drawLine(80 + 20 * i, 17, 76 + 20 * i, 20);
		}
	}
	
	// Draws this ship s to the specified graphics context
	private void drawShip(Graphics g, Ship s)
	{	
		//loops through the vertices to draw all the line segments
		for(int i = 0; i < 4; i++)
			g.drawLine((int)s.vertices[i].getX(), 
					(int)s.vertices[i].getY(), 
					(int)s.vertices[(i + 1) % 4].getX(), 
					(int)s.vertices[(i + 1) % 4].getY());
	}
		
	// Deals with keyboard keys being pressed
	public void keyPressed(KeyEvent key)
	{
		// Mark down which important keys have been pressed
		if(key.getKeyCode() == KeyEvent.VK_UP)
			this.accelerateHeld = true;
		if(key.getKeyCode() == KeyEvent.VK_LEFT)
			this.turnLeftHeld = true;
		if(key.getKeyCode() == KeyEvent.VK_RIGHT)
			this.turnRightHeld = true;
		if(key.getKeyCode() == KeyEvent.VK_SPACE)
			this.firing = true;
		if(key.getKeyCode() == KeyEvent.VK_DOWN)
			this.decelerateHeld = true;
		
		//displays if the game has been paused and erases if unpaused
		Graphics g = playArea.getGraphics();
		if(key.getKeyCode() == KeyEvent.VK_P && paused)
		{
			this.paused = false;
			g.setColor(Color.BLACK);
			g.drawString("Game Paused", playWidth/2, playHeight/2);
		}
		else if(key.getKeyCode() == KeyEvent.VK_P && !paused)
		{
			this.paused = true;
			g.setColor(Color.WHITE);
			g.drawString("Game Paused", playWidth/2, playHeight/2);
		}
	}

	// Deals with keyboard keys being released
	public void keyReleased(KeyEvent key)
	{
		// Mark down which important keys are no longer being pressed
		if(key.getKeyCode() == KeyEvent.VK_UP)
			this.accelerateHeld = false;
		if(key.getKeyCode() == KeyEvent.VK_LEFT)
			this.turnLeftHeld = false;
		if(key.getKeyCode() == KeyEvent.VK_RIGHT)
			this.turnRightHeld = false;
		if(key.getKeyCode() == KeyEvent.VK_DOWN)
			this.decelerateHeld = false;
	}

	// This method is not actually used, but is required by the KeyListener interface
	public void keyTyped(KeyEvent arg0)
	{
	}
	
	
	public static void main(String[] args)
	{
		// A GUI program begins by creating an instance of the GUI
		// object. The program is event driven from that point on.
		new SuperCometsMain();
	}

}