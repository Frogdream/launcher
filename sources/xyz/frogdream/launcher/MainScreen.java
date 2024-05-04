package xyz.frogdream.launcher;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import org.apache.http.client.methods.HttpGet;
import org.to2mbn.jmccc.internal.org.json.JSONArray;
import org.to2mbn.jmccc.internal.org.json.JSONException;
import org.to2mbn.jmccc.internal.org.json.JSONObject;

/* loaded from: C:\Users\User\AppData\Local\Temp\jadx-16181904278102483256\classes.dex */
public class MainScreen extends JFrame {
    static String currentVersion;
    private static Color defaultTextColor;
    private static Color defaultTextColor5;
    private static Color defaultTextColorSettings;
    private static Color defaultTextColorWhite;
    private static String downloadDirectory;
    private static final boolean isFreecamOn;
    private static final boolean isOptimizationOn;
    private static final boolean isUpdatesOn;
    private static final boolean isWhiteTheme;
    private static boolean makeFreecam;
    private static boolean makeOptimization;
    private static boolean makeUpdateCheck;
    private static boolean makeWhiteTheme;
    private static String valueFromFile;
    static int xmx;

    public MainScreen() {
        checkFolder();
        checkGithubRelease();
        setTitle("Frogdream Launcher");
        setDefaultCloseOperation(3);
        setSize(1022, 600);
        getContentPane().setBackground(new Color(12, 12, 12));
        setLayout(null);
        setResizable(false);
        centerWindow();
    }

