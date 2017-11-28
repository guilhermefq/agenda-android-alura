package br.com.softgran.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.softgran.agenda.dao.ContatoDAO;
import br.com.softgran.agenda.modelo.Contato;

public class FormularioActivity extends AppCompatActivity {

    private FormularioHelper helper;

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
