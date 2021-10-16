/***
 * 
 */
package stream;

import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
public class GUI {
/**
   * 
   * GUI interface 
   * Chat based
   * @return 
   * @throws IOException
   */
  public GUI(){
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

    // Panel Rençoit Message
    JPanel messagesPanel = new JPanel();
    JPanel liveChatPanel = new JPanel();
    JScrollPane scrollMessagePane = new JScrollPane(messagesPanel); 
    JLabel testMessage = new JLabel("xXXXDarkSasukeXXxx:    Hello, what's up my boy ?");
    messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
    liveChatPanel.add(testMessage);
    liveChatPanel.setPreferredSize(new Dimension(400,400));
    liveChatPanel.setBackground(Color.white);
    messagesPanel.add(liveChatPanel);


    // Panel Envoie Message
    JPanel textPanel = new JPanel();
    JTextArea textfield = new JTextArea(3,50);
    textfield.setLineWrap(true);
    JScrollPane scroll = new JScrollPane(textfield); 
    JButton button = new JButton("Envoyer");
    textPanel.add(scroll);
    textPanel.add(button);



    panel.setLayout(new GridLayout(0,2));
    panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
    frame.add(scrollMessagePane);
    frame.add(textPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Chat - 4IF");
    frame.setSize(800,500);
    frame.setVisible(true);
   }  
}