package android.app.printerapp.backend.s3Downloader;



public interface s3downloadInterface
{
    void downloadComplete();
    void downloadFailure(int message);
    void progressReport(int percentage);
}
