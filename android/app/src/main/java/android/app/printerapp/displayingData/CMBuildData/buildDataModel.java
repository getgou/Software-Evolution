package android.app.printerapp.displayingData.CMBuildData;


import android.app.Activity;
import android.app.printerapp.R;
import android.app.printerapp.backend.APIClient;
import android.app.printerapp.backend.endpointDB;
import android.app.printerapp.backend.models.PrintingData;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.enums;
import java.util.LinkedHashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class buildDataModel implements dataInterfaces.buildDataControllerToModelInterface
{
    //region Class variables and Class constructor
    private dataInterfaces.buildDataModelInterface controller;
    private Activity activity;
    private final String TAG = "buildDataModel";

    public buildDataModel(dataInterfaces.buildDataModelInterface controller, Activity activity)
    {
        this.controller = controller;
        this.activity   = activity;

    }
    //endregion

    //region Complying with dataInterfaces.buildDataControllerToModelInterface
    @Override
    public void getMapWithData(Personality personality)
    {
        // Get qrcode of the build
        String qrcode = ((MyApplication)activity.getApplication()).getQrcode();

        // Get the token
        String token = ((MyApplication)activity.getApplication()).getToken();

        // Get a backend service
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<PrintingData> call = null;

        // Get the printing data based on the appropriate QR Code given
        if(qrcode.contains("B"))
        {
            // Execute method for getting printing data based on Build QRCode
            call = endpoint.getPrintingDataByBuildQRCode(qrcode,token);
        }
        else if (qrcode.contains("P"))
        {
            // Execute method for getting printing data based on Part QRCode
            call = endpoint.getPrintingDataByPartQRCode(qrcode, token);
        }

        // Enqueue the call
        if(call != null)
        {
            // Execute the call
            call.enqueue(new Callback<PrintingData>()
            {
                @Override
                public void onResponse(Call<PrintingData> call, Response<PrintingData> response)
                {
                    if(response.body() == null)
                    {
                        Log.i(TAG, "Body is Null");
                        Log.i(TAG, "Status code = "+ response.code());

                        // Notify controller that a problem occurred
                        controller.sendMessageToUser(R.string.unableToGetPrintingData);

                    }
                    else
                    {
                        Log.i(TAG, "Body is NOT Null");

                        // Get the body
                        PrintingData data = response.body();

                        // Send the data obtained data to the controller
                        controller.displayPersonality(getItemsMap(data, qrcode));
                    }
                }

                @Override
                public void onFailure(Call<PrintingData> call, Throwable t)
                {
                    Log.i(TAG, "onFailure() = "+t.getMessage());

                    // Notify controller that a problem occurred
                    controller.sendMessageToUser(R.string.unableToGetPrintingData);
                }
            });
        }

    }

    @Override
    public void itemClicked(int position, String key)
    {
        // Not used
    }

    @Override
    public void updateDatabaseContent(String key, String newValue)
    {
        // Get a printing data object containing the new content
        PrintingData dataToStore  = this.getAPrintingDataObject(key, newValue);

        // Get the token
        String token = ((MyApplication)activity.getApplication()).getToken();

        // Get the ID
        String qrcode = ((MyApplication)activity.getApplication()).getQrcode();

        // Get a service
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<PrintingData> call = endpoint.updatePrintingData(qrcode,dataToStore,token);

        // Enqueue the call
        call.enqueue(new Callback<PrintingData>()
        {
            @Override
            public void onResponse(Call<PrintingData> call, Response<PrintingData> response)
            {
                Log.i("StatusCode", "Code = "+ response.code());
            }

            @Override
            public void onFailure(Call<PrintingData> call, Throwable t)
            {
                Log.i("StatusCode", "Failure = "+ t.getMessage());

                // Notify the controller that it was not possible to store the data
                controller.sendMessageToUser(R.string.unableToStorePrintigData);
            }
        });

    }

    //endregion

    //region Helper Method
    private LinkedHashMap<String, String> getItemsMap(PrintingData data, String qrCode)
    {
        // List of item name
        LinkedHashMap<String, String> itemsName = new LinkedHashMap<>();

        if(data!=null)
        {
            // Data to be manipulated
            String startTime    = data.getStartTime().trim().replaceFirst(activity.getString(R.string.timeRegex),"");
            String endTime      = data.getEndTime().trim().replaceFirst(activity.getString(R.string.timeRegex),"");
            String printedTime  = data.getPrintedTime().trim().replaceFirst(activity.getString(R.string.timeRegex),"");
            String exposureTime = data.getMinExposureTime().trim().replaceFirst(activity.getString(R.string.timeRegex),"");
            String date         = data.getPrintingDate().trim().replaceFirst(activity.getString(R.string.dateRegex),"");;

            // Placing items in the map
            itemsName.put(enums.printingData.buildID.getValue(),"B"+String.valueOf(data.getBuildId()));
            itemsName.put(enums.printingData.startTime.getValue(), startTime);
            itemsName.put(enums.printingData.endTime.getValue(),endTime);
            itemsName.put(enums.printingData.printingDate.getValue(),date);
            itemsName.put(enums.printingData.operator.getValue(), data.getOperator().trim());
            itemsName.put(enums.printingData.typeOfmachine.getValue(),data.getTypeOfmachine().trim());
            itemsName.put(enums.printingData.powderWeightStart.getValue(),data.getPowderWeightStart().toString());
            itemsName.put(enums.printingData.powderweightEnd.getValue(),data.getPowderweightEnd().toString());
            itemsName.put(enums.printingData.weightPowderWaste.getValue(),data.getWeightPowderWaste().toString());
            itemsName.put(enums.printingData.powerUsed.getValue(),String.valueOf(data.getPowerUsed()));
            itemsName.put(enums.printingData.platformMaterial.getValue(),data.getPlatformMaterial().trim());
            itemsName.put(enums.printingData.platformWeight.getValue(),data.getPlatformWeight().toString());
            itemsName.put(enums.printingData.printedTime.getValue(),printedTime);
            itemsName.put(enums.printingData.powderCondition.getValue(),data.getPowderCondition().toString());
            itemsName.put(enums.printingData.numberLayers.getValue(),data.getNumberLayers().toString());
            itemsName.put(enums.printingData.dpcFactor.getValue(),data.getDpcFactor().toString());
            itemsName.put(enums.printingData.minExposureTime.getValue(),exposureTime);
        }

        return itemsName;
    }

    private PrintingData getAPrintingDataObject(String key, String content)
    {
        // Local object
        PrintingData data = new PrintingData();

        if(key.equals(enums.printingData.startTime.getValue()))
        {
            data.setStartTime(content);
        }
        else if(key.equals(enums.printingData.endTime.getValue()))
        {
            data.setEndTime(content);
        }
        else if(key.equals(enums.printingData.printingDate.getValue()))
        {
            data.setPrintingDate(content);
        }
        else if(key.equals(enums.printingData.operator.getValue()))
        {
            data.setOperator(content);
        }
        else if(key.equals(enums.printingData.typeOfmachine.getValue()))
        {
            data.setTypeOfmachine(content);
        }
        else if(key.equals(enums.printingData.powderWeightStart.getValue()))
        {
            data.setPowderWeightStart(Double.valueOf(content));
        }
        else if(key.equals(enums.printingData.powderweightEnd.getValue()))
        {
            data.setPowderweightEnd(Double.valueOf(content));
        }
        else if(key.equals(enums.printingData.weightPowderWaste.getValue()))
        {
            data.setWeightPowderWaste(Double.valueOf(content));
        }
        else if(key.equals(enums.printingData.powerUsed.getValue()))
        {
            data.setPowerUsed(Double.valueOf(content));
        }
        else if(key.equals(enums.printingData.platformMaterial.getValue()))
        {
            data.setPlatformMaterial(content);
        }
        else if(key.equals(enums.printingData.platformWeight.getValue()))
        {
            data.setPlatformWeight(Double.valueOf(content));
        }
        else if(key.equals(enums.printingData.printedTime.getValue()))
        {
            data.setPrintedTime(content);
        }
        else if(key.equals(enums.printingData.powderCondition.getValue()))
        {
            data.setPowderCondition(content);
        }
        else if(key.equals(enums.printingData.numberLayers.getValue()))
        {
            data.setNumberLayers(Integer.valueOf(content));
        }
        else if(key.equals(enums.printingData.dpcFactor.getValue()))
        {
            data.setDpcFactor(Integer.valueOf(content));
        }
        else if(key.equals(enums.printingData.minExposureTime.getValue()))
        {
            data.setMinExposureTime(content);
        }
        return data;
    }

    //endregion

} //end of class
