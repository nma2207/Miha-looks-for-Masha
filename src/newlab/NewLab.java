/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlab;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;

/**
 *
 * @author Пользователь
 */
public class NewLab extends JFrame {

    JPanel panel;
    Animator A;

    public NewLab() {
//        this.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                //button.setBounds(e.getX(), e.getY(), button.getWidth(), button.getHeight());
//
//                
//                //System.out.println(e.getX()+" "+ e.getY());
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//
//            }
//        });

        setSize(750, 780);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);

        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(0, 0, 750, 780);
        add(panel);

        setVisible(true);
        A = new Animator(panel.getGraphics());
        Thread thread = new Thread(A);
        //A.Draw();
        thread.start();
        this.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {

                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_DOWN:
                        A.down();
                        break;
                    case KeyEvent.VK_UP:
                        A.up();
                        break;
                    case KeyEvent.VK_LEFT:
                        A.left();
                        break;
                    case KeyEvent.VK_RIGHT:
                        A.right();
                        break;
                }

            }

            public void keyReleased(KeyEvent e) {

            }

            public void keyTyped(KeyEvent e) {
            }

        });
        // A = new Animator(panel.getGraphics(),this.getGraphics());
        // Thread thread = new Thread(A);
        //A.Draw();
        // thread.start();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new NewLab();
        // TODO code application logic here
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (A != null) {
            A.Draw();
        }
    }
}
