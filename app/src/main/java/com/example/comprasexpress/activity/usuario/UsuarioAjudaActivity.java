package com.example.comprasexpress.activity.usuario;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comprasexpress.databinding.ActivityUsusarioAjudaBinding;

public class UsuarioAjudaActivity extends AppCompatActivity {
    private ActivityUsusarioAjudaBinding binding;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityUsusarioAjudaBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            iniciaComponentes();

            configClicks();
        }

    private void configClicks() {
        binding.include.include.ibVoltar.setOnClickListener(v -> finish());
    }

    private void iniciaComponentes() {
            binding.include.textTitulo.setText("Ajuda");
        }
}

