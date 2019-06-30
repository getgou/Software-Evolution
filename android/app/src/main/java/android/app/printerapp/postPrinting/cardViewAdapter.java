package android.app.printerapp.postPrinting;

import android.app.Activity;
import android.app.printerapp.R;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by eminahromic on 2017-11-08.
 */

public class cardViewAdapter extends RecyclerView.Adapter<cardViewAdapter.ViewHolder> {

    private List<cardViewData> cardViewData;
    private Activity activity;
    private boolean hasGas;
    private int layout;

   public cardViewAdapter(Activity activity, List<cardViewData> cardViewData, boolean hasGas, int layout) {
        this.cardViewData = cardViewData;
        this.activity = activity;
        this.hasGas = hasGas;
        this.layout = layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.temperatureButton.setText(cardViewData.get(i).getTemperature());
        viewHolder.timeButton.setText(cardViewData.get(i).getTime());
        viewHolder.detailedTextView.setText(cardViewData.get(i).getDetailedInfo());
        if (hasGas) {
            viewHolder.gasButton.setText(String.valueOf(cardViewData.get(i).getHasGas()));
        }
        viewHolder.cardView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return (null != cardViewData ? cardViewData.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        Button temperatureButton, timeButton, gasButton;
        TextView detailedTextView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            temperatureButton = (Button)itemView.findViewById(R.id.tempButton);
            timeButton = (Button)itemView.findViewById(R.id.timeButton);
            if (hasGas) {
                gasButton = (Button)itemView.findViewById(R.id.isGas);
            }
            detailedTextView = (TextView)itemView.findViewById(R.id.text);
            cardView = itemView.findViewById(R.id.cardViewLayout);
        }
    }
}
