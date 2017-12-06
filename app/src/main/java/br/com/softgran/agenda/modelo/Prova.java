package br.com.softgran.agenda.modelo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guilhermefq on 06/12/17.
 */

public class Prova implements Serializable {

    private String materia;
    private String data;
    private List<String> topicos;

    public Prova(String materia, String data, List<String> topicos) {
        this.materia = materia;
        this.data = data;
        this.topicos = topicos;
    }

    @Override
    public String toString() {
        return this.materia;
    }


    public String getMateria() {return materia;}

    public void setMateria(String materia) {this.materia = materia;}

    public String getData() {return data;}

    public void setData(String data) {this.data = data;}

    public List<String> getTopicos() {return topicos;}

    public void setTopicos(List<String> topicos) {this.topicos = topicos;}
}
