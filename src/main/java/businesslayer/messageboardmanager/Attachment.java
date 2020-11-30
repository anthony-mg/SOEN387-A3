package businesslayer.messageboardmanager;

public class Attachment {

    private String fileName;
    private long fileSize;
    private String mediatype;

    public Attachment(String fileName, long fileSize, String mediatype){
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mediatype = mediatype;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }
}
