package android.app.printerapp.displayingData.CMBuildHistory;

import android.app.Activity;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.util.ui.Personality;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

public class buildHistoryController implements Serializable, dataInterfaces.buildHistoryModelInterface, dataInterfaces.dfViewInterface
{
    //region Class variable and Constructor
    // Class variables
    private transient dataInterfaces.buildHistoryControllerToModelInterface model;
    private transient WeakReference<dataInterfaces.dfControllerToViewInterface> view;
    private transient Activity activity;

    //Class constructor
    public buildHistoryController(dataInterfaces.dfControllerToViewInterface view, Activity activity)
    {
        this.model  = new buildHistoryModel(this);
        this.view   = new WeakReference<dataInterfaces.dfControllerToViewInterface>(view);
        this.activity   =   activity;
    }
    //endregion

    //region Complying with dataInterfaces.buildHistoryModelInterface Methods
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
        this.view.get().changeContent(position,newContent);
    }
    //endregion

    //region Complying with dataInterfaces.dfViewInterface Methods
    @Override
    public void getMapWithData(Personality personality)
    {
        this.model.getMapWithData(personality);
    }

    @Override
    public void itemClicked(int position, String key)
    {
        this.model.itemClicked(position, key);
    }


    //endregion

} //End of class
