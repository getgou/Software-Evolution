package android.app.printerapp.postPrinting.General;

import android.app.Activity;
import android.app.printerapp.R;
import android.app.printerapp.backend.APIClient;
import android.app.printerapp.backend.endpointDB;
import android.app.printerapp.backend.models.GeneralTest;
import android.app.printerapp.backend.models.UpdateGeneral;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.enums;

import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eminahromic on 2017-11-21.
 */

public class generalTestModel implements dataInterfaces.generalTestControllerToModelInterface {

    private dataInterfaces.generalTestModelInterface controller;
    private Activity activity;

    //region Class constructor
    public generalTestModel(dataInterfaces.generalTestModelInterface controller, Activity activity) {
        this.controller = controller;
        this.activity = activity;
    }
    //endregion

    //region Complying with dataInterfaces.buildDataControllerToModelInterface
    @Override
    public void getMapWithData(Personality personality) {
        // Get qrcode of the build
        String qrcode = ((MyApplication)activity.getApplication()).getQrcode();

        // Get the token
        String token = ((MyApplication)activity.getApplication()).getToken();

        // Get a backend service
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<GeneralTest> call = endpoint.getGeneralTest(qrcode,token);

        // Execute the call
        call.enqueue(new Callback<GeneralTest>()
        {
            @Override
            public void onResponse(Call<GeneralTest> call, Response<GeneralTest> response)
            {
                if(response.body() == null) {
                    // Notify controller that a problem occurred
                    controller.sendMessageToUser(R.string.unableToGetPrintingData);

                    // Send an empty list of item to be displayed
                    controller.displayPersonality(getItemsMap(null));

                } else {
                    // Get the body
                    GeneralTest data = response.body();

                    // Send the data obtained data to the controller
                    controller.displayPersonality(getItemsMap(data));
                }
            }

            @Override
            public void onFailure(Call<GeneralTest> call, Throwable t) {
                // Notify controller that a problem occurred
                controller.sendMessageToUser(R.string.unableToGetPrintingData);

                // Send an empty list of item to be displayed
                controller.displayPersonality(getItemsMap(null));
            }
        });
    }
    //endregion

    //region Get Items from DB
    private LinkedHashMap<String, String> getItemsMap(GeneralTest data) {
        // List of item name
        LinkedHashMap<String, String> itemsName = new LinkedHashMap<>();

        if(data!=null) {
            // Data to be manipulated
            Boolean getSupportRemoval = data.getSupportRemoval();
            Boolean getWEDM = data.getWEDM();
            Boolean getBlasting = data.getBlasting();
            String supportRemoval = null;
            String WEDM = null;
            String blasting = null;
            String getWEDMComment = data.getWedmComment();
            String getBlastingComment = data.getBlastingComments();


            if (getSupportRemoval == true) {
                supportRemoval = "Yes";
            } else {
                supportRemoval = "No";
            }

            if (getWEDM == true) {
                WEDM = "Yes";
            } else {
                WEDM = "No";
            }

            if (getBlasting == true) {
                blasting = "Yes";
            } else {
                blasting = "No";
            }

            // Placing items in the map
            itemsName.put(enums.generalTests.Support.getValue(), supportRemoval);
            itemsName.put(enums.generalTests.WEDM.getValue(), WEDM);
            itemsName.put(enums.generalTests.WEDMComment.getValue(), getWEDMComment);
            itemsName.put(enums.generalTests.Blasting.getValue(), blasting);
            itemsName.put(enums.generalTests.BlastingComment.getValue(), getBlastingComment);
        }
        return itemsName;
    }
    //endregion

    //region Update Database Content
    @Override
    public void updateDatabaseContent(String key, String newValue) {
        // Get a printing data object containing the new content
        //GeneralTest dataToStore = this.getGeneralTestObject(key, newValue);

        Log.i("iscalled", "called");
        Log.i("key", key);
        Log.i("value", newValue);
        UpdateGeneral dataToStore = this.getGeneralTestObject(key, newValue);

        // Get the token
        String token = ((MyApplication) activity.getApplication()).getToken();

        // Get the ID
        String qrcode = ((MyApplication) activity.getApplication()).getQrcode();
        dataToStore.setQrcodeId(qrcode);

        // Get a service
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<String> call = endpoint.updateGeneralData(dataToStore, token);

        // Enqueue the call
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("StatusCode", "Code = " + response.code());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("StatusCode", "Failure = " + t.getMessage());

                // Notify the controller that it was not possible to store the data
                controller.sendMessageToUser(R.string.unableToStorePrintigData);
            }
        });
    }
    //endregion

    //region Item Clicked
    public void itemClicked(int position, String key) {}
    //endregion

    //region Get General Test Object
    private UpdateGeneral getGeneralTestObject(String key, String content) {
        // Local object
        UpdateGeneral data = new UpdateGeneral();

        if (key.equals("support")) {
            data.setSupportRemoval(Boolean.valueOf(content));
        } else if (key.equals(enums.generalTests.WEDM.getValue())) {
            data.setWEDM(Boolean.valueOf(content));
        } else if (key.equals(enums.generalTests.WEDMComment.getValue())) {
            data.setWedmComment(content);
        } else if (key.equals(enums.generalTests.Blasting.getValue())) {
            data.setBlasting(Boolean.valueOf(content));
        } else if (key.equals(enums.generalTests.BlastingComment.getValue())) {
            data.setBlastingComments(content);
        }
        return data;
    }
    //endregion
}
