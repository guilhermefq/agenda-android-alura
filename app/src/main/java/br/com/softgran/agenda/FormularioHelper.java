package br.com.softgran.agenda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.softgran.agenda.modelo.Contato;

public class FormularioHelper {

    private EditText campoNome;
    private EditText campoEndereco;
    private EditText campoTelefone;
    private EditText campoSite;
    private RatingBar campoNota;
    private Contato contato;
    private ImageView campoFoto;

    public FormularioHelper(FormularioActivity activity) {
        this.campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        this.campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        this.campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        this.campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        this.campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);
        this.campoFoto = (ImageView) activity.findViewById(R.id.formulario_foto);
        contato = new Contato();
    }

    public Contato pegaContato() {
        contato.setNome(campoNome.getText().toString());
        contato.setEndereco(campoEndereco.getText().toString());
        contato.setTelefone(campoTelefone.getText().toString());
        contato.setSite(campoSite.getText().toString());
        contato.setNota((double) campoNota.getProgress());
        contato.setCaminhoFoto((String) campoFoto.getTag());

        return contato;
    }

    public void preencheFormulario(Contato contato) {
        campoNome.setText(pegaContato().getNome());
        campoNome.setText(contato.getNome());
        campoEndereco.setText(contato.getEndereco());
        campoTelefone.setText(contato.getTelefone());
        campoSite.setText(contato.getSite());
        campoNota.setProgress(contato.getNota().intValue());
        carregaImagem(contato.getCaminhoFoto());
        this.contato = contato;
    }

    public void carregaImagem(String caminhoDaFoto) {
        if(caminhoDaFoto != null){
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoDaFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300,300, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminhoDaFoto);
        }
    }
}
