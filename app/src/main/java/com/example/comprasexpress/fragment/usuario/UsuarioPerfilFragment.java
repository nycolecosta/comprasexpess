package com.example.comprasexpress.fragment.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.comprasexpress.activity.usuario.MainActivityUsuario;
import com.example.comprasexpress.activity.usuario.UsuarioAjudaActivity;
import com.example.comprasexpress.activity.usuario.UsuarioPerfilActivity;
import com.example.comprasexpress.autenticacao.CadastroActivity;
import com.example.comprasexpress.autenticacao.LoginActivity;
import com.example.comprasexpress.databinding.FragmentUsuarioPerfilBinding;
import com.example.comprasexpress.helper.FirebaseHelper;

public class UsuarioPerfilFragment extends Fragment {

    private FragmentUsuarioPerfilBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsuarioPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configClicks();
    }

    @Override
    public void onStart() {
        super.onStart();
        configMenu();
    }

    private void configClicks() {
        binding.btnEntrar.setOnClickListener(v -> startActivity(LoginActivity.class));
        binding.btnCadastrar.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), CadastroActivity.class));
        });
        binding.btnMeusDados.setOnClickListener(v -> startActivity(UsuarioPerfilActivity.class));
        binding.btnAjudaUsuario.setOnClickListener(v -> startActivity(UsuarioAjudaActivity.class));
        binding.btnDeslogar.setOnClickListener(v -> {
            FirebaseHelper.getAuth().signOut();
            requireActivity().finish();
            startActivity(new Intent(requireContext(), MainActivityUsuario.class));
        });
    }

    private void startActivity(Class<?> clazz) {
        if (FirebaseHelper.getAutenticado()) {
            startActivity(new Intent(requireContext(), clazz));
        } else {
            startActivity(new Intent(requireContext(), LoginActivity.class));
        }
    }

    private void configMenu() {
        if(FirebaseHelper.getAutenticado()){
            binding.llLogado.setVisibility(View.GONE);
            binding.btnDeslogar.setVisibility(View.VISIBLE);
        }else {
            binding.llLogado.setVisibility(View.VISIBLE);
            binding.btnDeslogar.setVisibility(View.GONE);
        }
    }

}