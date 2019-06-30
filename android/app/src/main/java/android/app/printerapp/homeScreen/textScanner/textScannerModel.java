package android.app.printerapp.homeScreen.textScanner;


import android.app.Activity;
import android.app.printerapp.R;
import android.app.printerapp.backend.APIClient;
import android.app.printerapp.backend.endpointDB;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.enums;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class textScannerModel implements textScannerInterfaces.tsControllerToModelInterface
{
    //region Class variables and constructor
    // Class variables
    private Activity activity;
    private textScannerInterfaces.tsModelInterface controller;
    private final String TAG = "textScannerModel";

    // Class constructor
    public textScannerModel(textScannerInterfaces.tsModelInterface controller, Activity activity)
    {
        this.activity = activity;
        this.controller = controller;
    }
    //endregion

    //region Complying with textScannerInterfaces.tsControllerToModelInterface Methods
    @Override
    public void qrcodeReceived(String qrcode)
    {
        Log.i("TAG", "at qrcodeReceived() =" +qrcode);

        // Local Variables
        String methodType = "";
        enums.qrCodeType codeType = null;

        if(qrcode.contains("B"))
        {
            methodType  = enums.APIExistMethodType.BuildExist.getValue();
            codeType    = enums.qrCodeType.Build;
        }
        else if (qrcode.contains("P"))
        {
            methodType  = enums.APIExistMethodType.PartExist.getValue();
            codeType    = enums.qrCodeType.Part;
        }
        else if(qrcode.contains("M"))
        {
            methodType  = enums.APIExistMethodType.MaterialExists.getValue();
            codeType    = enums.qrCodeType.Material;
        }

        // Ensure the qrcode passes has a B|P|M
        if(codeType !=null)
        {
            // Determine if the code exits
            this.doesQrCodeExists(codeType,qrcode,methodType);
        }
        else
        {
            controller.sendMessageToUser(R.string.invalidqrcode);
        }
    }

    //endregion

    //region Method for Determining if a Qrcode exists or not
    private void doesQrCodeExists(enums.qrCodeType codeType, String qrcode, String method)
    {

        Log.i("TAG", "at doesQrCodeExists() qrCode=" +qrcode);
        Log.i("TAG", "at doesQrCodeExists() codeType=" +codeType.getValue());
        Log.i("TAG", "at doesQrCodeExists() method=" +method);


        // Get the token
        String token = ((MyApplication)activity.getApplication()).getToken();

        // Get an endpoint
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<Boolean> call = endpoint.doesPartIDExists(codeType.getValue(), method, qrcode, token);

        // Enqueue the call
        call.enqueue(new Callback<Boolean>()
        {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response)
            {
                Log.i("TAG", "onResponse()");
                Log.i("TAG", "Status code = "+ response.code());


                if(response.body() == null)
                {
                    Log.i("TAG", "Body is NULL ");
                    Log.i("TAG", "Status Code = "+ response.code());

                    // Notify the controller that it was not possible to validate the build
                    controller.sendMessageToUser(R.string.qrdoesNotFound);
                }
                else if (response.body() == false)
                {
                    // Notify the controller that it was not possible to validate the build
                    controller.sendMessageToUser(R.string.qrdoesNotFound);
                }
                else if (response.body() == true)
                {
                    // Save the Qrcode ID to the application
                    ((MyApplication)activity.getApplication()).setQrcode(qrcode);

                    // Notify the controller the qrcode is valid
                    controller.validQrcode(codeType);

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t)
            {
                // Notify the controller that it was not possible to validate the build
                controller.sendMessageToUser(R.string.qrdoesNotFound);

                Log.i("TAG", "onFailure(): " + t.getMessage());
            }
        });

    }


    //endregion

}
