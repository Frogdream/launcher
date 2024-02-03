package xyz.frogdream.launcher.downloader;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JLabel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.LaunchException;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.launch.ProcessListener;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloadOption;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloader;
import org.to2mbn.jmccc.mcdownloader.MinecraftDownloaderBuilder;
import org.to2mbn.jmccc.mcdownloader.provider.DownloadProviderChain;
import org.to2mbn.jmccc.mcdownloader.provider.fabric.FabricDownloadProvider;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;

/* loaded from: C:\Users\User\AppData\Local\Temp\jadx-16181904278102483256\classes.dex */
public class Download {
    static String PREFIX;
    private static final Map<String, Object> cache;
    static MinecraftDirectory dir;
    static String minecraftPath;

    static {
        String str;
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            str = System.getProperty("user.home") + "/.FrogDream";
        } else {
            str = System.getenv("LOCALAPPDATA") + "/.FrogDream";
        }
        minecraftPath = str;
        dir = new MinecraftDirectory(minecraftPath);
        PREFIX = "https://new.frogdream.xyz/launcher/";
        cache = new HashMap();
    }

    public static void download() {
        Map<String, Object> map = cache;
        if (map.containsKey("download")) {
            System.out.println("Using cached download result.");
            return;
        }
        processDownload();
        downloadMods();
        System.out.println("Mods downloaded.");
        map.put("download", true);
    }

    static void processDownload() {
        Map<String, Object> map = cache;
        if (map.containsKey("processDownload")) {
            System.out.println("Using cached processDownload result.");
            return;
        }
        FabricDownloadProvider fabricDownloadProvider = new FabricDownloadProvider();
        MinecraftDownloader downloader = MinecraftDownloaderBuilder.create().providerChain(DownloadProviderChain.create().addProvider(fabricDownloadProvider)).build();
        try {
            downloader.downloadIncrementally(dir, "fabric-loader-0.14.21-1.20.1", null, new MinecraftDownloadOption[0]).get();
            map.put("processDownload", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void downloadMods() {
        if (cache.containsKey("downloadMods")) {
            System.out.println("Using cached downloadMods result.");
            return;
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(PREFIX + "files")).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String[] files = (String[]) new Gson().fromJson((String) response.body(), (Class<Object>) String[].class);
            for (String file : files) {
                System.out.println("Load: " + file);
                try {
                    URL url = new URL(PREFIX + file);
                    File dest = new File(dir.getRoot(), file);
                    FileUtils.copyURLToFile(url, dest);
                } catch (IOException e) {
                    System.err.println("Load error: " + file);
                    e.printStackTrace();
                }
            }
            cache.put("downloadMods", true);
        } catch (Exception e2) {
            System.err.println("Download error.");
            throw new RuntimeException(e2);
        }
    }

    public static void launch(String nick, JLabel playTextLabel, AtomicReference<Float> currentBrightness, boolean swiftPlay, boolean makeOptimization, boolean makeFreecam, int xmx) {
        if (!dir.getRoot().exists()) {
            download();
        }
        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(threads, r -> {
            Thread thread = new Thread(r);
            thread.setPriority(10);
            return thread;
        });
        executorService.submit(() -> {
            Launcher launcher = LauncherBuilder.buildDefault();
            try {
                LaunchOption opts = new LaunchOption("fabric-loader-0.14.21-1.20.1", OfflineAuthenticator.name(nick), dir);
                try {
                    opts.setMaxMemory(xmx * 1024);
                    opts.setMinMemory(256);
                    System.setProperty("apple.awt.application.name", "Minecraft");
                    if (makeOptimization) {
                        Collections.addAll(opts.extraJvmArguments(), "-XX:+UseG1GC", "-XX:+ParallelRefProcEnabled", "-XX:+ParallelRefProcEnabled", "-XX:+UnlockExperimentalVMOptions", "-XX:+DisableExplicitGC", "-XX:+OptimizeStringConcat", "-XX:CompileThreshold=1500", "-Dsun.java2d.opengl=true", "-Dminecraft.disableVsync=true", "-Duser.language=ru", "-Duser.country=RU", "-Dminecraft.disableMinecraftVersionCheck=true", "-XX:+UseStringDeduplication", "-XX:+UseCompressedOops", "-XX:+UseCompressedClassPointers", "-XX:ReservedCodeCacheSize=256M", "-XX:+TieredCompilation", "-XX:MaxMetaspaceSize=256M", "-XX:SurvivorRatio=8", "-XX:NewRatio=3", "-XX:MaxDirectMemorySize=256M", "-XX:+AlwaysPreTouch", "-XX:+UseLargePages", "-XX:+UseStringDeduplication", "-XX:+UseCompressedOops", "-XX:+UseCompressedClassPointers", "-XX:ReservedCodeCacheSize=256M", "-XX:+TieredCompilation", "-XX:MaxMetaspaceSize=256M", "-XX:SurvivorRatio=8", "-XX:NewRatio=3", "-XX:MaxDirectMemorySize=256M", "-XX:+AlwaysPreTouch", "-XX:+UseLargePages");
                    } else {
                        Collections.addAll(opts.extraJvmArguments(), "-Dsun.java2d.opengl=true", "-Dminecraft.disableMinecraftVersionCheck=true", "-Dminecraft.disableVsync=true", "-Duser.language=ru", "-Duser.country=RU");
                    }
                    File modsFolder = new File(minecraftPath + "/mods");
                    final int finalInitialFileCount = ((File[]) Objects.requireNonNull(modsFolder.listFiles())).length;
                    try {
                        launcher.launch(opts, new ProcessListener() { // from class: xyz.frogdream.launcher.downloader.Download.1
                            int logCount = 0;

                            @Override // org.to2mbn.jmccc.launch.ProcessListener
                            public void onLog(String log) {
                                System.out.println(log);
                                int lineCount = log.split(StringUtils.LF).length;
                                int i = this.logCount + lineCount;
                                this.logCount = i;
                                if (!swiftPlay) {
                                    if (i >= 10 && i < 20) {
                                        playTextLabel.setText("Секунду...");
                                        return;
                                    } else if (i >= (finalInitialFileCount * 6) + 22) {
                                        playTextLabel.setText("Играть");
                                        currentBrightness.set(Float.valueOf(1.11f));
                                        return;
                                    } else {
                                        return;
                                    }
                                }
                                playTextLabel.setText("Swift-play");
                            }

                            @Override // org.to2mbn.jmccc.launch.ProcessListener
                            public void onErrorLog(String log) {
                                System.err.println(log);
                            }

                            @Override // org.to2mbn.jmccc.launch.ProcessListener
                            public void onExit(int code) {
                                System.out.println("Exit with code " + code);
                                boolean z = swiftPlay;
                                Float valueOf = Float.valueOf(1.11f);
                                if (z) {
                                    System.out.println("Swift-play");
                                    currentBrightness.set(valueOf);
                                    return;
                                }
                                playTextLabel.setText("Играть");
                                currentBrightness.set(valueOf);
                            }
                        });
                    } catch (IOException | LaunchException e) {
                        var3 = e;
                        throw new RuntimeException(var3);
                    }
                } catch (IOException | LaunchException e2) {
                    var3 = e2;
                    throw new RuntimeException(var3);
                }
            } catch (IOException | LaunchException e3) {
                var3 = e3;
            }
        });
        executorService.shutdown();
    }
}
