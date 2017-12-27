package br.com.softgran.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.softgran.agenda.modelo.Contato;

public class ContatoDAO extends SQLiteOpenHelper{

    public ContatoDAO(Context context) {
        super(context, "Agenda", null, 9);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Contatos (" +
                "id CHAR(36) PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "nota REAL," +
                "caminhoFoto TEXT" +
                "sincronizado INT DEFAULT 0" +
                "desativado INT DEFATULT 0);";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoNova) {
        String sql = "";
        switch (versaoAntiga){
            case 2:
            case 3:
                sql = "ALTER TABLE Contatos ADD COLUMN caminhoFoto TEXT;";
                db.execSQL(sql);
            case 4:
                String novaTabela = "CREATE TABLE Contatos_Novo (" +
                        "id CHAR(36) PRIMARY KEY, " +
                        "nome TEXT NOT NULL, " +
                        "endereco TEXT, " +
                        "telefone TEXT, " +
                        "site TEXT, " +
                        "nota REAL," +
                        "caminhoFoto TEXT);";
                db.execSQL(novaTabela);

                String atualizaNovatabela = "INSERT INTO Contatos_Novo " +
                        "(id, nome, endereco, telefone, site, nota, caminhoFoto) " +
                        "SELECT id, nome , endereco, telefone, site, nota, caminhoFoto " +
                        "FROM Contatos";
                db.execSQL(atualizaNovatabela);

                String removerTabelaAntiga = "DROP TABLE Contatos";
                db.execSQL(removerTabelaAntiga);

                String alterarNovaTabela = "ALTER TABLE Contatos_Novo " +
                        "RENAME TO Contatos";
                db.execSQL(alterarNovaTabela);
            case 5:
                String buscaContatos = "SELECT * FROM Contatos";
                Cursor cursor = db.rawQuery(buscaContatos, null);
                List<Contato> contatos = populaContatos(cursor);

                String atualizaIDContato = "UPDATE Contatos SET id=? WHERE id=?";
                for(Contato contato : contatos) {
                    db.execSQL(atualizaIDContato, new String[] {geraUUID(), contato.getId()});
                }
            case 6:
                sql = "UPDATE Contatos SET caminhoFoto = NULL";
                db.execSQL(sql);
            case 7:
                String addCampoSincronizado = "ALTER TABLE Contatos ADD COLUMN sincronizado INT DEFAULT 0";
                db.execSQL(addCampoSincronizado);
            case 8:
                sql = "ALTER TABLE Contatos ADD COLUMN desativado INT DEFAULT 0";
                db.execSQL(sql);
        }
    }

    private String geraUUID() {
        return UUID.randomUUID().toString();
    }

    public void insere(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();

        if(contato.getId() == null) {
            contato.setId(geraUUID());
        }
        ContentValues dados = pegaDadosContato(contato);

        db.insert("Contatos", null, dados);
    }

    @NonNull
    private ContentValues pegaDadosContato(Contato contato) {
        ContentValues dados = new ContentValues();
        dados.put("id", contato.getId());
        dados.put("nome", contato.getNome());
        dados.put("endereco", contato.getEndereco());
        dados.put("telefone", contato.getTelefone());
        dados.put("site", contato.getSite());
        dados.put("nota", contato.getNota());
        dados.put("caminhoFoto", contato.getCaminhoFoto());
        dados.put("sincronizado", contato.getSincronizado());
        dados.put("desativado", contato.getDesativado());
        return dados;
    }

    public List<Contato> getContatos() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Contatos WHERE desativado = 0";
        Cursor c = db.rawQuery(sql, null);
        List<Contato> contatoes = populaContatos(c);
        c.close();
        return contatoes;
    }

    @NonNull
    private List<Contato> populaContatos(Cursor c) {
        List<Contato> contatos = new ArrayList<Contato>();
        while(c.moveToNext()){
            Contato contato = new Contato();
            contato.setId(c.getString(c.getColumnIndex("id")));
            contato.setNome(c.getString(c.getColumnIndex("nome")));
            contato.setEndereco(c.getString(c.getColumnIndex("endereco")));
            contato.setTelefone(c.getString(c.getColumnIndex("telefone")));
            contato.setSite(c.getString(c.getColumnIndex("site")));
            contato.setNota(c.getDouble(c.getColumnIndex("nota")));
            contato.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));
            contato.setSincronizado(c.getInt(c.getColumnIndex("sincronizado")));
            contato.setDesativado(c.getInt(c.getColumnIndex("desativado")));
            contatos.add(contato);
        }
        return contatos;
    }

    public void deleta(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();

        String[] params = {String.valueOf(contato.getId())};

        if(contato.estaDesativado()){
            db.delete("Contatos","id = ?",params);
        } else {
            contato.desativa();
            altera(contato);
        }
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
        String sql =  "SELECT * FROM Contatos WHERE telefone = ?";

        //Faz uma select, passando o telefone com parametro.
        //A consulta retorna um Cursor, apontando para o primeiro objeto da lista(do cursor)
        Cursor cursor = db.rawQuery(sql, new String[]{telefone});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public void sincroniza(List<Contato> contatos) {

        for(Contato contato:
                contatos) {

            contato.sincroniza();

            if(existe(contato)) {// Verifica se o contato existe no BD interno
                if(contato.estaDesativado()){
                    deleta(contato);
                } else {
                    altera(contato);
                }
            } else if(!contato.estaDesativado()) {
                insere(contato);
            }
        }
    }

    private boolean existe(Contato contato) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT id FROM Contatos WHERE id=? LIMIT 1";//LIMIT 1 para retornar o primeiro resultado(linha)
        Cursor cursor = db.rawQuery(query, new String[]{contato.getId()});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public List<Contato> listaNaoSincronizado() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Contatos WHERE sincronizado = 0";
        Cursor cursor = db.rawQuery(sql, null);
        return populaContatos(cursor);
    }
}
