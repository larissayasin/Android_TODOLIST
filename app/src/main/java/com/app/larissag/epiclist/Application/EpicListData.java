package com.app.larissag.epiclist.Application;


import android.content.Context;

import com.app.larissag.epiclist.Enum.FuncionalidadeEnum;
import com.app.larissag.epiclist.Model.Categoria;
import com.app.larissag.epiclist.Model.Nivel;
import com.app.larissag.epiclist.Model.Task;

import io.realm.Realm;

public class EpicListData {

    public void populateDB(){
        populateCategoria();
        populateNivel();
    }

    private void populateCategoria(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Categoria cTrabalho = realm.createObject(Categoria.class);
        cTrabalho.setDescricao("Trabalho");
        cTrabalho.setRemovivel(false);

        Categoria cSaude = realm.createObject(Categoria.class);
        cSaude.setDescricao("Saúde");
        cSaude.setRemovivel(false);

        Categoria cVida = realm.createObject(Categoria.class);
        cVida.setDescricao("Vida");
        cVida.setRemovivel(false);

        realm.commitTransaction();
    }

    private void populateNivel(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Nivel nImagem = realm.createObject(Nivel.class);
        nImagem.setFuncionalidade(FuncionalidadeEnum.INCLUIR_FOTO.toString());
        nImagem.setNroNivel(2);
        nImagem.setNroAtividades(5);
        nImagem.setTexto("Agora você pode adicionar imagens a suas notas!");

        Nivel nCalendario = realm.createObject(Nivel.class);
        nCalendario.setFuncionalidade(FuncionalidadeEnum.ADICIONAR_CALENDARIO.toString());
        nCalendario.setNroNivel(3);
        nCalendario.setNroAtividades(10);
        nCalendario.setTexto("Agora você pode adicionar data a suas notas!");
        realm.commitTransaction();

    }

    public long getNextKey() { Realm realm = Realm.getDefaultInstance();
        return  realm.where(Task.class).maximumInt("chave") + 1; }

}
