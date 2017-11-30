package br.com.softgran.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.softgran.agenda.modelo.Contato;

public class ContatoDAO extends SQLiteOpenHelper{

    public ContatoDAO(Context context) {
        super(context, "Agenda", null, 4);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Contatos (" +
                "id INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "nota REAL" +
                "caminhoFoto TEXT);";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoNova) {
        String sql = "";
        switch (versaoAntiga){
            case 2:
            case 3:
                sql = "ALTER TABLE Contatos ADD COLUMN caminhoFoto TEXT;";
                db.execSQL(sql);
        }
    }

    public void insere(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosContato(contato);

        db.insert("Contatos", null, dados);
    }

    @NonNull
    private ContentValues pegaDadosContato(Contato contato) {
        ContentValues dados = new ContentValues();
        dados.put("nome", contato.getNome());
        dados.put("endereco", contato.getEndereco());
        dados.put("telefone", contato.getTelefone());
        dados.put("site", contato.getSite());
        dados.put("nota", contato.getNota());
        dados.put("caminhoFoto", contato.getCaminhoFoto());
        return dados;
    }


    public List<Contato> buscaContatos() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Contatos", null);
        List<Contato> contatoes = new ArrayList<Contato>();
        while(c.moveToNext()){
            Contato contato = new Contato();
            contato.setId(c.getLong(c.getColumnIndex("id")));
            contato.setNome(c.getString(c.getColumnIndex("nome")));
            contato.setEndereco(c.getString(c.getColumnIndex("endereco")));
            contato.setTelefone(c.getString(c.getColumnIndex("telefone")));
            contato.setSite(c.getString(c.getColumnIndex("site")));
            contato.setNota(c.getDouble(c.getColumnIndex("nota")));
            contato.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));
            contatoes.add(contato);
        }
        c.close();
        return contatoes;
    }

    public void deleta(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();

        String[] params = {String.valueOf(contato.getId())};
        db.delete("Contatos","id = ?",params);
    }

    public void altera(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosContato(contato);
        String[] params = {String.valueOf(contato.getId())};

        db.update("Contatos",dados,"id = ?",params);
    }

    @SuppressWarnings("SpellCheckingInspection")
    public boolean ehContato(String telefone) {
        SQLiteDatabase db = getReadableDatabase();// Pega uma instância de leitura do BD

        //Faz uma select, passando o telefone com parametro.
        //A consulta retorna um Cursor, apontando para o primeiro objeto da lista(do cursor)
        Cursor cursor = db.rawQuery("SELECT * FROM Contato WHERE telefone = ?", new String[]{telefone});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
}
