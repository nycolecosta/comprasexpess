package com.example.comprasexpress.autenticacao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comprasexpress.databinding.ActivityCadastroBinding;
import com.example.comprasexpress.FireHelper.FirebaseHelper;
import com.example.comprasexpress.model.Loja;
import com.example.comprasexpress.model.Usuario;

public class CadastroActivity extends AppCompatActivity {
    private ActivityCadastroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configClicks();

    }

    public void validaDados(View view){
        String nome = binding.edtNome.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String senha = binding.edtSenha.getText().toString().trim();
        String confirmaSenha = binding.edtConfirmaSenha.getText().toString().trim();

        if(!nome.isEmpty()){
            if(!email.isEmpty()){
                if(!senha.isEmpty()){
                    if(!confirmaSenha.isEmpty()){
                        if(senha.equals(confirmaSenha)){

                            binding.progressBar2.setVisibility(View.VISIBLE);

//                            Loja loja = new Loja();
//                            loja.setNome(nome);
//                            loja.setEmail(email);
//                            loja.setSenha(senha);
//
//                            criarLoja(loja);

                            Usuario usuario = new Usuario();
                            usuario.setNome(nome);
                            usuario.setEmail(email);
                            usuario.setSenha(senha);

                            criarConta(usuario);

                        }else {
                            binding.edtConfirmaSenha.requestFocus();
                            binding.edtConfirmaSenha.setError("Senha não confere.");
                        }
                    }else {
                        binding.edtConfirmaSenha.requestFocus();
                        binding.edtConfirmaSenha.setError("Confirme sua senha.");
                    }
                }else {
                    binding.edtSenha.requestFocus();
                    binding.edtSenha.setError("Informe uma senha.");
                }
            }else {
                binding.edtEmail.requestFocus();
                binding.edtEmail.setError("Informe seu email.");
            }
        }else {
            binding.edtNome.requestFocus();
            binding.edtNome.setError("Informe seu nome.");
        }

    }
//    private void criarLoja(Loja loja){
//        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
//                loja.getEmail(), loja.getSenha()
//        ).addOnCompleteListener(task -> {
//            if(task.isSuccessful()){
//
//                String id = task.getResult().getUser().getUid();
//
//                loja.setId(id);
//                loja.salvar();
//
//            }else {
//                Toast.makeText(this, FirebaseHelper.validaErros(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
//            }
//            binding.progressBar2.setVisibility(View.GONE);
//        });
//
//    }
//

    private void criarConta(Usuario usuario){
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                String id = task.getResult().getUser().getUid();

                usuario.setId(id);
                usuario.salvar();

                Intent intent = new Intent();
                intent.putExtra("email", usuario.getEmail());
                setResult(RESULT_OK, intent);
                finish();

            }else {
                Toast.makeText(this, FirebaseHelper.validaErros(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
            }

            binding.progressBar2.setVisibility(View.GONE);

        });
    }

    private void configClicks() {
        binding.include.ibVoltar.setOnClickListener(view -> finish());
        binding.btnLogin.setOnClickListener(view -> finish());
    }
}

