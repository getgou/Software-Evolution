package android.app.printerapp.homeScreen.textScanner;

import android.app.printerapp.util.ui.enums;

public interface textScannerInterfaces
{

    interface tsSharedRequestInterface
    {
        void qrcodeReceived(String qrcode);
    }

    interface tsViewInterface extends tsSharedRequestInterface
    {
        // No extra ops
    }

    interface tsControllerToModelInterface extends tsSharedRequestInterface
    {
        // No extra ops
    }

    interface tsSharedResultResultInterface
    {
        void sendMessageToUser(int message);
    }

    interface tsControllerToViewInterface extends tsSharedResultResultInterface
    {
        void startProgressDialog();
        void stopProgressDialog();
    }

    interface tsModelInterface extends tsSharedResultResultInterface
    {
        void validQrcode(enums.qrCodeType codeType);
    }

}
