package com.david.where2stop.activities.client;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.david.where2stop.R;
import com.david.where2stop.activities.LoginActivity;
import com.david.where2stop.includes.MyToolbar;
import com.david.where2stop.providers.GeofireProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GeneradorQrActivity extends AppCompatActivity {

    public static EditText mEditTextDatos;
    private Button mButtonGenerarQr;
    private ImageView mImageQr;
    private GeofireProvider mGeofireProvider;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://where2stop-625d0-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generador_qr);

        MyToolbar.show(this, "Generar QR", true);

        myRef.child("client").child(LoginActivity.iduser).child("code").setValue("123");
        mEditTextDatos = findViewById(R.id.textCambios);
        mButtonGenerarQr = findViewById(R.id.btnGenerarQr);
        mImageQr = findViewById(R.id.qrCode);
        mButtonGenerarQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(mEditTextDatos.getText().toString(), BarcodeFormat.QR_CODE, 750,750);
                    mImageQr.setImageBitmap(bitmap);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}