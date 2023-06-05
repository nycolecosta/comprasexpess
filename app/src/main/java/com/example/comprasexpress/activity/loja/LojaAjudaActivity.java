package com.example.comprasexpress.activity.loja;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comprasexpress.databinding.ActivityLojaAjudaBinding;

public class LojaAjudaActivity extends AppCompatActivity {
    private ActivityLojaAjudaBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLojaAjudaBinding.inflate(getLayoutInflater());
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