    private void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;
        setLocation(x, y);
    }

    private void checkGithubRelease() {
        GithubReleaseChecker releaseChecker = new GithubReleaseChecker();
        releaseChecker.checkGithubRelease("Frogdream", "Launcher", "v1.3.0");
    }

    public static void checkFolder() {
        String targetFolder = getGamePath();
        File folder = new File(targetFolder);
        if (folder.exists() && folder.isDirectory()) {
            System.out.println("Game folder exists.");
        } else {
            System.out.println("Game folder doesn't exist. Creating will be started.");
        }
    }

    private static void openDownloadsFolder() {
        String downloadDirectory2;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            downloadDirectory2 = System.getenv("LOCALAPPDATA") + "\\.FrogDream";
        } else if (osName.contains("mac")) {
            downloadDirectory2 = System.getProperty("user.home") + "/.FrogDream";
        } else if (osName.contains("linux")) {
            downloadDirectory2 = System.getProperty("user.home") + "/.FrogDream";
        } else {
            JOptionPane.showMessageDialog((Component) null, "Your system is not supported. Please, contact with developers.");
            return;
        }
        File file = new File(downloadDirectory2);
        if (!file.exists()) {
            JOptionPane.showMessageDialog((Component) null, "Game folder doesn't exist. It's because you didn't start the game yet.");
            System.out.println("Folder: " + downloadDirectory2 + " doesn't exist.");
            return;
        }
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException var1) {
            throw new RuntimeException(var1);
        }
    }

    private static void checkSettings() {
        makeOptimization = isOptimizationOn;
        makeUpdateCheck = isUpdatesOn;
        makeWhiteTheme = isWhiteTheme;
        makeFreecam = isFreecamOn;
    }

    private static String getAppDataPath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String appDataPath = System.getenv("LOCALAPPDATA") + "\\FrogDreamCache\\";
            return appDataPath;
        } else if (os.contains("mac")) {
            String appDataPath2 = System.getProperty("user.home") + "/Library/FrogDreamCache/";
            return appDataPath2;
        } else {
            String appDataPath3 = System.getProperty("user.home");
            return appDataPath3 + "/.FrogDreamCache/";
        }
    }

    private static String getGamePath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String gamePath = System.getenv("LOCALAPPDATA") + "\\.FrogDream";
            return gamePath;
        } else if (os.contains("mac")) {
            String gamePath2 = System.getProperty("user.home") + "/.FrogDream";
            return gamePath2;
        } else {
            String gamePath3 = System.getProperty("user.home");
            return gamePath3 + "/.FrogDream";
        }
    }

    static {
        String appDataPath = getAppDataPath();
        isOptimizationOn = readBooleanValueFromFile(appDataPath + "optimization.txt");
        isUpdatesOn = readBooleanValueFromFile(appDataPath + "updates.txt");
        isWhiteTheme = readBooleanValueFromFile(appDataPath + "whitetheme.txt");
        isFreecamOn = readBooleanValueFromFile(appDataPath + "freecam.txt");
    }

    /* loaded from: C:\Users\User\AppData\Local\Temp\jadx-16181904278102483256\classes.dex */
    public static class GithubReleaseChecker {
        public void checkGithubRelease(String repoOwner, String repoName, String currentVersion) {
            HttpURLConnection connection;
            int i;
            int option = 1;
            String url = String.format("https://api.github.com/repos/%s/%s/releases", repoOwner, repoName);
            try {
                URL apiUrl = new URL(url);
                connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod(HttpGet.METHOD_NAME);
            } catch (IOException | JSONException e) {
                e = e;
            } catch (URISyntaxException e2) {
                e = e2;
            }
            try {
                if (connection.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        response.append(line);
                    }
                    reader.close();
                    JSONArray releases = new JSONArray(response.toString());
                    MainScreen.checkSettings();
                    int i2 = 0;
                    while (i2 < releases.length()) {
                        JSONObject release = releases.getJSONObject(i2);
                        String tagName = release.getString("tag_name");
                        if (!isNewerVersion(tagName, currentVersion)) {
                            i = option;
                        } else {
                            System.out.println("New version available: " + tagName + ".");
                            if (!MainScreen.makeUpdateCheck) {
                                i = option;
                            } else {
                                int option2 = JOptionPane.showOptionDialog((Component) null, "New version is available!", "Launcher update", 0, 1, (Icon) null, new String[]{"Later", "Update"}, "Update");
                                if (option2 == 1) {
                                    Desktop.getDesktop().browse(new URI("https://github.com/Frogdream/Launcher/releases"));
                                    return;
                                }
                                return;
                            }
                        }
                        i2++;
                        option = i;
                    }
                    System.out.println("Launcher is up to date.");
                    return;
                }
                System.out.println("Failed to check for updates.");
            } catch (IOException | JSONException e3) {
                e = e3;
                e.printStackTrace();
            } catch (URISyntaxException e4) {
                e = e4;
                throw new RuntimeException(e);
            }
        }

        private boolean isNewerVersion(String newVersion, String currentVersion) {
            String newVersion2 = newVersion.replaceAll("^(v|-alpha)", "");
            String currentVersion2 = currentVersion.replaceAll("^(v|-alpha)", "");
            String[] newVersionParts = newVersion2.split("\\.");
            String[] currentVersionParts = currentVersion2.split("\\.");
            int minLength = Math.min(newVersionParts.length, currentVersionParts.length);
            for (int i = 0; i < minLength; i++) {
                int newPart = parseVersionPart(newVersionParts[i]);
                int currentPart = parseVersionPart(currentVersionParts[i]);
                if (newPart > currentPart) {
                    return true;
                }
                if (newPart < currentPart) {
                    return false;
                }
            }
            int i2 = newVersionParts.length;
            return i2 > currentVersionParts.length;
        }

        private int parseVersionPart(String versionPart) {
            if (versionPart.contains("-alpha")) {
                versionPart = versionPart.replace("-alpha", "");
            }
            return Integer.parseInt(versionPart);
        }
    }

    /* loaded from: C:\Users\User\AppData\Local\Temp\jadx-16181904278102483256\classes.dex */
    public static class MainScreenInitializer {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        /*  JADX ERROR: IndexOutOfBoundsException in pass: ConstInlineVisitor
            java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 1
            	at java.base/jdk.internal.util.Preconditions.outOfBounds(Unknown Source)
            	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Unknown Source)
            	at java.base/jdk.internal.util.Preconditions.checkIndex(Unknown Source)
            	at java.base/java.util.Objects.checkIndex(Unknown Source)
            	at java.base/java.util.ArrayList.get(Unknown Source)
            	at jadx.core.dex.visitors.ConstInlineVisitor.needExplicitCast(ConstInlineVisitor.java:282)
            	at jadx.core.dex.visitors.ConstInlineVisitor.replaceArg(ConstInlineVisitor.java:263)
            	at jadx.core.dex.visitors.ConstInlineVisitor.replaceConst(ConstInlineVisitor.java:181)
            	at jadx.core.dex.visitors.ConstInlineVisitor.checkInsn(ConstInlineVisitor.java:109)
            	at jadx.core.dex.visitors.ConstInlineVisitor.process(ConstInlineVisitor.java:53)
            	at jadx.core.dex.visitors.ConstInlineVisitor.visit(ConstInlineVisitor.java:45)
            */
        public static void initialize(xyz.frogdream.launcher.MainScreen r114, java.lang.String r115) {
            /*
                Method dump skipped, instructions count: 2015
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: xyz.frogdream.launcher.MainScreen.MainScreenInitializer.initialize(xyz.frogdream.launcher.MainScreen, java.lang.String):void");
        }

        /* renamed from: xyz.frogdream.launcher.MainScreen$MainScreenInitializer$1ActiveContainer */
        /* loaded from: C:\Users\User\AppData\Local\Temp\jadx-16181904278102483256\classes.dex */
        public class C1ActiveContainer {
            boolean active;

            C1ActiveContainer() {
            }
        }

        /* renamed from: xyz.frogdream.launcher.MainScreen$MainScreenInitializer$16 */
        /* loaded from: C:\Users\User\AppData\Local\Temp\jadx-16181904278102483256\classes.dex */
        public class AnonymousClass16 extends MouseAdapter {
            boolean click = false;
            AtomicReference<Float> currentBrightness = new AtomicReference<>(Float.valueOf(1.1f));
            final /* synthetic */ C1ActiveContainer val$activeContainer;
            final /* synthetic */ JLabel val$go2Label;
            final /* synthetic */ JLabel val$go3Label;
            final /* synthetic */ boolean[] val$isSettingsLabelClicked;
            final /* synthetic */ boolean[] val$isVersionLabelClicked;
            final /* synthetic */ MainScreen val$mainScreen;
            final /* synthetic */ JLabel val$mapLabel;
            final /* synthetic */ JLabel val$mapTextLabel;
            final /* synthetic */ JLabel val$newsLabel;
            final /* synthetic */ JLabel val$newsTextLabel;
            final /* synthetic */ JLabel val$nofivesmallrectangleTextLabel;
            final /* synthetic */ JLabel val$nofoursmallrectangleTextLabel;
            final /* synthetic */ JLabel val$nosmallrectangleTextLabel;
            final /* synthetic */ JLabel val$nothreesmallrectangleTextLabel;
            final /* synthetic */ JLabel val$notwosmallrectangleTextLabel;
            final /* synthetic */ JLabel val$offLabel;
            final /* synthetic */ JLabel val$onLabel;
            final /* synthetic */ JLabel val$onUpdatesLabel;
            final /* synthetic */ JLabel val$settingsTextLabel;
            final /* synthetic */ JLabel val$settingsrectangleLabel;
            final /* synthetic */ JLabel val$smallrectangleLabel;
            final /* synthetic */ JLabel val$smallrectangleTextLabel;
            final /* synthetic */ JTextField val$smallrectangleTextLabel2;
            final /* synthetic */ JLabel val$smallrectangleTextLabel3;
            final /* synthetic */ JLabel val$smallrectangleTextLabel4;
            final /* synthetic */ JLabel val$smallrectangleTextLabel5;
            final /* synthetic */ JLabel val$smallrectangleTextLabel6;
            final /* synthetic */ ImageIcon val$version;
            final /* synthetic */ JLabel val$versionChooserLabel;
            final /* synthetic */ JLabel val$versionLabel;
            final /* synthetic */ JLabel val$versionsTextLabel;
            final /* synthetic */ JLabel val$whiteOffLabel;
            final /* synthetic */ JLabel val$whiteOnLabel;
            final /* synthetic */ JLabel val$whiteOnUpdatesLabel;
            final /* synthetic */ JLabel val$whiteSettingsrectangleLabel;
            final /* synthetic */ JLabel val$whiteSmallrectangleLabel;

            AnonymousClass16(MainScreen mainScreen, JLabel jLabel, ImageIcon imageIcon, boolean[] zArr, boolean[] zArr2, C1ActiveContainer c1ActiveContainer, JLabel jLabel2, JLabel jLabel3, JLabel jLabel4, JLabel jLabel5, JLabel jLabel6, JLabel jLabel7, JLabel jLabel8, JLabel jLabel9, JLabel jLabel10, JLabel jLabel11, JLabel jLabel12, JLabel jLabel13, JLabel jLabel14, JLabel jLabel15, JLabel jLabel16, JLabel jLabel17, JLabel jLabel18, JLabel jLabel19, JLabel jLabel20, JTextField jTextField, JLabel jLabel21, JLabel jLabel22, JLabel jLabel23, JLabel jLabel24, JLabel jLabel25, JLabel jLabel26, JLabel jLabel27, JLabel jLabel28, JLabel jLabel29, JLabel jLabel30) {
                this.val$mainScreen = mainScreen;
                this.val$versionLabel = jLabel;
                this.val$version = imageIcon;
                this.val$isVersionLabelClicked = zArr;
                this.val$isSettingsLabelClicked = zArr2;
                this.val$activeContainer = c1ActiveContainer;
                this.val$versionChooserLabel = jLabel2;
                this.val$versionsTextLabel = jLabel3;
                this.val$nosmallrectangleTextLabel = jLabel4;
                this.val$notwosmallrectangleTextLabel = jLabel5;
                this.val$nothreesmallrectangleTextLabel = jLabel6;
                this.val$nofoursmallrectangleTextLabel = jLabel7;
                this.val$nofivesmallrectangleTextLabel = jLabel8;
                this.val$smallrectangleLabel = jLabel9;
                this.val$whiteSettingsrectangleLabel = jLabel10;
                this.val$settingsrectangleLabel = jLabel11;
                this.val$whiteOnLabel = jLabel12;
                this.val$whiteOnUpdatesLabel = jLabel13;
                this.val$whiteOffLabel = jLabel14;
                this.val$onLabel = jLabel15;
                this.val$onUpdatesLabel = jLabel16;
                this.val$offLabel = jLabel17;
                this.val$settingsTextLabel = jLabel18;
                this.val$whiteSmallrectangleLabel = jLabel19;
                this.val$smallrectangleTextLabel = jLabel20;
                this.val$smallrectangleTextLabel2 = jTextField;
                this.val$smallrectangleTextLabel3 = jLabel21;
                this.val$smallrectangleTextLabel4 = jLabel22;
                this.val$smallrectangleTextLabel5 = jLabel23;
                this.val$smallrectangleTextLabel6 = jLabel24;
                this.val$mapLabel = jLabel25;
                this.val$mapTextLabel = jLabel26;
                this.val$newsLabel = jLabel27;
                this.val$newsTextLabel = jLabel28;
                this.val$go2Label = jLabel29;
                this.val$go3Label = jLabel30;
            }

            public void mouseEntered(MouseEvent e) {
                this.val$mainScreen.setCursor(Cursor.getPredefinedCursor(12));
                this.val$versionLabel.setIcon(MainScreen.getBrighterIcon(this.val$version, 1.2f));
            }

            public void mouseExited(MouseEvent e) {
                this.val$mainScreen.setCursor(Cursor.getDefaultCursor());
                this.val$versionLabel.setIcon(MainScreen.getBrighterIcon(this.val$version, 1.0f));
            }

            public void mouseClicked(MouseEvent e) {
                boolean[] zArr = this.val$isVersionLabelClicked;
                zArr[0] = true;
                if (!this.click) {
                    if (this.val$isSettingsLabelClicked[0]) {
                        return;
                    }
                    this.click = true;
                    this.val$activeContainer.active = true;
                    this.val$versionChooserLabel.setVisible(true);
                    this.val$versionsTextLabel.setVisible(true);
                    this.val$nosmallrectangleTextLabel.setVisible(true);
                    this.val$notwosmallrectangleTextLabel.setVisible(true);
                    this.val$nothreesmallrectangleTextLabel.setVisible(true);
                    this.val$nofoursmallrectangleTextLabel.setVisible(true);
                    this.val$nofivesmallrectangleTextLabel.setVisible(true);
                    if (this.val$isSettingsLabelClicked[0]) {
                        this.val$smallrectangleLabel.setVisible(false);
                        if (MainScreen.makeWhiteTheme) {
                            this.val$whiteSettingsrectangleLabel.setVisible(false);
                            this.val$mainScreen.add(this.val$whiteSettingsrectangleLabel);
                        } else {
                            this.val$settingsrectangleLabel.setVisible(false);
                            this.val$mainScreen.add(this.val$settingsrectangleLabel);
                        }
                        if (MainScreen.makeWhiteTheme) {
                            this.val$mainScreen.remove(this.val$whiteOnLabel);
                            this.val$mainScreen.remove(this.val$whiteOnUpdatesLabel);
                            this.val$mainScreen.remove(this.val$whiteOffLabel);
                        }
                        this.val$mainScreen.remove(this.val$onLabel);
                        this.val$mainScreen.remove(this.val$onUpdatesLabel);
                        this.val$mainScreen.remove(this.val$offLabel);
                        this.val$mainScreen.remove(this.val$settingsTextLabel);
                        if (MainScreen.makeWhiteTheme) {
                            this.val$mainScreen.remove(this.val$whiteSmallrectangleLabel);
                        } else {
                            this.val$mainScreen.remove(this.val$smallrectangleLabel);
                        }
                        this.val$mainScreen.remove(this.val$smallrectangleTextLabel);
                        this.val$mainScreen.remove(this.val$smallrectangleTextLabel2);
                        this.val$mainScreen.remove(this.val$smallrectangleTextLabel3);
                        this.val$mainScreen.remove(this.val$smallrectangleTextLabel4);
                        this.val$mainScreen.remove(this.val$smallrectangleTextLabel5);
                        this.val$mainScreen.remove(this.val$smallrectangleTextLabel6);
                    }
                    this.val$mainScreen.repaint();
                    int delay = System.getProperty("os.name").toLowerCase().contains("mac") ? 0 : -2000;
                    Timer initialTimer = new Timer(delay, new ActionListener() { // from class: xyz.frogdream.launcher.MainScreen.MainScreenInitializer.16.1
                        private int frameCount = 2;

                        {
                            AnonymousClass16.this = this;
                        }

                        public void actionPerformed(ActionEvent e2) {
                            int i = this.frameCount;
                            if (i < 50) {
                                float fraction = i / 50;
                                AnonymousClass16.this.moveLabels((int) (r2 * fraction));
                                this.frameCount++;
                                return;
                            }
                            ((Timer) e2.getSource()).stop();
                        }
                    });
                    initialTimer.setRepeats(true);
                    initialTimer.start();
                    return;
                }
                zArr[0] = false;
                this.click = false;
                this.val$activeContainer.active = false;
                int delay2 = System.getProperty("os.name").toLowerCase().contains("mactest") ? 0 : -2000;
                Timer timer = new Timer(delay2, new ActionListener() { // from class: xyz.frogdream.launcher.MainScreen.MainScreenInitializer.16.2
                    private int frameCount = 2;

                    {
                        AnonymousClass16.this = this;
                    }

                    public void actionPerformed(ActionEvent e2) {
                        int i = this.frameCount;
                        if (i < 50) {
                            float fraction = i / 50;
                            AnonymousClass16.this.moveLabels((int) ((-r2) * fraction));
                            this.frameCount++;
                            return;
                        }
                        ((Timer) e2.getSource()).stop();
                        AnonymousClass16.this.cleanupAfterAnimation();
                    }
                });
                timer.setRepeats(true);
                timer.start();
            }

            private void moveLabels(int xOffset) {
                JLabel jLabel = this.val$mapLabel;
                jLabel.setLocation(jLabel.getX() + xOffset, this.val$mapLabel.getY());
                JLabel jLabel2 = this.val$mapTextLabel;
                jLabel2.setLocation(jLabel2.getX() + xOffset, this.val$mapTextLabel.getY());
                JLabel jLabel3 = this.val$newsLabel;
                jLabel3.setLocation(jLabel3.getX() + xOffset + (xOffset / 4), this.val$newsLabel.getY());
                JLabel jLabel4 = this.val$newsTextLabel;
                jLabel4.setLocation(jLabel4.getX() + xOffset + (xOffset / 4), this.val$newsTextLabel.getY());
                JLabel jLabel5 = this.val$go2Label;
                jLabel5.setLocation(jLabel5.getX() + xOffset + (xOffset / 4), this.val$go2Label.getY());
                JLabel jLabel6 = this.val$go3Label;
                jLabel6.setLocation(jLabel6.getX() + xOffset, this.val$go3Label.getY());
            }

            private void cleanupAfterAnimation() {
                this.val$versionChooserLabel.setVisible(false);
                this.val$versionsTextLabel.setVisible(false);
                this.val$nosmallrectangleTextLabel.setVisible(false);
                this.val$notwosmallrectangleTextLabel.setVisible(false);
                this.val$nothreesmallrectangleTextLabel.setVisible(false);
                this.val$nofoursmallrectangleTextLabel.setVisible(false);
                this.val$nofivesmallrectangleTextLabel.setVisible(false);
                this.val$mainScreen.repaint();
            }
        }

        /* renamed from: xyz.frogdream.launcher.MainScreen$MainScreenInitializer$17 */
        /* loaded from: C:\Users\User\AppData\Local\Temp\jadx-16181904278102483256\classes.dex */
        public class AnonymousClass17 extends MouseAdapter {
            boolean click = false;
            final /* synthetic */ C1ActiveContainer val$activeContainer;
            final /* synthetic */ JLabel val$go2Label;
            final /* synthetic */ JLabel val$go3Label;
            final /* synthetic */ boolean[] val$isSettingsLabelClicked;
            final /* synthetic */ boolean[] val$isVersionLabelClicked;
            final /* synthetic */ MainScreen val$mainScreen;
            final /* synthetic */ JLabel val$mapLabel;
            final /* synthetic */ JLabel val$mapTextLabel;
            final /* synthetic */ JLabel val$newsLabel;
            final /* synthetic */ JLabel val$newsTextLabel;
            final /* synthetic */ JLabel val$nofivesmallrectangleTextLabel;
            final /* synthetic */ JLabel val$nofoursmallrectangleTextLabel;
            final /* synthetic */ JLabel val$nosmallrectangleTextLabel;
            final /* synthetic */ JLabel val$nothreesmallrectangleTextLabel;
            final /* synthetic */ JLabel val$notwosmallrectangleTextLabel;
            final /* synthetic */ JLabel val$offLabel;
            final /* synthetic */ JLabel val$onLabel;
            final /* synthetic */ JLabel val$onUpdatesLabel;
            final /* synthetic */ ImageIcon val$settings;
            final /* synthetic */ JLabel val$settingsLabel;
            final /* synthetic */ JLabel val$settingsTextLabel;
            final /* synthetic */ JLabel val$settingsrectangleLabel;
            final /* synthetic */ JLabel val$smallrectangleLabel;
            final /* synthetic */ JLabel val$smallrectangleTextLabel;
            final /* synthetic */ JTextField val$smallrectangleTextLabel2;
            final /* synthetic */ JLabel val$smallrectangleTextLabel3;
            final /* synthetic */ JLabel val$smallrectangleTextLabel4;
            final /* synthetic */ JLabel val$smallrectangleTextLabel5;
            final /* synthetic */ JLabel val$smallrectangleTextLabel6;
            final /* synthetic */ JLabel val$versionChooserLabel;
            final /* synthetic */ JLabel val$versionsTextLabel;
            final /* synthetic */ JLabel val$whiteOffLabel;
            final /* synthetic */ JLabel val$whiteOnLabel;
            final /* synthetic */ JLabel val$whiteOnUpdatesLabel;
            final /* synthetic */ JLabel val$whiteSettingsrectangleLabel;
            final /* synthetic */ JLabel val$whiteSmallrectangleLabel;

            AnonymousClass17(JLabel jLabel, ImageIcon imageIcon, boolean[] zArr, C1ActiveContainer c1ActiveContainer, boolean[] zArr2, JLabel jLabel2, JLabel jLabel3, JLabel jLabel4, JLabel jLabel5, JLabel jLabel6, JLabel jLabel7, JLabel jLabel8, MainScreen mainScreen, JLabel jLabel9, JLabel jLabel10, JLabel jLabel11, JLabel jLabel12, JLabel jLabel13, JLabel jLabel14, JLabel jLabel15, JLabel jLabel16, JTextField jTextField, JLabel jLabel17, JLabel jLabel18, JLabel jLabel19, JLabel jLabel20, JLabel jLabel21, JLabel jLabel22, JLabel jLabel23, JLabel jLabel24, JLabel jLabel25, JLabel jLabel26, JLabel jLabel27, JLabel jLabel28, JLabel jLabel29, JLabel jLabel30) {
                this.val$settingsLabel = jLabel;
                this.val$settings = imageIcon;
                this.val$isSettingsLabelClicked = zArr;
                this.val$activeContainer = c1ActiveContainer;
                this.val$isVersionLabelClicked = zArr2;
                this.val$versionChooserLabel = jLabel2;
                this.val$versionsTextLabel = jLabel3;
                this.val$nosmallrectangleTextLabel = jLabel4;
                this.val$notwosmallrectangleTextLabel = jLabel5;
                this.val$nothreesmallrectangleTextLabel = jLabel6;
                this.val$nofoursmallrectangleTextLabel = jLabel7;
                this.val$nofivesmallrectangleTextLabel = jLabel8;
                this.val$mainScreen = mainScreen;
                this.val$smallrectangleTextLabel = jLabel9;
                this.val$settingsTextLabel = jLabel10;
                this.val$whiteOnUpdatesLabel = jLabel11;
                this.val$whiteOnLabel = jLabel12;
                this.val$whiteOffLabel = jLabel13;
                this.val$onUpdatesLabel = jLabel14;
                this.val$onLabel = jLabel15;
                this.val$offLabel = jLabel16;
                this.val$smallrectangleTextLabel2 = jTextField;
                this.val$smallrectangleTextLabel3 = jLabel17;
                this.val$smallrectangleTextLabel4 = jLabel18;
                this.val$smallrectangleTextLabel5 = jLabel19;
                this.val$smallrectangleTextLabel6 = jLabel20;
                this.val$whiteSmallrectangleLabel = jLabel21;
                this.val$smallrectangleLabel = jLabel22;
                this.val$whiteSettingsrectangleLabel = jLabel23;
                this.val$settingsrectangleLabel = jLabel24;
                this.val$mapLabel = jLabel25;
                this.val$mapTextLabel = jLabel26;
                this.val$newsLabel = jLabel27;
                this.val$newsTextLabel = jLabel28;
                this.val$go2Label = jLabel29;
                this.val$go3Label = jLabel30;
            }

            public void mouseEntered(MouseEvent e) {
                if (this.click) {
                    this.val$settingsLabel.setCursor(Cursor.getPredefinedCursor(12));
                }
                this.val$settingsLabel.setIcon(MainScreen.getBrighterIcon(this.val$settings, 1.4f));
            }

            public void mouseExited(MouseEvent e) {
                this.val$settingsLabel.setCursor(Cursor.getPredefinedCursor(12));
                this.val$settingsLabel.setIcon(this.val$settings);
            }

            public void mouseClicked(MouseEvent e) {
                int delay;
                this.val$isSettingsLabelClicked[0] = true;
                if (!this.click) {
                    this.click = true;
                    this.val$activeContainer.active = true;
                    if (this.val$isVersionLabelClicked[0]) {
                        this.val$versionChooserLabel.setVisible(false);
                        this.val$versionsTextLabel.setVisible(false);
                        this.val$nosmallrectangleTextLabel.setVisible(false);
                        this.val$notwosmallrectangleTextLabel.setVisible(false);
                        this.val$nothreesmallrectangleTextLabel.setVisible(false);
                        this.val$nofoursmallrectangleTextLabel.setVisible(false);
                        this.val$nofivesmallrectangleTextLabel.setVisible(false);
                    }
                    this.val$mainScreen.add(this.val$smallrectangleTextLabel);
                    this.val$mainScreen.add(this.val$settingsTextLabel);
                    if (MainScreen.makeWhiteTheme) {
                        this.val$mainScreen.add(this.val$whiteOnUpdatesLabel);
                        this.val$mainScreen.add(this.val$whiteOnLabel);
                        this.val$mainScreen.add(this.val$whiteOffLabel);
                    } else {
                        this.val$mainScreen.add(this.val$onUpdatesLabel);
                        this.val$mainScreen.add(this.val$onLabel);
                        this.val$mainScreen.add(this.val$offLabel);
                    }
                    this.val$mainScreen.add(this.val$smallrectangleTextLabel2);
                    this.val$mainScreen.add(this.val$smallrectangleTextLabel3);
                    this.val$mainScreen.add(this.val$smallrectangleTextLabel4);
                    this.val$mainScreen.add(this.val$smallrectangleTextLabel5);
                    this.val$mainScreen.add(this.val$smallrectangleTextLabel6);
                    if (MainScreen.makeWhiteTheme) {
                        this.val$mainScreen.add(this.val$whiteSmallrectangleLabel);
                    } else {
                        this.val$mainScreen.add(this.val$smallrectangleLabel);
                    }
                    if (MainScreen.makeWhiteTheme) {
                        this.val$mainScreen.add(this.val$whiteSettingsrectangleLabel);
                        this.val$whiteSettingsrectangleLabel.setVisible(true);
                    } else {
                        this.val$mainScreen.add(this.val$settingsrectangleLabel);
                        this.val$settingsrectangleLabel.setVisible(true);
                    }
                    this.val$smallrectangleLabel.setVisible(true);
                    this.val$mainScreen.repaint();
                    delay = System.getProperty("os.name").toLowerCase().contains("mactest") ? 0 : -2000;
                    Timer initialTimer = new Timer(delay, new ActionListener() { // from class: xyz.frogdream.launcher.MainScreen.MainScreenInitializer.17.1
                        private int frameCount = 2;

                        {
                            AnonymousClass17.this = this;
                        }

                        public void actionPerformed(ActionEvent e2) {
                            int i = this.frameCount;
                            if (i < 50) {
                                float fraction = i / 50;
                                AnonymousClass17.this.moveLabels((int) (r2 * fraction));
                                this.frameCount++;
                                return;
                            }
                            ((Timer) e2.getSource()).stop();
                        }
                    });
                    initialTimer.setRepeats(true);
                    initialTimer.start();
                    return;
                }
                this.click = false;
                this.val$activeContainer.active = false;
                this.val$isSettingsLabelClicked[0] = false;
                delay = System.getProperty("os.name").toLowerCase().contains("mac") ? 0 : -2000;
                Timer timer = new Timer(delay, new ActionListener() { // from class: xyz.frogdream.launcher.MainScreen.MainScreenInitializer.17.2
                    private int frameCount = 2;

                    {
                        AnonymousClass17.this = this;
                    }

                    public void actionPerformed(ActionEvent e2) {
                        int i = this.frameCount;
                        if (i < 50) {
                            float fraction = i / 50;
                            AnonymousClass17.this.moveLabels((int) ((-r2) * fraction));
                            this.frameCount++;
                            return;
                        }
                        ((Timer) e2.getSource()).stop();
                        AnonymousClass17.this.cleanupAfterAnimation();
                    }
                });
                timer.setRepeats(true);
                timer.start();
            }

            private void moveLabels(int xOffset) {
                JLabel jLabel = this.val$mapLabel;
                jLabel.setLocation(jLabel.getX() + xOffset, this.val$mapLabel.getY());
                JLabel jLabel2 = this.val$mapTextLabel;
                jLabel2.setLocation(jLabel2.getX() + xOffset, this.val$mapTextLabel.getY());
                JLabel jLabel3 = this.val$newsLabel;
                jLabel3.setLocation(jLabel3.getX() + xOffset + (xOffset / 4), this.val$newsLabel.getY());
                JLabel jLabel4 = this.val$newsTextLabel;
                jLabel4.setLocation(jLabel4.getX() + xOffset + (xOffset / 4), this.val$newsTextLabel.getY());
                JLabel jLabel5 = this.val$go2Label;
                jLabel5.setLocation(jLabel5.getX() + xOffset + (xOffset / 4), this.val$go2Label.getY());
                JLabel jLabel6 = this.val$go3Label;
                jLabel6.setLocation(jLabel6.getX() + xOffset, this.val$go3Label.getY());
            }

            private void cleanupAfterAnimation() {
                this.val$smallrectangleLabel.setVisible(false);
                if (MainScreen.makeWhiteTheme) {
                    this.val$whiteSettingsrectangleLabel.setVisible(false);
                    this.val$mainScreen.add(this.val$whiteSettingsrectangleLabel);
                } else {
                    this.val$settingsrectangleLabel.setVisible(false);
                    this.val$mainScreen.add(this.val$settingsrectangleLabel);
                }
                if (MainScreen.makeWhiteTheme) {
                    this.val$mainScreen.remove(this.val$whiteOnLabel);
                    this.val$mainScreen.remove(this.val$whiteOnUpdatesLabel);
                    this.val$mainScreen.remove(this.val$whiteOffLabel);
                }
                this.val$mainScreen.remove(this.val$onLabel);
                this.val$mainScreen.remove(this.val$onUpdatesLabel);
                this.val$mainScreen.remove(this.val$offLabel);
                this.val$mainScreen.remove(this.val$settingsTextLabel);
                if (MainScreen.makeWhiteTheme) {
                    this.val$mainScreen.remove(this.val$whiteSmallrectangleLabel);
                } else {
                    this.val$mainScreen.remove(this.val$smallrectangleLabel);
                }
                this.val$mainScreen.remove(this.val$smallrectangleTextLabel);
                this.val$mainScreen.remove(this.val$smallrectangleTextLabel2);
                this.val$mainScreen.remove(this.val$smallrectangleTextLabel3);
                this.val$mainScreen.remove(this.val$smallrectangleTextLabel4);
                this.val$mainScreen.remove(this.val$smallrectangleTextLabel5);
                this.val$mainScreen.remove(this.val$smallrectangleTextLabel6);
                this.val$mainScreen.repaint();
            }
        }
    }

    private static ImageIcon getBrighterIcon(ImageIcon icon, float brightness) {
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
                int r2 = Math.min((int) (r * brightness), 255);
                int g2 = Math.min((int) (g * brightness), 255);
                bufferedImage.setRGB(x, y, ((-16777216) & rgb) | (r2 << 16) | (g2 << 8) | Math.min((int) (b * brightness), 255));
            }
        }
        return new ImageIcon(bufferedImage);
    }

    private static void saveBooleanValueToFile(String fileName, boolean value) {
        String appDataPath = getAppDataPath();
        String filePath = appDataPath + fileName;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(Boolean.toString(value));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean readBooleanValueFromFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                boolean parseBoolean = Boolean.parseBoolean(line);
                reader.close();
                return parseBoolean;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private static void saveValueToFile(String filePath, String value) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(value);
            writer.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private static String readValueFromFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                String readLine = reader.readLine();
                reader.close();
                return readLine;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
