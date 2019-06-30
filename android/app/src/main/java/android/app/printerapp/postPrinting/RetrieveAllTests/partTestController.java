package android.app.printerapp.postPrinting.RetrieveAllTests;

import android.app.Activity;
import android.app.printerapp.displayingData.dataInterfaces;
import android.app.printerapp.postPrinting.AgingTreatment.agingTreatmentTestActivity;
import android.app.printerapp.postPrinting.General.generalTestActivity;
import android.app.printerapp.postPrinting.Hardening.hardeningTestActivity;
import android.app.printerapp.postPrinting.SolutionTreatment.solutionTreatmentTestActivity;
import android.app.printerapp.postPrinting.StressRelieving.stressRelievingTestActivity;
import android.app.printerapp.postPrinting.Tampering.tamperingTestActivity;
import android.app.printerapp.util.ui.Personality;
import android.app.printerapp.util.ui.enums;
import android.content.Intent;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

/**
 * Created by eminahromic on 2017-12-12.
 */

public class partTestController implements Serializable, dataInterfaces.partTestModelInterface, dataInterfaces.dfViewInterface {

    private transient dataInterfaces.partTestControllerToModelInterface model;
    private transient WeakReference<dataInterfaces.dfControllerToViewInterface> view;
    private transient Activity activity;

    //region Class Constructor
    public partTestController(dataInterfaces.dfControllerToViewInterface view, Activity activity) {
        this.model = new partTestModel(this, activity);
        this.view = new WeakReference<dataInterfaces.dfControllerToViewInterface>(view);
        this.activity = activity;
    }
    //endregion

    //region Send Message To User
    @Override
    public void sendMessageToUser(int message) {
        this.view.get().sendMessageToUser(message);
    }
    //endregion

    //region Display Personality
    @Override
    public void displayPersonality(LinkedHashMap<String, String> data) {
        this.view.get().displayPersonality(data);
    }
    //endregion

    //region Change content
    @Override
    public void changeContent(int position, String newContent) {
        this.view.get().changeContent(position, newContent);
    }
    //endregion

    //region Get Map With Data
    @Override
    public void getMapWithData(Personality personality) {
        this.model.getMapWithData(personality);
    }
    //endregion

    //region Item Clicked
    @Override
    public void itemClicked(int position, String key) {

        if(key.equals(enums.postPrintingTests.General.getValue())) {
            Intent generalTestIntent = new Intent(activity, generalTestActivity.class);
            generalTestIntent.putExtra("General", enums.postPrintingTests.General.getValue());
            activity.startActivity(generalTestIntent);
        } else if (key.equals(enums.postPrintingTests.StressRelieving.getValue())) {
            Intent stressRelievingIntent = new Intent(activity, stressRelievingTestActivity.class);
            stressRelievingIntent.putExtra("StressRelieving", enums.postPrintingTests.StressRelieving.getValue());
            activity.startActivity(stressRelievingIntent);
        } else if (key.equals(enums.postPrintingTests.Hardening.getValue())) {
            Intent intent = new Intent(activity, hardeningTestActivity.class);
            intent.putExtra("Hardening", enums.postPrintingTests.Hardening.getValue());
            activity.startActivity(intent);
        } else if (key.equals(enums.postPrintingTests.Tampering.getValue())) {
            Intent intent = new Intent(activity, tamperingTestActivity.class);
            intent.putExtra("Tampering", enums.postPrintingTests.Tampering.getValue());
            activity.startActivity(intent);
        } else if (key.equals(enums.postPrintingTests.SolutionTreatment.getValue())) {
            Intent intent = new Intent(activity, solutionTreatmentTestActivity.class);
            intent.putExtra("Solution", enums.postPrintingTests.SolutionTreatment.getValue());
            activity.startActivity(intent);
        } else if (key.equals(enums.postPrintingTests.AgingTreatment.getValue())) {
            Intent intent = new Intent(activity, agingTreatmentTestActivity.class);
            intent.putExtra("Aging", enums.postPrintingTests.AgingTreatment.getValue());
            activity.startActivity(intent);
        }
    }
    //endregion
}
