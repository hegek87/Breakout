package breakout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import breakout.Brick.Type;

public class Canvas extends JPanel implements ActionListener, MouseMotionListener, MouseListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5699255769305413877L;
	public static final int HEIGHT = 600;
	public static final int WIDTH = 720;
	
	private int horizontalCount;
	private BufferedImage image;
	private Graphics2D bufferedGraphics;
	private Timer time;
	private static final Font endFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
	private static final Font scoreFont = new Font(Font.SANS_SERIF, Font.BOLD, 15);
	
	private Paddle player;
	private Ball ball;
	ArrayList<ArrayList<Brick> > bricks;
	
	/**
	 * Prepares the screen, centers the paddle and the ball. The ball
	 * will be located in the center of the paddle, and the paddle will
	 * be located on the center of the screen
	 * 
	 * The bricks are displayed in columns across the screen with the 
	 * screen being split based on the width of an individual brick. 
	 * Each brick is stored in a temporary ArrayList, which is added
	 * to the classes ArrayList which contains all of the bricks.
	 */
	public Canvas(){
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		bufferedGraphics = image.createGraphics();
		time = new Timer(15, this);
		player = new Paddle((WIDTH/2)-(Paddle.PADDLE_WIDTH/2));
		ball = new Ball(((player.getX() + (Paddle.PADDLE_WIDTH / 2)) - (Ball.DIAMETER / 2)), 
				(Paddle.Y_POS - (Ball.DIAMETER + 10)), -5, -5);
		
		bricks = new ArrayList<ArrayList<Brick> >();
		horizontalCount = WIDTH / Brick.BRICK_WIDTH;
		for(int i = 0; i < 8; ++i){
			ArrayList<Brick> temp = new ArrayList<Brick>();
			Type rowColor = null;
			switch(i){
				case 0:
				case 2:
					rowColor = Type.LOW;
					break;
				case 1:
				case 3:
				case 5:
					rowColor = Type.MEDIUM;
					break;
				case 4:
				case 6:
					rowColor = Type.HIGH;
					break;
				case 7:
				default:
					rowColor = Type.ULTRA;
					break;
			}
			for(int j = 0; j < horizontalCount; ++j){
				Brick tempBrick = new Brick((j * Brick.BRICK_WIDTH), ((i+2) * Brick.BRICK_HEIGHT), rowColor);
				temp.add(tempBrick);
			}
			bricks.add(temp);
			addMouseMotionListener(this);
			addMouseListener(this);
			addKeyListener(this);
			requestFocus();
		}
	}
	
	@Override public void actionPerformed(ActionEvent e){
		checkCollisions();
		ball.move();
		for(int i = 0; i < bricks.size(); ++i){
			ArrayList<Brick> al = bricks.get(i);
			for(int j = 0; j < al.size(); ++j){
				Brick b = al.get(j);
				if(b.dead()){
					al.remove(b);
				}
			}
		}
		repaint();
	}
	
	/**
	 * Checks for any collisions, if the ball hits the upper wall, or the side
	 * walls it changes direction. If the ball goes below the paddle, the position
	 * of the ball gets reset and the player loses a life
	 */
	private void checkCollisions() {
		if(player.hitPaddle(ball)){
			ball.setDY(ball.getDY() * -1);
			return;
		}
		//first check if ball hit any walls
		if(ball.getX() >= (WIDTH - Ball.DIAMETER) || ball.getX() <= 0){
			ball.setDX(ball.getDX() * -1);
		}
		if(ball.getY() > (Paddle.Y_POS + Paddle.PADDLE_HEIGHT + 10)){
			resetBall();
		}
		if(ball.getY() <= 0){
			ball.setDY(ball.getDY() * -1);
		}
		
		//next handle collisions between bricks
		int brickRowsActive = 0;
		for(ArrayList<Brick> alb : bricks){
			if(alb.size() == horizontalCount){
				++brickRowsActive;
			}
		}
		
		for(int i = (brickRowsActive==0) ? 0 : (brickRowsActive - 1); i < bricks.size(); ++i){
			for(Brick b : bricks.get(i)){
				if(b.hitBy(ball)){
					player.setScore(player.getScore() + b.getBrickType().getPoints());
					b.decrementType();
				}
			}
		}
	}
	
	/**
	 * Sets the balls position to approximately the center of the screen, and
	 * deducts a point from the user. If necessary, ends the game
	 */
	private void resetBall() {
		if(gameOver()){
			player.setLives(player.getLives() - 1);
			time.stop();
			return;
		}
		ball.setX(WIDTH/2);
		ball.setY((HEIGHT/2) + 80);
		ball.setDX(0);
		ball.setDY(0);
		player.setLives(player.getLives() - 1);
		player.setScore(player.getScore() - 1000);
	}
	
	private boolean gameOver() {
		if(player.getLives() <= 1)
			return true;
		return false;
	}

	/**
	 *  Draws the screen for the game, first sets the screen up (clears it)
	 *  and then it begins by setting the entire screen to be white. Finally
	 *  it draws all of the bricks, the players paddle, and the ball on the 
	 *  screen
	 */
	@Override public void paintComponent(Graphics g){
		super.paintComponent(g);
		bufferedGraphics.clearRect(0, 0, WIDTH, HEIGHT);
		bufferedGraphics.setColor(Color.WHITE);
		bufferedGraphics.fillRect(0, 0, WIDTH, HEIGHT);
		player.drawPaddle(bufferedGraphics);
		ball.drawBall(bufferedGraphics);
		for(ArrayList<Brick> row : bricks){
			for(Brick b : row){
				b.drawBrick(bufferedGraphics);
			}
		}
		bufferedGraphics.setFont(scoreFont);
		bufferedGraphics.drawString("Score: " + player.getScore(), 10, 25);
		bufferedGraphics.drawString("Lives: " + player.getLives(), WIDTH - 65, 25);
		if(gameOver() &&
				ball.getY() >= HEIGHT){
			bufferedGraphics.setColor(Color.black);
			bufferedGraphics.setFont(endFont);
			bufferedGraphics.drawString("Game Over!  Score: " + player.getScore(), (WIDTH/2) - 85, (HEIGHT/2));
		}
		if(empty()){
			bufferedGraphics.setColor(Color.black);
			bufferedGraphics.setFont(endFont);
			bufferedGraphics.drawString("You won!  Score: " + player.getScore(), (WIDTH/2) - 85, (HEIGHT/2));
			time.stop();
		}
		g.drawImage(image, 0, 0, this);
		Toolkit.getDefaultToolkit().sync();
	}
	
	

	private boolean empty() {
		for(ArrayList<Brick> al : bricks){
			if(al.size() != 0){
				return false;
			}
		}
		return true;
	}

	@Override public void mouseMoved(MouseEvent e){
		player.setX(e.getX() - (Paddle.PADDLE_WIDTH / 2));
	}
	
	@Override public void mouseDragged(MouseEvent e){}
	
	@Override public void mouseClicked(MouseEvent e){
		if(ball.getDX() == 0 && ball.getDY() == 0){
			ball.setDX(5);
			ball.setDY(5);
		}
		if(time.isRunning()){
			return;
		}
		time.start();
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		Canvas c = new Canvas();
		frame.add(c);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			if(time.isRunning()) return;
			time.start();
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			player.setX(player.getX() - Paddle.DELTA_X);
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			player.setX(player.getX() - Paddle.DELTA_X);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			if(time.isRunning()) return;
			time.start();
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			player.setX(player.getX() - Paddle.DELTA_X);
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			player.setX(player.getX() - Paddle.DELTA_X);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			if(time.isRunning()) return;
			time.start();
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			player.setX(player.getX() - Paddle.DELTA_X);
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			player.setX(player.getX() - Paddle.DELTA_X);
		}
	}
}
