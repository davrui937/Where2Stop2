package com.david.where2stop.providers;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProvider {

    private DatabaseReference mDatabase;
    private GeoFire mGeofire;

    public GeofireProvider () {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("active_clients");
        mGeofire = new GeoFire(mDatabase);
    }

    public void saveLocation(String idClient, LatLng latLng) {
        mGeofire.setLocation(idClient, new GeoLocation(latLng.latitude, latLng.longitude));
    }

    public void removeLocation(String idClient) {
        mGeofire.removeLocation(idClient);
    }

    public GeoQuery getActiveClients(LatLng latLng){
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude), 5);
        geoQuery.removeAllListeners();
        return geoQuery;

    }
}

