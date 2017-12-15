package br.com.softgran.agenda.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import br.com.softgran.agenda.modelo.Contato;

/**
 * Created by guilhermefq on 15/12/17.
 */

public class ContatoSync {

    @JsonProperty("alunos")
    private List<Contato> contatos;
    private String momentoDaUltimaModificacao;

    public List<Contato> getContatos() {return contatos;}
    public String getMomentoDaUltimaModificacao() {return momentoDaUltimaModificacao;}

}
