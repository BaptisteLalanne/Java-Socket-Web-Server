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

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class GUI extends JFrame implements ActionListener { 

   private JPanel pan_connection;
   private Container container;

   public GUI(){

      setLocationRelativeTo(null);
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Chat - 4IF");
      setSize(800,500);

      container = getContentPane();
      pan_connection = new JPanel();
      initPanelConnection();
      container.add(pan_connection);

      setVisible(true);
   }  

   public void initPanelConnection() {

      JButton send_button = null;
      JTextArea infos_textarea = null;
      JTextField input_textfield = null;

      setBackground(new Color(240, 240, 240));

      // TODO: create textarea
      infos_textarea = new JTextArea();
      infos_textarea.setBounds(10, 10, 780, 430);        

      // TODO: create input
      input_textfield = new JTextField();
      input_textfield.setBounds(10, 450, 650, 30); 

      // TODO: create button
      send_button = new JButton("Envoyer");
      send_button.addActionListener(this);
      send_button.setBounds(670, 450, 120, 30);

      pan_connection.setLayout(null);

      // TODO: add elements to panel
      pan_connection.add(infos_textarea);
      pan_connection.add(input_textfield);
      pan_connection.add(send_button);

   }

   public void actionPerformed(ActionEvent e)
   {
       Object source = e.getSource();
   }
}