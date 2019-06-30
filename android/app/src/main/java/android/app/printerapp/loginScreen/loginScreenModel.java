package android.app.printerapp.loginScreen;

import android.app.Activity;
import android.app.printerapp.R;
import android.app.printerapp.backend.tokenManagement.TokenManager;
import android.app.printerapp.backend.tokenManagement.tokenInterfaces;

public class loginScreenModel implements loginScreenInterfaces.lsControllerToModelInterface, tokenInterfaces
{

    //region Class variables and constructor
    //Class variables
    private loginScreenInterfaces.lsModelInterface controller;
    private Activity activity;
    private static final String TAG = "loginScreenModel.class";

    //Class constructor
    public loginScreenModel(Activity activity, loginScreenInterfaces.lsModelInterface controller)
    {
        this.controller = controller;
        this.activity   = activity;
    }

    //endregion

    //region Complying with loginScreenInterfaces.lsControllerToModelInterface Methods
    @Override
    public void loginUser(String email, String password)
    {
        // Get a token manager
        TokenManager<loginScreenModel> tManager = new TokenManager<>(this, activity);

        // Request the token
        tManager.generateNewToken(email, password);
    }
    //endregion

    //region Complying with tokenInterfaces
    @Override
    public void tokenGranted()
    {
        this.controller.loginResult(true, R.string.emptyMessage);
    }

    //TODO: change to false
    @Override
    public void tokenDenied()
    {
        this.controller.loginResult(false, R.string.invalidCredentials);
    }
    //endregion


} //End of class
