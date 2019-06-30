package android.app.printerapp.viewer;

import android.app.Activity;
import android.app.printerapp.R;
import android.app.printerapp.backend.APIClient;
import android.app.printerapp.util.ui.FileHelper;
import android.app.printerapp.backend.endpointDB;
import android.app.printerapp.backend.models.PartFile;
import android.app.printerapp.backend.s3Downloader.S3Download;
import android.app.printerapp.backend.s3Downloader.s3downloadInterface;
import android.app.printerapp.util.ui.CustomEditableSlider;
import android.app.printerapp.util.ui.CustomPopupWindow;
import android.app.printerapp.util.ui.ListIconPopupWindowAdapter;
import android.app.printerapp.util.ui.Log;
import android.app.printerapp.util.ui.MyApplication;
import android.app.printerapp.util.ui.enums;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.devsmart.android.ui.HorizontalListView;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.afollestad.materialdialogs.MaterialDialog;


public class ViewerMainFragment extends Fragment implements s3downloadInterface {

    private static final String TAG = "ViewerMainFragment";
    private String filename;

    //Tabs
    private static final int NORMAL = 0;
    private static final int OVERHANG = 1;
    private static final int TRANSPARENT = 2;
    private static final int XRAY = 3;
    private static final int LAYER = 4;
    public static final String ARG_STL_PATH = "stl_path";

    private static int mCurrentViewMode = 0;

    //Constants
    public static final int DO_SNAPSHOT = 0;
    public static final int DONT_SNAPSHOT = 1;
    public static final int PRINT_PREVIEW = 3;
    public static final boolean STL = true;
    public static final boolean GCODE = false;

    private static final float POSITIVE_ANGLE = 15;
    private static final float NEGATIVE_ANGLE = -15;

    private static final int MENU_HIDE_OFFSET_SMALL = 20;
    private static final int MENU_HIDE_OFFSET_BIG = 1000;

    //Variables
    private static File mFile;

    private static ViewerSurfaceView mSurface;
    private static FrameLayout mLayout;

    //Advanced settings expandable panel
    private int mSettingsPanelMinHeight;

    //Buttons
    private static ImageButton mVisibilityModeButton;

    private static SeekBar mSeekBar;
    private boolean isKeyboardShown = false;

    private static List<DataStorage> mDataList = new ArrayList<DataStorage>();

    //Undo button bar
    private static LinearLayout mUndoButtonBar;

    //Edition menu variables
    private static ProgressBar mProgress;

    private static Context mContext;
    private static View mRootView;

    private static LinearLayout mStatusBottomBar;
    private static FrameLayout mBottomBar;
    private static LinearLayout mRotationLayout;
    private static LinearLayout mScaleLayout;
    private static CustomEditableSlider mRotationSlider;
    private static ImageView mActionImage;

    private static EditText mScaleEditX;
    private static EditText mScaleEditY;
    private static EditText mScaleEditZ;
    private static ImageButton mUniformScale;

    private static ScaleChangeListener mTextWatcherX;
    private static ScaleChangeListener mTextWatcherY;
    private static ScaleChangeListener mTextWatcherZ;
    private static boolean fileUploaded = false;

    /**
     * ****************************************************************************
     */

    private static int mCurrentType = WitboxFaces.TYPE_WITBOX;
    ;
    private static int[] mCurrentPlate = new int[]{WitboxFaces.WITBOX_LONG, WitboxFaces.WITBOX_WITDH, WitboxFaces.WITBOX_HEIGHT};
    ;

    private static LinearLayout mSizeText;
    private static int mCurrentAxis;
    private String mStlPath;

    //private static Geometry.Point mPreviousOffset;

    //Empty constructor
    public ViewerMainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Retain instance to keep the Fragment from destroying itself
        setRetainInstance(true);

        if (getArguments() != null) {
            mStlPath = getArguments().getString(ARG_STL_PATH);
            filename= this.getArguments().getString(enums.bundleKeys.filenameComm.getValue());
            Log.i(TAG, "Argument obtained. Filename = "+ filename);
        }
        else
        {
            Log.i(TAG, "No argument obtained");
        }

//        LibraryController.reloadFiles(LibraryController.TAB_ALL);
//        List<File> files = LibraryController.getFileList();
//        for (File file: files
//             ) {
//            Log.i("FILE!!!", file.getName());
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Reference to View
        mRootView = null;

