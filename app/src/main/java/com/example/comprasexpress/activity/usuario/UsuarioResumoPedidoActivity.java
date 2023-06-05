package com.example.comprasexpress.activity.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comprasexpress.DAO.ItemPedidoDAO;
import com.example.comprasexpress.R;
import com.example.comprasexpress.databinding.ActivityUsuarioResumoPedidoBinding;
import com.example.comprasexpress.helper.FirebaseHelper;
import com.example.comprasexpress.model.FormaPagamento;
import com.example.comprasexpress.model.Pedido;
import com.example.comprasexpress.model.StatusPedido;
import com.example.comprasexpress.util.GetMask;

public class UsuarioResumoPedidoActivity extends AppCompatActivity {

    private ActivityUsuarioResumoPedidoBinding binding;

    private FormaPagamento formaPagamento;

    private ItemPedidoDAO itemPedidoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioResumoPedidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        itemPedidoDAO = new ItemPedidoDAO(this);

        configClicks();

        getExtra();

    }

    private void getExtra() {
        formaPagamento = (FormaPagamento) getIntent().getExtras().getSerializable("pagamentoSelecionado");
        configDados();
    }

    private void configClicks() {

        binding.btnAlterarPagamento.setOnClickListener(v -> finish());

        binding.btnFinalizar.setOnClickListener(v -> {
            if (this.formaPagamento.isCredito()) {
                Intent intent = new Intent(this, UsuarioPagamentoPedidoActivity.class);
                intent.putExtra("pagamentoSelecionado", formaPagamento);
                startActivity(intent);
            } else {
                finalizarPedido();
            }
        });
    }

    private void finalizarPedido() {
        Pedido pedido = new Pedido();
        pedido.setIdCliente(FirebaseHelper.getIdFirebase());
        pedido.setTotal(itemPedidoDAO.getTotalPedido());
        pedido.setPagamento(formaPagamento.getNome());
        pedido.setStatusPedido(StatusPedido.PENDENTE);

        if (formaPagamento.getTipoValor().equals("DESC")) {
            pedido.setDesconto(formaPagamento.getValor());
        } else {
            pedido.setAcrescimo(formaPagamento.getValor());
        }

        pedido.setItemPedidoList(itemPedidoDAO.getList());

        pedido.salvar(true);

        itemPedidoDAO.limparCarrinho();

        Intent intent = new Intent(this, MainActivityUsuario.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("id", 1);
        startActivity(intent);
    }

    private void configDados() {
        ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(this);

        binding.include.textTitulo.setText("Resumo pedido");
        binding.include.include.ibVoltar.setOnClickListener(v -> finish());

        binding.textNomePagamento.setText(formaPagamento.getNome());

        if (formaPagamento.getTipoValor().equals("DESC")) {
            binding.textTipoPagamento.setText("Desconto");
        } else {
            binding.textTipoPagamento.setText("Acréscimo");
        }

        double valorExtra = formaPagamento.getValor();

        binding.textValorTipoPagamento.setText(
                getString(R.string.valor, GetMask.getValor(valorExtra))
        );

        binding.textValorProdutos.setText(getString(R.string.valor,
                GetMask.getValor(itemPedidoDAO.getTotalPedido())));

        if (itemPedidoDAO.getTotalPedido() >= valorExtra) {
            binding.textValorTotal.setText(getString(R.string.valor, GetMask.getValor(itemPedidoDAO.getTotalPedido() - valorExtra)));
            binding.textValor.setText(getString(R.string.valor, GetMask.getValor(itemPedidoDAO.getTotalPedido() - valorExtra)));
        } else {
            binding.textValorTotal.setText(getString(R.string.valor, GetMask.getValor(0)));
            binding.textValor.setText(getString(R.string.valor, GetMask.getValor(0)));
        }
        binding.progressBar.setVisibility(View.GONE);
    }
}