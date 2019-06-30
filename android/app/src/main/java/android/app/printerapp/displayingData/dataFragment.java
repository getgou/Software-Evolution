package android.app.printerapp.displayingData;


import android.app.printerapp.R;
import android.app.printerapp.adapters.gridViewAdapter;
import android.app.printerapp.displayingData.CMBuildData.buildDataController;
import android.app.printerapp.displayingData.CMBuildHistory.buildHistoryController;
import android.app.printerapp.displayingData.CMBuildStl.buildStlController;
import android.app.printerapp.postPrinting.General.generalTestController;
import android.app.printerapp.postPrinting.RetrieveAllTests.partTestController;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.dialogs.waitingDialog;
import android.app.printerapp.util.ui.enums;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.LinkedHashMap;

public class dataFragment extends Fragment implements dataInterfaces.dfControllerToViewInterface, AdapterView.OnItemClickListener
{

    //region Class variables and constructor
    private GridView grid;
    private gridViewAdapter gridAdapter;
    private Personality myPersonality;
    private dataInterfaces.dfViewInterface controller;
    private waitingDialog dialogWaiting;
    private boolean shouldConsiderColor = false;

    // Class constructor
    public dataFragment()
    {

    }
    //endregion

    //region Fragment Lifecycle Methods
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        // Get bundle
        this.getBundle();

        // Initialize variables
        this.initializeVariables(view);

        // Set listeners
        this.setListeners();

        // Display waiting dialog
        this.dialogWaiting.show();

        // Request Data
        this.controller.getMapWithData(this.myPersonality);

        return view;
    }
    //endregion

    //region Implements dataInterfaces.dfControllerToViewInterface Methods
    @Override
    public void sendMessageToUser(int message)
    {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();

        if(dialogWaiting != null)
        {
            this.dialogWaiting.dismiss();
        }
    }

    @Override
    public void displayPersonality(LinkedHashMap<String, String> data)
    {
        // Remove the progress dialog
        this.dialogWaiting.dismiss();

        if(this.shouldConsiderColor)
        {
            // Get user role
            String userRole = ((MyApplication)getActivity().getApplication()).getRole();

            // Initialize the other version of the grid adapter
            this.gridAdapter = new gridViewAdapter(this.getContext(), data, userRole);

        }
        else
        {
            // Create an adapter with the data
            this.gridAdapter = new gridViewAdapter(this.getContext(), data);
        }


        //Configure view
        this.configureGridView();
    }

    @Override
    public void changeContent(int position, String newContent)
    {
        // Modify the content in the adapter
        this.gridAdapter.modifyList(position, newContent);

        // Make the change
        this.gridAdapter.notifyDataSetChanged();

    }

    //endregion

    //region Complying with AdapterView.OnItemClickListener Method

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        // Getting the key
        String key = this.gridAdapter.getKey(i);

        // Notify controller
        this.controller.itemClicked(i, key);
    }
    //endregion

    //region Helper Methods
    private void initializeVariables(View view)
    {
        this.grid           = view.findViewById(R.id.gridView);
        this.dialogWaiting  = new waitingDialog(this.getActivity());
    }

    private void getBundle()
    {
        Bundle bundle = this.getArguments();

        if(bundle != null)
        {
          // Getting personality
          this.myPersonality    = (Personality) bundle.getSerializable(enums.bundleKeys.personalityComm.getValue());

          // Getting controller
          Object obj = bundle.getSerializable(enums.bundleKeys.controllerComm.getValue());
          this.settingController(obj, bundle);

        }
    }

    private void configureGridView()
    {
        this.grid.setAdapter(this.gridAdapter);
    }

    private void setListeners()
    {
        this.grid.setOnItemClickListener(this);
    }

    private void settingController(Object object, Bundle bundle)
    {
        if(object instanceof buildDataController)
        {
            // Set the flag to true
            this.shouldConsiderColor = true;

            // Set the controller
            this.controller = (buildDataController) bundle.getSerializable(enums.bundleKeys.controllerComm.getValue());
        }
        else if (object instanceof buildHistoryController)
        {
            // Set the flag to false
            this.shouldConsiderColor = false;

            // Set the controller
            this.controller = (buildHistoryController) bundle.getSerializable(enums.bundleKeys.controllerComm.getValue());
        }
        else if (object instanceof buildStlController)
        {
            // Set the flag to false
            this.shouldConsiderColor = false;

            // Set the controller
            this.controller = (buildStlController) bundle.getSerializable(enums.bundleKeys.controllerComm.getValue());
        }
        else if (object instanceof partTestController) {

            // Set the flag to false
            this.shouldConsiderColor = false;

            // Set the controller
            this.controller = (partTestController) bundle.getSerializable(enums.bundleKeys.controllerComm.getValue());
        }
        else if (object instanceof generalTestController) {

            this.shouldConsiderColor = true;
            this.controller = (generalTestController) bundle.getSerializable(enums.bundleKeys.controllerComm.getValue());
        }
    }

    //endregion

    //region Public Methods
    public String getValueForKey(String key)
    {
        return this.gridAdapter.getValue(key);
    }
    //endregion


} //End of class
