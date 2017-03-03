package pong;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Александр on 02.03.2017.
 */
public class Renderer extends JPanel {

    private static final long serialVersionUID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Pong.pong.render((Graphics2D) g);
    }
}
