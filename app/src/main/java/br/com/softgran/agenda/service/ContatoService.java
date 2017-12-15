package br.com.softgran.agenda.service;

import br.com.softgran.agenda.modelo.Contato;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by guilhermefq on 14/12/17.
 */

public interface ContatoService {

    @POST("aluno")
    Call<Void> insere(@Body Contato contato);


}




