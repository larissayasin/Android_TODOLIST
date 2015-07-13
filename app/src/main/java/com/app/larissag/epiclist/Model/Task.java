package com.app.larissag.epiclist.Model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by larissag on 7/13/15.
 */
public class Task extends RealmObject {

    private String titulo;
    private String descricao;
    private String imagem;
    private Categoria categoria;
    private Date data;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
