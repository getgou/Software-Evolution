package android.app.printerapp.postPrinting.StressRelieving;

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
 * Created by eminahromic on 2017-12-12.
 */

public class stressRelievingCardViewAdapter extends RecyclerView.Adapter<stressRelievingCardViewAdapter.ViewHolder> {

    private List<stressRelievingCardViewData> stressRelievingCardViewData;
    private Activity activity;
    private boolean hasGas;
    private int layout;

    public stressRelievingCardViewAdapter(Activity activity, List<stressRelievingCardViewData> stressRelievingCardViewData, boolean hasGas, int layout) {
        this.stressRelievingCardViewData = stressRelievingCardViewData;
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
        viewHolder.temperatureButton.setText(stressRelievingCardViewData.get(i).getTemperature());
        viewHolder.timeButton.setText(stressRelievingCardViewData.get(i).getTime());
        viewHolder.detailedTextView.setText(stressRelievingCardViewData.get(i).getDetailedInfo());
        if (hasGas) {
            viewHolder.gasButton.setText(String.valueOf(stressRelievingCardViewData.get(i).getHasGas()));
        }
        viewHolder.cardView.setTag(i);
        viewHolder.temperatureButton.setTag(i);
        viewHolder.timeButton.setTag(i);
        viewHolder.detailedTextView.setTag(i);
        viewHolder.gasButton.setTag(i);
    }

    @Override
    public int getItemCount() {
        return (null != stressRelievingCardViewData ? stressRelievingCardViewData.size() : 0);
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