        //If is not new
        if (savedInstanceState == null) {

            //Show custom option menu
            setHasOptionsMenu(true);

            //Inflate the fragment
            mRootView = inflater.inflate(R.layout.print_panel_main,
                    container, false);

            mContext = getActivity();



            initUIElements();

//            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            //Init slicing elements
            mCurrentType = WitboxFaces.TYPE_WITBOX;
            mCurrentPlate = new int[]{WitboxFaces.WITBOX_LONG, WitboxFaces.WITBOX_WITDH, WitboxFaces.WITBOX_HEIGHT};

            mSurface = new ViewerSurfaceView(mContext, mDataList, NORMAL, DONT_SNAPSHOT);
            draw();

            //Hide the action bar when editing the scale of the model
            mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    Rect r = new Rect();
                    mRootView.getWindowVisibleDisplayFrame(r);

                    if (mSurface.getEditionMode() == ViewerSurfaceView.SCALED_EDITION_MODE) {

                        int[] location = new int[2];
                        int heightDiff = mRootView.getRootView().getHeight() - (r.bottom - r.top);

                        if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...

                            if (!isKeyboardShown) {
                                isKeyboardShown = true;
                                mActionModePopupWindow.getContentView().getLocationInWindow(location);

                                if (Build.VERSION.SDK_INT >= 19)
                                    mActionModePopupWindow.update(location[0], location[1] - MENU_HIDE_OFFSET_SMALL);
                                else
                                    mActionModePopupWindow.update(location[0], location[1] + MENU_HIDE_OFFSET_BIG);
                            }
                        } else {
                            if (isKeyboardShown) {
                                isKeyboardShown = false;
                                mActionModePopupWindow.getContentView().getLocationInWindow(location);

                                if (Build.VERSION.SDK_INT >= 19)
                                    mActionModePopupWindow.update(location[0], location[1] + MENU_HIDE_OFFSET_SMALL);
                                else
                                    mActionModePopupWindow.update(location[0], location[1] - MENU_HIDE_OFFSET_BIG);

                            }

                        }
                    }

                }
            });
        }

        // This check is to make sure the same part is not loaded everytime the view is created
        if(!this.fileUploaded)
        {
            this.fileUploaded = true;
        }
        else
        {
            Log.i(TAG, "Filename Already Being Shown");
            this.resetWhenCancel();
        }

        // Start algorithm
        this.STlAlgorithm();

        return mRootView;

    }

    public static void resetWhenCancel() {


        //Crashes on printview
        try {
            mDataList.remove(mDataList.size() - 1);
            mSurface.requestRender();

            mCurrentViewMode = NORMAL;
            mSurface.configViewMode(mCurrentViewMode);

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    /**
     * ********************** UI ELEMENTS *******************************
     */

    private void initUIElements() {

        //Set behavior of the expandable panel
//        final FrameLayout expandablePanel = (FrameLayout) mRootView.findViewById(R.id.advanced_options_expandable_panel);
//        expandablePanel.post(new Runnable() { //Get the initial height of the panel after onCreate is executed
//            @Override
//            public void run() {
//                mSettingsPanelMinHeight = expandablePanel.getMeasuredHeight();
//            }
//        });
        /*final CheckBox expandPanelButton = (CheckBox) mRootView.findViewById(R.id.expand_button_checkbox);
        expandPanelButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Expand/collapse the expandable panel
                if (isChecked) ExpandCollapseAnimation.collapse(expandablePanel, mSettingsPanelMinHeight);
                else ExpandCollapseAnimation.expand(expandablePanel);
            }
        });*/

        //Set elements to handle the model
        mSeekBar = (SeekBar) mRootView.findViewById(R.id.barLayer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mSeekBar.getThumb().mutate().setAlpha(0);
        mSeekBar.setVisibility(View.INVISIBLE);

        //Undo button bar
        mUndoButtonBar = (LinearLayout) mRootView.findViewById(R.id.model_button_undo_bar_linearlayout);

        mLayout = (FrameLayout) mRootView.findViewById(R.id.viewer_container_framelayout);

        mVisibilityModeButton = (ImageButton) mRootView.findViewById(R.id.visibility_button);
        mVisibilityModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                showVisibilityPopUpMenu();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDataList.get(0).setActualLayer(progress);
                mSurface.requestRender();
            }
        });


        /*****************************
         * EXTRA
         *****************************/
        mProgress = (ProgressBar) mRootView.findViewById(R.id.progress_bar);
        mProgress.setVisibility(View.GONE);
        mSizeText = (LinearLayout) mRootView.findViewById(R.id.axis_info_layout);
        mActionImage = (ImageView) mRootView.findViewById(R.id.print_panel_bar_action_image);


        mRotationSlider = (CustomEditableSlider) mRootView.findViewById(R.id.print_panel_slider);
        mRotationSlider.setValue(12);
        mRotationSlider.setShownValue(0);
        mRotationSlider.setMax(24);
        mRotationSlider.setShowNumberIndicator(true);
        mRotationSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        break;

                    case MotionEvent.ACTION_UP:

                        if (mSurface.getEditionMode() == ViewerSurfaceView.ROTATION_EDITION_MODE)
                            mSurface.refreshRotatedObject();

                        break;
                }


                return false;
            }
        });
        mRotationSlider.setOnValueChangedListener(new CustomEditableSlider.OnValueChangedListener() {

            boolean lock = false;


            @Override
            public void onValueChanged(int i) {

                //Calculation on a 12 point seekbar
                float newAngle = (i - 12) * POSITIVE_ANGLE;

                mRotationSlider.setShownValue((int) newAngle);

                try {


                    if (!lock) {

                        switch (mCurrentAxis) {

                            case 0:
                                mSurface.rotateAngleAxisX(newAngle);
                                break;
                            case 1:
                                mSurface.rotateAngleAxisY(newAngle);
                                break;
                            case 2:
                                mSurface.rotateAngleAxisZ(newAngle);
                                break;
                            default:
                                return;

                        }

                    }

                    mSurface.requestRender();


                } catch (ArrayIndexOutOfBoundsException e) {

                    e.printStackTrace();
                }


            }
        });

        mStatusBottomBar = (LinearLayout) mRootView.findViewById(R.id.model_status_bottom_bar);
        mRotationLayout = (LinearLayout) mRootView.findViewById(R.id.model_button_rotate_bar_linearlayout);
        mScaleLayout = (LinearLayout) mRootView.findViewById(R.id.model_button_scale_bar_linearlayout);

        mTextWatcherX = new ScaleChangeListener(0);
        mTextWatcherY = new ScaleChangeListener(1);
        mTextWatcherZ = new ScaleChangeListener(2);

        mScaleEditX = (EditText) mScaleLayout.findViewById(R.id.scale_bar_x_edittext);
        mScaleEditY = (EditText) mScaleLayout.findViewById(R.id.scale_bar_y_edittext);
        mScaleEditZ = (EditText) mScaleLayout.findViewById(R.id.scale_bar_z_edittext);
        mUniformScale = (ImageButton) mScaleLayout.findViewById(R.id.scale_uniform_button);
        mUniformScale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUniformScale.isSelected()) {
                    mUniformScale.setSelected(false);
                } else {
                    mUniformScale.setSelected(true);
                }


            }
        });
        mUniformScale.setSelected(true);

        mScaleEditX.addTextChangedListener(mTextWatcherX);
        mScaleEditY.addTextChangedListener(mTextWatcherY);
        mScaleEditZ.addTextChangedListener(mTextWatcherZ);

        mStatusBottomBar.setVisibility(View.VISIBLE);
        mBottomBar = (FrameLayout) mRootView.findViewById(R.id.bottom_bar);
        mBottomBar.setVisibility(View.INVISIBLE);
        mCurrentAxis = -1;

    }

    /**
     * Change the current rotation axis and update the text accordingly
     * <p/>
     * Alberto
     */
    public static void changeCurrentAxis(int currentAxis) {

        mCurrentAxis = currentAxis;

        float currentAngle = 12;

        switch (mCurrentAxis) {

            case 0:
                mRotationSlider.setBackgroundColor(Color.GREEN);
                break;

            case 1:
                mRotationSlider.setBackgroundColor(Color.RED);
                break;
            case 2:
                mRotationSlider.setBackgroundColor(Color.BLUE);
                break;
            default:
                mRotationSlider.setBackgroundColor(Color.TRANSPARENT);
                break;

        }

        mSurface.setRendererAxis(mCurrentAxis);

        mRotationSlider.setValue((int) currentAngle);

    }


    /**
     * *************************************************************************
     */

    public static void initSeekBar(int max) {
        mSeekBar.setMax(max);
        mSeekBar.setProgress(max);
    }

    public static void configureProgressState(int v) {
        if (v == View.GONE) mSurface.requestRender();
        else if (v == View.VISIBLE) mProgress.bringToFront();

        mProgress.setVisibility(v);
    }


    /**
     * ********************** OPTIONS MENU *******************************
     */
    //Create option menu and inflate viewer menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.print_panel_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId()) {

            case R.id.viewer_open:
                openFile(mStlPath);
