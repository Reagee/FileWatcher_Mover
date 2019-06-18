import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.nio.file.*;

public class FileWatcher {
    private WatchService watchService;
    private UserFile userFile;

    public FileWatcher() throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.userFile = new UserFile();
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
                        TrayNotification trayNotification = new TrayNotification();
                        trayNotification.setTitle("File has been moved !");
                        trayNotification.setMessage("The file ["+file.getFileName()+"] appeared in: "+Paths.DOWNLOAD_PATH+" and has been successfully to the proper directory.");
                        trayNotification.setNotificationType(NotificationType.SUCCESS);
                        trayNotification.showAndWait();
                        Thread.sleep(500);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
