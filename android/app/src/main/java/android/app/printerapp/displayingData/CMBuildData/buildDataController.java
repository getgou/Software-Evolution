package android.app.printerapp.displayingData.CMBuildData;


import android.app.Activity;
import android.app.printerapp.R;
import android.app.printerapp.displayingData.dataFragment;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.magicsScreenshot.magicsScreenshotActivity;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.dialogs.DatePickerFragment;
import android.app.printerapp.util.ui.dialogs.EditTextDialog;
import android.app.printerapp.util.ui.dialogs.TimePickerFragment;
import android.app.printerapp.util.ui.dialogs.dialogComInterfaces;
import android.app.printerapp.util.ui.dialogs.singleItemPicker;
import android.app.printerapp.util.ui.enums;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

public class buildDataController implements Serializable, dataInterfaces.buildDataModelInterface, dataInterfaces.dfViewInterface, dialogComInterfaces.dateDialog, dialogComInterfaces.timeDialog, dialogComInterfaces.stringDialog, dialogComInterfaces.singleItemPowderConditionPicker
{
    // region Class variables and Class contructor
    private transient WeakReference<dataInterfaces.dfControllerToViewInterface> view;
    private transient dataInterfaces.buildDataControllerToModelInterface model;
    private transient Activity activity;
    private transient int positionClicked;
    private transient String keyClicked;
    private transient final String TAG = "buildDataController";

    public buildDataController(dataInterfaces.dfControllerToViewInterface view, Activity activity)
    {
        this.view       = new WeakReference<dataInterfaces.dfControllerToViewInterface>(view);
        this.model      = new buildDataModel(this, activity);
        this.activity   = activity;
    }
    //endregion

    //region Complying with dataInterfaces.buildDataModelInterface Methods
    @Override
    public void sendMessageToUser(int message)
    {
        this.view.get().sendMessageToUser(message);
    }

    @Override
    public void displayPersonality(LinkedHashMap<String, String> data)
    {
        this.view.get().displayPersonality(data);
    }

    @Override
    public void changeContent(int position, String newContent)
    {
        this.view.get().changeContent(position, newContent);
    }

    //endregion

    // region Complying with dataInterfaces.dfViewInterface Methods
    @Override
    public void getMapWithData(Personality personality)
    {
        this.model.getMapWithData(personality);
    }

    @Override
    public void itemClicked(int position, String key)
    {
        // Getting the user role
        String userRole = ((MyApplication)activity.getApplication()).getRole();

        Log.i(TAG, "User role = "+ userRole);

        // Show Alert Dialog only if the selected item is not the build id
        if(position != 0)
        {
            if(userRole == null || userRole.equals(enums.userRoleType.Read.getValue()))
            {
                // Notify user that he has a read permission only
                this.view.get().sendMessageToUser(R.string.readPermissionMsg);
            }
            else
            {
                // Store the position clicked
                this.positionClicked = position;

                // Store the clicked key
                this.keyClicked = key;

                // Display the appropriate dialog
                this.showDialog(key);
            }

        }
        else
        {
            // Create an intent
            Intent intent = new Intent(this.activity,magicsScreenshotActivity.class);

            // Get the build ID from the dataFragment
            String buildID = ((dataFragment)this.view.get()).getValueForKey(key);

            if(buildID != null)
            {
                // Pass the build ID to the next activity
                intent.putExtra(enums.qrCodeType.Build.getValue(), buildID);

                this.activity.startActivity(intent);
            }
            else
            {
                // Send message to user
                this.view.get().sendMessageToUser(R.string.unableTOGetMS);
            }

        }

    }
    //endregion
    
    //region Complying with dialogComInterfaces.dateDialog Methods
    @Override
    public void didEnterDate(int year, int month, int day)
    {
        // Transform int values to string values
        String sDay     = String.valueOf(day);
        String sMonth   = String.valueOf(month);
        String sYear    = String.valueOf(year);

        // Set the date as a string

        String selectedDate =   sYear+"-"+sMonth+"-"+sDay;

        // Notify the view of the new content
        this.view.get().changeContent(this.positionClicked, selectedDate);
        
        // Notify the model about the change
        this.model.updateDatabaseContent(this.keyClicked,selectedDate);

    }
    
    //endregion

