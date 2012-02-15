package breakout;

import java.awt.Color;
import java.awt.Graphics2D;

public class Ball {
	private int xPos, yPos;
	private int dX, dY;
	public static final int DIAMETER = 15;
	public static final Color BALL_COLOR = Color.black;
	
	public Ball(int xPos, int yPos, int dX, int dY){
		this.xPos	= xPos;
		this.yPos	= yPos;
		this.dX 	= dX;
		this.dY		= dY;
	}
	
	public void setX(int xPos){	this.xPos 	= xPos;	}
	public void setY(int yPos){	this.yPos	= yPos;	}
	public void setDX(int dX){	this.dX 	= dX;	}
	public void setDY(int dY){	this.dY		= dY;	}
	public int getX(){	return xPos;	}
	public int getY(){	return yPos;	}
	public int getDX(){	return dX;		}
	public int getDY(){	return dY;		}
	
	public void move(){
		xPos += dX;
		yPos += dY;
	}
	
	public void drawBall(Graphics2D g){
		g.setColor(BALL_COLOR);
		g.fillOval(xPos, yPos, DIAMETER, DIAMETER);
		g.setColor(Color.gray);
		g.drawOval(xPos, yPos, DIAMETER, DIAMETER);
	}
}
