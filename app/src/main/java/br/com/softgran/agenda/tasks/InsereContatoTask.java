package br.com.softgran.agenda.tasks;

import android.os.AsyncTask;

import br.com.softgran.agenda.WebClient;
import br.com.softgran.agenda.converter.ContatoConverter;
import br.com.softgran.agenda.modelo.Contato;

/**
 * Created by guilhermefq on 14/12/17.
 */

public class InsereContatoTask extends AsyncTask{
    private final Contato contato;

    public InsereContatoTask(Contato contato) {
        this.contato = contato;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String json = new ContatoConverter().converteParaJsonCompleto(contato);

        new WebClient().insere(json);


        return null;
    }
}
