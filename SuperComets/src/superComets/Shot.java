//Andrew Bertrand
//Assignment 3 EC
//Shot class
// 3/15/10

package superComets;

//class for the shots in the game
public class Shot extends SpaceObject{
	
int age;
	
	public Shot(double xPos, double yPos, double xVel, double yVel)
	{
		super(xPos, yPos, xVel, yVel, 3);
		age = 0;
	}
	
	//returns the age of the shot
	public int getAge()
	{
		return age;
	}
	
	//moves the shot by its velocity and increases its age
	public void move()
	{
		super.move();
		age++;
	}

}
