package com.example.comprasexpress.activity.loja;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.comprasexpress.databinding.ActivityLojaConfigBinding;
import com.example.comprasexpress.helper.FirebaseHelper;
import com.example.comprasexpress.model.Loja;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class LojaConfigActivity extends AppCompatActivity {

    private ActivityLojaConfigBinding binding;

    private String caminhoImagem = null;

    private Loja loja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLojaConfigBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recuperaLoja();

        configClicks();
    }

    private void configClicks() {
        binding.include.textTitulo.setText("Configurações");
        binding.include.include.ibVoltar.setOnClickListener(v -> finish());

        binding.imgLogo.setOnClickListener(v -> {
            verificaPermissaoGaleria();
        });

        binding.btnSalvar.setOnClickListener(v -> {
            if (loja != null) {
                validaDados();
            } else {
                Toast.makeText(this, "Ainda estamos recuperando as informações da loja, aguarde...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recuperaLoja() {
        DatabaseReference lojaRef = FirebaseHelper.getDatabaseReference()
                .child("loja");
        lojaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loja = snapshot.getValue(Loja.class);

                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configDados() {
        if (loja.getUrlLogo() != null) {
            Glide.with(this)
                    .load(loja.getUrlLogo())
                    .into(binding.imgLogo);
        }

        if (loja.getNome() != null) {
            binding.edtLoja.setText(loja.getNome());
        }

        if (loja.getCNPJ() != null) {
            binding.edtCNPJ.setText(loja.getCNPJ());
        }


        if (loja.getPublicKey() != null) {
            binding.edtPublicKey.setText(loja.getPublicKey());
        }

        if (loja.getAccessToken() != null) {
            binding.edtAcessToken.setText(loja.getAccessToken());
        }

        if (loja.getParcelas() != 0) {
            binding.edtParcelas.setText(String.valueOf(loja.getParcelas()));
        }
    }

    private void validaDados() {
        String nomeLoja = binding.edtLoja.getText().toString().trim();
        String CNPJ = binding.edtCNPJ.getMasked();
        String publicKey = binding.edtPublicKey.getText().toString().trim();
        String acessToken = binding.edtAcessToken.getText().toString().trim();

        String parcelasStr = binding.edtParcelas.getText().toString().trim();
        int parcelas = 0;
        if (!parcelasStr.isEmpty()) {
            parcelas = Integer.parseInt(binding.edtParcelas.getText().toString().trim());
        }

        if (!nomeLoja.isEmpty()) {
            if (!CNPJ.isEmpty()) {
                if (CNPJ.length() == 18) {
                    if (!publicKey.isEmpty()) {
                        if (!acessToken.isEmpty()) {
                            if (parcelas > 0 && parcelas <= 12) {

                                ocultaTeclado();

                                loja.setNome(nomeLoja);
                                loja.setCNPJ(CNPJ);
                                loja.setPublicKey(publicKey);
                                loja.setAccessToken(acessToken);
                                loja.setParcelas(parcelas);

                                if (caminhoImagem != null) {
                                    salvarImagemFirebase();
                                } else if (loja.getUrlLogo() != null) {
                                    loja.salvar();
                                } else {
                                    ocultaTeclado();
                                    Toast.makeText(this, "Escolha uma logo para sua loja.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                binding.edtParcelas.setError("Mínimo 1 e máximo 12.");
                                binding.edtParcelas.requestFocus();
                            }
                        } else {
                            binding.edtAcessToken.setError("Informe seu acess token.");
                            binding.edtAcessToken.requestFocus();
                        }
                    } else {
                        binding.edtPublicKey.setError("Informe sua public key.");
                        binding.edtPublicKey.requestFocus();
                    }
                } else {
                    binding.edtCNPJ.setError("CNPJ inválido.");
                    binding.edtCNPJ.requestFocus();
                }
            } else {
                binding.edtCNPJ.setError("Informe o CNPJ da loja.");
                binding.edtCNPJ.requestFocus();
            }
        } else {
            binding.edtLoja.setError("Informe um nome para sua loja.");
            binding.edtLoja.requestFocus();
        }
    }

    private void salvarImagemFirebase() {
        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("loja")
                .child(loja.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            loja.setUrlLogo(task.getResult().toString());
            loja.salvar();

            caminhoImagem = null;

        })).addOnFailureListener(e -> Toast.makeText(
                this, "Ocorreu um erro com o upload, tente novamente.",
                Toast.LENGTH_SHORT).show());
    }

    private void verificaPermissaoGaleria() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "Permissão Negada.", Toast.LENGTH_SHORT).show();
            }
        };

        showDialogPermissao(
                permissionlistener,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                "Se você não aceitar a permissão não poderá acessar a Galeria do dispositivo, deseja ativar a permissão agora ?"
        );
    }

    private void showDialogPermissao(PermissionListener permissionListener, String[] permissoes, String msg) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissão negada")
                .setDeniedMessage(msg)
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes)
                .check();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri imagemSelecionada = result.getData().getData();
                    caminhoImagem = imagemSelecionada.toString();
                    binding.imgLogo.setImageBitmap(getBitmap(imagemSelecionada));
                }
            }
    );

    private Bitmap getBitmap(Uri caminhoUri) {
        Bitmap bitmap = null;
        try {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), caminhoUri);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), caminhoUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    private void ocultaTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.edtParcelas.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}