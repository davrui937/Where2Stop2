package com.david.where2stop.activities.client;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.david.where2stop.R;
import com.david.where2stop.activities.LoginActivity;
import com.david.where2stop.includes.MyToolbar;
import com.david.where2stop.providers.GeofireProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Objects;

public class GeneradorQrActivity extends AppCompatActivity {

    public static EditText mEditTextDatos;
    private Button mButtonGenerarQr;
    private ImageView mImageQr;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://where2stop-625d0-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference myRef = database.getReference();
    private String code;
    private HashMap mapaUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generador_qr);

        MyToolbar.show(this, "Generar QR", true);


        mButtonGenerarQr = findViewById(R.id.btnGenerarQr);
        mImageQr = findViewById(R.id.qrCode);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot item_snapshot:snapshot.getChildren()) {

                    mapaUsers = (HashMap) Objects.requireNonNull(item_snapshot.getValue());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("client").child("cliente@gmailcom").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                code = (dataSnapshot.child("code").getValue().toString());
            }
        });

        mButtonGenerarQr.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(code.toString(), BarcodeFormat.QR_CODE, 750,750);
                    mImageQr.setImageBitmap(bitmap);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}