    //region Complying with dialogComInterfaces.timeDialog Methods
    @Override
    public void didEnterTime(int hour, int minute)
    {
        // Change int values to strings
        String sHour    = String.valueOf(hour);
        String sMinute  = "";

        // Set the appropriate format for the minute
        if (minute < 10)
        {
            sMinute = "0"+String.valueOf(minute);
        }
        else
        {
            sMinute = String.valueOf(minute);
        }

        // Selected the complete time format
        String selectedString   = sHour+":"+sMinute;

        // Notify the view of the change
        this.view.get().changeContent(this.positionClicked,selectedString);
        
        // Notify the model about the change
        this.model.updateDatabaseContent(this.keyClicked,selectedString);
    }

    //endregion

    //region Complying with dialogComInterfaces.stringDialog Methods
    @Override
    public void didEnterStringData(String data)
    {
        // Ensuring the data is not empty
        if(!data.isEmpty())
        {
            // Notify the view about the change
            this.view.get().changeContent(this.positionClicked,data);
            
            // Notify the model about the change
            this.model.updateDatabaseContent(this.keyClicked,data);
        }
    }
    //endregion

    //region Complying with dialogComInterfaces.singleItemDialog Methods
    @Override
    public void didSelectAPowerCondition(enums.powderCondition powderCondition, int reusedAmount)
    {
        String[] powderConditionsArray = this.activity.getResources().getStringArray(R.array.powder_conditions);

        if(powderCondition == enums.powderCondition.New)
        {
            // Notify the view to place new data
            this.view.get().changeContent(this.positionClicked, powderConditionsArray[enums.powderCondition.New.getValue()]);

            // Notify the model to store the new data
            this.model.updateDatabaseContent(this.keyClicked,powderConditionsArray[enums.powderCondition.New.getValue()]);
        }
        else if(powderCondition == enums.powderCondition.Used)
        {
            // Notify the view to place new data
            this.view.get().changeContent(this.positionClicked, powderConditionsArray[enums.powderCondition.Used.getValue()]);

            // Notify the model to store the new data
            this.model.updateDatabaseContent(this.keyClicked,powderConditionsArray[enums.powderCondition.Used.getValue()]);
        }
        else
        {
            // Create custom string
            String text = powderConditionsArray[enums.powderCondition.Reused.getValue()];

            // Concatenate the amount
            text = text + " ("+String.valueOf(reusedAmount)+")";

            // Notify the view
            this.view.get().changeContent(this.positionClicked, text);

            // Notify the model to store the new data
            this.model.updateDatabaseContent(this.keyClicked,text);
        }


    }

    //endregion

    //region Helper Methods
    private void showDialog(String key)
    {
        // Creating bundle with delegate
        Bundle bundleWithDelegate = new Bundle();
        bundleWithDelegate.putSerializable(enums.bundleKeys.dialogDelegate.getValue(),buildDataController.this);

        // Show time dialog
        if(key.equals(enums.printingData.startTime.getValue()) || key.equals(enums.printingData.endTime.getValue()))
        {
            // Show time dialog
            TimePickerFragment<buildDataController> timePicker = new TimePickerFragment<>();

            // Setting arguments of the date picker
            timePicker.setArguments(bundleWithDelegate);

            // Showing the time picker
            timePicker.show(this.activity.getFragmentManager(),"Time");
        }

        // Show date Dialog
        else if (key.equals(enums.printingData.printingDate.getValue()))
        {
            // Creating a datePicker
            DatePickerFragment <buildDataController> datePicker = new DatePickerFragment<>();

            // Setting arguments of the date picker
            datePicker.setArguments(bundleWithDelegate);

            // Showing the date picker
            datePicker.show(this.activity.getFragmentManager(),"Date");
        }

        // Show text dialog with ABC pad
        else if(key.equals(enums.printingData.operator.getValue())||key.equals(enums.printingData.typeOfmachine.getValue()))
        {
            EditTextDialog editDialog = new EditTextDialog(this.activity,buildDataController.this);
            editDialog.showDialog(key, key, InputType.TYPE_CLASS_TEXT);
        }

        // Show the dialog with radio button and all materials
        else if (key.equals(enums.printingData.platformMaterial.getValue()))
        {

        }

        // Show dialog with radio button containing the options for powder conditions
        else if (key.equals(enums.printingData.powderCondition.getValue()))
        {
            singleItemPicker picker = new singleItemPicker(this.activity,this);
            picker.singlePickerPowderCondition();
        }

        // Show text dialog with Number pad
        else
        {
            EditTextDialog editDialog = new EditTextDialog(this.activity, buildDataController.this);
            editDialog.showDialog(key, key, InputType.TYPE_CLASS_NUMBER);
        }

    }

    //endregion

} //end of class
