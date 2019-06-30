package android.app.printerapp.adapters;

import android.app.printerapp.R;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.enums;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class gridViewAdapter extends BaseAdapter
{
    //region Class variables and constructor
    private Context context;
    private LinkedHashMap<String, String> itemsName;
    private ArrayList<String> keys;
    private String userRole;

    public gridViewAdapter(Context context, LinkedHashMap<String, String> itemsName)
    {
        this.context    = context;
        this.itemsName  = itemsName;
        this.keys       = new ArrayList<>(itemsName.keySet());
    }

    public gridViewAdapter(Context context, LinkedHashMap<String, String> itemsName, String userRole)
    {
        this.context    = context;
        this.itemsName  = itemsName;
        this.keys       = new ArrayList<>(itemsName.keySet());
        this.userRole   = userRole;
    }

    //endregion

    // region Complying with BaseAdapter Methods
    @Override
    public int getCount()
    {
        // Get size of the map
        return itemsName.size();
    }

    @Override
    public Object getItem(int position)
    {
        // Getting the key for a given position
        String key = keys.get(position);

        // Get the item based on the key
        return itemsName.get(key);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get an inflater
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Initialize view if needed
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.grid_layout, null);
        }

        // Intializing textviews
        TextView innerCircleData = (TextView) convertView.findViewById(R.id.textViewInnerData);
        TextView outerCircleData = (TextView) convertView.findViewById(R.id.textViewOuterData);

        // Assigning circle color based on role. This code is only relevant if one type of constructor is used.
        if(this.userRole != null)
        {
            Log.i("gridAdapter", "user not null");
            Log.i("gridAdapter", "user = "+ userRole);

            if(this.userRole.equals(enums.userRoleType.Read.getValue()) )
            {
                if(position != 0 || !this.keys.get(0).equals(enums.printingData.buildID.getValue()))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        Log.i("gridAdapter", "SDK supported");
                        Drawable background = context.getDrawable(R.drawable.circular_shape_black);
                        innerCircleData.setBackground(background);
                    }
                    else
                    {
                        Log.i("gridAdapter", "SDK not supported");
                    }
                }
            }
        }
        else
        {
            Log.i("gridAdapter", "user is null");
        }

        // Get a data and it key
        String key      = this.keys.get(position);
        String value    = this.itemsName.get(key);

        Log.i("gridAdapter", "Key = "+ key);

            // Adding the appropriate weight units, if applicable.
            if(key.equals(enums.printingData.powderWeightStart.getValue())
                    || key.equals(enums.printingData.powderweightEnd.getValue())
                    || key.equals(enums.printingData.weightPowderWaste.getValue())
                    || key.equals(enums.printingData.powerUsed.getValue())
                    || key.equals(enums.printingData.platformWeight.getValue()))
            {
                value = value + " kg";
            }

            // Adding the approrpiate time unit, if applicable.
            if (key.equals(enums.printingData.minExposureTime.getValue())
                    || key.equals(enums.printingData.printedTime.getValue()))
            {
                value = value + " min";
            }

            // Display the data
            innerCircleData.setText(value);
            outerCircleData.setText(key);

        return convertView;
    }
    //endregion

    //region Custom Public Methods
    public String getKey(int position)
    {
        return this.keys.get(position);
    }

    public String getValue(String key)
    {
        if(itemsName.containsKey(key))
        {
            return this.itemsName.get(key);
        }

        return null;
    }

    public void modifyList(int position, String content)
    {
        // getting the index
        String index = this.keys.get(position);

        // Modifying the item
        itemsName.put(index, content);
    }

    //endregion

} //End of class
