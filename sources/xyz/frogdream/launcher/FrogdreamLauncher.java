package xyz.frogdream.launcher;

import com.google.gson.Gson;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.apache.http.HttpStatus;
import org.objectweb.asm.Opcodes;
import xyz.frogdream.launcher.MainScreen;

/* loaded from: C:\Users\User\AppData\Local\Temp\jadx-16181904278102483256\classes.dex */
public class FrogdreamLauncher extends JFrame {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static Config config;
    static FrogdreamLauncher display;
    private static JLabel enterLabel;
    static String folderPath;
    static boolean isTextChanged = false;
    static JTextField nickname;

    /* loaded from: C:\Users\User\AppData\Local\Temp\jadx-16181904278102483256\classes.dex */
    public static class Config {
        public String nickName;
    }

    static {
        folderPath = System.getenv("LOCALAPPDATA") + "/FrogDreamCache";
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            folderPath = System.getProperty("user.home") + "/Library/FrogDreamCache";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FrogdreamLauncher() {
        initializeWindow();
    }

    public void initializeWindow() {
        ImageIcon logo = new ImageIcon((URL) Objects.requireNonNull(getClass().getResource("/Images/logo.png")));
        JLabel logoLabel = new JLabel(logo);
        Dimension size = logoLabel.getPreferredSize();
        logoLabel.setBounds((int) HttpStatus.SC_MULTIPLE_CHOICES, (int) HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, size.width, size.height);
        add(logoLabel);
    }

    public void center() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;
        setLocation(x, y);
    }

    public static Config loadConfig() {
        String text;
        String filePath = folderPath + "/autofill.json";
        try {
            text = Files.readString(Path.of(filePath, new String[0]));
        } catch (IOException e) {
            text = "{}";
        }
        return (Config) new Gson().fromJson(text, (Class<Object>) Config.class);
    }

