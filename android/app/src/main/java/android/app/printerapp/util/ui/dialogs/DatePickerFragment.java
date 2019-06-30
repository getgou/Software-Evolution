package android.app.printerapp.util.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.printerapp.R;
import android.app.printerapp.util.ui.enums;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment<T> extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    // region Class variables
    private T delegate;
    //endregion

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Getting the delegate
        this.getDelegate();

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.AppBaseTheme, this, year,month,day);
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        // Put month starting in 1
        month = month +1;

        // Notify the delegate of the chosen date
        ((dialogComInterfaces.dateDialog)delegate).didEnterDate(year,month,day);
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
