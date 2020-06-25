package com.qdemiraj.chat;

/**
 * @author Qendrim Demiraj Created in May 21 2020 , all rights reserved
 *
 * Programi i krijuar sherben si Server i krijuar nga Sockets. Qellimi i
 * programit eshte komunikimi me kliente .
 */
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.TextComponent;

import javax.swing.JFrame;

import java.awt.TextArea;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.text.DefaultCaret;

public class Server {

    static Socket s;
    static ServerSocket ss;
    static Socket s2;
    static ServerSocket ss2;
    static DataInputStream din;
    static DataOutputStream dout;
    static DataInputStream din1;
    static DataOutputStream dout1;
    private static Color cl = new Color(48, 92, 92);

    private JFrame frame;
    private static JTextField msg_text;
    private static TextArea msg_area = new TextArea();
    private JButton btnAudio = new JButton("Regjistrimi i Audios");
    private JButton btnDergoAudio = new JButton("Dergo Audio");

    /**
     * Launch the application.
     */
    public void start() {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Server window = new Server();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        String msgin = "";
        Color cl_2 = new Color(139, 224, 149);
        msg_area.setBackground(cl_2);
        msg_area.setText("Serveri startoi ... \n");

        try {
            ss = new ServerSocket(1201);// serveri starton ne portin 1201
            s = ss.accept();// now server accepts the conn
            ss2 = new ServerSocket(1202);// komunikimi me klientin e pare
            s2 = ss2.accept();
            while (!msgin.equals("exit")) {

                if (s == null) {
                    ss = new ServerSocket(1201);
                    s = ss.accept();// now server accepts the conn
                }
                if (s2 == null) {
                    ss2 = new ServerSocket(1202);
                    s2 = ss2.accept();// now server accepts the conn
                }
                din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                din1 = new DataInputStream(s2.getInputStream());
                dout1 = new DataOutputStream(s2.getOutputStream());

                if (din.available() > 0) {
                    msgin = din.readUTF();
                    if (msgin.equals("Audio po fillon")) {
                        msg_area.setText(msg_area.getText().trim()
                                + "\n Boeing777 : ka derguar audio \t");
                        msg_area.setText(msg_area.getText().trim()
                                + "\n Audio ka filluar \t");

                        PlayAudio a = new PlayAudio();

                        a.playSound("RecordAudio.wav");
                        msg_area.setText(msg_area.getText().trim()
                                + "\n Audio ka mbaruar \t");

                    } else {

                        if (msgin.equals("Boeing777 :  shkeputi lidhjen")) {
                            msg_area.setText(msg_area.getText().trim() + "\n"
                                    + msgin);// shfaqja e msg
                            s.close();
                            ss.close();
                            s = null;
                            ss = null;

                        } else {
                            msg_area.setText(msg_area.getText().trim() + "\n"
                                    + msgin);// shfaqja e msg
                            msg_text.setText("");
                        }
                    }
                }

                if (din1.available() > 0) {
                    msgin = din1.readUTF();
                    if (msgin.equals("Audio po fillon")) {
                        msg_area.setText(msg_area.getText().trim()
                                + "\n FranceAir : ka derguar audio \t");
                        msg_area.setText(msg_area.getText().trim()
                                + "\n Audio ka filluar \t");

                        PlayAudio a = new PlayAudio();

                        a.playSound("RecordAudio.wav");
                        msg_area.setText(msg_area.getText().trim()
                                + "\n Audio ka mbaruar \t");

                    } else {

                        if (msgin.equals("FranceAir312 :  shkeputi lidhjen")) {
                            msg_area.setText(msg_area.getText().trim() + "\n"
                                    + msgin);// shfaqja e msg
                            s2.close();
                            ss2.close();
                            s2 = null;
                            ss2 = null;
                        } else {
                            msg_area.setText(msg_area.getText().trim() + "\n"
                                    + msgin);// shfaqja e msg
                            msg_text.setText("");
                        }
                    }
                }
            }

        } catch (Exception e) {

        }
    }

    /**
     * Create the application.
     */
    public Server() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 530, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setTitle("Serveri");
        btnAudio.setBackground(cl);
        btnAudio.setForeground(Color.white);

