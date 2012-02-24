//Andrew Bertrand
//Assignment 3 EC
//Comet class
// 3/15/10

package superComets;

import java.awt.geom.*;
import java.util.Vector;

//class for all comets in the game
public abstract class Comet extends SpaceObject{
	
	//all comets have 12 vertices made by 12 angles and distances,
	//and an angular velocity and keep track of how many
	//times they were hit
	int timesHit;
	Point2D outline[] = new Point2D[12];
	double angle[] = new double[12], distance[] = new double[12], angularVel;
	
	//constructor creates another spaceObject
	public Comet(double xPos, double yPos, double xVel, double yVel, double radius)
	{
		super(xPos, yPos, xVel, yVel, radius);
		
		Point2D.Double vertices[] = new Point2D.Double[12];
		
		//this generates 12 random angles in a circle at least .38
		//radians appart
		angularVel = Math.random() * 0.2;
		for(int j = 0; j < 12; j++)
		{
			angle[j] = Math.random() * 2 * Math.PI;
			
			for(int m = 0; m < j; m++)
			{
				if(Math.abs(angle[j] - angle[m]) < .38)
				{
					angle[j] = Math.random() * 2 * Math.PI;
					m = 0;
				}
			}
		}
		
		//sorts the angles so they will be in a circle
		double smallestAngle = 0;
		int smallestPos = 0;
		for(int a = 0; a < 12; a++)
		{
			smallestAngle = angle[a];
			for(int b = a; b < 12; b++)
			{
				if(angle[b] < smallestAngle)
				{
					smallestAngle = angle[b];
					smallestPos = b;
				}
			}
			angle[smallestPos] = angle[a];
			angle[a] = smallestAngle;
		}
		
		//generates the random distances and converts them to the points
		for(int n = 0; n < 12; n++)
		{
			distance[n] = Math.random() * (radius / 10) + (9 * radius / 10);
			vertices[n] = new Point2D.Double(distance[n] * Math.sin(angle[n])
					+ xPos, distance[n] * Math.cos(angle[n]) + yPos);
		}
		
		int timesHit = 0;
		outline = vertices;
		
	}
	
	//all comets should have an explode() method
	abstract Vector<Comet> explode();
	
	//this moves the center of the comet and then reworks the vertices' positions
	//including the angles based on the angularVel
	public void move()
	{
		super.move();
		for(int i = 0; i < 12; i++)
		{
			angle[i] += angularVel;
			outline[i] = new Point2D.Double(distance[i] * Math.sin(angle[i]) + xPos,
					distance[i] * Math.cos(angle[i]) + yPos);
		}
	}

}
