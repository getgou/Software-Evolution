package android.app.printerapp.util.ui.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.printerapp.util.ui.enums;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment<T> extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    //region Class variables
    private T delegate;
    //endregion

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Get the delegate
        this.getDelegate();

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        // Notify the delegate of the results
        ((dialogComInterfaces.timeDialog)delegate).didEnterTime(hourOfDay,minute);
    }

    private void getDelegate()
    {
        Bundle bundle = this.getArguments();

        if(bundle != null)
        {
            // Getting personality
            this.delegate    = (T) bundle.getSerializable(enums.bundleKeys.dialogDelegate.getValue());
        }
    }



}