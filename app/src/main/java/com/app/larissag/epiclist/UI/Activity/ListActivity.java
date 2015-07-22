package com.app.larissag.epiclist.UI.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.larissag.epiclist.Application.EpicListApplication;
import com.app.larissag.epiclist.Model.Categoria;
import com.app.larissag.epiclist.Model.Nivel;
import com.app.larissag.epiclist.Model.Task;
import com.app.larissag.epiclist.R;
import com.app.larissag.epiclist.UI.Adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ListActivity extends Activity {

    @Bind(R.id.categoria_spinner)
    Spinner categoriaSp;
    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.nivel_progress)
    ProgressBar progresso;
    @Bind(R.id.list_linearLayout)
    LinearLayout layout;

    private ListViewAdapter adapter;
    private Context mContext = this;
    private RealmResults<Task> tasks;
    private Realm realm;
    private EpicListApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        application = (EpicListApplication) getApplicationContext();
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        RealmResults<Categoria> categorias = realm.where(Categoria.class).findAll();
        tasks = realm.where(Task.class).findAll();

        adapter = new ListViewAdapter(this, tasks);
        listView.setAdapter(adapter);

        updateCategoria(categorias);

        categoriaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    RealmResults<Task> tasksFiltradas = realm.where(Task.class)
                            .equalTo("categoria.descricao", parent.getItemAtPosition(position).toString()).findAll();
                    adapter.updateTaskList(tasksFiltradas);
                } else {
                    adapter.updateTaskList(tasks);
                }
                updateProgress();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Nivel nivel = realm.where(Nivel.class).equalTo("nroNivel", application.getUserLevel() + 1).findFirst();
        progresso.setMax(nivel.getNroAtividades());
        updateProgress();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_listview) {
            startActivity(new Intent(this, ListActivity.class));
            finish();
            return true;
        }
        if (id == R.id.action_add) {
            startActivityForResult(new Intent(this, TaskActivity.class), 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        tasks = realm.where(Task.class).findAll();
        adapter.updateTaskList(tasks);
        updateProgress();
        updateCategoria(realm.where(Categoria.class).findAll());
        super.onRestart();
    }

    private void updateCategoria(List<Categoria> categorias) {
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Todos");
        for (Categoria c : categorias) {
            if (c.getDescricao() != null)
                spinnerArray.add(c.getDescricao());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categoriaSp.setAdapter(arrayAdapter);
    }

    public void updateProgress() {
        progresso.setProgress(application.getUserProgress());
    }
}
