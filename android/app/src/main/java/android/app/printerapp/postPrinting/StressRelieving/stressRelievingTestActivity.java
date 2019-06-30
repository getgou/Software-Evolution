package android.app.printerapp.postPrinting.StressRelieving;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.printerapp.R;
import android.app.printerapp.backend.APIClient;
import android.app.printerapp.backend.endpointDB;
import android.app.printerapp.backend.models.CreateStressRelieving;
import android.app.printerapp.backend.models.StressRelieving;
import android.app.printerapp.backend.models.UpdateStressRelieving;
import android.app.printerapp.postPrinting.RetrieveAllTests.partTestModel;
import android.app.printerapp.postPrinting.cardViewAdapter;
import android.app.printerapp.postPrinting.cardViewData;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.dialogs.EditTextDialog;
import android.app.printerapp.util.ui.dialogs.TimePickerFragment;
import android.app.printerapp.util.ui.dialogs.dialogComInterfaces;
import android.app.printerapp.util.ui.enums;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eminahromic on 2017-11-08.
 */

public class stressRelievingTestActivity extends Activity implements Serializable, dialogComInterfaces.timeDialog, dialogComInterfaces.stringDialog {

    private RecyclerView recyclerView;
    private ArrayList<stressRelievingCardViewData> stressRelievingCardViewData;
    private partTestModel model;
    private String hasGas;
    private boolean gas;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Button temperatureButton, timeButton, gasButton;
    TextView commentView;
    Boolean didSetTemp, didEditComment;
    HashMap<Integer, Integer> partTestIdList = null;
    int didPresstag = -1;
    //  String token = "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1laWQiOiI3M2U5OTVkNS1hNzVhLTRhMjktOGE1YS0wMDgwNDU2ZTBjNGYiLCJ1bmlxdWVfbmFtZSI6ImVkaXRAbWFpbC5jb20iLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL2FjY2Vzc2NvbnRyb2xzZXJ2aWNlLzIwMTAvMDcvY2xhaW1zL2lkZW50aXR5cHJvdmlkZXIiOiJBU1AuTkVUIElkZW50aXR5IiwiQXNwTmV0LklkZW50aXR5LlNlY3VyaXR5U3RhbXAiOiI3NWIxN2Q3YS1jMjE5LTQyMzAtYTk1Ny0yZjRkZWRmMjk5YmUiLCJyb2xlIjoiRWRpdCIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q1ODYzOCIsImF1ZCI6IjQxNGUxOTI3YTM4ODRmNjhhYmM3OWY3MjgzODM3ZmQxIiwiZXhwIjoxNTE3MzA0MDIwLCJuYmYiOjE1MTIxMjAwMjB9.6rBveGs09VfgfLLo_hW4_gjYQNQUW2T2pmrKVOlntbk";
    //  String partId = "P8";

