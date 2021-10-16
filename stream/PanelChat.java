package stream;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


public class PanelChat extends JPanel{
    
    public PanelChat()
    {
        super();
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        // testing purpose
        setBackground(new Color(0, 0, 255));
    }
}
