package android.app.printerapp.util.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.printerapp.R;
import android.app.printerapp.adapters.dialogPowderConditionAdapter;
import android.app.printerapp.util.ui.enums;
import android.content.DialogInterface;
import android.view.WindowManager;

import java.util.ArrayList;

public class singleItemPicker<T>
{
    //region Class variables and class constructor
    private Activity activity;
    private int positionOfItemSelectedInSCDialog;
    private T delegate;
    private final int DEFAULT_INITIAL_POSITION = -1;
    private final int POSITION_OF_REUSED_CONDITION = 2;

    public singleItemPicker(Activity activity, T delegate)
    {
        this.activity   = activity;
        this.delegate   = delegate;
    }
    //endregion

    //region Creating a singleChoiceDialog
    public void singlePickerPowderCondition()
    {
        //Initialize an alert dialog builder.
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);

        // Change ArrayList of list of items to a charSequence
        String[] items = activity.getResources().getStringArray(R.array.powder_conditions);

        // Set the dialog title
        builder.setTitle(R.string.powderCondition);

        // Create an adapter
        dialogPowderConditionAdapter adapter = new dialogPowderConditionAdapter(this.activity, items);

        // Set the type of dialog
        builder.setSingleChoiceItems(adapter, this.DEFAULT_INITIAL_POSITION, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                // Determine the item selected
                positionOfItemSelectedInSCDialog = i;

                // Show the edit text for specifying how much a powder has been reused
                if (i == POSITION_OF_REUSED_CONDITION)
                {
                    // Notify adapter to show edit text
                    adapter.showEditText();

                }
                else
                {
                    // Notify adapter to remove the edit text
                    adapter.removeEditText();
                }
            }
        });

        // Set positive button
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(positionOfItemSelectedInSCDialog == enums.powderCondition.New.getValue()) // An item has been selected
                {
                    // Notify the controller that the condition "New"
                    ((dialogComInterfaces.singleItemPowderConditionPicker)delegate).didSelectAPowerCondition(enums.powderCondition.New,0);
                }
                else if(positionOfItemSelectedInSCDialog == enums.powderCondition.Used.getValue())
                {
                    // Notify the controller that the condition "Used"
                    ((dialogComInterfaces.singleItemPowderConditionPicker)delegate).didSelectAPowerCondition(enums.powderCondition.Used,0);
                }
                else
                {
                    // Request to the adapter the amount reused
                    int reusedAmount = adapter.getReusedAmount();

                    // Notify the controller that the condition "New"
                    ((dialogComInterfaces.singleItemPowderConditionPicker)delegate).didSelectAPowerCondition(enums.powderCondition.Reused, reusedAmount);
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                // Do nothing
            }
        });


        // Return the dialog builder
        AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Show the dialog
        alertDialog.show();
    }
    //endregion

    //region Helper methods
    private CharSequence[] arrayToCharSequence(ArrayList<String> listOfCategories)
    {
        return listOfCategories.toArray(new CharSequence[listOfCategories.size()]);
    }
    //endregion
}
