package breakout;

import java.awt.Color;
import java.awt.Graphics2D;

public class Paddle {
	public static final int Y_POS = Canvas.HEIGHT - 30;
	public static final int PADDLE_WIDTH = 80;
	public static final int PADDLE_HEIGHT = 10;
	public static final Color PADDLE_COLOR = Color.black;
	private int xPos;
	public static final int DELTA_X = 5;
	private int score;
	private int lives;
	
	public Paddle(int xPos){
		this.xPos = xPos;
		score = 0;
		lives = 5;
	}
	
	public void setX(int xPos){ 
		this.xPos = xPos;
		if(xPos < 0) this.xPos = 0;
		if(xPos > (Canvas.WIDTH - PADDLE_WIDTH)) this.xPos = (Canvas.WIDTH - PADDLE_WIDTH);
	}
	
	public int getX(){ return xPos; }
	public int getScore(){ return score; }
	public void setScore(int score){ this.score = score; }
	public int getLives(){ return lives; }
	public void setLives(int lives){ this.lives = lives; }
	
	/**
	 * Determines whether a Ball has hit the paddle
	 * 
	 * Checks if ball is in range of the paddles x's first. If
	 * it ball.x > paddle.x but less than paddle.x + PADDLE_WIDTH + 15
	 * it is in the horizontal range.
	 * 
	 * Next checks if it is actually hitting the paddle. if ball.y + the diameter
	 * is close to the paddles y position the method returns true. Has a final
	 * condition which gives somewhat of an error buffer to make sure the method
	 * doesn't allow the ball to skip over the threshhold and thus go right through
	 * the paddle
	 * 
	 * @param b
	 * @return
	 */
	public boolean hitPaddle(Ball b){
		if(b.getX() <= xPos + (PADDLE_WIDTH + 15)){
			if(b.getX() >= xPos - 10){
				if((b.getY() + (Ball.DIAMETER - 1)) >= (Y_POS)){
					if((b.getY() + (Ball.DIAMETER - 1)) <= (Y_POS + (PADDLE_HEIGHT - 5))){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void drawPaddle(Graphics2D g){
		g.setColor(PADDLE_COLOR);
		g.fillRect(xPos, Y_POS, PADDLE_WIDTH, PADDLE_HEIGHT);
		g.setColor(Color.gray);
		g.drawRect(xPos, Y_POS, PADDLE_WIDTH, PADDLE_HEIGHT);
	}
	
	public static void main(String[] args){
		Ball b = new Ball(110, (Y_POS - (Ball.DIAMETER - 5)), 5, 5);
		Paddle p = new Paddle(110);
		for(int i = 1; i <= PADDLE_WIDTH; ++i){
			b.setX(b.getX() + 1);
			System.out.println(p.hitPaddle(b));
		}
		System.out.println(p.hitPaddle(b));
	}
}
