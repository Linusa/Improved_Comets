//Andrew Bertrand
//Assignment 3 EC
//SpaceObject class
// 3/15/10

package superComets;

//class for all space objects in the game
public abstract class SpaceObject {
	
	double xPos, yPos, radius;
	protected double xVelocity, yVelocity;
	static double playfieldHeight, playfieldWidth;
	
	//simply copies the info for the constructor
	public SpaceObject(double xPos, double yPos, double xVel, double yVel,
			double radius)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		xVelocity = xVel;
		yVelocity = yVel;
		this.radius = radius;
	}
	
	//returns the radius
	double getRadius()
	{
		return radius;
	}
	
	//returns the xPos
	public double getXPosition()
	{
		return xPos;
	}
	
	//returns the yPos
	public double getYPosition()
	{
		return yPos;
	}
	
	//moves the object based on the velocities, and transfers
	//to the opposite side if it goes over the edge
	public void move()
	{
		this.xPos += xVelocity;
		this.yPos += yVelocity;
		
		if(xPos - radius > playfieldWidth)
			xPos = 0 - radius;
		else if(xPos + radius < 0)
			xPos = playfieldWidth + radius;
		if(yPos - radius > playfieldHeight)
			yPos = 0 - radius;
		else if(yPos + radius < 0)
			yPos = playfieldHeight + radius;
	}
	
	//compares the distance to the radii to see if two objects are overlapping,
	//returns true if they are
	public boolean overlapping(SpaceObject rhs)
	{
		if(Math.sqrt(Math.pow((Math.abs(xPos - rhs.getXPosition())), 2) + 
				Math.pow(Math.abs((yPos - rhs.getYPosition())), 2)) < (radius + rhs.getRadius()))
			return true;
		else
			return false;
		
	}

}
