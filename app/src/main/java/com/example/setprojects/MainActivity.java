package com.example.setprojects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.setprojects.constants.Constants;
import com.example.setprojects.interfaces.JsonApiHolder;
import com.example.setprojects.model.RestApi;
import com.example.setprojects.model.TestClas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private Unbinder unbinder;
    private String projectId, district, lat, lng, psdp;
    public static int IMAGE = 1001;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    private Uri imageurl;
    private ArrayList<String> a = new ArrayList<>();
    private ArrayList<String> b = new ArrayList<>();

    @BindView(R.id.districtSpinner)
    Spinner districtSpinner;


    @BindView(R.id.psdpSpinner)
    Spinner psdpSpinner;

    @BindView(R.id.destrictText)
    TextView destrictText;

    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;

    @BindView(R.id.image)
    CircleImageView imageView;

    private LocationManager mLocationManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (unbinder == null) {
            unbinder = ButterKnife.bind(this);
        }



        ActivityCompat.requestPermissions(this, permissions, Constants.REQUEST_RECORD_AUDIO_PERMISSION);
//        requestPermission();
//        isStoragePermissionGranted();
        a.add("QUETTA");
        a.add("KECH");
        a.add("DASHT");
        a.add("MASTUNG");
        a.add("BOLAN");
        a.add("PISHIN");
        a.add("CHAMAN");


        b.add("ZZ2008.0015");
        b.add("ZZ2008.0016");
        b.add("ZZ2008.0017");
        b.add("ZZ2008.0018");
        b.add("ZZ2008.0019");
        b.add("ZZ2008.0020");
        b.add("ZZ2008.0021");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, a);
        ArrayAdapter adapterb = new ArrayAdapter(this, android.R.layout.simple_list_item_1, b);
        districtSpinner.setAdapter(adapter);
        psdpSpinner.setAdapter(adapterb);


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.LOCATION_REFRESH_TIME,
                Constants.LOCATION_REFRESH_DISTANCE, mLocationListener);
//        mLocationManager.getLastKnownLocation()



    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            lat = String.valueOf(location.getLatitude());
            lng = String.valueOf(location.getLongitude());
            Log.d(TAG, "onLocationChanged: latitude: " + lat);
            Log.d(TAG, "onLocationChanged: longitude: " + lng);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        switch (requestCode){
            case Constants.REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
//        if (!permissionToRecordAccepted ) finish();

    }

    @OnClick(R.id.image)
    public void getImageFromGallery(){
        functionForBothImageFrontAndBack(IMAGE);
    }

    @OnClick(R.id.buttonSubmit)
    public void sendDataToApi(){
        district = districtSpinner.getSelectedItem().toString();
        psdp = psdpSpinner.getSelectedItem().toString();
        if(imageurl != null && lat != null && lng != null && district != null){
            if(!lat.isEmpty() || !lng.isEmpty()) {
                Log.d(TAG, "onLocationChanged: latitude: " + lat);
                Log.d(TAG, "onLocationChanged: longitude: " + lng);
                showProgressDialogue("Posting data", "Please wait ...");
                sendDataToServer(lat, lng, district, imageurl);
            }else {
                Toast.makeText(this, "enable your location", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Kindly, attach a photo", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendDataToServer(String lat, String lng, String district, Uri imageurl) {
        projectId = UUID.randomUUID().toString();
        JsonApiHolder service = RestApi.getApi();
        File image = new File(getRealPathFromURI(imageurl));
        if(/*frontImage == null || backImage == null ||*/ imageurl == null){
            Toast.makeText(this, "Kindly Attach the image", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody imageRqst = RequestBody.create(MediaType.parse("image/jpg"), image);
        MultipartBody.Part imageMultiPartRqst = MultipartBody.Part.createFormData("image", image.getName(), imageRqst);
        RequestBody projectIdRqst = RequestBody.create(MediaType.parse("text/plain"), projectId);
        RequestBody latRqst = RequestBody.create(MediaType.parse("text/plain"), lat);
        RequestBody lngRqst = RequestBody.create(MediaType.parse("text/plain"), lng);
        RequestBody districtRqst = RequestBody.create(MediaType.parse("text/plain"), district);
        RequestBody psdpRqst = RequestBody.create(MediaType.parse("text/plain"), psdp);


        Call<TestClas> call = service.createProjectData(imageMultiPartRqst, psdpRqst, districtRqst, latRqst, lngRqst, projectIdRqst);
        call.enqueue(new Callback<TestClas>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this, response.body().getSuccess() , Toast.LENGTH_SHORT).show();
                    imageView.setImageDrawable(getDrawable(R.drawable.image));
                    districtSpinner.setSelection(0);
                    psdpSpinner.setSelection(0);
                    dissmissProgressDialogue();
                    onBackPressed();
                }else {
                    Toast.makeText(MainActivity.this, "Response Failed!", Toast.LENGTH_SHORT).show();
                    dissmissProgressDialogue();
                }
            }

            @Override
            public void onFailure(Call<TestClas> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Response Failed!: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
                dissmissProgressDialogue();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isStoragePermissionGranted () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat
                        .requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
    public void functionForBothImageFrontAndBack(int f) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, f);
    }
    @SuppressLint("ResourceType")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
                if(requestCode == IMAGE){
                    imageurl = data.getData();
                    File i = new File(getRealPathFromURI(imageurl));
                    Log.d(TAG, "onActivityResult: " + i);
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(imageurl);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(selectedImage);
//                    scanTextFromUri(imageUriForFront);
                }
            }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
