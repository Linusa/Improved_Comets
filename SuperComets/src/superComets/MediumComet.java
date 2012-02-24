//Andrew Bertrand
//Assignment 3 EC
//MediumComet class
// 3/15/10

package superComets;

import java.util.*;

//class for medium sized comets in the game
public class MediumComet extends Comet{
	
	public MediumComet(double xPos, double yPos, double xVel, double yVel)
	{
		super(xPos, yPos, xVel, yVel, 30);
	}
	
	//increases the amount of times hit, returns null if it hasn't been
	//hit enough to die, and returns three small comets if it does die
	public Vector<Comet> explode()
	{
		timesHit++;
		if(timesHit >= 2)
		{
			Vector<Comet> fragments = new Vector<Comet>();
			fragments.add(new SmallComet(xPos, yPos, Math.random() * 5 - 2, 
					Math.random() * 5 - 2));
			fragments.add(new SmallComet(xPos, yPos, Math.random() * 5 - 2, 
					Math.random() * 5 - 2));
			fragments.add(new SmallComet(xPos, yPos, Math.random() * 5 - 2, 
					Math.random() * 5 - 2));
			return fragments;
		}
		
		else
			return null;
		
	}

}
