package android.app.printerapp.backend.tokenManagement;


import android.app.Activity;
import android.app.printerapp.backend.APIClient;
import android.app.printerapp.backend.endpointDB;
import android.app.printerapp.backend.models.Role;
import android.app.printerapp.backend.models.Token;
import android.app.printerapp.backend.models.User;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.enums;
import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenManager<T>
{
    //region Class variables
    private final String TAG = "TokenManager.class";
    private T delegate;
    private Activity activity;
    //endregion

    //region Class constructor
    public TokenManager(T delegate, Activity activity)
    {
        this.delegate   = delegate;
        this.activity    = activity;
    }
    //endregion

    // region Code For Generating a new Token
    public void generateNewToken(String username, String password)
    {
        // Get the Endpoint
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Call the service
        Call<Token> call  =  endpoint.getToken(enums.grantTypes.Password.getValue(), username, password);

        // Make the call
        call.enqueue(new Callback<Token>()
        {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response)
            {
                Log.i(TAG,"Status Code = " + response.code());

                if(response.body() == null)
                {
                    Log.i(TAG,"Body is null");

                    // Notify the delegate that the token was denied
                    ((tokenInterfaces)delegate).tokenDenied();
                }
                else
                {
                    // Store the token locally
                    String token = response.body().getTokenType() + " " + response.body().getAccessToken();

                    // Store token in the shared preferences
                    saveToken(token);

                    // Determine the role of the user
                    getUserRole(token);

                    // Notify the delegate that the token was granted
                    ((tokenInterfaces)delegate).tokenGranted();

                    // Logging
                    Log.i(TAG,"Token = "+ response.body().getTokenType());
                    Log.i(TAG,"Token = "+ response.body().getAccessToken());
                    Log.i(TAG,"Token = "+ response.body().getExpiresIn());
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t)
            {
                Log.i(TAG,"Failure = "+ t.getMessage());

                // Notify the delegate that the token was denied
                ((tokenInterfaces)delegate).tokenDenied();
            }
        });
    }
    //endregion

    //region Code for getting the role of the user
    private void getUserRole(String token)
    {
        // Get an endpoint
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<User> call = endpoint.getUserRole(token);

        // Enqueue the call
        call.enqueue(new Callback<User>()
        {
            @Override
            public void onResponse(Call<User> call, Response<User> response)
            {
                if(response.body() == null)
                {
                    Log.i(TAG, "onResponse(): getUserRole() = Body is null");
                    Log.i(TAG, "onResponse(): getUserRole() = Unable to get user roles");
                }
                else
                {
                    Log.i(TAG, "onResponse(): getUserRole() = Body is NOT null");

                    try
                    {
                        // Store the user roles in the application
                        if(response.body().getRoles()!= null)
                        {
                            // Getting the role of the user
                            ArrayList<Role> roles = new ArrayList<>(response.body().getRoles());

                            // Store the first role
                            saveUserRoles(roles.get(0).getName());
                        }
                    }
                    catch(NullPointerException e)
                    {
                        Log.i(TAG, "onResponse(): getUserRole() = "+ e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t)
            {
                Log.i(TAG, "onFailure(): getUserRole() = Unable to get user roles");

            }
        });
    }
    //endregion

    //region Code for Saving Token to the Application
    private void saveToken(String token)
    {
        // Storing the data in the application class
        ((MyApplication)activity.getApplication()).setToken(token);
    }
    //endregion

    //region Code for saving the role of the user
    private void saveUserRoles(String role)
    {
        Log.i(TAG, "saveUserRoles(): Roles = "+ role);

        // Save the role to the application
        ((MyApplication)activity.getApplication()).setRole(role);
    }

    //endregion
}