//                FileBrowser.openFileBrowser(getActivity(), FileBrowser.VIEWER, getString(R.string.choose_file), ".stl", ".gcode");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * ********************** FILE MANAGEMENT *******************************
     */


    /**
     * Restore the original view and discard the modifications by clearing the data list
     */
    public void optionRestoreView() {


        if (mDataList.size() > 0) {
            String pathStl = mDataList.get(0).getPathFile();
            mDataList.clear();

            openFile(pathStl);
        }


    }

    /**
     * Clean the print panel and delete all references
     */
    public static void optionClean() {

        //Delete slicing reference
        //DatabaseController.handlePreference("Slicing", "Last", null, false);

        mDataList.clear();
        mFile = null;


    }

//    /**
//     * Open a dialog if it's a GCODE to warn the user about unsaved data loss
//     *
//     * @param filePath
//     */
//    public static void openFileDialog(final String filePath) {
//
//
//        if (!StlFile.checkFileSize(new File(filePath), mContext)) {
//            new MaterialDialog.Builder(mContext)
//                    .title(R.string.warning)
//                    .content(R.string.viewer_file_size)
//                    .negativeText(R.string.cancel)
//                    .negativeColorRes(R.color.body_text_2)
//                    .positiveText(R.string.ok)
//                    .positiveColorRes(R.color.theme_accent_1)
//                    .callback(new MaterialDialog.ButtonCallback() {
//                        @Override
//                        public void onPositive(MaterialDialog dialog) {
//                            openFile(filePath);
//
//                        }
//                    })
//                    .build()
//                    .show();
//
//        } else {
//            openFile(filePath);
//        }
//
//
//    }


    //Select the last object added
    public static void doPress() {

        mSurface.doPress(mDataList.size() - 1);

    }


    public static void openFile(String filePath) {
        DataStorage data = null;
        //Open the file
        data = new DataStorage();

        mVisibilityModeButton.setVisibility(View.VISIBLE);
        mFile = new File(filePath);
        StlFile.openStlFile(mContext, mFile, data, DONT_SNAPSHOT);
        mCurrentViewMode = NORMAL;

        mDataList.add(data);


    }

    private void changeStlViews(int state) {

        //Handle the special mode: LAYER

        //Handle TRANSPARENT, NORMAL and OVERHANG modes

        if (mFile != null) {
            if (!mFile.getPath().endsWith(".stl") && !mFile.getPath().endsWith(".STL")) {


                if (openStlFile()) {

                    mCurrentViewMode = state;


                }
                ;
            } else {

                mSurface.configViewMode(state);
                mCurrentViewMode = state;
            }


        } else {
            Toast.makeText(getActivity(), R.string.viewer_toast_not_available_2, Toast.LENGTH_SHORT).show();
        }

    }

    private boolean openStlFile() {

        //Name didn't work with new gcode creation so new stuff!
        //String name = mFile.getName().substring(0, mFile.getName().lastIndexOf('.'));

        String pathStl;


        //Here's the new stuff!
        pathStl = //LibraryController.getParentFolder().getAbsolutePath() + "/Files/" + name + "/_stl/";
                mFile.getParentFile().getParent() + "/_stl/";
        File f = new File(pathStl);

        //Only when it's a project
        if (f.isDirectory() && f.list().length > 0) {
            openFile(pathStl + f.list()[0]);

            return true;

        } else {
            Toast.makeText(getActivity(), R.string.devices_toast_no_stl, Toast.LENGTH_SHORT).show();

            return false;
        }


    }

    public static void draw() {
        //Once the file has been opened, we need to refresh the data list. If we are opening a .gcode file, we need to ic_action_delete the previous files (.stl and .gcode)
        //If we are opening a .stl file, we need to ic_action_delete the previous file only if it was a .gcode file.
        //We have to do this here because user can cancel the opening of the file and the Print Panel would appear empty if we clear the data list.

        String filePath = "";
        if (mFile != null) filePath = mFile.getAbsolutePath();

        Geometry.relocateIfOverlaps(mDataList);
        mSeekBar.setVisibility(View.INVISIBLE);


        //Add the view
        mLayout.removeAllViews();
        mLayout.addView(mSurface, 0);
        mLayout.addView(mSeekBar, 1);
        mLayout.addView(mSizeText, 2);

//      mLayout.addView(mUndoButtonBar, 3);
//      mLayout.addView(mEditionLayout, 2);
    }

    /**
     * ********************** SURFACE CONTROL *******************************
     */
    //This method will set the visibility of the surfaceview so it doesn't overlap
    //with the video grid view
    public void setSurfaceVisibility(int i) {

        if (mSurface != null) {
            switch (i) {
                case 0:
                    mSurface.setVisibility(View.GONE);
                    break;
                case 1:
                    mSurface.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private static PopupWindow mActionModePopupWindow;
    private static PopupWindow mCurrentActionPopupWindow;

    /**
     * ********************** ACTION MODE *******************************
     */

    /**
     * Show a pop up window with the available actions of the item
     */
    public static void showActionModePopUpWindow() {

        hideCurrentActionPopUpWindow();

        mSizeText.setVisibility(View.VISIBLE);

        if (mActionModePopupWindow == null) {

            //Get the content view of the pop up window
            final LinearLayout popupLayout = (LinearLayout) ((Activity) mContext).getLayoutInflater()
                    .inflate(R.layout.item_edit_popup_menu, null);
            popupLayout.measure(0, 0);

            //Set the behavior of the action buttons
            int imageButtonHeight = 0;
            for (int i = 0; i < popupLayout.getChildCount(); i++) {
                View v = popupLayout.getChildAt(i);
                if (v instanceof ImageButton) {
                    ImageButton ib = (ImageButton) v;
                    imageButtonHeight = ib.getMeasuredHeight();
                    ib.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onActionItemSelected((ImageButton) view);
                        }
                    });
                }
            }

            //Show the pop up window in the correct position
            int[] viewerContainerCoordinates = new int[2];
            mLayout.getLocationOnScreen(viewerContainerCoordinates);
            int popupLayoutPadding = (int) mContext.getResources().getDimensionPixelSize(R.dimen.content_padding_normal);
            int popupLayoutWidth = popupLayout.getMeasuredWidth();
            int popupLayoutHeight = popupLayout.getMeasuredHeight();
            final int popupLayoutX = viewerContainerCoordinates[0] + mLayout.getWidth() - popupLayoutWidth;
            final int popupLayoutY = viewerContainerCoordinates[1] + imageButtonHeight + popupLayoutPadding;

            mActionModePopupWindow = (new CustomPopupWindow(popupLayout, popupLayoutWidth,
                    popupLayoutHeight, R.style.SlideRightAnimation).getPopupWindow());

            mActionModePopupWindow.showAtLocation(mSurface, Gravity.NO_GRAVITY,
                    popupLayoutX, popupLayoutY);

        }
    }

    /**
     * Hide the action mode pop up window
     */
    public static void hideActionModePopUpWindow() {
        if (mActionModePopupWindow != null) {
            mActionModePopupWindow.dismiss();
            mSurface.exitEditionMode();
            mRotationLayout.setVisibility(View.GONE);
            mScaleLayout.setVisibility(View.GONE);
            mStatusBottomBar.setVisibility(View.VISIBLE);
            mBottomBar.setVisibility(View.INVISIBLE);
            mActionModePopupWindow = null;
            mSurface.setRendererAxis(-1);
        }

        //Hide size text
        if (mSizeText != null)
            if (mSizeText.getVisibility() == View.VISIBLE) mSizeText.setVisibility(View.INVISIBLE);

        //hideCurrentActionPopUpWindow();
    }

    /**
     * Hide the current action pop up window if it is showing
     */
    public static void hideCurrentActionPopUpWindow() {
        if (mCurrentActionPopupWindow != null) {
            mCurrentActionPopupWindow.dismiss();
            mCurrentActionPopupWindow = null;
        }
        hideSoftKeyboard();
    }

    public static void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {

        }

    }

    /**
     * Perform the required action depending on the pressed button
     *
     * @param item Action button that has been pressed
     */
    public static void onActionItemSelected(final ImageButton item) {

        mStatusBottomBar.setVisibility(View.VISIBLE);
        mSurface.setRendererAxis(-1);
        mRotationLayout.setVisibility(View.GONE);
        mScaleLayout.setVisibility(View.GONE);
        mBottomBar.setVisibility(View.INVISIBLE);
        mSizeText.setVisibility(View.VISIBLE);

        selectActionButton(item.getId());

        switch (item.getId()) {
            case R.id.move_item_button:
                hideCurrentActionPopUpWindow();
                mSurface.setEditionMode(ViewerSurfaceView.MOVE_EDITION_MODE);
                break;
            case R.id.rotate_item_button:

                if (mCurrentActionPopupWindow == null) {
                    final String[] actionButtonsValues = mContext.getResources().getStringArray(R.array.rotate_model_values);
                    final TypedArray actionButtonsIcons = mContext.getResources().obtainTypedArray(R.array.rotate_model_icons);
                    showHorizontalMenuPopUpWindow(item, actionButtonsValues, actionButtonsIcons,
                            null, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    changeCurrentAxis(Integer.parseInt(actionButtonsValues[position]));
                                    mBottomBar.setVisibility(View.VISIBLE);
                                    mRotationLayout.setVisibility(View.VISIBLE);
                                    mSurface.setEditionMode(ViewerSurfaceView.ROTATION_EDITION_MODE);
                                    hideCurrentActionPopUpWindow();
                                    item.setImageResource(actionButtonsIcons.getResourceId(position, -1));
                                    mActionImage.setImageDrawable(mContext.getResources().getDrawable(actionButtonsIcons.getResourceId(position, -1)));
                                }
                            });
                } else {
                    hideCurrentActionPopUpWindow();
                }
                break;
        }

    }


    /**
     * Set the state of the selected action button
     *
     * @param selectedId Id of the action button that has been pressed
     */
    public static void selectActionButton(int selectedId) {

        if (mActionModePopupWindow != null) {
            //Get the content view of the pop up window
            final LinearLayout popupLayout = (LinearLayout) mActionModePopupWindow.getContentView();

            //Set the behavior of the action buttons
            for (int i = 0; i < popupLayout.getChildCount(); i++) {
                View v = popupLayout.getChildAt(i);
                if (v instanceof ImageButton) {
                    ImageButton ib = (ImageButton) v;
                    if (ib.getId() == selectedId)
                        ib.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.oval_background_green));
                    else
                        ib.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.action_button_selector_dark));
                }
            }
        }
    }

    /**
     * Show a pop up window with the visibility options: Normal, overhang, transparent and layers.
     */
    public void showVisibilityPopUpMenu() {

        //Hide action mode pop up window to show the new menu
        hideActionModePopUpWindow();


        //Show a menu with the visibility options
        if (mCurrentActionPopupWindow == null) {
            final String[] actionButtonsValues = mContext.getResources().getStringArray(R.array.models_visibility_values);
            final TypedArray actionButtonsIcons = mContext.getResources().obtainTypedArray(R.array.models_visibility_icons);
            showHorizontalMenuPopUpWindow(mVisibilityModeButton, actionButtonsValues, actionButtonsIcons,
                    Integer.toString(mCurrentViewMode), new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            //Change the view mode of the model
                            changeStlViews(Integer.parseInt(actionButtonsValues[position]));
                            hideCurrentActionPopUpWindow();
                        }
                    });
        } else {
            hideCurrentActionPopUpWindow();
        }

    }

    /**
     * Show a pop up window with a horizontal list view as a content view
     */
    public static void showHorizontalMenuPopUpWindow(View currentView, String[] actionButtonsValues,
                                                     TypedArray actionButtonsIcons,
                                                     String selectedOption,
                                                     AdapterView.OnItemClickListener onItemClickListener) {

        HorizontalListView landscapeList = new HorizontalListView(mContext, null);
        ListIconPopupWindowAdapter listAdapter = new ListIconPopupWindowAdapter(mContext, actionButtonsValues, actionButtonsIcons, selectedOption);
        landscapeList.setOnItemClickListener(onItemClickListener);
        landscapeList.setAdapter(listAdapter);

        landscapeList.measure(0, 0);

        int popupLayoutHeight = 0;
        int popupLayoutWidth = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View mView = listAdapter.getView(i, null, landscapeList);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            popupLayoutHeight = mView.getMeasuredHeight();
            popupLayoutWidth += mView.getMeasuredWidth();
        }

        //Show the pop up window in the correct position
        int[] actionButtonCoordinates = new int[2];
        currentView.getLocationOnScreen(actionButtonCoordinates);
        int popupLayoutPadding = (int) mContext.getResources().getDimensionPixelSize(R.dimen.content_padding_normal);
        final int popupLayoutX = actionButtonCoordinates[0] - popupLayoutWidth - popupLayoutPadding / 2;
        final int popupLayoutY = actionButtonCoordinates[1];

        mCurrentActionPopupWindow = (new CustomPopupWindow(landscapeList, popupLayoutWidth,
                popupLayoutHeight + popupLayoutPadding, R.style.SlideRightAnimation).getPopupWindow());

        mCurrentActionPopupWindow.showAtLocation(mSurface, Gravity.NO_GRAVITY, popupLayoutX, popupLayoutY);
    }

    /**
     * ********************** MULTIPLY ELEMENTS *******************************
     */

    public static void showMultiplyDialog() {
        View multiplyModelDialog = LayoutInflater.from(mContext).inflate(R.layout.dialog_multiply_model, null);
        final NumberPicker numPicker = (NumberPicker) multiplyModelDialog.findViewById(R.id.number_copies_numberpicker);
        numPicker.setMaxValue(10);
        numPicker.setMinValue(0);

        final int count = numPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numPicker)).setColor(mContext.getResources().getColor(R.color.theme_primary_dark));
                    ((EditText) child).setTextColor(mContext.getResources().getColor(R.color.theme_primary_dark));

                    Field[] pickerFields = NumberPicker.class.getDeclaredFields();
                    for (Field pf : pickerFields) {
                        if (pf.getName().equals("mSelectionDivider")) {
                            pf.setAccessible(true);
                            try {
                                pf.set(numPicker, mContext.getResources().getDrawable(R.drawable.separation_line_horizontal));
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }

                    numPicker.invalidate();
                } catch (NoSuchFieldException e) {
                    Log.w("setNumberPickerTextColor", e.toString());
                } catch (IllegalAccessException e) {
                    Log.w("setNumberPickerTextColor", e.toString());
                } catch (IllegalArgumentException e) {
                    Log.w("setNumberPickerTextColor", e.toString());
                }
            }
        }

        //Remove soft-input from number picker
        numPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//        final MaterialDialog.Builder createFolderDialog = new MaterialDialog.Builder(mContext);
