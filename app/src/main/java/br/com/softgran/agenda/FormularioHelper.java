package br.com.softgran.agenda;

import android.widget.EditText;
import android.widget.RatingBar;

import br.com.softgran.agenda.modelo.Contato;

public class FormularioHelper {

    private EditText campoNome;
    private EditText campoEndereco;
    private EditText campoTelefone;
    private EditText campoSite;
    private RatingBar campoNota;
    private Contato contato;

    public FormularioHelper(FormularioActivity activity) {
        this.campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        this.campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        this.campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        this.campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        this.campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);
        contato = new Contato();
    }

    public Contato pegaContato() {
        contato.setNome(campoNome.getText().toString());
        contato.setEndereco(campoEndereco.getText().toString());
        contato.setTelefone(campoTelefone.getText().toString());
        contato.setSite(campoSite.getText().toString());
        contato.setNota(Double.valueOf(campoNota.getProgress()));

        return contato;
    }

    public void preencheFormulario(Contato contato) {
        campoNome.setText(pegaContato().getNome());
        campoNome.setText(contato.getNome());
        campoEndereco.setText(contato.getEndereco());
        campoTelefone.setText(contato.getTelefone());
        campoSite.setText(contato.getSite());
        campoNota.setProgress(contato.getNota().intValue());
        this.contato = contato;
    }
}
