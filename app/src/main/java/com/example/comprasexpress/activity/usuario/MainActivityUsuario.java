package com.example.comprasexpress.activity.usuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.comprasexpress.R;
import com.example.comprasexpress.autenticacao.LoginActivity;
import com.example.comprasexpress.databinding.ActivityMainUsuarioBinding;
import com.example.comprasexpress.FireHelper.FirebaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityUsuario extends AppCompatActivity {

    private ActivityMainUsuarioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.bottomNavigationView ,navController);

    }
}