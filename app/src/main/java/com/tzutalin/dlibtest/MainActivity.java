/*
*  Copyright (C) 2015-present TzuTaLin
*/

package com.tzutalin.dlibtest;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.dexafree.materialList.view.MaterialListView;
import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceVerify;
import com.tzutalin.dlibtest.db.ImageDatabaseHelper;
import com.tzutalin.dlibtest.mat.DivideGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;

    private static final String TAG = "dlib";

    // Storage Permissions
    private static String[] PERMISSIONS_REQ = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    protected ArrayList<String> imagePath;
    // UI
    @ViewById(R.id.material_listview)
    protected MaterialListView mListView;
    @ViewById(R.id.fab)
    protected FloatingActionButton mFabActionBt;
    @ViewById(R.id.fab_cam)
    protected FloatingActionButton mFabCamActionBt;
    @ViewById(R.id.toolbar)
    protected Toolbar mToolbar;

    protected FloatingActionButton mFabCamActionDispaly;

    FaceVerify mFaceVer;
    private DivideGroup resGroups;

    public static final String AUTHORITY = "imageprovider";
    private static final String CONTENT_AUTHORITY_SLASH = "content://" + AUTHORITY + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = (MaterialListView) findViewById(R.id.material_listview);
        setSupportActionBar(mToolbar);
        // Just use hugo to print log
        isExternalStorageWritable();
        isExternalStorageReadable();

        // For API 23+ you need to request the read/write permissions even if they are already in your manifest.
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentapiVersion >= Build.VERSION_CODES.M) {
            verifyPermissions(this);
        }
        //test.
        if (getAllImage()) {
            runVerifyAsync(imagePath);
        }
    }

    @AfterViews
    protected void setupUI() {
        mToolbar.setTitle(getString(R.string.app_name));
        Toast.makeText(MainActivity.this, getString(R.string.description_info), Toast.LENGTH_LONG).show();
    }

    @Click({R.id.fab})
    protected void launchGallery() {
        Toast.makeText(MainActivity.this, "Pick one image", Toast.LENGTH_SHORT).show();
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Click({R.id.fab_cam})
    protected void launchCameraPreview() {
        startActivity(new Intent(this, CameraActivity.class));
    }

    /**
     * Checks if the app has permission to write to device storage or open camera
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    @DebugLog
    private static boolean verifyPermissions(Activity activity) {
        // Check if we have write permission
        int write_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_persmission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (write_permission != PackageManager.PERMISSION_GRANTED ||
                read_persmission != PackageManager.PERMISSION_GRANTED ||
                camera_permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_REQ,
                    REQUEST_CODE_PERMISSION
            );
            return false;
        } else {
            return true;
        }
    }

    /* Checks if external storage is available for read and write */
    @DebugLog
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    @DebugLog
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    // get path of all pictures
    public boolean getAllImage() {
        ArrayList<String> imagePath = new ArrayList<>();

        Timber.tag(TAG).d("getAllImage in");
        try {
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, null);
            while (cursor.moveToNext()) {
                byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                imagePath.add(new String(data, 0, data.length - 1));
            }
            this.imagePath = imagePath;
        } catch (Exception e) {
            Timber.tag(TAG).d(e.toString());
            return false;
        }
        return true;
    }

    //for testing. get path of pictures from txt file
    public boolean getAllImage(String path){
        ArrayList<String> tempList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();

            while (line != null)
            {
                tempList.add(line);
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        this.imagePath = tempList;
        return true;
    }

    // ==========================================================
    // Tasks inner class
    // ==========================================================
    private ProgressDialog mDialog;

    // print info of groups
    public void InfoLog(Set<Integer> set) {
        Iterator<Integer> iter = set.iterator();
        while (iter.hasNext()) {
            Integer temp = iter.next();
            Timber.tag(TAG).d(imagePath.get(temp) + ", ");
        }
    }

    @Background
    @NonNull
    // test demo. calculate error ratio of predict
    protected void testDemo(ArrayList<float[]> featureMat) {

        DivideGroup mc = new DivideGroup();
        int size = featureMat.size();

        float threshold = (float) 0.45;
        if (threshold < 0.6) {
            int postive_error_item = 0;
            int negative_erroe_item = 0;
            Timber.tag(TAG).d("threshold: " + threshold + " and ratio: ");
            for (int i = 0; i < size; ++i) {
                float[] alpha = featureMat.get(i);
                String alphaName = imagePath.get(i).split("_")[0];
                for (int j = i + 1; j < size; ++j) {
                    float[] beta = featureMat.get(j);
                    String betaName = imagePath.get(j).split("_")[0];
                    float res = mc.calDistance(alpha, beta);

                    if (res > threshold && alphaName.equals(betaName)) {
//                        Timber.tag(TAG).d(alphaName + " " + betaName);
                        postive_error_item++;
                    }
                    if (res < threshold && !alphaName.equals(betaName)) {
//                        Timber.tag(TAG).d(alphaName + " " + betaName);
                        negative_erroe_item++;
                    }
                }
            }
            float p_error = (float) postive_error_item / (float) (size * (size - 1) / 2) * 100;
            float n_error = (float) negative_erroe_item / (float) (size * (size - 1) / 2) * 100;
            Timber.tag(TAG).d("ratio of error: " + p_error);
            Timber.tag(TAG).d("ratio of error: " + n_error);
            Timber.tag(TAG).d("ratio of error: " + (p_error + n_error));
//            threshold += 0.02;
        }
    }



    // cal feature vector of image
    protected ArrayList<float[]> calFeatureVec(ArrayList<String> imagePath)
    {
        // load verify model
        if (mFaceVer == null)
        {
            mFaceVer = new FaceVerify(Constants.getFaceShapeModelPath(), Constants.getRecognitionModelPath());
        }
        ArrayList<float[]> featureMat = new ArrayList<>();

        if (!imagePath.isEmpty())
        {
            float[] tempVec = new float[]{};
            for (int i = 0; i < imagePath.size(); ++i)
            {
                tempVec = mFaceVer.verify(imagePath.get(i))[0];
                if(tempVec == null){
                    continue;
                }
                featureMat.add(tempVec);
            }
        }

        return featureMat;
    }

    protected ArrayList<float[]> loadDatabase()
    {
        ArrayList<float[]> featureMat = new ArrayList<>();
        ArrayList<String> imageName = new ArrayList<>();
        try {
            Cursor cursor = getContentResolver().query(ImageDatabaseHelper.IMAGE_TABLE_URI, null, null, null, null);
            if (cursor.getCount() == imagePath.size()) {
                Timber.tag(TAG).d("get the image vec from db in");
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(1);
                    Timber.tag(TAG).d("id: " + id);
                    String tempPath = cursor.getString(1);
                    imageName.add(tempPath);
                    Timber.tag(TAG).d("path: " + tempPath);
                    float[] imageVec = String2FloatArray(cursor.getString(2));
                    featureMat.add(imageVec);
                }
            }
        } catch (Exception e) {
            Timber.tag(TAG).d(e.toString());
        }
        return featureMat;
    }

    private boolean updateDatabase(ArrayList<String> imagePath, ArrayList<float[]> featureMat)
    {
        if (imagePath.size() != featureMat.size())
        {
            return false;
        }
        for (int i = 0; i < imagePath.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(ImageDatabaseHelper.IMAGE_ID, i);
            values.put(ImageDatabaseHelper.IMAGE_PATH, imagePath.get(i));
            Timber.tag(TAG).d("insert image vec tempVec = " + imagePath.get(i).toString());
            values.put(ImageDatabaseHelper.IMAGE_VEC, floatArray2String(featureMat.get(i)));
            Uri uri = getContentResolver().insert(ImageDatabaseHelper.IMAGE_TABLE_URI, values);
            if (uri == null) {
                Timber.tag(TAG).d("insert image vec failure");
                return false;
            }
        }
        return true;
    }

    protected boolean checkForUpdate()
    {
        ArrayList<String> dbImagePath = new ArrayList<>();
        try {
            Cursor cursor = getContentResolver().query(ImageDatabaseHelper.IMAGE_TABLE_URI, null, null, null, null);
            if (cursor.getCount() == imagePath.size()) {
                Timber.tag(TAG).d("get the image vec from db in");
                ArrayList<float[]> featuresMatInDb = new ArrayList<>();
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(1);
                    Timber.tag(TAG).d("id: " + id);
                    String tempPath = cursor.getString(1);
                    dbImagePath.add(tempPath);
                    Timber.tag(TAG).d("path: " + tempPath);
                }
            }
        } catch (Exception e) {
            Timber.tag(TAG).d(e.toString());
        }
        // compare db and current image path
        if (dbImagePath.equals(this.imagePath))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Background
    @NonNull
    //divide all pictures to different groups
    protected void runVerifyAsync(@NonNull ArrayList<String> imagePath) {
        Timber.tag(TAG).d("runVerifyAsync in");

        final String mDetectPath = Constants.getFaceShapeModelPath();
        final String mVerifyPath = Constants.getRecognitionModelPath();

        //
        if (!new File(mDetectPath).exists()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Copy landmark model to " + mDetectPath, Toast.LENGTH_SHORT).show();
                }
            });
            FileUtils.copyFileFromRawToOthers(getApplicationContext(), R.raw.shape_predictor_68_face_landmarks, mDetectPath);
        }

        //
        if (!new File(mVerifyPath).exists()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Copy landmark model to " + mVerifyPath, Toast.LENGTH_SHORT).show();
                }
            });
            FileUtils.copyFileFromRawToOthers(getApplicationContext(), R.raw.dlib_face_recognition_resnet_model_v1, mVerifyPath);
        }

        // read image path from database and compare to current image path
        ArrayList<float[]> featureMat = new ArrayList<>();
        if (resGroups == null)
        {
            resGroups = new DivideGroup();
        }
        // check
        if (checkForUpdate())
        {
            featureMat = calFeatureVec(imagePath);
            updateDatabase(imagePath, featureMat);
        }
        // needn't update, load db
        featureMat = loadDatabase();

        // init reaGroup
        resGroups.Put(featureMat);
        while (resGroups.updateDistance()) {
//            Timber.tag(TAG).d("iter");
        }
        Timber.tag(TAG).d("successful");

        int n_size = resGroups.groups.size();
        for (int k = 0; k < n_size; ++k)
        {
            ArrayList<Integer> mID = new ArrayList<>();
            mID = resGroups.groups.get(k).id;
            if (mID.size() < 2)
                continue;
            //
            Collections.sort(mID);
            Timber.tag(TAG).d("group: " + k);
            for (Integer id:mID) {
                String temp = imagePath.get(id);
                Timber.tag(TAG).d(temp);
            }
        }
        // test
        testDemo(featureMat);
    }

    private String floatArray2String(float[] imageVec) {
        if (imageVec.length < 1) {
            return null;
        }
        String imageVecText = "";

        for (int i = 0; i < imageVec.length; i++) {
            imageVecText += Float.toString(imageVec[i]);
            imageVecText += ",";
        }
        Timber.tag(TAG).d("imageVecText: " + imageVecText);
        return imageVecText;
    }

    private float[] String2FloatArray(String Text) {
        String[] signs = Text.split(",");
        int size = signs.length;
        float[] vec = new float[size];
        for (int i = 0; i < size; ++i) {
            float temp = Float.parseFloat(signs[i]);
            vec[i] = temp;
        }
        return vec;
    }
}