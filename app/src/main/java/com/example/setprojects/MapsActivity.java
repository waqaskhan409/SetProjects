package com.example.setprojects;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.setprojects.constants.Constants;
import com.example.setprojects.interfaces.JsonApiHolder;
import com.example.setprojects.model.Projects;
import com.example.setprojects.model.RestApi;
import com.example.setprojects.model.TestClas;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private Unbinder unbinder;

    private GoogleMap mMap;


    @BindView(R.id.buttonForm)
    FloatingActionButton buttonForm;

    @BindView(R.id.image)
    ImageView image;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(unbinder == null){
            unbinder = ButterKnife.bind(this);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    @OnClick(R.id.buttonForm)
    public void form(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getData(mMap);
    }

    private void getData(GoogleMap mMap) {
        JsonApiHolder service = RestApi.getApi();
        Call <List<Projects>> call = service.getDepartment();

        call.enqueue(new Callback<List<Projects>>() {
            @Override
            public void onResponse(Call<List<Projects>> call, Response<List<Projects>> response) {
                if(response.isSuccessful()){
                    List<Projects> list = response.body();
                    for (int i = 0; i < list.size(); i++) {
                        LatLng projects = new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLng()));
                        mMap.addMarker(new MarkerOptions().position(projects).title(list.get(i).getProject_id_psdp()));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(projects,14));
                        Picasso.get().load(Constants.IMAGE_FILE +list.get(i).getImage_url()).into(image);

//                       final MarkerOptions markerOptions = new MarkerOptions();
//                        markerOptions.position(projects);
//                        markerOptions.icon(BitmapDescriptorFactory.fromAsset(list.get(i).getImage_url()));
//

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Projects>> call, Throwable t) {

            }
        });

    }
}
