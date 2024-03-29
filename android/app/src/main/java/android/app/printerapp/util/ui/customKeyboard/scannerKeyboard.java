package android.app.printerapp.util.ui.customKeyboard;

import android.app.printerapp.R;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;


public class scannerKeyboard<T> extends LinearLayout implements View.OnClickListener
{
    //region Class variables
    T delegate;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private Button mButton0;
    private Button mButtonDelete;
    private Button mButtonEnter;
    private Button mButtonBuild;
    private Button mButtonPart;
    private Button mButtonMaterial;

    // This will map the button resource id to the String value that we want to
    // input when that button is clicked.
    SparseArray<String> keyValues = new SparseArray<>();

    // Our communication link to the EditText
    InputConnection inputConnection;

    //endregion

    //region Constructors
    public scannerKeyboard(Context context) {
        this(context, null, 0);
    }

    public scannerKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public scannerKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }
    //endregion

    // region Helper Methods
    private void init(Context context, AttributeSet attrs)
    {
        // initialize buttons
        LayoutInflater.from(context).inflate(R.layout.keyboard, this, true);
        mButton1        = (Button) findViewById(R.id.button_1);
        mButton2        = (Button) findViewById(R.id.button_2);
        mButton3        = (Button) findViewById(R.id.button_3);
        mButton4        = (Button) findViewById(R.id.button_4);
        mButton5        = (Button) findViewById(R.id.button_5);
        mButton6        = (Button) findViewById(R.id.button_6);
        mButton7        = (Button) findViewById(R.id.button_7);
        mButton8        = (Button) findViewById(R.id.button_8);
        mButton9        = (Button) findViewById(R.id.button_9);
        mButton0        = (Button) findViewById(R.id.button_0);
        mButtonDelete   = (Button) findViewById(R.id.button_delete);
        mButtonEnter    = (Button) findViewById(R.id.button_search);
        mButtonBuild    = (Button) findViewById(R.id.buttonBuild);
        mButtonPart     = (Button) findViewById(R.id.buttonPart);
        mButtonMaterial = (Button) findViewById(R.id.buttonMaterial);

        // set button click listeners
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
        mButton7.setOnClickListener(this);
        mButton8.setOnClickListener(this);
        mButton9.setOnClickListener(this);
        mButton0.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);
        mButtonEnter.setOnClickListener(this);
        mButtonBuild.setOnClickListener(this);
        mButtonPart.setOnClickListener(this);
        mButtonMaterial.setOnClickListener(this);

        // map buttons IDs to input strings
        keyValues.put(R.id.button_1, "1");
        keyValues.put(R.id.button_2, "2");
        keyValues.put(R.id.button_3, "3");
        keyValues.put(R.id.button_4, "4");
        keyValues.put(R.id.button_5, "5");
        keyValues.put(R.id.button_6, "6");
        keyValues.put(R.id.button_7, "7");
        keyValues.put(R.id.button_8, "8");
        keyValues.put(R.id.button_9, "9");
        keyValues.put(R.id.button_0, "0");
        keyValues.put(R.id.buttonBuild,"B");
        keyValues.put(R.id.buttonPart,"P");
        keyValues.put(R.id.buttonMaterial,"M");
    }
    //endregion

    //region Complying with OnClick Listener
    @Override
    public void onClick(View v)
    {
        if(v == this.mButtonEnter)
        {
            // Notify the delegate that the search button was clicked
            ((keyboardInterface)delegate).searchButtonClicked();
        }
        else
        {
            // do nothing if the InputConnection has not been set yet
            if (inputConnection == null)
            {
                return;
            }

            // Delete text or input key value
            // All communication goes through the InputConnection
            if (v.getId() == R.id.button_delete)
            {
                CharSequence selectedText = inputConnection.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText))
                {
                    // no selection, so delete previous character
                    inputConnection.deleteSurroundingText(1, 0);
                }
                else
                {
                    // delete the selection
                    inputConnection.commitText("", 1);
                }
            }

            else
            {
                String value = keyValues.get(v.getId());
                inputConnection.commitText(value, 1);
            }
        }
    }
    //endregion

    //region Public Methods - Configuration Methods
    // The activity (or some parent or controller) must give us a reference to the current EditText's InputConnection
    public void setInputConnection(InputConnection ic) {
        this.inputConnection = ic;
    }

    public void setDelegate(T delegate)
    {
        this.delegate = delegate;
    }

    public void disableSearchButton()
    {
        // Change alpha value
        this.mButtonEnter.setAlpha((float)0.5);

        // Disable the button
        this.mButtonEnter.setEnabled(false);
    }

    public void enableSearchButton()
    {
        // Change alpha value
        this.mButtonEnter.setAlpha((float)1.0);

        // Disable the button
        this.mButtonEnter.setEnabled(true);
    }
    //endregion


}