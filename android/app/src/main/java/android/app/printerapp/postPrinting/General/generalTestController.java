package android.app.printerapp.postPrinting.General;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.printerapp.R;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.dialogs.EditTextDialog;
import android.app.printerapp.util.ui.dialogs.dialogComInterfaces;
import android.app.printerapp.util.ui.enums;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by eminahromic on 2017-12-12.
 */

public class generalTestController implements Serializable, dataInterfaces.generalTestModelInterface, dataInterfaces.dfViewInterface, dialogComInterfaces.stringDialog {
    // region Class variables and Class contructor
    private transient dataInterfaces.generalTestControllerToModelInterface model;
    private transient WeakReference<dataInterfaces.dfControllerToViewInterface> view;
    private transient Activity activity;
    private transient int positionClicked;
    private transient String keyClicked;
    private String hasComment;
    private boolean comment;
    private transient final String TAG = "generalTestController";

    public generalTestController(dataInterfaces.dfControllerToViewInterface view, Activity activity) {
        this.view = new WeakReference<dataInterfaces.dfControllerToViewInterface>(view);
        this.model = new generalTestModel(this, activity);
        this.activity = activity;
    }
    //endregion

    //region Complying with dataInterfaces.buildDataModelInterface Methods
    @Override
    public void sendMessageToUser(int message) {
        this.view.get().sendMessageToUser(message);
    }

    @Override
    public void displayPersonality(LinkedHashMap<String, String> data) {
        this.view.get().displayPersonality(data);
    }

    @Override
    public void changeContent(int position, String newContent) {
        this.view.get().changeContent(position, newContent);
    }
    //endregion

    // region Complying with dataInterfaces.dfViewInterface Methods
    @Override
    public void getMapWithData(Personality personality) {
        this.model.getMapWithData(personality);
    }

    @Override
    public void itemClicked(int position, String key) {

        // Getting the user role
        String userRole = ((MyApplication)activity.getApplication()).getRole();

        Log.i(TAG, "User role = "+ userRole);
        this.keyClicked = key;

        if(userRole == null || userRole.equals(enums.userRoleType.Read.getValue())) {
            // Notify user that he has a read permission only
            this.view.get().sendMessageToUser(R.string.readPermissionMsg);
        }
        else
        {
            this.positionClicked = position;
            this.showDialog(key);
        }

    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                if (position == 0) {
                    comment = true;
                    hasComment = "Yes";
                } else {
                    comment = false;
                    hasComment = "No";
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
            }
        };
    }

    private void showDialog(String key) {
        // Creating bundle with delegate
        Bundle bundleWithDelegate = new Bundle();
        bundleWithDelegate.putSerializable(enums.bundleKeys.dialogDelegate.getValue(), generalTestController.this);

        // Show time dialog
        if(key.equals(enums.generalTests.Support.getValue())) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            View dialogView = inflater.inflate(R.layout.dialog_modify_gas, null);

            // Get a builder
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            // Set the alert dialog view
            alertDialogBuilder.setTitle("Was support used?");
            alertDialogBuilder.setView(dialogView);

            Spinner gasSpinner = (Spinner) dialogView.findViewById(R.id.gas);

            //set spinner adapter
            ArrayList<String> hasGasList = new ArrayList<>();
            hasGasList.add("Yes");
            hasGasList.add("No");
            ArrayAdapter<String> spinnAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, hasGasList);
            gasSpinner.setAdapter(spinnAdapter);
            gasSpinner.setOnItemSelectedListener(onItemSelectedListener());

            // Set buttons
            alertDialogBuilder
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // gasButton.setText(hasComment);
                            view.get().changeContent(0, hasComment);
                            model.updateDatabaseContent("support", String.valueOf(comment));
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Do nothing
                        }
                    });
            alertDialogBuilder.show();
        } else if (key.equals(enums.generalTests.WEDM.getValue())) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            View dialogView = inflater.inflate(R.layout.dialog_modify_gas, null);

            // Get a builder
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            // Set the alert dialog view
            alertDialogBuilder.setTitle("Was WEDM used?");
            alertDialogBuilder.setView(dialogView);

            Spinner gasSpinner = (Spinner) dialogView.findViewById(R.id.gas);

            //set spinner adapter
            ArrayList<String> hasGasList = new ArrayList<>();
            hasGasList.add("Yes");
            hasGasList.add("No");
            ArrayAdapter<String> spinnAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, hasGasList);
            gasSpinner.setAdapter(spinnAdapter);
            gasSpinner.setOnItemSelectedListener(onItemSelectedListener());

            // Set buttons
            alertDialogBuilder
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // gasButton.setText(hasComment);
                            view.get().changeContent(1, hasComment);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Do nothing
                        }
                    });
            alertDialogBuilder.show();
        } else if (key.equals(enums.generalTests.WEDMComment.getValue())) {
            EditTextDialog editTextDialog = new EditTextDialog(activity, this);
            editTextDialog.showDialog(key, key, InputType.TYPE_CLASS_TEXT);
        } else if (key.equals(enums.generalTests.Blasting.getValue())) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            View dialogView = inflater.inflate(R.layout.dialog_modify_gas, null);

            // Get a builder
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            // Set the alert dialog view
            alertDialogBuilder.setTitle("Was blasting used?");
            alertDialogBuilder.setView(dialogView);

            Spinner gasSpinner = (Spinner) dialogView.findViewById(R.id.gas);

            //set spinner adapter
            ArrayList<String> hasGasList = new ArrayList<>();
            hasGasList.add("Yes");
            hasGasList.add("No");
            ArrayAdapter<String> spinnAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, hasGasList);
            gasSpinner.setAdapter(spinnAdapter);
            gasSpinner.setOnItemSelectedListener(onItemSelectedListener());

            // Set buttons
            alertDialogBuilder
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // gasButton.setText(hasComment);
                            view.get().changeContent(3, hasComment);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Do nothing
                        }
                    });
            alertDialogBuilder.show();
        } else if (key.equals(enums.generalTests.WEDMComment.getValue())) {
            EditTextDialog editTextDialog = new EditTextDialog(activity, this);
            editTextDialog.showDialog(key, key, InputType.TYPE_CLASS_TEXT);
        }

    }
    //endregion

    //region Complying with dialogComInterfaces.stringDialog Methods
    // @Override
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void didEnterStringData(String data) {
        // Ensuring the data is not empty
        if(!data.isEmpty()) {
            // Notify the view about the change
            this.view.get().changeContent(this.positionClicked,data);
            this.model.updateDatabaseContent(this.keyClicked,data);
        }
    }
    //endregion

    //endregion
}