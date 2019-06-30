package android.app.printerapp.util.ui.dialogs;


import android.app.printerapp.util.ui.enums;

public interface dialogComInterfaces
{
    interface stringDialog
    {
        void didEnterStringData(String data);
    }

    interface timeDialog
    {
        void didEnterTime(int hour, int minute);
    }

    interface dateDialog
    {
        void didEnterDate(int year, int month, int day);
    }

    interface singleItemPowderConditionPicker
    {
        void didSelectAPowerCondition(enums.powderCondition powderCondition, int reusedAmount);
    }

} //End of class
