package io.karthik.Messanger.Clients;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client2 extends JFrame implements ActionListener {
    String userName;
    PrintWriter pw;
    BufferedReader br;
    JTextField chatIp;
    JTextArea chatMsg;
    JButton send, exit;
    Socket chatUser;

    class  MessagesThread extends Thread {
        @Override
        public void run() {
            String line;
            try {
                while(true) {
                    line = br.readLine();
                    chatMsg.append(line + "\n");
                }
            } catch(Exception ex) {}
        }
    }

    public Client2(String userName, String serverName) throws IOException {
        super(userName);
        this.userName = userName;
        chatUser = new Socket(serverName,90);
        br = new BufferedReader(new InputStreamReader(chatUser.getInputStream()));
        pw = new PrintWriter(chatUser.getOutputStream());
        pw.println(userName);
        buildInterface();
        new MessagesThread().start();
    }

    private void buildInterface() {
        send = new JButton("Send");
        exit = new JButton("Exit");
        chatMsg = new JTextArea();
        chatMsg.setRows(30);
        chatMsg.setColumns(50);
        JScrollPane sp = new JScrollPane(chatMsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp, "Center");
        JPanel bp = new JPanel(new FlowLayout());
        bp.add(chatIp);
        bp.add(send);
        bp.add(exit);
        bp.setBackground(Color.CYAN);
        bp.setName("Instant Messenger");
        add(bp, "North");
        send.addActionListener(this);
        exit.addActionListener(this);
        setSize(500,300);
        setVisible(true);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit) {
            pw.println("end");
            System.exit(0);
        } else {
            pw.println(chatIp.getText());
            chatIp.setText(null);
        }
    }

    public static void main(String[] args) {
        String userName = JOptionPane.showInputDialog(null, "Please enter your name here to begin chatting:", "Instant Chat Application",JOptionPane.PLAIN_MESSAGE);
        String serverName = "localhost";
        try {
            new Client(userName,serverName);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
