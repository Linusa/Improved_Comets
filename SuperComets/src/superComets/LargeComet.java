//Andrew Bertrand
//Assignment 3 EC
//LargeComet class
// 3/15/10
package superComets;

import java.util.Vector;

//this class is for all large comets in the game
public class LargeComet extends Comet{
	
	public LargeComet(double xPos, double yPos, double xVel, double yVel)
	{
		super(xPos, yPos, xVel, yVel, 40);
	}
	
	//this returns null before the comet dies, and then at 4 shots
	//the comet dies and returns two medium comets
	public Vector<Comet> explode()
	{
		timesHit++;
		if(timesHit >= 4)
		{
			Vector<Comet> fragments = new Vector<Comet>();
			fragments.add(new MediumComet(xPos , yPos, Math.random() * 5 - 2, Math.random() * 5 - 2));
			fragments.add(new MediumComet(xPos, yPos, Math.random() * 5 - 2, Math.random() * 5 - 2));
			return fragments;
		}
		
		else
			return null;
		
	}

}
