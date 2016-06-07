package com.app.larissag.epiclist.Model;

import io.realm.RealmObject;

public class Nivel extends RealmObject {

    private int nroNivel;
    private String funcionalidade;
    private String texto;
    private String imagem;
    private int nroAtividades;

    public int getNroNivel() {
        return nroNivel;
    }

    public void setNroNivel(int nroNivel) {
        this.nroNivel = nroNivel;
    }

    public String getFuncionalidade() {
        return funcionalidade;
    }

    public void setFuncionalidade(String funcionalidade) {
        this.funcionalidade = funcionalidade;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getNroAtividades() {
        return nroAtividades;
    }

    public void setNroAtividades(int nroAtividades) {
        this.nroAtividades = nroAtividades;
    }
}
