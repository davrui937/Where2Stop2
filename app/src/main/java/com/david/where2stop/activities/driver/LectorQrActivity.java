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
import com.david.where2stop.includes.MyToolbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

public class LectorQrActivity extends AppCompatActivity {

    Button mButtonScan;
    EditText mTextResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qr);

        mButtonScan = findViewById(R.id.btnScan);
        mTextResult = findViewById(R.id.txtResult);

        MyToolbar.show(this,"Scan",true);

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