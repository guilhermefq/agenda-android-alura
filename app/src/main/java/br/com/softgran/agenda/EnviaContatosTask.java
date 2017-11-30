package br.com.softgran.agenda;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.softgran.agenda.converter.ContatoConverter;
import br.com.softgran.agenda.dao.ContatoDAO;
import br.com.softgran.agenda.modelo.Contato;

public class EnviaContatosTask extends AsyncTask<Void,Void, String> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialog alertDialog;

    public EnviaContatosTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {

        WebClient webClient = new WebClient();
        ContatoConverter converter = new ContatoConverter();
        ContatoDAO dao = new ContatoDAO(context);
        List<Contato> contatos = dao.getContatos();
        dao.close();

        String json = converter.toJson(contatos);
        String resposta = webClient.post(json);

        return resposta;
    }

    @Override
    protected void onPostExecute(String resposta) {
        alertDialog.dismiss();
        Toast.makeText(context, resposta, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        alertDialog = ProgressDialog.show(context,"Aguarde", "Enviado para o servidor...", true, true);
        alertDialog.show();
    }
}
