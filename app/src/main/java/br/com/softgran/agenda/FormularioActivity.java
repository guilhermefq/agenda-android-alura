package br.com.softgran.agenda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import br.com.softgran.agenda.dao.ContatoDAO;
import br.com.softgran.agenda.modelo.Contato;

public class FormularioActivity extends AppCompatActivity {

    public static final int CODIGO_CAMERA = 123;
    private FormularioHelper helper;
    private String caminhoDaFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Formulário");
        setContentView(R.layout.activity_formulario);
        this.helper = new FormularioHelper(this);

        Intent intent = getIntent();
        Contato contato = (Contato) intent.getSerializableExtra("contato");

        if(contato != null){
            helper.preencheFormulario(contato);
        }

        Button botaoFoto = findViewById(R.id.formulario_botao_foto);
        botaoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoDaFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File arquivoFoto = new File(caminhoDaFoto);

                intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));

                //Para o Android 7
                //estamos construindo uma URI para acessar a foto utilizando o FileProvider
                /*intentFoto.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
                                arquivoFoto));*/

                startActivityForResult(intentFoto, CODIGO_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODIGO_CAMERA) {
            if(resultCode == RESULT_OK) {
                helper.carregaImagem(caminhoDaFoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:
                Contato contato = helper.pegaContato();
                ContatoDAO dao = new ContatoDAO(this);

                contato.desincroniza();

                if(contato.getId() != null){
                    dao.altera(contato);
                    Toast.makeText(FormularioActivity.this, "Contato " + contato.getNome() + " alterado!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    dao.insere(contato);
                    Toast.makeText(FormularioActivity.this, "Contato " + contato.getNome() + " salvo!",
                            Toast.LENGTH_SHORT).show();
                }
                dao.close();

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
