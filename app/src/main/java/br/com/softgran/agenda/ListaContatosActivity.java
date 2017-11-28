package br.com.softgran.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.softgran.agenda.dao.ContatoDAO;
import br.com.softgran.agenda.modelo.Contato;

public class ListaContatosActivity extends AppCompatActivity {

    private ListView listaContatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos);
        setTitle("Contatos");

        listaContatos = (ListView) findViewById(R.id.lista_contatos);

        Button novoContato = (Button) findViewById(R.id.novo_contato);
        novoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaContatosActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });

        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Contato contato = (Contato) lista.getItemAtPosition(position);

                Intent intent = new Intent(ListaContatosActivity.this, FormularioActivity.class);

                intent.putExtra("contato", contato);
                startActivity(intent);

                /*Toast.makeText(ListaContatosActivity.this,"Contato " + contato.getNome() + " selecionado",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        registerForContextMenu(listaContatos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                final Contato contato = (Contato) listaContatos.getItemAtPosition(info.position);
                ContatoDAO dao = new ContatoDAO(ListaContatosActivity.this);
                dao.deleta(contato);
                dao.close();

                Toast.makeText(ListaContatosActivity.this, "Deletando o contato " + contato.getNome(), Toast.LENGTH_SHORT).show();

                carregaLista();
                return false;
            }
        });
    }

    public void carregaLista() {
        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> contatoes = dao.buscaContatos();
        dao.close();

        ListView listaContatos = (ListView) findViewById(R.id.lista_contatos);
        ArrayAdapter<Contato> adapter =
                new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, contatoes);

        listaContatos.setAdapter(adapter);
    }
}
