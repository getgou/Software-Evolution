package android.app.printerapp.loginScreen;


public class loginScreenInterfaces
{
    interface lsSharedRequestInterface
    {
        void loginUser(String email, String password);
    }

    interface lsViewInterface extends lsSharedRequestInterface
    {
        // No extra ops
    }

    interface lsControllerToModelInterface extends  lsSharedRequestInterface
    {
        // No extra ops
    }

    interface lsControllerToViewInterface
    {
        void sendMessageToUser(int message);
    }

    interface lsModelInterface
    {
        void loginResult(boolean result, int message);
    }


} //End of class
