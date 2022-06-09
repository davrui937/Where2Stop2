package com.david.where2stop.activities.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.david.where2stop.R;
import com.david.where2stop.activities.client.GeneradorQrActivity;
import com.david.where2stop.includes.MyToolbar;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.util.Objects;

public class LectorQrActivity extends AppCompatActivity {

    private Button mButtonScan;
    private Button mButtonComprobar;
    private EditText mTextResult;
    private  Double code;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://where2stop-625d0-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qr);

        mButtonScan = findViewById(R.id.btnScan);
        mTextResult = findViewById(R.id.txtResult);
        mButtonComprobar = findViewById(R.id.btnComprobar);

        MyToolbar.show(this,"Scan",true);

        database.getReference().child("client").child("cliente@gmailcom").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                code = Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("lat").getValue()).toString()) + Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("long").getValue()).toString());
            }
        });

        mButtonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntregator  = new IntentIntegrator(LectorQrActivity.this);
                intentIntregator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntregator.setPrompt("Lector - CDP");
                intentIntregator.setCameraId(0);
                intentIntregator.setBeepEnabled(true);
                intentIntregator.setBarcodeImageEnabled(true);
                intentIntregator.initiateScan();

            }
        });

        mButtonComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(code+"XXXXXXXXXXXXXXXXXXXXXXXX");
                System.out.println(mTextResult.toString()+"XXXXXXXXXXXXXXXXXXXXXXXX");

                if(mTextResult.getText().toString().equals(code)){
                    Toast.makeText(LectorQrActivity.this, "Pedido entregado", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(LectorQrActivity.this, "Codigo de pedido erroneo", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
         if(result != null){
             if (result.getContents() == null) {
                 Toast.makeText(this, "Lectora cancelada", Toast.LENGTH_SHORT).show();
             }else{
                 Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                 mTextResult.setText(result.getContents());
             }
         } else{
             super.onActivityResult(requestCode,resultCode,data);
         }




    }
}