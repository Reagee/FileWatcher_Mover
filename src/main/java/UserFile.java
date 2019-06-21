import animations.Animations;
import javafx.util.Duration;
import lombok.Data;
import notification.Notification;
import notification.Notifications;
import notification.TrayNotification;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Data
public class UserFile implements FileMoverInterface {
    private String fileName;
    private String filePath;

    @Override
    public void copyFile(List<String> fileTypes) {
        try {
            List<String> files = Files.list(new File(Paths.DOWNLOAD_PATH).toPath())
                    .map(Path::getFileName)
                    .map(Objects::toString)
                    .filter(s -> s.contains("."))
                    .collect(Collectors.toList());

            files.forEach(file -> {
                try {
                    File destination = new File(FileSorter.EXTENSION.getDestinationPath(fileTypes));
                    if (!destination.exists())
                        destination.mkdirs();
                    if (fileTypes.contains(file.split("\\.")[1])) {
                        System.out.println("Copying: " + Paths.DOWNLOAD_PATH.concat(file) + " to: " + destination.getAbsolutePath().concat("\\" + file));
                        Files.copy(new File(Paths.DOWNLOAD_PATH.concat(file)).toPath(), new File(destination.getAbsolutePath().concat("\\" + file)).toPath(), REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void moveFile(List<String> fileTypes) {

        try {
            List<String> files = Files.list(new File(Paths.DOWNLOAD_PATH).toPath())
                    .map(Path::getFileName)
                    .map(Objects::toString)
                    .filter(s -> s.contains("."))
                    .collect(Collectors.toList());
            files.forEach(file -> {
                try {
                    File destination = new File(FileSorter.EXTENSION.getDestinationPath(fileTypes));
                    if (!destination.exists())
                        destination.mkdirs();
                    if (fileTypes.contains(file.split("\\.")[1])) {
                        String concat = destination.getAbsolutePath().concat("\\" + file);
                        System.out.println("Moving: " + Paths.DOWNLOAD_PATH.concat(file) + " to: " + concat);
                        Files.move(new File(Paths.DOWNLOAD_PATH.concat(file)).toPath(), new File(concat).toPath(), REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    showTray(java.nio.file.Paths.get(file));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showTray(Path file) {
        TrayNotification trayNotification = new TrayNotification();
        String title = "Error !";
        String message = "The file:" + file.getFileName() + " couldn't be moved.";
        Notification notification = Notifications.ERROR;

        trayNotification.setTitle(title);
        trayNotification.setMessage(message);
        trayNotification.setNotification(notification);
        trayNotification.setAnimation(Animations.FADE);
        trayNotification.showAndDismiss(Duration.millis(3000));
    }
}
