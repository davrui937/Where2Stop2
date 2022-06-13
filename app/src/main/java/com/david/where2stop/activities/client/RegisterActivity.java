package com.david.where2stop.activities.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.david.where2stop.R;
import com.david.where2stop.activities.LoginActivity;
import com.david.where2stop.activities.driver.MapDriverActivity;
import com.david.where2stop.activities.driver.RegisteredDriverActivity;
import com.david.where2stop.includes.MyToolbar;
import com.david.where2stop.models.Client;
import com.david.where2stop.providers.AuthProvider;
import com.david.where2stop.providers.ClientProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    AuthProvider mAuthProvider;
    ClientProvider mClientProvider;
    // VIEWS
    Button mButtonRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputPassword;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://where2stop-625d0-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference myRef = database.getReference();

    Toolbar mToolbar;

    AlertDialog mDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MyToolbar.show(this,"Registro de usuario",true);

        mAuthProvider = new AuthProvider();
        mClientProvider = new ClientProvider();


        mDialog = new SpotsDialog.Builder().setContext(RegisterActivity.this).setMessage("Espere un momento").build();


        mButtonRegister = findViewById(R.id.btnRegister);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputName = findViewById(R.id.textInputName);
        mTextInputPassword = findViewById(R.id.textInputPassword);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               clickUser();
            }
        });
    }

    void clickUser() {
        final String name = mTextInputName.getText().toString();
        final String email = mTextInputEmail.getText().toString();
        final String password = mTextInputPassword.getText().toString();

        String iduser = email.replace(".","");
        int numero = (int)(Math.random()*99999+1);

        myRef.child("client").child(LoginActivity.iduser).child(iduser).child("code").setValue(numero);

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            if (password.length() >= 6) {
                mDialog.show();
                register(name,email,password);
            }
            else {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    void register(String name,String email, String password){
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if (task.isSuccessful()) {
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Client client = new Client(id,name,email);
                    create(client);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void create(Client client){
        mClientProvider.create(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "El registro ha fallado", Toast.LENGTH_SHORT).show();

                }
                else{
                    Intent intent = new Intent(RegisterActivity.this, MapClientActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);                }
            }
        });

    }

}
