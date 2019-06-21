import animations.Animations;
import javafx.application.Platform;
import javafx.util.Duration;
import notification.Notification;
import notification.Notifications;
import notification.TrayNotification;

import java.io.IOException;
import java.nio.file.*;

public class FileWatcher implements Runnable{
    private WatchService watchService;
    private UserFile userFile;
    private TrayNotification trayNotification;

    public FileWatcher() throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.userFile = new UserFile();
        this.trayNotification = new TrayNotification();
    }

    public void startWatching() throws InterruptedException {
        Path directory = java.nio.file.Paths.get(Paths.DOWNLOAD_PATH);
        WatchKey watchKey;
        try {
            watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            while (true) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    Path file = (Path) event.context();
                    if (file.getFileName().toString().contains(".")) {
                        userFile.moveFile(FileSorter.EXTENSION.getExtensionList(file.getFileName().toString().split("\\.")[1]));

                        Platform.setImplicitExit(true);
                        Platform.runLater(() -> showTray(file));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showTray(Path file) {
        String title = "Files has been moved !";
        String message = "The file:" + file.getFileName() + " has been successfully moved.";
        Notification notification = Notifications.SUCCESS;

        trayNotification.setTitle(title);
        trayNotification.setMessage(message);
        trayNotification.setNotification(notification);
        trayNotification.setAnimation(Animations.FADE);
        trayNotification.showAndDismiss(Duration.millis(3000));
    }

    @Override
    public void run() {
        try {
            startWatching();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