        msg_area.setBounds(24, 10, 461, 278);
        frame.getContentPane().add(msg_area);

        msg_text = new JTextField();
        msg_text.setBounds(40, 315, 241, 34);
        frame.getContentPane().add(msg_text);
        msg_text.setColumns(10);

        final JButton msg_send = new JButton("Send");
        msg_send.setBackground(cl);
        msg_send.setForeground(Color.white);
        msg_send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                try {
                    String msgout = "";
                    msgout = msg_text.getText().trim();
                    if (msgout.equals("exit")) {
                        ss.close();
                        ss2.close();
                        dout.writeUTF(msgout);// dergimi i msg te mesazhit te
                        dout1.writeUTF(msgout);// dergimi i msg te mesazhit te
                        System.exit(0);
                    }
                    if (msgout.length() > 5
                            && ((msgout.substring(0, 6)).toLowerCase())
                            .equals("boeing")) {
                        dout.writeUTF(msgout);// dergimi i msg te mesazhit te
                        // klienti
                        msg_text.setText("");

                    } else {
                        if (msgout.length() > 6
                                && ((msgout.substring(0, 6)).toLowerCase())
                                .equals("france")) {
                            dout1.writeUTF(msgout);// dergimi i msg te mesazhit
                            // te klienti
                            msg_text.setText("");

                        } else {
                            dout.writeUTF(msgout);// dergimi i msg te mesazhit
                            // te klienti
                            dout1.writeUTF(msgout);// dergimi i msg te mesazhit
                            // te klienti
                            msg_text.setText("");

                        }
                    }
                    msg_send.setBackground(cl);

                } catch (Exception e) {

                }
            }
        });
        btnAudio.setBounds(40, 380, 186, 40);
        frame.getContentPane().add(btnAudio);

        btnDergoAudio.setBackground(cl);
        btnDergoAudio.setForeground(Color.white);
        btnDergoAudio.setBounds(259, 380, 186, 40);
        btnDergoAudio.setEnabled(false);
        frame.getContentPane().add(btnDergoAudio);
        btnDergoAudio.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnDergoAudio.setEnabled(false);
                String msgaudio = "Audio po fillon";
                try {
                    dout.writeUTF(msgaudio);
                    dout1.writeUTF(msgaudio);
                    msg_area.setText(msg_area.getText().trim()
                            + "\nAudio u dergua me sukses ... \t");

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }
        });

        msg_send.setBounds(291, 312, 154, 40);
        frame.getContentPane().add(msg_send);
        
        JButton btnFilloKomunikiminMe = new JButton("Fillo komunikimin me video");
        btnFilloKomunikiminMe.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		try {
        		    Desktop.getDesktop().browse(new URL("http://localhost:3000/").toURI());
        		} catch (Exception e) {}

        	}
        });
        btnFilloKomunikiminMe.setForeground(Color.WHITE);
        btnFilloKomunikiminMe.setBackground(new Color(48, 92, 92));
        btnFilloKomunikiminMe.setBounds(40, 462, 405, 63);
        frame.getContentPane().add(btnFilloKomunikiminMe);
        
        JLabel lblStartoServerinNga = new JLabel("Starto node server.js nga Command promp para se te filloi komunikimi");
        lblStartoServerinNga.setBounds(40, 431, 425, 34);
        frame.getContentPane().add(lblStartoServerinNga);

        btnAudio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                final JavaSoundRecorder recorder = new JavaSoundRecorder();
				// creates a new thread that waits for a specified
                // of time before stopping
                btnDergoAudio.setEnabled(true);
                Thread stopper = new Thread(new Runnable() {

                    public void run() {

                        try {
                            msg_area.setText(msg_area.getText().trim()
                                    + "\nRexhistrimi i audios filloi ... \t");
                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }

                        recorder.finish();
                        msg_area.setText(msg_area.getText().trim()
                                + "\nRexhistrimi i audios mbaroi ... \t");
                    }
                });
                stopper.start();
                // start recording
                recorder.start();

            }

        });

    }

    public static void main(String args[]) {
        Server a = new Server();
        a.start();

    }
}