    //region On Create
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_printing_test);
        stressRelievingCardViewData = new ArrayList<>();
        partTestIdList = new HashMap<>();

        recyclerView = this.findViewById(R.id.recycleView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getDatabaseContent();
        adapter = new stressRelievingCardViewAdapter(this, stressRelievingCardViewData, true, R.layout.stress_relieving_card_layout);
        recyclerView.setAdapter(adapter);

        TextView title = this.findViewById(R.id.title);
        title.setText("Stress Relieving");
    }
    //endregion

    //region Temperature Button Pressed
    public void tempButtonPressed(View view) {
        didSetTemp = true;
        didEditComment = false;
        int position = (int) view.getTag();
        didPresstag = position;
        EditTextDialog editTextDialog = new EditTextDialog(this, this);
        editTextDialog.showDialog("Edit temperature", "Change the temperature here...", InputType.TYPE_CLASS_NUMBER);
        temperatureButton = view.findViewById(R.id.tempButton);
    }
    //endregion

    //region Time Button Pressed
    public void timeButtonPressed(View view) {
        int position = (int) view.getTag();
        didPresstag = position;
        Bundle bundleWithDelegate = new Bundle();
        bundleWithDelegate.putSerializable(enums.bundleKeys.dialogDelegate.getValue(), stressRelievingTestActivity.this);
        // Show time dialog
        TimePickerFragment<stressRelievingTestActivity> timePicker = new TimePickerFragment<>();

        // Setting arguments of the date picker
        timePicker.setArguments(bundleWithDelegate);

        // Showing the time picker
        timePicker.show(this.getFragmentManager(), "Time");
        timeButton = view.findViewById(R.id.timeButton);
    }
    //endregion

    //region Comment View Pressed
    public void commentViewPressed(View view) {
        didEditComment = true;
        didSetTemp = false;
        int position = (int) view.getTag();
        didPresstag = position;
        EditTextDialog editTextDialog = new EditTextDialog(this, this);
        editTextDialog.showDialog("Add comment", "Add your comment here...", InputType.TYPE_CLASS_TEXT);
        commentView = view.findViewById(R.id.text);
    }
    //endregion

    //region Add New Card Button Pressed
    public void fabPressed(View view) {

        LayoutInflater inflater = LayoutInflater.from(stressRelievingTestActivity.this);
        View dialogView = inflater.inflate(R.layout.activity_add_stress_relieving_dialog, null);

        // Get a builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(stressRelievingTestActivity.this);
        // Set the alert dialog view
        alertDialogBuilder.setView(dialogView);

        // Set the editText hint
        EditText temp = (EditText) dialogView.findViewById(R.id.tempEditTextView);
        EditText time = (EditText) dialogView.findViewById(R.id.timeEditTextView);
        Spinner gasSpinner = (Spinner) dialogView.findViewById(R.id.gas);
        EditText detailedInfo = (EditText) dialogView.findViewById(R.id.info);

        //set spinner adapter
        ArrayList<String> hasGasList = new ArrayList<>();
        hasGasList.add("Yes");
        hasGasList.add("No");
        ArrayAdapter<String> spinnAdapter = new ArrayAdapter<String>(stressRelievingTestActivity.this,
                android.R.layout.simple_dropdown_item_1line, hasGasList);
        gasSpinner.setAdapter(spinnAdapter);
        gasSpinner.setOnItemSelectedListener(onItemSelectedListener());

        // Set buttons
        alertDialogBuilder
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temperature = temp.getText().toString().trim() + " " + "C";
                        String timeevent = time.getText().toString();
                        stressRelievingCardViewData newCard = new stressRelievingCardViewData(temperature, timeevent, hasGas, detailedInfo.getText().toString().trim());
                        //adding new object to arraylist
                        if (!(TextUtils.isEmpty(temp.getText())) && (!(TextUtils.isEmpty(time.getText())))) {

                            if (timeevent.matches("^(?:(?:([01]?\\d|2[0-3]):)([0-5]?\\d):)([0-5]?\\d)$")) {
                                stressRelievingCardViewData.add(0, newCard);
                                addNewCardInDatabaseContent(temp.getText().toString().trim(), time.getText().toString().trim(), gas, detailedInfo.getText().toString().trim());
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid time format. Please try again. \n Valid format: HH:MM:SS", Toast.LENGTH_LONG).show();
                            }
                        } else if (TextUtils.isEmpty(temp.getText()) || (TextUtils.isEmpty(time.getText()))) {
                            Toast.makeText(getApplicationContext(), "Both time and temperature is required. Please try again.", Toast.LENGTH_LONG).show();
                        }
                        //notify data set changed in RecyclerView adapter
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing
                    }
                });

        alertDialogBuilder.show();
    }
    //endregion

    //region Listener For Gas Spinner Value
    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                if (position == 0) {
                    gas = true;
                    hasGas = "Yes";
                } else {
                    gas = false;
                    hasGas = "No";
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        };
    }
    //endregion

    //region Delete Card
    public void cardViewPressed(View view) {
        LayoutInflater inflater = LayoutInflater.from(stressRelievingTestActivity.this);
        View dialogView = inflater.inflate(R.layout.activity_add_stress_relieving_dialog, null);

        // Get a builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(stressRelievingTestActivity.this);
        // Set the alert dialog view
        alertDialogBuilder.setTitle("Would you like to delete this test?");
        alertDialogBuilder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = (int) view.getTag();
                didPresstag = position;
                Log.i("tagCard", String.valueOf(position));
                stressRelievingCardViewData.remove(position);
                deleteCardInDatabaseContent();
                adapter.notifyDataSetChanged();
            }
        }).setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialogBuilder.show();
    }
    //endregion

    //region Gas Button Pressed
    public void gasButtonPressed(View view) {
        int position = (int) view.getTag();
        didPresstag = position;
        LayoutInflater inflater = LayoutInflater.from(stressRelievingTestActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_modify_gas, null);

        // Get a builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(stressRelievingTestActivity.this);
        // Set the alert dialog view
        alertDialogBuilder.setTitle("Was shieldgas used?");
        alertDialogBuilder.setView(dialogView);

        Spinner gasSpinner = (Spinner) dialogView.findViewById(R.id.gas);

        //set spinner adapter
        ArrayList<String> hasGasList = new ArrayList<>();
        hasGasList.add("Yes");
        hasGasList.add("No");
        ArrayAdapter<String> spinnAdapter = new ArrayAdapter<String>(stressRelievingTestActivity.this,
                android.R.layout.simple_dropdown_item_1line, hasGasList);
        gasSpinner.setAdapter(spinnAdapter);
        gasSpinner.setOnItemSelectedListener(onItemSelectedListener());
        gasButton = view.findViewById(R.id.isGas);

        // Set buttons
        alertDialogBuilder
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String button = String.valueOf(gasButton);
                        gasButton.setText(hasGas);
                        updateDatabaseContent("gas", String.valueOf(gas));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing
                    }
                });
        alertDialogBuilder.show();
    }
    //endregion

    //region Did Enter String Data
    @Override
    public void didEnterStringData(String data) {
        // Ensuring the data is not empty
        if (!data.isEmpty()) {

            if (didSetTemp == true) {
                temperatureButton.setText(data + " " + "C");

                // Notify the model about the change
                // String button = String.valueOf(R.id.tempButton);
                updateDatabaseContent("temperature",data);
            } else if (didEditComment == true) {
                commentView.setText(data);
                updateDatabaseContent("comment",data);
            }
        }
    }
    //endregion

    //region Did Enter Time
    @Override
    public void didEnterTime(int hour, int minute) {
        // Change int values to strings
        String sHour = String.valueOf(hour);
        String sMinute = String.valueOf(minute);

        // Set the appropriate format for the minute
        if (minute < 10) {
            sMinute = "0"+String.valueOf(minute);
        } else {
            sMinute = String.valueOf(minute);
        }

        // Selected String
        String selectedString = sHour + ":" + sMinute;

        timeButton.setText(selectedString);

        // Notify the model about the change
        String button = String.valueOf(R.id.timeButton);
        this.updateDatabaseContent("time",selectedString);
    }
    //endregion

    //region Get Database Content
    public void getDatabaseContent() {
        // Get the token
        String token = ((MyApplication) this.getApplication()).getToken();

        // Get the ID
        String qrcode = ((MyApplication) this.getApplication()).getQrcode();

        // Get a service
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<List<StressRelieving>> call = endpoint.getStressRelievingTest(qrcode, token);

        // Enqueue the call
        call.enqueue(new Callback<List<StressRelieving>>() {
            @Override
            public void onResponse(Call<List<StressRelieving>> call, Response<List<StressRelieving>> response) {
                Log.i("test", "statuscode =" + response.code());
                if (response.body() == null) {
                    Log.i("test", "Body is null =");
                } else {

                    Log.i("test", "Body not null =");

                    // Get the body
                    List<StressRelieving> data = response.body();

                    for (int i = 0; i < data.size(); i++) {

                        // Store the token locally
                        int partTestId = data.get(i).getPartTestId();
                        // Store token in the shared preferences
                        savePartTestId(String.valueOf(partTestId));

                        Log.i("test", String.valueOf(partTestId));
                    }

                    createCards(data);
                    if (data.size() > 0) {
                        Log.i("test", "temp =" + data.get(0).getTemperature());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<StressRelieving>> call, Throwable t) {
                Log.i("test", "Failure @ = " + t.getMessage());
            }
        });
    }
    //endregion

    //region Save Part Test Id
    private void savePartTestId(String partTestId) {
        // Storing the data in the application class
        ((MyApplication)this.getApplication()).setPartTestId(partTestId);
    }
    //endregion

    //region Update Database Content
    public void updateDatabaseContent(String key, String newValue) {
        // Get a printing data object containing the new content
        UpdateStressRelieving dataToStore = this.getStressRelievingDataObject(key, newValue);
        dataToStore.setPartTestId(partTestIdList.get(didPresstag));

        // Get the token
        String token = ((MyApplication) this.getApplication()).getToken();

        // Get a service
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<String> call = endpoint.updateStressRelievingData(dataToStore, token);


        // Enqueue the call
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("StatusCode", "Code = " + response.code());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("StatusCode", "Failur€ = " + t.getMessage());

                Log.i("StatusCode", "Fail localized = " + t.getLocalizedMessage());

                // Notify the controller that it was not possible to store the data
                // controller.sendMessageToUser(R.string.unableToStorePrintigData);
            }
        });
    }
    //endregion

    //region Delete Database Content
    public void deleteCardInDatabaseContent() {
        // Get a printing data object containing the new content

        // Get the token
        String token = ((MyApplication) this.getApplication()).getToken();

        // Get a service
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        Log.i("parttest", "" + partTestIdList.get(didPresstag));
        // Get a call
        Call<String> call = endpoint.deleteStressTest(partTestIdList.get(didPresstag), token);


        // Enqueue the call
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("StatusCode", "Code = " + response.code());
                try {
                    JSONObject json = new JSONObject(response.errorBody().string());
                    Log.i("code", json.getString("message"));
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("StatusCode", "Failur€ = " + t.getMessage());

                Log.i("StatusCode", "Fail localized = " + t.getLocalizedMessage());

                // Notify the controller that it was not possible to store the data
                // controller.sendMessageToUser(R.string.unableToStorePrintigData);
            }
        });
    }
    //endregion

    //region Add New Card In Database Content
    public void addNewCardInDatabaseContent(String temperature, String time, Boolean gas, String comment) {
        // Get a printing data object containing the new content
        String qrcode = ((MyApplication) this.getApplication()).getQrcode();

        // Get the token
        String token = ((MyApplication) this.getApplication()).getToken();

        // Get a service
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

       // Log.i("parttest", "" + partTestIdList.get(didPresstag));
        CreateStressRelieving data = new CreateStressRelieving();
        data.setQrcode(qrcode);
        data.setTemperature(temperature);
        data.setTimeEvent(time);
        data.setShieldingGas(gas);
        data.setComment(comment);

        // Get a call
        Call<String> call = endpoint.createStressTest(data, token);

        // Enqueue the call
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("StatusCode", "Code = " + response.code());
                try {
                    JSONObject json = new JSONObject(response.errorBody().string());
                    Log.i("code", json.getString("message"));
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("StatusCode", "Failur€ = " + t.getMessage());

                Log.i("StatusCode", "Fail localized = " + t.getLocalizedMessage());

                // Notify the controller that it was not possible to store the data
                // controller.sendMessageToUser(R.string.unableToStorePrintigData);
            }
        });
    }
    //endregion

    //region Create Cards
    public void createCards(List<StressRelieving> data) {

        for (int i = 0; i < data.size(); i++) {
            String temperature = String.valueOf(data.get(i).getTemperature() + " " + "C");
            String time;
            if (data.get(i).getTimeEvent() == null) {
                time = data.get(i).getTimeEvent();
            } else {
                time = data.get(i).getTimeEvent().trim().replaceFirst(this.getString(R.string.timeRegex), "");
            }
            String comment = data.get(i).getComment();
            String didUseShieldGas = null;
            Boolean getShieldGas = data.get(i).getShieldingGas();
            int partTestId = data.get(i).getPartTestId();
            partTestIdList.put(i, partTestId);


            if (getShieldGas == true) {
                didUseShieldGas = "Yes";
            } else {
                didUseShieldGas = "No";
            }

            stressRelievingCardViewData.add(new stressRelievingCardViewData(temperature, time, didUseShieldGas, comment, partTestId));
            adapter.notifyDataSetChanged();
        }
    }
    //endregion

    //region Get StressRelieving Data Object
    private UpdateStressRelieving getStressRelievingDataObject(String key, String content) {
        // Local object
        UpdateStressRelieving data = new UpdateStressRelieving();

        if (key.equals("temperature")) {
            data.setTemperature(Integer.valueOf(content));
        } else if (key.equals("time")) {
            data.setTimeEvent(content);
        } else if (key.equals("comment")) {
            data.setComment(content);
        } else if (key.equals("gas")) {
            data.setShieldingGas(Boolean.valueOf(content));
        }

        return data;
    }
    //endregion

}