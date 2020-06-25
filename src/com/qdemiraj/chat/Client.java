package com.qdemiraj.chat;

import java.awt.*;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * @author Qendrim Demiraj Created in May 21 2020 , all rights reserved
 *
 * Programi i krijuar sherben si Klient i krijuar nga Sockets. Qellimi i
 * programit eshte komunikimi me Server .
 */
public class Client {

    static Socket s;
    static Socket s1;
    static DataInputStream din;
    static DataOutputStream dout;
    static Color color1 = new Color(194, 76, 81);
    static Color color2 = new Color(48, 92, 92);

    private static JFrame frame = new JFrame();
    private static JTextField msg_text = new JTextField();

    private static TextArea msg_area = new TextArea();
    public static String emriAroplanit = "Boeing777 : ";
    public static String msgout = "";
    private static final JButton button = new JButton("Regjistrimi i Audios");
    private static final JButton button_1 = new JButton("Dergo Audio");
    private static final JButton button_2 = new JButton("Fillo komunikimin me video");
    private static final JLabel label = new JLabel("Starto node server.js nga Command promp para se te filloi komunikimi");

    /**
     * Launch the application.
     */
   public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Client window = new Client();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        msg_area.setBackground(color1);

        button.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent arg0) {
button_1.setEnabled(true);
                final JavaSoundRecorder recorder = new JavaSoundRecorder();
                // creates a new thread that waits for a specified
                // of time before stopping

                Thread stopper = new Thread(new Runnable() {

                    public void run() {

                        try {
                            msg_area.setText(msg_area.getText().trim() + "\nRexhistrimi i audios filloi ... \t");
                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }

                        recorder.finish();
                        msg_area.setText(msg_area.getText().trim() + "\nRexhistrimi i audios mbaroi ... \t");
                    }
                });
                stopper.start();
                // start recording
                recorder.start();

            }

        });
        button.setBounds(41, 380, 186, 40);
        button.setBackground(color2);
        button.setForeground(Color.white);
        frame.getContentPane().add(button);
        
        
         button_1.setBounds(260, 380, 186, 40);
        button_1.setBackground(color2);
        button_1.setForeground(Color.white);
button_1.setEnabled(false);
        frame.getContentPane().add(button_1);
        button_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        		    Desktop.getDesktop().browse(new URL("http://localhost:3000/").toURI());
        		} catch (Exception e2) {}

        	
        	}
        });
        button_2.setForeground(Color.WHITE);
        button_2.setBackground(new Color(48, 92, 92));
        button_2.setBounds(41, 460, 405, 63);
        
        frame.getContentPane().add(button_2);
        label.setBounds(41, 431, 425, 34);
        
        frame.getContentPane().add(label);
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button_1.setEnabled(false);
                String msgaudio = "Audio po fillon";
                try {
                    dout.writeUTF(msgaudio);
                    msg_area.setText(msg_area.getText().trim() + "\nAudio u dergua me sukses ... \t");

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
       
        while (true) {
            try {
                msg_area.setText(emriAroplanit + " startoi ... \n");
                s = new Socket("127.0.0.1", 1201);//Adresa lokale e komunikimit pranaim
                din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                String msgin = "";

                dout.writeUTF(emriAroplanit + " startoi lidhjen me server .");

                while (!msgin.equals("exit")) {
                    msgin = din.readUTF();
                    if (msgin.equals("Audio po fillon")) {
                        msg_area.setText(msg_area.getText().trim() + "\n Serveri ka derguar audio \t");
                        msg_area.setText(msg_area.getText().trim() + "\n Audio ka filluar \t");

                        PlayAudio a = new PlayAudio();

                        a.playSound("RecordAudio.wav");
                        msg_area.setText(msg_area.getText().trim() + "\n Audio ka mbaruar \t");

                    } else {
                        msg_area.setText(msg_area.getText().trim() + "\n Server : \t" + msgin);
                        msg_text.setText("");
                    }
                }
                System.exit(0);

            } catch (Exception e) {
            }
        }
    }

    /**
     * Create the application.
     */
    public Client() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        frame.setBounds(100, 100, 530, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setTitle(emriAroplanit);

        msg_area.setBounds(21, 10, 469, 285);
        frame.getContentPane().add(msg_area);

        msg_text.setBounds(41, 315, 253, 34);
        frame.getContentPane().add(msg_text);
        msg_text.setColumns(10);

        JButton msg_send = new JButton("Send");
        msg_send.setBackground(color2);
        msg_send.setForeground(Color.white);
        msg_send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    String msgout = "";
                    msgout = msg_text.getText().trim();
                    if (msgout.equals("exit")) {
                        dout.writeUTF(emriAroplanit + " shkeputi lidhjen");//dergimi i msg te mesazhit te klienti
                        System.exit(0);
                    }
                    dout.writeUTF(emriAroplanit + msgout);//dergimi i msg te mesazhit te klienti
                    msg_text.setText("");

                } catch (Exception e) {

                }
            }
        });
        msg_send.setBounds(316, 312, 130, 40);
        frame.getContentPane().add(msg_send);
        
        
    }
    
    

}
