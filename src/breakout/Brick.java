package breakout;

import java.awt.Color;
import java.awt.Graphics2D;

public class Brick {
	public static final int BRICK_WIDTH = 60;
	public static final int BRICK_HEIGHT = 20;
	private int 	xPos, yPos;	
	private Type 	brickType;
	
	enum Type{
		ULTRA	(6, 700, Color.black),
		HIGH	(3, 150, Color.RED), 
		MEDIUM	(2, 100, Color.BLUE),
		LOW		(1, 50, Color.GREEN),
		DEAD	(0, 0, Color.WHITE);
		private int life;
		private Color color;
		private int points;
		
		Type(int life, int points, Color color){
			this.life = life;
			this.points = points;
			this.color = color;
		}
		public int getPoints(){	return points;	}
		public Color getColor(){	return color;	}
		public int getLife(){	return life;	}
	}
	
	public Brick(int xPos, int yPos, Type brickType){
		this.xPos = xPos;
		this.yPos = yPos;
		this.brickType = brickType;
	}
	
	public int getX(){	return xPos;	}
	public int getY(){	return yPos;	}
	public Type getBrickType(){	return brickType;	}
	
	public boolean hitBy(Ball b){
		//first check if it hits from the bottom or top
		if(b.getX() <= (xPos + BRICK_WIDTH) && b.getX() >= xPos){
			//hit bottom
			if(b.getY() <= (yPos + BRICK_HEIGHT) && b.getY() >= (yPos + (BRICK_HEIGHT / 2))){
				b.setDY(b.getDY() * -1);
				return true;
			}
			//hit top
			else if(b.getY() >= (yPos - Ball.DIAMETER) && b.getY() < (yPos + (Ball.DIAMETER / 3))){
				b.setDY(b.getDY() * -1);
				return true;
			}
		}
		//determines if it from a side
		else if(b.getY() <= (yPos + BRICK_HEIGHT) && b.getY() >= yPos){
			//hit right
			if(b.getX() <= (xPos + BRICK_WIDTH) && b.getX() > (xPos + (BRICK_WIDTH - (Ball.DIAMETER / 2)))){
				b.setDX(b.getDX() * -1);
				return true;
			}
			//hit left
			else if(b.getX() >= (xPos - Ball.DIAMETER) && b.getX() < (xPos + (Ball.DIAMETER / 2))){
				b.setDX(b.getDX() * -1);
				return true;
			}
		}
		return false;
	}
	
	public void decrementType(){
		switch(brickType.life){
			case 6:
			case 5:
			case 4:
				--brickType.life;
				break;
			case 3:
				brickType = Type.MEDIUM;
				break;
			case 2: 
				brickType = Type.LOW;
				break;
			case 1:
			default:
				brickType = Type.DEAD;
				break;
		}
	}
	
	public void drawBrick(Graphics2D g){
		g.setColor(Color.white);
		g.fillRect(xPos, yPos, BRICK_WIDTH, BRICK_HEIGHT);
		g.setColor(brickType.color);
		g.fillRect((xPos+2), (yPos+2), BRICK_WIDTH-4, BRICK_HEIGHT-4);
		g.setColor(Color.black);
		g.drawRect((xPos+2), (yPos+2), BRICK_WIDTH-4, BRICK_HEIGHT-4);
	}

	public boolean dead() {
		if(brickType.life == 0)
			return true;
		return false;
	}
}
