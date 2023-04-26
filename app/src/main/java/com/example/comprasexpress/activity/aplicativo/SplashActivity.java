package com.example.comprasexpress.activity.aplicativo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.comprasexpress.FireHelper.FirebaseHelper;
import com.example.comprasexpress.activity.loja.MainActivityLoja;
import com.example.comprasexpress.activity.usuario.MainActivityUsuario;
import com.example.comprasexpress.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseHelper.getAuth().signOut();

        new Handler(getMainLooper()).postDelayed(this::verificaAcesso, 3000);

    }
    private void verificaAcesso(){
        if (FirebaseHelper.getAutenticado()){
            recuperaAcesso();
        }else {
            finish();
            startActivity(new Intent(this, MainActivityUsuario.class));
        }
    }
    private void recuperaAcesso() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){ // Usuário
                    startActivity(new Intent(getBaseContext(), MainActivityUsuario.class));
                }else { // Loja
                    startActivity(new Intent(getBaseContext(), MainActivityLoja.class));
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}