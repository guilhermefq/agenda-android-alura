package br.com.softgran.agenda;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by guilhermefq on 07/12/17.
 */

public class Localizador implements GoogleApiClient.ConnectionCallbacks, LocationListener {
    private final GoogleApiClient client;
    //private final MapaFragment mapaFragment;
    private final GoogleMap googleMap;
    private final Context context;
    public LatLng coordenada;

    public LatLng getCoordenada() {return coordenada;}

    public void setCoordenada(LatLng coordenada) {this.coordenada = coordenada;}


    public Localizador(Context context, GoogleMap googleMap) {
        this.context = context;
        client = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();

        client.connect();

        //this.mapaFragment = mapaFragment;
        this.googleMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = new LocationRequest();
        request.setSmallestDisplacement(50); //Distancia minima a ser percorrida para que seja atualizado
        request.setInterval(1000); //Intervalo mínimo entre as atualizações
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if ((checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && (checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 432);

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        coordenada = new LatLng(location.getLatitude(), location.getLongitude());

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(coordenada, 17);
        googleMap.moveCamera(update);

        Toast.makeText(context,"Coordenadas: " + String.valueOf(coordenada),Toast.LENGTH_SHORT).show();

        //mapaFragment.centralizaEm(coordenada);
    }


}
