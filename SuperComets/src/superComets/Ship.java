//Andrew Bertrand
//Assignment 3 EC
//Ship class
// 3/15/10

package superComets;

import java.awt.geom.Point2D;

//class for the ship in the game
public class Ship extends SpaceObject{
	
double angle, distances[] = new double[4], vertAngles[] = new double[4];
Point2D vertices[] = new Point2D.Double[4];
	
	public Ship(double xPos, double yPos, double xVel, double yVel)
	{
		super(xPos, yPos, xVel, yVel, 10);
		
		//creates the shape
		angle = 0;
		distances[0] = 10;
		distances[1] = 10;
		distances[2] = 3;
		distances[3] = 10;
		vertAngles[0] = 0;
		vertAngles[1] = 3 * Math.PI / 4;
		vertAngles[2] = Math.PI;
		vertAngles[3] = 5 * Math.PI / 4;
		
		for(int i = 0; i < 4; i++)
		vertices[i] = new Point2D.Double(distances[i]*Math.sin(vertAngles[i])+xPos, 
				distances[i]*Math.cos(vertAngles[i])+yPos);
		
	}
	
	//increases the velocity as necessary but keeps the speed less than 10
	public void accelerate()
	{
		xVelocity += 0.1 * Math.sin(angle);
		yVelocity += 0.1 * Math.cos(angle);
		
		if(Math.sqrt(Math.pow(xVelocity, 2) + Math.pow(yVelocity, 2)) > 10)
		{
			xVelocity -= 0.1 * Math.sin(angle);
			yVelocity -= 0.1 * Math.cos(angle);
		}
	}
	
	//decreases the velocity as necessary, but keeps the speed less than 10
	public void decelerate()
	{
		xVelocity -= 0.1 * Math.sin(angle);
		yVelocity -= 0.1 * Math.cos(angle);
		
		if(Math.sqrt(Math.pow(xVelocity, 2) + Math.pow(yVelocity, 2)) > 10)
		{
			xVelocity += 0.1 * Math.sin(angle);
			yVelocity += 0.1 * Math.cos(angle);
		}
	}
	
	//creates a new shot
	public Shot fire()
	{
		return new Shot(xPos, yPos, 3 * Math.sin(angle) + xVelocity, 3 * 
				Math.cos(angle) + yVelocity);
	}

	//returns the angle the ship is facing
	public double getAngle() 
	{
		return angle;
	}
	
	//increases the ship's angle and the angles of all the points
	public void rotateLeft()
	{
		angle += 0.1;
		for(int i = 0; i < 4; i++)
			vertAngles[i] += 0.1;
	}
	
	//decreases the ship's angle and the angles of all the points
	public void rotateRight()
	{
		angle -= 0.1;
		for(int i = 0; i < 4; i++)
			vertAngles[i] -= 0.1;
	}
	
	//moves the ship's center and changes all the vertices as necessary
	public void move()
	{
		super.move();
		
		for(int i = 0; i < 4; i++)
			vertices[i] = new Point2D.Double(distances[i]*Math.sin(vertAngles[i])+xPos, 
					distances[i]*Math.cos(vertAngles[i])+yPos);
	}
}
