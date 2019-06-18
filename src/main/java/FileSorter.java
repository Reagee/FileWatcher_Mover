import java.util.List;

public enum FileSorter {
    EXTENSION {
        @Override
        public String getDestinationPath(List<String> fileTypes) {
            if (Extensions.ZIP_Files.equals(fileTypes)) {
                return Paths.DESTINATION_PATH_ARCHIVES;
            } else if (Extensions.OFFICE_Files.equals(fileTypes)) {
                return Paths.DESTINATION_PATH_DOCX;
            } else if (Extensions.IMG_Files.equals(fileTypes)) {
                return Paths.DESTINATION_PATH_IMG;
            } else if (Extensions.PDF_Files.equals(fileTypes)) {
                return Paths.DESTINATION_PATH_PDF;
            }
            return Paths.DESTINATION_PATH_OTHERS;
        }

        @Override
        public List<String> getExtensionList(String extension) {
            if (Extensions.ZIP_Files.contains(extension)) {
                return Extensions.ZIP_Files;
            } else if (Extensions.OFFICE_Files.contains(extension)) {
                return Extensions.OFFICE_Files;
            } else if (Extensions.IMG_Files.contains(extension)) {
                return Extensions.IMG_Files;
            } else if (Extensions.PDF_Files.contains(extension)) {
                return Extensions.PDF_Files;
            }
            return Extensions.OTHER_Files;
        }
    };

    public abstract String getDestinationPath(List<String> fileTypes);

    public abstract List<String> getExtensionList(String extension);
}
