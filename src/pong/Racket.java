package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Racket {

    public int racketNumber;
    public int x, y, width = 15, height = 150;
    public int score;

    public Racket(Pong pong, int racketNumber) {

        this.racketNumber = racketNumber;

        // Позиция ракеток по х, у
        if (racketNumber == 1) this.x = 0;

        if (racketNumber == 2) {
            this.x = pong.width - (width + 7);
        }

        this.y = pong.height / 2 - this.height / 2;
    }

    // Цвет ракеток
    public void render(Graphics g)
    {
        if(racketNumber == 1) {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
        if(racketNumber == 2){
            g.setColor(Color.blue);
            g.fillRect(x, y, width, height);
        }
    }

    // Скорость движения ракетки
    public void move(boolean up) {
        int speed = 15;

        if (up) {
            if (y - speed > 0) y -= speed;
             else y = 0;
        }
        else if (y + height + speed < Pong.pong.height) y += speed;
             else y = Pong.pong.height - height;
    }
}