    public static void saveConfig(Config config2) {
        String filePath = folderPath + "/autofill.json";
        try {
            Files.writeString(Path.of(filePath, new String[0]), new Gson().toJson(config2, Config.class), new OpenOption[0]);
        } catch (IOException e) {
            System.err.println("Config file not found.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            display = new FrogdreamLauncher();
            File folder = new File(folderPath);
            if (!folder.exists() && !folder.mkdirs()) {
                return;
            }
            Config loadConfig = loadConfig();
            config = loadConfig;
            if (loadConfig.nickName != null) {
                System.out.println("Auto-fill is successful, changing screen to MainScreen...");
                String enteredNickname = config.nickName;
                MainScreen mainscreen = new MainScreen();
                MainScreen.MainScreenInitializer.initialize(mainscreen, enteredNickname);
                display.setVisible(false);
                display.dispose();
                return;
            }
            System.out.println("Error with auto-fill, changing screen to FrogdreamLauncher...");
            display.setDefaultCloseOperation(3);
            display.setTitle("Frogdream Launcher");
            display.setSize(1022, 600);
            display.getContentPane().setBackground(new Color(12, 12, 12));
            display.setLayout(null);
            Component jLabel = new JLabel("Launcher");
            jLabel.setForeground(new Color((int) Opcodes.INEG, (int) Opcodes.JSR, 50));
            InputStream is = FrogdreamLauncher.class.getResourceAsStream("/Fonts/GolosText-Bold.ttf");
            try {
                Font font = Font.createFont(0, is);
                Font sizedFont = font.deriveFont(56.0f);
                jLabel.setFont(sizedFont);
                jLabel.setBounds(478, -19, 2000, 600);
                display.add(jLabel);
                nickname = new JTextField();
                InputStream nicknameFontStream = FrogdreamLauncher.class.getResourceAsStream("/Fonts/GolosText-Medium.ttf");
                try {
                    Font nicknameFont = Font.createFont(0, nicknameFontStream);
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(nicknameFont);
                    Font sizedNicknameFont = nicknameFont.deriveFont(16.0f);
                    nickname.setFont(sizedNicknameFont);
                    nickname.setText("Nickname");
                    nickname.setBorder((Border) null);
                    nickname.setOpaque(false);
                    final Color defaultTextColor = new Color(100, 101, 101);
                    nickname.setForeground(defaultTextColor);
                    nickname.setBounds(315, 375, 385, 60);
                    nickname.addMouseListener(new MouseAdapter() { // from class: xyz.frogdream.launcher.FrogdreamLauncher.1
                        public void mouseClicked(MouseEvent e) {
                            if (!FrogdreamLauncher.isTextChanged) {
                                FrogdreamLauncher.nickname.setText("");
                                FrogdreamLauncher.isTextChanged = true;
                                FrogdreamLauncher.nickname.setForeground(defaultTextColor);
                            }
                        }
                    });
                    ImageIcon rectangle = new ImageIcon((URL) Objects.requireNonNull(FrogdreamLauncher.class.getResource("/Images/rectangle.png")));
                    Component jLabel2 = new JLabel(rectangle);
                    Dimension rectangleSize = jLabel2.getPreferredSize();
                    jLabel2.setBounds(293, 380, rectangleSize.width, rectangleSize.height);
                    final ImageIcon enter = new ImageIcon((URL) Objects.requireNonNull(FrogdreamLauncher.class.getResource("/Images/enter.png")));
                    JLabel jLabel3 = new JLabel(enter);
                    enterLabel = jLabel3;
                    jLabel3.setBounds(704, 390, 30, 30);
                    enterLabel.addMouseListener(new MouseAdapter() { // from class: xyz.frogdream.launcher.FrogdreamLauncher.2
                        public void mouseEntered(MouseEvent e) {
                            FrogdreamLauncher.enterLabel.setIcon(FrogdreamLauncher.getBrighterIcon(enter));
                        }

                        public void mouseExited(MouseEvent e) {
                            FrogdreamLauncher.enterLabel.setIcon(enter);
                        }

                        public void mouseClicked(MouseEvent e) {
                            FrogdreamLauncher.performAction();
                        }
                    });
                    nickname.addKeyListener(new KeyAdapter() { // from class: xyz.frogdream.launcher.FrogdreamLauncher.3
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == 10) {
                                FrogdreamLauncher.performAction();
                            }
                        }
                    });
                    display.add(enterLabel);
                    display.add(nickname);
                    display.add(jLabel2);
                    display.center();
                    display.setResizable(false);
                    display.setVisible(true);
                } catch (IOException | FontFormatException var16) {
                    throw new RuntimeException(var16);
                }
            } catch (IOException | FontFormatException var17) {
                throw new RuntimeException(var17);
            }
        });
    }

    private static void performAction() {
        String enteredNickname = nickname.getText();
        if (!enteredNickname.matches("[a-zA-Z0-9_-]+")) {
            nickname.setText("Incorrect nickname!");
            nickname.setForeground(new Color(222, 45, 56));
        } else {
            MainScreen mainscreen = new MainScreen();
            MainScreen.MainScreenInitializer.initialize(mainscreen, enteredNickname);
            display.setVisible(false);
        }
        config.nickName = enteredNickname;
        saveConfig(config);
    }

    private static ImageIcon getBrighterIcon(ImageIcon icon) {
        Image img = icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(img.getWidth((ImageObserver) null), img.getHeight((ImageObserver) null), 2);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(img, 0, 0, (ImageObserver) null);
        graphics.dispose();
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                int r = (rgb >> 16) & 255;
                int g = (rgb >> 8) & 255;
                int b = rgb & 255;
                int r2 = Math.min((int) (r * 1.4f), 255);
                int g2 = Math.min((int) (g * 1.4f), 255);
                bufferedImage.setRGB(x, y, ((-16777216) & rgb) | (r2 << 16) | (g2 << 8) | Math.min((int) (b * 1.4f), 255));
            }
        }
        return new ImageIcon(bufferedImage);
    }
}
