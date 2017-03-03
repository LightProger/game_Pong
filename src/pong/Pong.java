package pong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Pong implements ActionListener, KeyListener {

    // Переменные
    public static Pong pong;
    public int width = 700, height = 500;
    public Renderer renderer;
    public Racket player1;
    public Racket player2;
    public Ball ball;
    public boolean bot = false, selectingDifficulty;
    public boolean w, s, up, down;
    public int gameStatus = 0, scoreLimit = 7, playerWon; //0 = Меню, 1 = Пауза, 2 = Играть, 3 = Конец
    public int botDifficulty, botMoves, botCooldown = 0;
    public Random random;
    public JFrame jframe;

    // Конструктор
    public Pong()
    {
        // Таймер
        Timer timer = new Timer(20, this);
        random = new Random();

        // Cсылка на класс Renderer
        renderer = new Renderer();

        // Создание окна
        jframe = new JFrame("Тенис");
        jframe.setSize(width, height);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(renderer);
        jframe.setResizable(false);
        jframe.setLocationRelativeTo(null);
        jframe.addKeyListener(this);

        // Запуск таймера
        timer.start();
    }

    // Запуск игры
    public void start()
    {
        gameStatus = 2;// Играть

        // Рисуем ракетки и мяч
        player1 = new Racket(this, 1);
        player2 = new Racket(this, 2);
        ball = new Ball(this);
    }

    // Обновление игры
    public void update()
    {
        // Если выиграл игрок 1
        if (player1.score >= scoreLimit)// Если счет игрока 1, больше или равен лимиту очков. Лимит = 7
        {
            playerWon = 1;// Выиграл игрок 1
            gameStatus = 3; // Конец игры
        }

        // Если выиграл игрок 2
        if (player2.score >= scoreLimit)// Если счет игрока 2, больше или равен лимиту очков. Лимит = 7
        {
            playerWon = 2;// Выиграл игрок 2
            gameStatus = 3;// Конец игры

        }

        if (w)
        {
            player1.move(true);
        }
        if (s)
        {
            player1.move(false);
        }

        if (!bot)
        {
            if (up)
            {
                player2.move(true);
            }
            if (down)
            {
                player2.move(false);
            }
        }
        else
        {
            if (botCooldown > 0)
            {
                botCooldown--;

                if (botCooldown == 0)
                {
                    botMoves = 0;
                }
            }

            if (botMoves < 10)
            {
                if (player2.y + player2.height / 2 < ball.y)
                {
                    player2.move(false);
                    botMoves++;
                }

                if (player2.y + player2.height / 2 > ball.y)
                {
                    player2.move(true);
                    botMoves++;
                }

                // Уровни слжности игры
                if (botDifficulty == 0)
                {
                    botCooldown = 20;
                }
                if (botDifficulty == 1)
                {
                    botCooldown = 15;
                }
                if (botDifficulty == 2)
                {
                    botCooldown = 10;
                }
            }
        }

        ball.update(player1, player2);
    }
    // Отрисовка объектов игры
    public void render(Graphics2D g)
    {
        // Начальный экран
        g.setColor(Color.green);
        g.fillRect(0, 0, width, height);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (gameStatus == 0)
        {
            // Текст на начальном экране
            g.setColor(Color.black);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("ТЕНИС", width / 2 - 85, 50);

            if (!selectingDifficulty)
            {
                g.setFont(new Font("Arial", 1, 30));
                g.drawString("Нажми \"ПРОБЕЛ\" для начала игры", width / 2 - 250, height / 2 - 75);

                g.setFont(new Font("Arial", 1, 20));
                g.drawString("Кнопки управления: Игрок 1: W и S",
                        width / 2 - 260, height / 2 - 25);
                g.drawString("Игрок 2: Стрелки \"Вверх\" и \'Вниз\"",
                        width / 2 - 52, height / 2 - 0);

                g.setFont(new Font("Arial", 1, 30));
                g.drawString("Нажми \"SHIFT\" для игры с компьютером", width / 2 - 300, height / 2 + 45);

                g.setFont(new Font("Arial", 1, 20));
                g.drawString("Кнопки управления: W и S",
                        width / 2 - 253, height / 2 + 95);

                g.setFont(new Font("Arial", 1, 30));
                g.drawString("<< Игра до: " + scoreLimit + " очков >>", width / 2 - 150, height / 2 + 150);


            }
        }

        // Текст при запуске игры с компьютером
        if (selectingDifficulty)
        {
            String string = botDifficulty == 0 ? "Легкий" : (botDifficulty == 1 ? "Средний" : "Тяжелый");
            g.setFont(new Font("Arial", 1, 30));
            g.drawString("<< Уровень сложности: " + string + " >>", width / 2 - 260, height / 2 - 25);
            g.drawString("Нажмите \"ПРОБЕЛ\" для начала игры", width / 2 - 270, height / 2 + 25);
        }

        // Текст паузы
        if (gameStatus == 1)
        {
            g.setColor(Color.black);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("Пауза", width / 2 + 150, height / 2 - 200);
        }

        if (gameStatus == 1 || gameStatus == 2)
        {
            // Отрисовка линий на поле и счет игры
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(5f));
            g.drawLine(width / 2, 0, width / 2, height);
            g.setStroke(new BasicStroke(2f));
            g.drawOval(width / 2 - 150, height / 2 - 150, 300, 300);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString(String.valueOf(player1.score), width / 2 - 90, 50);
            g.drawString(String.valueOf(player2.score), width / 2 + 65, 50);

            // Запуск отрисовки ракеток и мяча
            player1.render(g);
            player2.render(g);
            ball.render(g);
        }

        if (gameStatus == 3)
        {
            // Отрисовка последнего экрана, после окончания игры
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("ТЕНИС", width / 2 - 85, 50);

            if (bot && playerWon == 2)
            {
                g.drawString("Выиграл компьютер!", width / 2 - 270, 200);
            }
            else
            {
                g.drawString("Игрок " + playerWon + " выиграл!", width / 2 - 220, 200);
            }

            g.setFont(new Font("Arial", 1, 30));
            g.drawString("Нажми \"ПРОБЕЛ\" для запуска игры", width / 2 - 270, height / 2 - 5);
            g.drawString("Нажми \"ESC\" для возврата в МЕНЮ", width / 2 - 270, height / 2 + 35);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (gameStatus == 2)
        {
            update();
        }

        renderer.repaint();
    }

    public static void main(String[] args)
    {
        // Ccылка на конструктор
        pong = new Pong();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W)
        {
            w = true;
        }
        else if (id == KeyEvent.VK_S)
        {
            s = true;
        }
        else if (id == KeyEvent.VK_UP)
        {
            up = true;
        }
        else if (id == KeyEvent.VK_DOWN)
        {
            down = true;
        }
        else if (id == KeyEvent.VK_RIGHT)
        {
            if (selectingDifficulty)
            {
                if (botDifficulty < 2)
                {
                    botDifficulty++;
                }
                else
                {
                    botDifficulty = 0;
                }
            }
            else if (gameStatus == 0)
            {
                scoreLimit++;
            }
        }
        else if (id == KeyEvent.VK_LEFT)
        {
            if (selectingDifficulty)
            {
                if (botDifficulty > 0)
                {
                    botDifficulty--;
                }
                else
                {
                    botDifficulty = 2;
                }
            }
            else if (gameStatus == 0 && scoreLimit > 1)
            {
                scoreLimit--;
            }
        }
        else if (id == KeyEvent.VK_ESCAPE && (gameStatus == 2 || gameStatus == 3))
        {
            gameStatus = 0;
        }
        else if (id == KeyEvent.VK_SHIFT && gameStatus == 0)
        {
            bot = true;
            selectingDifficulty = true;
        }
        else if (id == KeyEvent.VK_SPACE)
        {
            if (gameStatus == 0 || gameStatus == 3)
            {
                if (!selectingDifficulty)
                {
                    bot = false;
                }
                else
                {
                    selectingDifficulty = false;
                }

                start();
            }
            else if (gameStatus == 1)
            {
                gameStatus = 2;
            }
            else if (gameStatus == 2)
            {
                gameStatus = 1;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W)
        {
            w = false;
        }
        else if (id == KeyEvent.VK_S)
        {
            s = false;
        }
        else if (id == KeyEvent.VK_UP)
        {
            up = false;
        }
        else if (id == KeyEvent.VK_DOWN)
        {
            down = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }
}