//        createFolderDialog.title(R.string.viewer_menu_multiply_title)
//                .customView(multiplyModelDialog, true)
//                .positiveColorRes(R.color.theme_accent_1)
//                .positiveText(R.string.dialog_continue)
//                .negativeColorRes(R.color.body_text_2)
//                .negativeText(R.string.cancel)
//                .autoDismiss(true)
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onPositive(MaterialDialog dialog) {
//                        drawCopies(numPicker.getValue());
//                    }
//                })
//                .show();

    }

    private static void drawCopies(int numCopies) {
        int model = mSurface.getObjectPresed();
        int num = 0;

        while (num < numCopies) {
            final DataStorage newData = new DataStorage();
            newData.copyData(mDataList.get(model));
            mDataList.add(newData);

            /**
             * Check if the piece is out of the plate and stop multiplying
             */
            if (!Geometry.relocateIfOverlaps(mDataList)) {

                Toast.makeText(mContext, R.string.viewer_multiply_error, Toast.LENGTH_LONG).show();
                mDataList.remove(newData);
                break;

            }

            num++;
        }

        draw();
    }

/**
 * **************************** PROGRESS BAR FOR SLICING ******************************************
 */

    /**
     * Static method to show the progress bar by sending an integer when receiving data from the socket
     *
     * @param i either -1 to hide the progress bar, 0 to show an indefinite bar, or a normal integer
     */
    public static void showProgressBar(int status, int i) {


        if (mRootView != null) {


            ProgressBar pb = (ProgressBar) mRootView.findViewById(R.id.progress_slice);
            TextView tv = (TextView) mRootView.findViewById(R.id.viewer_text_progress_slice);
            TextView tve = (TextView) mRootView.findViewById(R.id.viewer_text_estimated_time);
            TextView tve_title = (TextView) mRootView.findViewById(R.id.viewer_estimated_time_textview);


            pb.setVisibility(View.INVISIBLE);
            tve_title.setVisibility(View.INVISIBLE);
            tv.setText(null);
            tve.setText(null);
            mRootView.invalidate();


        }


    }

    /**
     * Display model width, depth and height when touched
     */
    public static void displayModelSize(int position) {
        try {
            //TODO RANDOM CRASH ArrayIndexOutOfBoundsException
            DataStorage data = mDataList.get(position);

            //Set point instead of comma
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator(',');

            //Define new decimal format to display only 2 decimals
            DecimalFormat df = new DecimalFormat("##.##", otherSymbols);

            String width = df.format((data.getMaxX() - data.getMinX()));
            String depth = df.format((data.getMaxY() - data.getMinY()));
            String height = df.format((data.getMaxZ() - data.getMinZ()));

            //Display size of the model
            //mSizeText.setText("W = " + width + " mm / D = " + depth + " mm / H = " + height + " mm");
            //mSizeText.setText(String.format(mContext.getResources().getString(R.string.viewer_axis_info), Double.parseDouble(width), Double.parseDouble(depth), Double.parseDouble(height)));

            Log.i("Scale", "Vamos a petar " + width);
            ((TextView) mSizeText.findViewById(R.id.print_panel_x_size)).setText(width);
            ((TextView) mSizeText.findViewById(R.id.print_panel_y_size)).setText(depth);
            ((TextView) mSizeText.findViewById(R.id.print_panel_z_size)).setText(height);

            if (mScaleLayout.getVisibility() == View.VISIBLE) {

                mScaleEditX.removeTextChangedListener(mTextWatcherX);
                mScaleEditY.removeTextChangedListener(mTextWatcherY);
                mScaleEditZ.removeTextChangedListener(mTextWatcherZ);

                mScaleEditX.setText(width);
                mScaleEditX.setSelection(mScaleEditX.getText().length());
                mScaleEditY.setText(depth);
                mScaleEditY.setSelection(mScaleEditY.getText().length());
                mScaleEditZ.setText(height);
                mScaleEditZ.setSelection(mScaleEditZ.getText().length());

                mScaleEditX.addTextChangedListener(mTextWatcherX);
                mScaleEditY.addTextChangedListener(mTextWatcherY);
                mScaleEditZ.addTextChangedListener(mTextWatcherZ);
            }

        } catch (ArrayIndexOutOfBoundsException e) {

            e.printStackTrace();
        }


    }

    //Refresh printers when the fragmetn is shown
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    static Runnable mSliceRunnable = new Runnable() {

        @Override
        public void run() {

            final List<DataStorage> newList = new ArrayList<DataStorage>(mDataList);

            //Code to update the UI─aÇ >
            //Check if the file is not yet loaded
            for (int i = 0; i < newList.size(); i++) {

                if (newList.get(i).getVertexArray() == null) {
                    return;
                }

            }

        }
    };


    /**
     * *********************************  SIDE PANEL *******************************************************
     */

    public static File getFile() {
        return mFile;
    }

    public static int[] getCurrentPlate() {
        return mCurrentPlate;
    }

    public static int getCurrentType() {
        return mCurrentType;
    }

    public static void displayErrorInAxis(int axis) {

        if (mScaleLayout.getVisibility() == View.VISIBLE) {
            switch (axis) {

                case 0:
                    mScaleEditX.setError(mContext.getResources().getString(R.string.viewer_error_bigger_plate, mCurrentPlate[0] * 2));
                    break;

                case 1:
                    mScaleEditY.setError(mContext.getResources().getString(R.string.viewer_error_bigger_plate, mCurrentPlate[1] * 2));
                    break;

            }
        }


    }

    private class ScaleChangeListener implements TextWatcher {

        int mAxis;

        private ScaleChangeListener(int axis) {

            mAxis = axis;

        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            mScaleEditX.setError(null);
            mScaleEditY.setError(null);
            mScaleEditZ.setError(null);

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            boolean valid = true;


            //Check decimals
            if (editable.toString().endsWith(".")) {
                valid = false;

            }


            if (valid)
                try {
                    switch (mAxis) {

                        case 0:
                            mSurface.doScale(Float.parseFloat(editable.toString()), 0, 0, mUniformScale.isSelected());
                            break;

                        case 1:
                            mSurface.doScale(0, Float.parseFloat(editable.toString()), 0, mUniformScale.isSelected());
                            break;

                        case 2:
                            mSurface.doScale(0, 0, Float.parseFloat(editable.toString()), mUniformScale.isSelected());
                            break;

                    }
                } catch (NumberFormatException e) {

                    e.printStackTrace();

                }


        }
    }

    //region API-related Methods
    private void STlAlgorithm()
    {
        if(filename == null)
        {
            Log.i(TAG, "Filename is null -> calling getSTLFilename()");
            // Get the STL
            this.getStlFilename();
        }
        else
        {
            openMyStl(filename);
            Log.i(TAG, "Filename is NOT null -> calling openMyFile()");
        }
    }

    private void getStlFilename()
    {
        // Get the token
        String token = ((MyApplication)this.getActivity().getApplication()).getToken();

        // Get the part ID
        String partID = ((MyApplication)this.getActivity().getApplication()).getQrcode();

        // Create an endpoint
        endpointDB endpoint = APIClient.getClient().create(endpointDB.class);

        // Create a call
        Call<List<PartFile>> call = endpoint.getPartFiles(partID, token);

        // Enqueue the call
        call.enqueue(new Callback<List<PartFile>>()
        {
            @Override
            public void onResponse(Call<List<PartFile>> call, Response<List<PartFile>> response)
            {
                android.util.Log.i(TAG, "statuscode =" + response.code());
                if(response.body() == null)
                {
                    android.util.Log.i(TAG, "Body is null =");
                }
                else
                {

                    Log.i(TAG, "Body not null");

                    // Get the body
                    List<PartFile> data = response.body();

                    // Store the filename globally
                    filename = data.get(0).getStlFileName();

                    // Show the STL File
                    showStlFile(filename);

                    Log.i(TAG, "Number of Test = " + data.size() );
                    Log.i(TAG, "STL File Name = " + filename);

                }
            }

            @Override
            public void onFailure(Call<List<PartFile>> call, Throwable t)
            {
                Log.i(TAG, "Failure @ = "+ t.getMessage());
            }
        });
    }

    private void showStlFile(String filename)
    {
        if(FileHelper.doesFileExists(this.getContext(), filename, enums.FileDownloadType.STLFile)) // The file exists
        {
           // Open the file directly since it already exists
           this.openMyStl(filename);
        }
        else
        {
            // Download the STL file from S3
            this.downloadSTLFileFromS3(filename);

        }
    }

    private void openMyStl(String filename)
    {
        // Generate the path
        String path = Environment.getExternalStorageDirectory()+getString(R.string.pathForSTLFile)+filename;

        // Open the file
        openFile(path);
    }

    private void downloadSTLFileFromS3(String filename)
    {
        S3Download<ViewerMainFragment> s3Downloader = new S3Download<>(this.getActivity(),this);
        s3Downloader.downloadFile(filename,enums.FileDownloadType.STLFile);
    }

    //endregion

    //region Complying with s3downloadInterface Method

    @Override
    public void downloadComplete()
    {
        Log.i(TAG, "S3: downloadComplete()");
        if(filename!=null)
        {
            this.openMyStl(this.filename);
        }

    }

    @Override
    public void downloadFailure(int message)
    {
        Log.i(TAG, "S3: downloadFailure(): "+ message);
    }

    @Override
    public void progressReport(int percentage)
    {
        Log.i(TAG, "S3: progressReport(): "+ percentage);
    }

    //endregion

}
