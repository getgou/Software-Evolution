package android.app.printerapp.homeScreen.textScanner;

import android.app.printerapp.R;
import android.app.printerapp.util.ui.customKeyboard.keyboardInterface;
import android.app.printerapp.util.ui.customKeyboard.scannerKeyboard;
import android.app.printerapp.util.ui.dialogs.waitingDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.Toast;

public class textScannerFragment extends Fragment implements keyboardInterface, textScannerInterfaces.tsControllerToViewInterface

{
    //region Class variables and constructor
    // Class variables
    private EditText editTextTextId;
    private textScannerInterfaces.tsViewInterface controller;
    private scannerKeyboard keyboard;
    private waitingDialog dialogWaiting;

    // Class constructor
    public textScannerFragment()
    {
        // Empty constructor.
    }
    //endregion

    // region Activity Lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_text_scanner, container, false);

        // Initialize variables
        this.initializeVariables(view);

        // Set custom keyboard
        this.setCustomKeyboard(view);

        // Set the initial configuration of the keyboard
        this.initialKeyboardConfiguration();

        // Set edit text listeners
        this.setEditTextChangeListeners();

        return view;
    }
    //endregion

    //region Complying with textScannerInterfaces.tsControllerToViewInterface Methods
    @Override
    public void sendMessageToUser(int message)
    {
        // Send a toast to the user
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();

        // Dismiss the dialog
        if(this.dialogWaiting!=null)
        {
            this.dialogWaiting.dismiss();
        }
    }

    @Override
    public void startProgressDialog()
    {
        // Show the dialog
        this.dialogWaiting.show();
    }

    @Override
    public void stopProgressDialog()
    {
        // Dismiss the dialog
        this.dialogWaiting.dismiss();
    }

    //endregion

    //region Helper Methods - General
    private void initialKeyboardConfiguration()
    {
        this.keyboard.disableSearchButton();
    }

    private void initializeVariables(View view)
    {
        this.editTextTextId     = (EditText) view.findViewById(R.id.editTextIDtoScan);
        this.controller         = new textScannerController(this,this.getActivity());
        this.dialogWaiting      = new waitingDialog(this.getActivity());
    }

    private void setCustomKeyboard(View view)
    {
        // prevent system keyboard from appearing when EditText is tapped
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Initialize the keyboard
        this.keyboard = (scannerKeyboard) view.findViewById(R.id.keyboard);

        // Configuring the edit text
        this.editTextTextId.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editTextTextId.setTextIsSelectable(true);

        // Create connection between editText and keyboard
        InputConnection ic = editTextTextId.onCreateInputConnection(new EditorInfo());
        keyboard.setInputConnection(ic);

        // Set the delegate of the keyboard
        keyboard.setDelegate(this);

    }

    //endregion

    //region Adding editText Text Change Listeners
    private void setEditTextChangeListeners()
    {
        this.editTextTextId.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
               // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                Log.i("Count", editTextTextId.getText().length()+"");
                if(editTextTextId.getText().length() > 0)
                {
                    // Enable Search Button
                    keyboard.enableSearchButton();
                }
                else
                {
                    // Disable Search Button
                    keyboard.disableSearchButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // Not used
            }
        });
    }

    //endregion

    //region Complying with keyboardInterface Methods
    @Override
    public void searchButtonClicked()
    {
        // Get the ID provided
        String qrcode = this.editTextTextId.getText().toString().trim();

        // Notify the controller that a qrcode has been submitted
        this.controller.qrcodeReceived(qrcode);
    }
    //endregion


} //End of class