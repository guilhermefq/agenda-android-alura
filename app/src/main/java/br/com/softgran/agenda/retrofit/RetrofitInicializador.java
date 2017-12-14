package br.com.softgran.agenda.retrofit;

import br.com.softgran.agenda.service.ContatoService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by guilhermefq on 14/12/17.
 */

public class RetrofitInicializador {

    private final Retrofit retrofit;

    public RetrofitInicializador() {
        retrofit = new Retrofit.Builder()
                .baseUrl("Http:192.168.15.180:8080/api/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public ContatoService getContatoService() {
        return retrofit.create(ContatoService.class);
    }
}
