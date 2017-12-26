package br.com.softgran.agenda.sinc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import br.com.softgran.agenda.dao.ContatoDAO;
import br.com.softgran.agenda.dto.ContatoSync;
import br.com.softgran.agenda.event.AtualizaListaContatoEvent;
import br.com.softgran.agenda.preferences.ContatoPreferences;
import br.com.softgran.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContatoSincronizador {
    private final Context context;
    private final ContatoPreferences preferences;
    private EventBus bus = EventBus.getDefault();

    public ContatoSincronizador(Context context) {
        this.context = context;
        preferences = new ContatoPreferences(context);
    }

    public void buscaNovosContatos() {
        String versao = preferences.getVersao();
        Call<ContatoSync> call = new RetrofitInicializador().getContatoService().novos(versao);
        call.enqueue(buscaContatoCallback());
    }

    public void buscaContatos() {
        Call<ContatoSync> call = new RetrofitInicializador().getContatoService().lista();
        call.enqueue(buscaContatoCallback());
    }

    @NonNull
    private Callback<ContatoSync> buscaContatoCallback() {
        return new Callback<ContatoSync>() {
            @Override
            public void onResponse(Call<ContatoSync> call, Response<ContatoSync> response) {
                ContatoSync contatoSync = response.body();// Pega a resposta da requisição.Retorna o conteúdo do corpo(body) da requisição HTTP

                String versao = contatoSync.getMomentoDaUltimaModificacao();
                preferences.salvarVersao(versao);

                ContatoDAO contatoDAO = new ContatoDAO(context);
                contatoDAO.sincroniza(contatoSync.getContatos());
                contatoDAO.close();

                Log.i("Versão: ", preferences.getVersao());

                bus.post(new AtualizaListaContatoEvent());
            }

            @Override
            public void onFailure(Call<ContatoSync> call, Throwable t) {
                Log.e("onFailure chamado", t.getMessage());
                Toast.makeText(context, "Erro ao atualizar os contatos!", Toast.LENGTH_SHORT).show();
                bus.post(new AtualizaListaContatoEvent());
            }
        };
    }

    public void buscaTodos() {
        if(preferences.temVersao()){
            buscaNovosContatos();
        } else {
            buscaContatos();
        }
    }




}