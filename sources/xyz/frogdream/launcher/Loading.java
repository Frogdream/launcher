package xyz.frogdream.launcher;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/* loaded from: C:\Users\User\AppData\Local\Temp\jadx-16181904278102483256\classes.dex */
public class Loading {
    public static void main(String[] args) {
        JPanel panel = new JPanel() { // from class: xyz.frogdream.launcher.Loading.1
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon image = Loading.loadImage();
                g.drawImage(image.getImage(), 0, 0, (ImageObserver) null);
            }
        };
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.getContentPane().add(panel);
        frame.setSize(250, 250);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screenSize.getWidth() - frame.getWidth()) / 2.0d);
        int y = (int) ((screenSize.getHeight() - frame.getHeight()) / 2.0d);
        frame.setLocation(x, y);
        frame.setVisible(true);
        FrogdreamLauncher.main(new String[0]);
        frame.dispose();
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frame.dispose();
    }

    private static ImageIcon loadImage() {
        return new ImageIcon((URL) Objects.requireNonNull(Loading.class.getResource("/Images/FDL.png")));
    }
}
