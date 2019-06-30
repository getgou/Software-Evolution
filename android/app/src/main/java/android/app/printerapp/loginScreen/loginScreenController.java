package android.app.printerapp.loginScreen;


import android.app.Activity;
import android.app.printerapp.R;
import android.app.printerapp.homeScreen.homeScreenActivity;
import android.content.Intent;
import android.util.Patterns;

import java.lang.ref.WeakReference;

public class loginScreenController implements loginScreenInterfaces.lsModelInterface, loginScreenInterfaces.lsViewInterface
{
    //region Class variables and constructor
    //Class variables
    private loginScreenInterfaces.lsControllerToModelInterface model;
    private WeakReference<loginScreenInterfaces.lsControllerToViewInterface> view;
    private Activity activity;

    //Class constructor
    public loginScreenController(Activity activity,loginScreenInterfaces.lsControllerToViewInterface view)
    {
        this.activity = activity;
        this.view       = new WeakReference<loginScreenInterfaces.lsControllerToViewInterface>(view);
        this.model      = new loginScreenModel(activity,this);
    }


    //endregion

    //region Complying with loginScreenInterfaces.lsModelInterface Methods

    @Override
    public void loginResult(boolean result, int message)
    {
        if(result)
        {
            // Go to homepage activity
            Intent intent = new Intent(activity.getApplicationContext(),homeScreenActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
        else
        {
            // Notify view that it was not possible to log in
            this.view.get().sendMessageToUser(message);
        }
    }

    //endregion

    //region Complying with loginScreenInterfaces.lsViewInterface Methods
    @Override
    public void loginUser(String email, String password)
    {
        this.model.loginUser(email,password);
        // Verifying the email is in a valid email format
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            // Send credentials to model

        }
        else
        {
            // Notify user that email is invalid
            //this.view.get().sendMessageToUser(R.string.invalidEMail);
        }
    }

    //endregion


} //End of class
