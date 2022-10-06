package com.example.kdm.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText eTUsername, eTTextPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        eTUsername = findViewById(R.id.username);
        eTTextPassword = findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(MainActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = eTUsername.getText().toString();
                String userPassword = eTTextPassword.getText().toString();

                if (userEmail.isEmpty()) {
                    eTUsername.setError("Please enter Username/Email id");
                    eTUsername.requestFocus();
                } else if (userPassword.isEmpty()) {
                    eTTextPassword.setError("Please enter the Password");
                    eTTextPassword.requestFocus();
                } else if (!(userEmail.isEmpty() && userPassword.isEmpty())) {
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                            } else {
                                IntentCalculatorActivity();
                                progressDialog.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public void IntentCalculatorActivity() {
        Intent i = new Intent(MainActivity.this, CalculatorActivity.class);
        startActivity(i);
    }
}