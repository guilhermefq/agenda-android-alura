package br.com.softgran.agenda.converter;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import br.com.softgran.agenda.modelo.Contato;


//Clase especialista em convertar um List para JSON
public class ContatoConverter {

    public String toJson(List<Contato> contatos) {


        try {
            JSONStringer jsonStringer = new JSONStringer();
            jsonStringer.object().key("list").array().object().key("aluno").array();

            for (Contato contato: contatos) {
                jsonStringer.object()
                        .key("id").value(contato.getId())
                        .key("nome").value(contato.getNome())
                        .key("telefone").value(contato.getTelefone())
                        .key("endereco").value(contato.getEndereco())
                        .key("site").value(contato.getSite())
                        .key("nota").value(contato.getNota())
                        .endObject();
            }
            return jsonStringer.endArray().endObject().endArray().endObject().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String converteParaJsonCompleto(Contato contato) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("id").value(contato.getId())
                    .key("nome").value(contato.getNome())
                    .key("endereco").value(contato.getEndereco())
                    .key("telefone").value(contato.getTelefone())
                    .key("site").value(contato.getSite())
                    .key("nota").value(contato.getNota())
                    .key("caminhoFoto").value(contato.getCaminhoFoto())
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonStringer.toString();
    }
}
