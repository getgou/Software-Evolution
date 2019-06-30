package android.app.printerapp.displayingData.CMBuildHistory;


import android.app.printerapp.util.ui.Log;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.util.ui.Personality;

import java.util.LinkedHashMap;

public class buildHistoryModel implements dataInterfaces.buildHistoryControllerToModelInterface
{

    //region Class variables and Class constructor
    //Class variables
    private dataInterfaces.buildHistoryModelInterface controller;

    // Class constructor
    public buildHistoryModel(dataInterfaces.buildHistoryModelInterface controller)
    {
        this.controller =  controller;
    }

    //endregion

    //region Complying with dataInterfaces.buildHistoryControllerToModelInterface Methods
    @Override
    public void getMapWithData(Personality personality)
    {
        LinkedHashMap<String, String> itemsName = new LinkedHashMap<>();

        // Dummy Data for Build history data
        itemsName.put("Anton","9/9/17");
        itemsName.put("Sepehr","10/10/17");
        itemsName.put("John","5/5/17");

        // Notifying controller
        this.controller.displayPersonality(itemsName);
    }

    @Override
    public void itemClicked(int position, String key)
    {
        Log.i("ClickLis", "Click at History Model");
    }
    //endregion



} //End of class
