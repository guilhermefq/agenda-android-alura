package br.com.softgran.agenda.service;

import java.util.List;

import br.com.softgran.agenda.dto.ContatoSync;
import br.com.softgran.agenda.modelo.Contato;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by guilhermefq on 14/12/17.
 */

public interface ContatoService {

    @POST("aluno")
    Call<Void> insere(@Body Contato contato);

    @GET("aluno")
    Call<ContatoSync> lista();

}




