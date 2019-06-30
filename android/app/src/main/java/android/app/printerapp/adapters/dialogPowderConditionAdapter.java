package android.app.printerapp.adapters;


import android.app.printerapp.R;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;

public class dialogPowderConditionAdapter extends BaseAdapter
{
    //region Class variables and constructor
    private ArrayList<String> objects;
    private Context context;
    private int reusedValueSelected;
    private final int EDIT_TEXT_POSITION = 3;


    public dialogPowderConditionAdapter(@NonNull Context context, @NonNull String[] objects)
    {
        this.context    = context;
        this.objects    = this.arrayToList(objects);
    }
    //endregion

    //region Complying with BaseAdapter Methods
    @Override
    public int getCount()
    {
        return this.objects.size();
    }

    @Override
    public Object getItem(int i)
    {
        return this.objects.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        // Initiate the view in case it is null
        if (position == this.EDIT_TEXT_POSITION)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.setting_reused_amount,null);

            BubbleSeekBar BSB = convertView.findViewById(R.id.BSBReusedAmount);
            BSB.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener()
            {
                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat)
                {
                    // Not used
                }

                @Override
                public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat)
                {
                    // Storing the progress locally
                    reusedValueSelected = progress;
                }

                @Override
                public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat)
                {
                    // Not used
                }
            });

            return convertView;
        }
        else
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.powder_condition_picker,null);

            // Set the item
            CheckedTextView TV = (CheckedTextView)convertView.findViewById(R.id.item);

            // Set the text
            TV.setText(this.objects.get(position));

            return convertView;
        }

    }
    //endregion

    //region Public Methods
    public void showEditText()
    {
        // Add an item to the string
        this.objects.add(this.EDIT_TEXT_POSITION,"Bla Bla");

        // Notify adapter
        this.notifyDataSetChanged();
    }

    public void removeEditText()
    {
        if(this.objects.size() == 4)
        {
            this.objects.remove(this.EDIT_TEXT_POSITION);

            // Notify adapter
            this.notifyDataSetChanged();
        }

    }

    public int getReusedAmount()
    {
       return this.reusedValueSelected;
    }

    //endregion

    //region Helper Methods
    private ArrayList<String> arrayToList(String[] array)
    {
        // Create a local list
        ArrayList<String> list = new ArrayList<>();

        //Make the transformation
        for(int i = 0; i < array.length; i++)
        {
           list.add(array[i]);
        }

        // Return the list
        return list;
    }
    //endregion



} //End of class
