package br.com.softgran.agenda.service;

import java.util.List;

import br.com.softgran.agenda.dto.ContatoSync;
import br.com.softgran.agenda.modelo.Contato;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by guilhermefq on 14/12/17.
 */

public interface ContatoService {

    @POST("aluno")
    Call<Void> insere(@Body Contato contato);

    @GET("aluno")
    Call<ContatoSync> lista();

    @DELETE("aluno/{id}")
    Call<Void> deleta(@Path("id") String id);

    @GET("aluno/diff")
    Call<ContatoSync> novos(@Header("datahora") String versao);

    @PUT("aluno/lista")
    Call<ContatoSync> atualiza(@Body List<Contato> contatos);

}




