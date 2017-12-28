package br.com.softgran.agenda.sinc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.softgran.agenda.ListaContatosActivity;
import br.com.softgran.agenda.dao.ContatoDAO;
import br.com.softgran.agenda.dto.ContatoSync;
import br.com.softgran.agenda.event.AtualizaListaContatoEvent;
import br.com.softgran.agenda.modelo.Contato;
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

                sincroniza(contatoSync);

//                Log.i("Versão: ", preferences.getVersao());

                bus.post(new AtualizaListaContatoEvent());
                sincronizaContatosInternos();
            }

            @Override
            public void onFailure(Call<ContatoSync> call, Throwable t) {
                Log.e("onFailure chamado", t.getMessage());
                Toast.makeText(context, "Erro ao atualizar os contatos!", Toast.LENGTH_SHORT).show();
                bus.post(new AtualizaListaContatoEvent());
            }
        };
    }


    //Pega a requisição do servidor e verifica se a versão server é mais recente que a atual
    public void sincroniza(ContatoSync contatoSync) {
        String versao = contatoSync.getMomentoDaUltimaModificacao();

        Log.i("versao externa", versao);

        if(temVersaoNova(versao))
        preferences.salvarVersao(versao);
        Log.i("versao atual", preferences.getVersao());

        ContatoDAO contatoDAO = new ContatoDAO(context);
        contatoDAO.sincroniza(contatoSync.getContatos());
        contatoDAO.close();
    }

    private boolean temVersaoNova(String versao) {
        if(!preferences.temVersao())
            return true;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            Date dataExterna = format.parse(versao);
            Date dataInterna = format.parse(preferences.getVersao());//Versão Internar dos dados

            Log.i("versao interna ", preferences.getVersao());

            return dataExterna.after(dataInterna);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void buscaTodos() {
        if(preferences.temVersao()){
            buscaNovosContatos();
        } else {
            buscaContatos();
        }
    }

    private void sincronizaContatosInternos() {
        final ContatoDAO dao = new ContatoDAO(context);
        List<Contato> contatos =  dao.listaNaoSincronizado();

        dao.close();

        Call<ContatoSync> call = new RetrofitInicializador().getContatoService().atualiza(contatos);

        call.enqueue(new Callback<ContatoSync>() {
            @Override
            public void onResponse(Call<ContatoSync> call, Response<ContatoSync> response) {
                ContatoSync contatoSync = response.body();
                sincroniza(contatoSync);
            }

            @Override
            public void onFailure(Call<ContatoSync> call, Throwable t) {

            }
        });
    }


    public void deleta(final Contato contato) {
        Call<Void> call = new RetrofitInicializador().getContatoService().deleta(contato.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                ContatoDAO dao = new ContatoDAO(context);
                dao.deleta(contato);
                dao.close();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(context, "Erro ao deletar o contato. Tente novamente!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}