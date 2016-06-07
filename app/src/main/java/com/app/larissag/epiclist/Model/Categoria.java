package com.app.larissag.epiclist.Model;


import io.realm.RealmObject;

public class Categoria extends RealmObject{

    private String descricao;
    private boolean removivel;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isRemovivel() {
        return removivel;
    }

    public void setRemovivel(boolean removivel) {
        this.removivel = removivel;
    }
}
