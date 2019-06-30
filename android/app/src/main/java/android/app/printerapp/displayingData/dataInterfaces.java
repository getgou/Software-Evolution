package android.app.printerapp.displayingData;


import android.app.printerapp.util.ui.Personality;

import java.util.LinkedHashMap;

public interface dataInterfaces
{
    // Implemented by the dataFragment
    interface dfControllerToViewInterface
    {
        void sendMessageToUser(int message);
        void displayPersonality(LinkedHashMap<String, String> data);
        void changeContent(int position, String newContent);
    }

    // Implemented by any controller that wants to speak to the dataFragment.
    interface dfViewInterface
    {
        void getMapWithData(Personality personality);
        void itemClicked(int position, String key);
    }

    //region Interfaces between CMBuildData
    interface buildDataModelInterface extends dfControllerToViewInterface
    {
        // No extra methods
    }

    interface buildDataControllerToModelInterface extends dfViewInterface
    {
        void updateDatabaseContent(String key, String newValue);
    }
    //endregion

    //region Interfaces between CMBuildHistory
    interface buildHistoryModelInterface extends dfControllerToViewInterface
    {
        // No extra methods
    }

    interface buildHistoryControllerToModelInterface extends dfViewInterface
    {
        // No extra methods
    }
    //endregion

    //region Interfaces between CMBuildStl
    interface buildStlModelInterface extends dfControllerToViewInterface
    {
        void openSTLViewer(String fileName);
        void displayProgress(int percentage);
    }

    interface buildStlControllerToModelInterface extends dfViewInterface
    {
        // No extra methods
    }

    //endregion

    //region Interfaces Between CMPartTest
    interface partTestModelInterface extends dfControllerToViewInterface {}

    interface partTestControllerToModelInterface extends dfViewInterface {
         void updateDatabaseContent(String key, String newValue);
    }
    //endregion

    //region Interfaces Between General Test
    interface generalTestModelInterface extends dfControllerToViewInterface {}

    interface generalTestControllerToModelInterface extends dfViewInterface {
        void updateDatabaseContent(String key, String newValue);
    }
    //endregion

} //End of class
