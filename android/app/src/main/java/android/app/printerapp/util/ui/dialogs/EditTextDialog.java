package android.app.printerapp.util.ui.dialogs;

import android.app.AlertDialog;
import android.app.printerapp.R;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextDialog<T>
{
    // region Class variables and Constructor
    // Class variables
    private Context context;
    private T delegate;

    // Class constructor
    public EditTextDialog(Context context, T delegate)
    {
        this.context    = context;
        this.delegate   = delegate;
    }
    //endregion
    
    //region Public Methods
    public void showDialog(String dialogTitle, String editTextHint, int inputType)
    {
        // Get the custom view
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_modifying_data, null);

        // Get a builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // Set the alert dialog view
        alertDialogBuilder.setView(dialogView);

        // Set alert dialog title
        TextView title = (TextView) dialogView.findViewById(R.id.textViewAlertDialogTitle);
        title.setText(dialogTitle);

        // Set the editText hint
        EditText userInput = (EditText) dialogView.findViewById(R.id.editTextAlertDialogContent);
        userInput.setHint(editTextHint);

        // Change if it is text. Else leave it as default value
        if(inputType == InputType.TYPE_CLASS_TEXT)
        {
            userInput.setInputType(inputType);
        }


        // Set buttons
        alertDialogBuilder
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        // Get new content
                        String newContent = userInput.getText().toString().trim();
                        
                        // Notify the delegate of the specified text
                        ((dialogComInterfaces.stringDialog)delegate).didEnterStringData(newContent);
                        
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        // Do nothing
                    }
                });

        // Create alert dialog
        alertDialogBuilder.show();
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//
//        // Show it
//        alertDialog.show();
    }
    //endregion

}
