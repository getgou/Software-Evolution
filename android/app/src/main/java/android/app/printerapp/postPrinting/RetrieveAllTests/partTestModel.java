package android.app.printerapp.postPrinting.RetrieveAllTests;


import android.app.Activity;
import android.app.printerapp.R;
import android.app.printerapp.backend.APIClient;
import android.app.printerapp.backend.endpointDB;
import android.app.printerapp.backend.models.NumberOfTests;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.enums;

import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eminahromic on 2017-12-12.
 */

public class partTestModel implements dataInterfaces.partTestControllerToModelInterface {

    private dataInterfaces.partTestModelInterface controller;
    private Activity activity;
    //Class constructor

    public partTestModel(dataInterfaces.partTestModelInterface controller, Activity activity) {
        this.controller = controller;
        this.activity = activity;
    }

    //region Get Items from DB
    private LinkedHashMap<String, String> getItemsMap(NumberOfTests data) {
        // List of item name
        LinkedHashMap<String, String> itemsName = new LinkedHashMap<>();

        if(data!=null) {
            // Data to be manipulated
            String numberOfGeneralTests = String.valueOf(data.getGeneralTests());
            String numberOfStressRelievingTests = String.valueOf(data.getStressRelievingTests());
            String numberOfHardeningTests = String.valueOf(data.getHardeningTests());
            String numberOfTamperingTests = String.valueOf(data.getTamperingTests());
            String numberOfSolutionTreatmentTests = String.valueOf(data.getSolutionTreatmenTest());
            String numberOfAgingTests = String.valueOf(data.getAgingTests());

            // Placing items in the map
            itemsName.put(enums.postPrintingTests.General.getValue(), numberOfGeneralTests);
            itemsName.put(enums.postPrintingTests.StressRelieving.getValue(), numberOfStressRelievingTests);
            itemsName.put(enums.postPrintingTests.Hardening.getValue(), numberOfHardeningTests);
            itemsName.put(enums.postPrintingTests.Tampering.getValue(), numberOfTamperingTests);
            itemsName.put(enums.postPrintingTests.SolutionTreatment.getValue(), numberOfSolutionTreatmentTests);
            itemsName.put(enums.postPrintingTests.AgingTreatment.getValue(), numberOfAgingTests);
        }
        return itemsName;
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
        Call<NumberOfTests> call = endpoint.getNumberOfPartTests(qrcode,token);

        // Execute the call
        call.enqueue(new Callback<NumberOfTests>() {
            @Override
            public void onResponse(Call<NumberOfTests> call, Response<NumberOfTests> response) {
                if(response.body() == null) {
                    // Notify controller that a problem occurred
                    controller.sendMessageToUser(R.string.unableToGetPrintingData);

                    // Send an empty list of item to be displayed
                    controller.displayPersonality(getItemsMap(null));
                } else {
                    // Get the body
                    NumberOfTests data = response.body();

                    // Send the data obtained data to the controller
                    controller.displayPersonality(getItemsMap(data));
                }
            }

            @Override
            public void onFailure(Call<NumberOfTests> call, Throwable t) {
                // Notify controller that a problem occurred
                controller.sendMessageToUser(R.string.unableToGetPrintingData);

                // Send an empty list of item to be displayed
                controller.displayPersonality(getItemsMap(null));
            }
        });
    }
    //endregion

    //region Update Database Content
    @Override
    public void updateDatabaseContent(String key, String newValue) {
        // Get a printing data object containing the new content
     /*   GeneralTest dataToStore = this.getGeneralTestObject(key, newValue);
        // Get the token
        String token = ((MyApplication) activity.getApplication()).getToken();
        // Get the ID
        String qrcode = ((MyApplication) activity.getApplication()).getQrcode();
        // Get a service
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);
        // Get a call
        Call<GeneralTest> call = endpoint.updateGeneralData(qrcode, dataToStore, token);
        // Enqueue the call
        call.enqueue(new Callback<GeneralTest>() {
            @Override
            public void onResponse(Call<GeneralTest> call, Response<GeneralTest> response) {
                Log.i("StatusCode", "Code = " + response.code());
            }
            @Override
            public void onFailure(Call<GeneralTest> call, Throwable t) {
                Log.i("StatusCode", "Failure = " + t.getMessage());
                // Notify the controller that it was not possible to store the data
                controller.sendMessageToUser(R.string.unableToStorePrintigData);
            }
        });*/
    }
    //endregion

    //region Item Clicked
    @Override
    public void itemClicked(int position, String key) {}
    //endregion
}
