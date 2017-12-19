package br.com.softgran.agenda.service;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by GUIFA on 19/12/2017.
 */

public interface DispositivoService {

    @POST("firebase/dispositivo")
    Call<Void> enviaToken(@Header("token") String token);
}
