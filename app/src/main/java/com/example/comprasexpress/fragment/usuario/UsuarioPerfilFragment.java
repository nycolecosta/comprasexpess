package com.example.comprasexpress.fragment.usuario;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comprasexpress.R;
import com.example.comprasexpress.autenticacao.LoginActivity;
import com.example.comprasexpress.databinding.FragmentUsuarioPerfilBinding;


public class UsuarioPerfilFragment extends Fragment {

    private FragmentUsuarioPerfilBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsuarioPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });
    }
}