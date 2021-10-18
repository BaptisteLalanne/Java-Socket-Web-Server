package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

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

public class GUI extends JFrame { 

   private List<String> users = new ArrayList<String>();
   private List<String> connected_users = new ArrayList<String>();

   // connection panel components
   private JPanel pan_connection;
   private JLabel connection_infos_label = null;
   private JLabel connection_status_label = null;
   private JButton connection_send_button = null;
   private JTextField connection_input_textfield = null;
   
   // chat panel components
   private JPanel pan_chat;
   private JButton chat_send_button = null;
   private JTextArea chat_infos_textarea = null;
   private JTextField chat_input_textfield = null;
   private JList chat_users_list = null;
   private DefaultListModel chat_users_model = null;
   private String chat_selected_username = null;
   private Integer chat_selected_index = null;

   private Container container;

   public GUI(){

      // adding data 
      users.add("Baptiste");
      users.add("Tom");
      users.add("Emile");
      users.add("Marie");
      users.add("Fabien");
      users.add("Thomas");
      users.add("Batman");
      users.add("Robin");
      users.add("Martin");
      users.add("Fabien");
      users.add("Tristan");

      // window settings
      setLocationRelativeTo(null);
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Chat - 4IF");
      setSize(800,500);
      
      container = getContentPane();

      // setup panels
      initPanelConnection();
      initPanelChat();

      container.add(pan_connection);

      setVisible(true);
   }

   public void initPanelConnection() {
      
      pan_connection = new JPanel();

      setBackground(new Color(240, 240, 240));

      // create info label
      connection_infos_label = new JLabel("Votre nom d'utilisateur: ");
      connection_infos_label.setBounds(200, 100, 400, 50);    
      
      // create status label
      connection_status_label = new JLabel();
      connection_status_label.setBounds(200, 300, 400, 30); 

      // create input
      connection_input_textfield = new JTextField();
      connection_input_textfield.setBounds(200, 200, 300, 30); 

      // create button
      connection_send_button = new JButton("Envoyer");
      connection_send_button.addActionListener(new ActionListener() {
         @Override
            public void actionPerformed(ActionEvent e) {
               Logger.debug("GUI_actionPerformed", "connect button");
               Boolean res = false;
               
               try {
                  res = EchoClient.connectUser(connection_input_textfield.getText());
               } catch (IOException e1) {
                  e1.printStackTrace();
               }

               if (res) {
                  Logger.debug("GUI_actionPerformed", "connection success");
                  switchToChatPanel();
               } else {
                  Logger.debug("GUI_actionPerformed", "connection fail");
                  connection_status_label.setText("Le nom d'utilisateur est déjà utilisé ou est incorrect");
                  pan_connection.repaint();
                  pan_connection.revalidate();
               }
            }
      });
      connection_send_button.setBounds(510, 200, 90, 30);

      pan_connection.setLayout(null);

      // add elements to panel
      pan_connection.add(connection_infos_label);
      pan_connection.add(connection_status_label);
      pan_connection.add(connection_input_textfield);
      pan_connection.add(connection_send_button);
   }

