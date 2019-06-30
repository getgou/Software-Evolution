package android.app.printerapp.loginScreen;

import android.app.Activity;
import android.app.printerapp.R;
import android.app.printerapp.util.ui.dialogs.waitingDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginScreenActivity extends Activity implements View.OnClickListener, loginScreenInterfaces.lsControllerToViewInterface
{
    //region Class variables
    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private loginScreenInterfaces.lsViewInterface controller;
    private waitingDialog dialogWaiting;
    //endregion

    //region Activity Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // Initialize variables
        this.initializeVariables();

        //Set Listener
        this.setListener();

        // Initial configurations of the login button
        this.initialButtonConfigurations();

        // Listen to changes in edittext
        this.setTextChangeListeners();

    }
    //endregion

    //region Complying with loginScreenInterfaces.lsControllerToViewInterface Methods

    @Override
    public void sendMessageToUser(int message)
    {
        // Dismiss the dialog
        this.dialogWaiting.dismiss();

        //Send message to user
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    //endregion

    //region Complying with OnClickListener Methods

    @Override
    public void onClick(View v)
    {
        if(v == this.buttonLogin)
        {
            // Hide keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.editTextPassword.getWindowToken(), 0);

            // Show the dialog
            this.dialogWaiting.show();

            // Get credentials
            String email    = this.editTextEmail.getText().toString().trim();
            String password = this.editTextPassword.getText().toString().trim();

            // Send email and password to controller
            this.controller.loginUser(email,password);
        }
    }

    //endregion

    //region Helper Methods - General
    private void initializeVariables()
    {
        this.buttonLogin        = (Button) findViewById(R.id.buttonLogin);
        this.editTextEmail      = (EditText)findViewById(R.id.editTextEmail);
        this.editTextPassword   = (EditText) findViewById(R.id.editTextPassword);
        this.controller         = new loginScreenController(this,this);
        this.dialogWaiting      = new waitingDialog(this);
    }
    private void setListener()
    {
        this.buttonLogin.setOnClickListener(this);
    }

    private void initialButtonConfigurations()
    {
        this.buttonLogin.setAlpha((float)0.5);
        this.buttonLogin.setEnabled(false);
    }
    //endregion

    //region Text Change Listeners
    private void configuringLoginButton()
    {
        if(this.editTextPassword.getText().length() > 0 && editTextEmail.getText().length() > 0)
        {
            this.buttonLogin.setAlpha((float)1.0);
            this.buttonLogin.setEnabled(true);
        }
        else
        {
            this.buttonLogin.setAlpha((float)0.5);
            this.buttonLogin.setEnabled(false);
        }

    }

    private void setTextChangeListeners()
    {
        // Listening to changes in the password editText
        this.editTextPassword.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                configuringLoginButton();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // Not used
            }
        });

        // Listening to changes in the email editText
        this.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                configuringLoginButton();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // Not used
            }
        });


    }



    //endregion

} // End of class
