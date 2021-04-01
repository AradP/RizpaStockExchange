package bl;

public class FileIO {
    // static variable single_instance of type Singleton
    private static FileIO single_instance = null;
    private String filePath;

    // static method to create instance of Singleton class
    public static FileIO getInstance() {
        if (single_instance == null)
            single_instance = new FileIO();

        return single_instance;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