   public void initPanelChat() {

      pan_chat = new JPanel();

      setBackground(new Color(240, 240, 240));

      // create list model
      chat_users_model = new DefaultListModel();

      // create list
      chat_users_list = new JList(chat_users_model);
      chat_users_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
      chat_users_list.setLayoutOrientation(JList.VERTICAL_WRAP );
      chat_users_list.setVisibleRowCount(-1);
      chat_users_list.setBounds(10, 10, 130, 450);
      chat_users_list.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {

            // check if a new user is selected
            String new_selected = chat_users_list.getSelectedValue().toString();
            if (!new_selected.equals(chat_selected_username)) {
               chat_selected_username = new_selected;
               Logger.debug("GUI_MouseAdapter", "selected user: " + chat_selected_username);

               // TODO: check if connected (with *)
               String real_username = chat_selected_username;
               if (chat_selected_username.charAt(chat_selected_username.length()-1) == '*')
                  real_username = chat_selected_username.substring(0, chat_selected_username.length() - 1);  

               boolean joined = EchoClient.joinConversation(real_username);
               Logger.debug("GUI_MouseAdapter", "joined: " + String.valueOf(joined));
               if (joined) {
                  List<String> old_messages = EchoClient.getOldMessages();
                  resetMessageArea();
                  showMessages(old_messages);
               } else {
                  // TODO: display error message
               }
            }

         }
      });

      // create textarea
      chat_infos_textarea = new JTextArea();
      chat_infos_textarea.setBounds(150, 10, 640, 400);
      chat_infos_textarea.setEditable(false);

      // create input
      chat_input_textfield = new JTextField();
      chat_input_textfield.setBounds(150, 420, 510, 30);

      // create button
      chat_send_button = new JButton("Envoyer");
      chat_send_button.addActionListener(new ActionListener() {
         @Override
            public void actionPerformed(ActionEvent e) {
               Logger.debug("GUI_actionPerformed", "send message button");
               // TODO: check input
               EchoClient.sendMessage(chat_input_textfield.getText());
               
            }
      });
      chat_send_button.setBounds(670, 420, 120, 30);

      pan_chat.setLayout(null);

      // add elements to panel
      pan_chat.add(chat_users_list);
      pan_chat.add(chat_infos_textarea);
      pan_chat.add(chat_input_textfield);
      pan_chat.add(chat_send_button);

   }

   public void addConnectedUser(String _user) {

      // TODO: check if user is registered
      // if yes ->  connect (with *)

      int pos = this.users.indexOf(_user.trim());

      if (pos == -1) {
         // not registered
         this.users.add(_user);
      }

      this.connected_users.add(_user);
   }

   public void removeConnectedUser(String _user) {
      this.connected_users.remove(_user);
   }

   public void setConnectedUsers(String[] _users) {
      // _users : connected users

      for (String u: _users) {

         int pos = this.users.indexOf(u.trim());

         if (pos == -1) {
            // new user, not registered
            this.users.add(u);
         }

         this.connected_users.add(u);
      }
   }

   public void refreshUsers() {
      
      Logger.debug("GUI_refreshUsers", "in jlist: " + String.valueOf(chat_users_model.size()));

      int i = 0;
      while (i < chat_users_model.size()) {
         if (chat_selected_index == null || chat_users_model.get(i) != chat_selected_username) {
            try {
               chat_users_model.remove(i);
            } catch(Exception e) {
               Logger.error("GUI_refreshUsers", e.getMessage());
            }
            Logger.warning("GUI_refreshUsers", "removing");
         } else {
            i++;
         }
      }
      
      for (String u: this.users) {
         Logger.warning("GUI_refreshUsers", "adding : " + u);
         if ((chat_selected_username == null || u != chat_selected_username) && !u.equals(EchoClient.username)) {
            // check if connected
            int pos = this.connected_users.indexOf(u.trim());
            if (pos != -1) {
               // connected
               chat_users_model.addElement(u + '*');
            } else {
               chat_users_model.addElement(u);
            }
         }
      }

      pan_chat.repaint();
      pan_chat.revalidate();
   }

   public void switchToChatPanel() {
      container.removeAll();
      container.add(pan_chat);

      String[] connected = EchoClient.getConnectedUsers();
      setConnectedUsers(connected);
      refreshUsers();

      pan_chat.repaint();
      pan_chat.revalidate();
      container.revalidate();

      EchoClient.initMulticastPublic();
   }

   public void resetMessageArea() {
      chat_infos_textarea.setText("");
   }

   public void showMessages(List<String> _messages) {
      for (String s: _messages)
         showMessage(s + "\n");
   }

   public void showMessage(String _message) {
      chat_infos_textarea.append(_message);
   }
}