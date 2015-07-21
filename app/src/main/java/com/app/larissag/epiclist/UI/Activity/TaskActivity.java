package com.app.larissag.epiclist.UI.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.larissag.epiclist.Application.EpicListApplication;
import com.app.larissag.epiclist.Enum.FuncionalidadeEnum;
import com.app.larissag.epiclist.Model.Categoria;
import com.app.larissag.epiclist.Model.Nivel;
import com.app.larissag.epiclist.Model.Task;
import com.app.larissag.epiclist.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class TaskActivity extends Activity {

    private static int RESULT_LOAD_IMG = 1;
    @Bind(R.id.titulo_text)
    EditText titulo;
    @Bind(R.id.descricao_text)
    EditText descricao;
    @Bind(R.id.categoria_text)
    AutoCompleteTextView categoria;
    @Bind(R.id.image_layout)
    LinearLayout imageLayout;
    @Bind(R.id.add_image)
    ImageView imageView;
    String imgDecodableString;
    private Realm realm;
    private Task task;
    private EpicListApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        application = (EpicListApplication) getApplicationContext();

        RealmResults<Categoria> categorias = realm.where(Categoria.class).findAll();
        List<String> autocompleteArray = new ArrayList<String>();
        for (Categoria c : categorias) {
            autocompleteArray.add(c.getDescricao());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, autocompleteArray);
        categoria.setAdapter(adapter);

        Intent intent = getIntent();

        Long key = intent.getSerializableExtra("chave") != null ? (long) intent.getSerializableExtra("chave") : -1;
        task = realm.where(Task.class)
                .equalTo("chave", key)
                .findFirst();

        if (task != null) {
            titulo.setText(task.getTitulo());
            descricao.setText(task.getDescricao());
            categoria.setText(task.getCategoria() != null ? task.getCategoria().getDescricao() : null);
            if (task.getImagem() != null)
                imageView.setImageBitmap(BitmapFactory.decodeFile(task.getImagem()));
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Nivel nivel = realm.where(Nivel.class).equalTo("nroNivel", application.getUserLevel()).findFirst();
        if (nivel != null && nivel.getFuncionalidade().equals(FuncionalidadeEnum.INCLUIR_FOTO.toString())) {
            imageLayout.setVisibility(View.VISIBLE);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                imageView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "Você não escolheu uma imagem",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            if (validaCampos()) {
                realm.beginTransaction();
                boolean create = (task == null);
                if (create) task = realm.createObject(Task.class);
                task.setTitulo(titulo.getText().toString());
                task.setDescricao(descricao.getText().toString());
                task.setCategoria(validaCategoria());
                if (imgDecodableString != null)
                    task.setImagem(imgDecodableString);
                task.setChave(application.getNextKey());
                if (!create) realm.copyToRealmOrUpdate(task);

                realm.commitTransaction();
                this.finish();
            } else {
                Toast.makeText(this, "Campo Título não foi preenchido", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == R.id.action_delete) {
            if (task != null) {
                realm.beginTransaction();
                task.removeFromRealm();
                realm.commitTransaction();
            }
            this.finish();
        }
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validaCampos() {
        return !titulo.getText().toString().isEmpty();
    }

    private Categoria validaCategoria() {
        if (categoria.getText().toString().isEmpty()) return null;
        RealmQuery<Categoria> query = realm.where(Categoria.class);
        // Add query conditions:
        query.equalTo("descricao", categoria.getText().toString());
        // Execute the query:
        Categoria result = query.findFirst();
        if (result == null) {
//            realm.beginTransaction();
            result = realm.createObject(Categoria.class);
            result.setDescricao(categoria.getText().toString());
            result.setRemovivel(true);
         //   realm.commitTransaction();
        }
        return result;
    }
}
