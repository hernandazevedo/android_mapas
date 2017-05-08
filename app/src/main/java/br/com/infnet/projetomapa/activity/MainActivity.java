package br.com.infnet.projetomapa.activity;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements android.location.LocationListener, OnMapReadyCallback {


    public static final int REQUEST_CODE = 1001;
    private GoogleMap googleMap;

    private LocationManager locationManager;

    private TextView tvLocalizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLocalizacao = (TextView) findViewById(R.id.tvLocalizacao);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5000L, 1.0f, (android.location.LocationListener) this);

        }

        supportMapFragment.getMapAsync(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (REQUEST_CODE == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "impossibilidade de usar a funcionalidade", Toast.LENGTH_LONG).show();
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //Mensagem de impossibilidade de usar a funcionalidade

                    Toast.makeText(this, "permissao nao concedida pelo android", Toast.LENGTH_LONG).show();
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 1.0f, (android.location.LocationListener) this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 1.0f, (android.location.LocationListener) this);

                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        googleMap.clear();
        tvLocalizacao.setText("LAT" +location.getLatitude() + " LONG: "+location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(location.getLatitude(),location.getLongitude()));
        LatLng currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
        googleMap.addMarker(markerOptions);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setZoomGesturesEnabled(true);
        this.googleMap.getUiSettings().setZoomGesturesEnabled(true);
        this.googleMap.getUiSettings().setRotateGesturesEnabled(false);
        this.googleMap.getUiSettings().setTiltGesturesEnabled(true);

        LatLng novaIguacu = new LatLng(-22.7598,-43.4516);

        MarkerOptions marker = new MarkerOptions();
        marker.position(novaIguacu).title("Nova iguaçu city");

        googleMap.addMarker(marker);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(novaIguacu,15));

    }
}
