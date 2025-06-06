import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    // Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBodyGrow; // to store all segment which snake eating

    // Food
    Tile snakeFood;
    Random random; // to place food position randomly

    // game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth; // this.memeber of class=parameter;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5); // snake
        snakeBodyGrow = new ArrayList<Tile>(); // snake body

        snakeFood = new Tile(10, 10); // food
        random = new Random(); // random food
        placeFoodRandom();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    // Functions

    public void paintComponent(Graphics g) { // (Graphics g) use for drawing
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid -if we want grid

        // for (int i = 0; i < boardWidth / tileSize; i++) { // 600/25=24 rows & columns
        // // (x1,y1,x2,y2)
        // g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
        // g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        // }

        // Snake Food
        g.setColor(Color.red);
        // g.fillRect(snakeFood.x * tileSize, snakeFood.y * tileSize, tileSize,
        // tileSize);
        g.fill3DRect(snakeFood.x * tileSize, snakeFood.y * tileSize, tileSize, tileSize, true);

        // Snake Head
        g.setColor(Color.green);
        // g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize,
        // tileSize); // draw a rectangle
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true); // draw a rectangle

        // Snake body grow
        for (int i = 0; i < snakeBodyGrow.size(); i++) {
            Tile snakePart = snakeBodyGrow.get(i);
            // g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize,
            // tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);

        }

        // Score
        g.setFont(new Font("Arial ", Font.PLAIN, 20));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over" + String.valueOf(snakeBodyGrow.size()), tileSize - 20, tileSize);// (score,x,y)
        } else {
            g.drawString("Score: " + String.valueOf(snakeBodyGrow.size()), tileSize - 20, tileSize);
        }

    }

    public void placeFoodRandom() {
        snakeFood.x = random.nextInt(boardWidth / tileSize); // 600/25=24 random number from 0 to 24
        snakeFood.y = random.nextInt(boardHeight / tileSize);
    }

    // collision of snake and food
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // eat food
        if (collision(snakeHead, snakeFood)) {
            snakeBodyGrow.add(new Tile(snakeFood.x, snakeFood.y));
            placeFoodRandom();

        }

        // snake body grow -moving each segment of snake with snake head
        for (int i = snakeBodyGrow.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBodyGrow.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBodyGrow.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;

            }

        }

        // snakeHead
        snakeHead.x += velocityX; // snakeHead.x =snakeHead.x + velocityX
        snakeHead.y += velocityY;

        // game over condition
        // 1.snake head with it's body part
        for (int i = 0; i < snakeBodyGrow.size(); i++) {
            Tile snakePart = snakeBodyGrow.get(i);
            // colide with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }

        }
        // 2.collide with wall
        // (left||right||up||down)
        if (snakeHead.x * tileSize < 0 || snakeHead.x > boardWidth ||
                snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // to move snake
        repaint(); // draw over again & again
        if (gameOver) { // game end
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) { // if we add this (&& velocityY != 1) condition if we
                                                                  // press up and then
                                                                  // down so snake will not go down
            velocityX = 0;
            velocityY = -1; // up
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    // no need
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
