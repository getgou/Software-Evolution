package android.app.printerapp.postPrinting.AgingTreatment;

import android.app.AlertDialog;
import android.app.printerapp.R;
import android.app.printerapp.backend.APIClient;
import android.app.printerapp.backend.endpointDB;
import android.app.printerapp.backend.models.AgingTreatmentTest;
import android.app.printerapp.backend.models.CreateAgingTest;
import android.app.printerapp.backend.models.UpdateAgingTreatmentTest;
import android.app.printerapp.postPrinting.partTestCardViewAdapter;
import android.app.printerapp.postPrinting.partTestCardViewData;
import android.app.printerapp.postPrinting.StressRelieving.stressRelievingTestActivity;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.dialogs.EditTextDialog;
import android.app.printerapp.util.ui.dialogs.TimePickerFragment;
import android.app.printerapp.util.ui.dialogs.dialogComInterfaces;
import android.app.printerapp.util.ui.enums;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class agingTreatmentTestActivity extends AppCompatActivity implements Serializable, dialogComInterfaces.timeDialog, dialogComInterfaces.stringDialog {

    private RecyclerView recyclerView;
    private ArrayList<partTestCardViewData> cardViewData;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Button temperatureButton, timeButton;
    Boolean didSetTemp, didEditComment;
    TextView commentView;
    HashMap<Integer, Integer> partTestIdList = null;
    int didPresstag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_printing);
        partTestIdList = new HashMap<>();

        cardViewData = new ArrayList<>();

        recyclerView = this.findViewById(R.id.recycleView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getDatabaseContent();
        adapter = new partTestCardViewAdapter(this, cardViewData, R.layout.part_tests_card_layout);
        recyclerView.setAdapter(adapter);
        TextView title = this.findViewById(R.id.title);
        title.setText("Aging Treatment");
    }

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
        bundleWithDelegate.putSerializable(enums.bundleKeys.dialogDelegate.getValue(), agingTreatmentTestActivity.this);
        // Show time dialog
        TimePickerFragment<stressRelievingTestActivity> timePicker = new TimePickerFragment<>();

        // Setting arguments of the date picker
        timePicker.setArguments(bundleWithDelegate);

        // Showing the time picker
        timePicker.show(this.getFragmentManager(),"Time");
        timeButton = view.findViewById(R.id.timeButton);
    }
    //endregion

    //region Add New Card Button Pressed
    public void fabPressed(View view) {

        LayoutInflater inflater = LayoutInflater.from(agingTreatmentTestActivity.this);
        View dialogView = inflater.inflate(R.layout.activity_add_test_card_dialog, null);

        // Get a builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(agingTreatmentTestActivity.this);
        // Set the alert dialog view
        alertDialogBuilder.setView(dialogView);

        // Set the editText hint
        EditText temp = (EditText) dialogView.findViewById(R.id.tempEditText);
        EditText time = (EditText) dialogView.findViewById(R.id.timeEditText);
        EditText detailedInfo = (EditText) dialogView.findViewById(R.id.infoText);

        // Set buttons
        alertDialogBuilder
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temperature = temp.getText().toString().trim();
                        String timeevent = time.getText().toString().trim();
                        String comment = detailedInfo.getText().toString().trim();
                         partTestCardViewData newCard = new partTestCardViewData(temperature + " " + "C", timeevent, comment);
                            //adding new object to arraylist
                            if (!(TextUtils.isEmpty(temp.getText())) && (!(TextUtils.isEmpty(time.getText())))) {
                                if (timeevent.matches("^(?:(?:([01]?\\d|2[0-3]):)([0-5]?\\d):)([0-5]?\\d)$")) {
                                    cardViewData.add(0, newCard);
                                    createNewAgingTestDatabaseContent(temperature, timeevent, comment);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid time format. Please try again. \n Valid format: HH:MM:SS.", Toast.LENGTH_LONG).show();
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

    //region Delete Card Button Pressed
    public void cardViewPressed(View view) {
        LayoutInflater inflater = LayoutInflater.from(agingTreatmentTestActivity.this);
        View dialogView = inflater.inflate(R.layout.activity_add_test_card_dialog, null);

        // Get a builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(agingTreatmentTestActivity.this);
        // Set the alert dialog view
        alertDialogBuilder.setTitle("Would you like to delete this test?");
        alertDialogBuilder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = (int) view.getTag();
                didPresstag = position;
                cardViewData.remove(position);
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

    //region Helper Method Did Enter String Data
    public void didEnterStringData(String data) {
        // Ensuring the data is not empty
        if(!data.isEmpty()) {
            if (didSetTemp == true) {
                temperatureButton.setText(data + " " + "C");
                // Notify the model about the change
                updateDatabaseContent("temperature",data);
            } else if (didEditComment == true) {
                commentView.setText(data);
                updateDatabaseContent("comment",data);
            }
        }
    }
    //endregion

    //region Helper Method Did Enter Time
    @Override
    public void didEnterTime(int hour, int minute) {
        // Change int values to strings
        String sHour    = String.valueOf(hour);
        String sMinute  = String.valueOf(minute);

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
        Call<List<AgingTreatmentTest>> call = endpoint.getAgingTreatmentTest(qrcode, token);

        // Enqueue the call
        call.enqueue(new Callback<List<AgingTreatmentTest>>() {
            @Override
            public void onResponse(Call<List<AgingTreatmentTest>> call, Response<List<AgingTreatmentTest>> response) {
                Log.i("test", "statuscode =" + response.code());
                if (response.body() == null) {

                    Log.i("test", "Body is null =");
                } else {

                    Log.i("test", "Body not null =");

                    // Get the body
                    List<AgingTreatmentTest> data = response.body();

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
            public void onFailure(Call<List<AgingTreatmentTest>> call, Throwable t) {
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

        UpdateAgingTreatmentTest dataToStore = this.getAgingTreatmentDataObject(key, newValue);
        dataToStore.setPartTestId(partTestIdList.get(didPresstag));

        Log.i("parttest", "" + dataToStore.getPartTestId());
        // Get the token
        String token = ((MyApplication) this.getApplication()).getToken();

        // Get a service
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<String> call = endpoint.updateAgingTreatmentData(dataToStore, token);

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
        Call<String> call = endpoint.deletePartTest(partTestIdList.get(didPresstag), token);


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

    //region Create New Aging Test Database Content
    public void createNewAgingTestDatabaseContent(String temperature, String time, String comment) {
        // Get a printing data object containing the new content
      //  Log.i("parttest", "" + dataToStore.getPartTestId());
        // Get the token
        String token = ((MyApplication) this.getApplication()).getToken();

        String qrcode = ((MyApplication) this.getApplication()).getQrcode();

        CreateAgingTest data = new CreateAgingTest();
        data.setQrcode(qrcode);
        data.setTemperature(temperature);
        data.setTimeEvent(time);
        data.setComment(comment);

        // Get a service
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Get a call
        Call<String> call = endpoint.createAgingTest(data, token);

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
                Log.i("StatusCode", "Failure = " + t.getMessage());

                // Notify the controller that it was not possible to store the data
                // controller.sendMessageToUser(R.string.unableToStorePrintigData);
            }
        });
    }
    //endregion

    //region Create New Card
    public void createCards(List<AgingTreatmentTest> data) {

        for (int i = 0; i < data.size(); i++) {

            String temperature = String.valueOf(data.get(i).getTemperature() + " " + "C");
            String time = data.get(i).getTimeEvent().trim().replaceFirst(this.getString(R.string.timeRegex), "");
            String comment = data.get(i).getComment();
            int partTestId = data.get(i).getPartTestId();
            partTestIdList.put(i, partTestId);

            cardViewData.add(new partTestCardViewData(temperature, time, comment, partTestId));
            adapter.notifyDataSetChanged();
        }
    }
    //endregion

    //region Get Hardening Data Object
    private UpdateAgingTreatmentTest getAgingTreatmentDataObject(String key, String content) {
        // Local object
        UpdateAgingTreatmentTest data = new UpdateAgingTreatmentTest();

        if (key.equals("temperature")) {
            data.setTemperature(Integer.valueOf(content));
        } else if (key.equals("time")) {
            data.setTimeEvent(content);
        } else if (key.equals("comment")) {
            data.setComment(content);
        }

        return data;
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
}
