package android.app.printerapp.postPrinting;

import android.app.AlertDialog;
import android.app.printerapp.R;
import android.app.printerapp.postPrinting.StressRelieving.stressRelievingTestActivity;
import android.app.printerapp.util.ui.dialogs.EditTextDialog;
import android.app.printerapp.util.ui.dialogs.TimePickerFragment;
import android.app.printerapp.util.ui.dialogs.dialogComInterfaces;
import android.app.printerapp.util.ui.enums;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.Serializable;
import java.util.ArrayList;

import static android.app.printerapp.R.layout.activity_post_printing;

public class postPrintingTestActivity extends AppCompatActivity implements Serializable, dialogComInterfaces.timeDialog, dialogComInterfaces.stringDialog {

    private RecyclerView recyclerView;
    private ArrayList<partTestCardViewData> cardViewData;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Button temperatureButton, timeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_post_printing);
        cardViewData = new ArrayList<>();

        recyclerView = this.findViewById(R.id.recycleView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setRecyclerViewData();
        adapter = new partTestCardViewAdapter(this, cardViewData, R.layout.part_tests_card_layout);
        recyclerView.setAdapter(adapter);
        Intent intent = getIntent();
        intent.getData();
        String action = intent.getAction();
        String hardening = intent.getStringExtra("Hardening");
        String tampering = intent.getStringExtra("Tampering");
        TextView title = this.findViewById(R.id.title);
        if ( action == enums.postPrintingTests.Hardening.getValue()); {
        // if (hardening.equals(enums.postPrintingTests.Hardening.getValue())) {
            title.setText("Hardening");
        }
        if (action == enums.postPrintingTests.Tampering.getValue()) {
            title.setText("Tampering");
        }
        /*else if (tampering.equals(enums.postPrintingTests.Tampering.getValue())) {
            title.setText("Tampering");
        }*/
    }

    public void tempButtonPressed(View view) {
        EditTextDialog editTextDialog = new EditTextDialog(this, this);
        editTextDialog.showDialog("Edit temperature", "Change the temperature here...", InputType.TYPE_CLASS_NUMBER);
        temperatureButton = view.findViewById(R.id.tempButton);
    }

    public void timeButtonPressed(View view) {
        Bundle bundleWithDelegate = new Bundle();
        bundleWithDelegate.putSerializable(enums.bundleKeys.dialogDelegate.getValue(), postPrintingTestActivity.this);
        // Show time dialog
        TimePickerFragment<stressRelievingTestActivity> timePicker = new TimePickerFragment<>();

        // Setting arguments of the date picker
        timePicker.setArguments(bundleWithDelegate);

        // Showing the time picker
        timePicker.show(this.getFragmentManager(),"Time");
        timeButton = view.findViewById(R.id.timeButton);
    }

    private void setRecyclerViewData() {
        cardViewData.add(new partTestCardViewData("50", "10", "Detailed info here"));
        cardViewData.add(new partTestCardViewData("10", "70", "Detailed info here"));
        cardViewData.add(new partTestCardViewData("20", "30", "Detailed info here"));
        cardViewData.add(new partTestCardViewData("90", "50", "Detailed info here"));
    }

    public void fabPressed(View view) {

        LayoutInflater inflater = LayoutInflater.from(postPrintingTestActivity.this);
        View dialogView = inflater.inflate(R.layout.activity_add_test_card_dialog, null);

        // Get a builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(postPrintingTestActivity.this);
        // Set the alert dialog view
        alertDialogBuilder.setView(dialogView);

        // Set the editText hint
        EditText temp = (EditText) dialogView.findViewById(R.id.tempEditTextView);
        EditText time = (EditText) dialogView.findViewById(R.id.timeEditTextView);
        EditText detailedInfo = (EditText) dialogView.findViewById(R.id.info);

        // Set buttons
        alertDialogBuilder
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        partTestCardViewData newCard = new partTestCardViewData(temp.getText().toString().trim(), time.getText().toString().trim(), detailedInfo.getText().toString().trim());
                        //adding new object to arraylist
                        if (!(TextUtils.isEmpty(temp.getText())) && (!(TextUtils.isEmpty(time.getText())))) {
                            cardViewData.add(0, newCard);
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

    public void cardViewPressed(View view) {
        LayoutInflater inflater = LayoutInflater.from(postPrintingTestActivity.this);
        View dialogView = inflater.inflate(R.layout.activity_add_stress_relieving_dialog, null);

        // Get a builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(postPrintingTestActivity.this);
        // Set the alert dialog view
        alertDialogBuilder.setTitle("Would you like to delete this test?");
        alertDialogBuilder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = (int) view.getTag();
                cardViewData.remove(position);
                adapter.notifyDataSetChanged();
            }
        }).setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialogBuilder.show();
    }

    public void didEnterStringData(String data) {
        // Ensuring the data is not empty
        if(!data.isEmpty()) {
            // Notify the view about the change
            temperatureButton.setText(data);

            // Notify the model about the change
            //this.model.updateDatabaseContent(this.positionClicked,data);
        }
    }

    @Override
    public void didEnterTime(int hour, int minute) {
// Change int values to strings
        String sHour    = String.valueOf(hour);
        String sMinute  = String.valueOf(minute);

        // Selected String
        String selectedString   = sHour+":"+sMinute;
        timeButton.setText(selectedString);

        // Notify the model about the change
        //  this.model.updateDatabaseContent(this.positionClicked,selectedString);
    }
}
