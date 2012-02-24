//Andrew Bertrand
//Assignment 3 EC
//SmallComet class
// 3/15/10

package superComets;

import java.util.*;

//this class is for the smallest comets in the game
public class SmallComet extends Comet{
	
	public SmallComet(double xPos, double yPos, double xVel, double yVel)
	{
		super(xPos, yPos, xVel, yVel, 20);
	}
	
	//gives a 70% chance a shot will destroy a comet and return a comets
	//vector with no comets, otherwise returns null
	public Vector<Comet> explode()
	{
		if(Math.random() > .3)
		{
			Vector<Comet> fragments = new Vector<Comet>();
			return fragments;
		}
		
		return null;
	}

}
