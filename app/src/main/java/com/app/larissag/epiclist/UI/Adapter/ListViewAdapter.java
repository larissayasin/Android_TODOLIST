package com.app.larissag.epiclist.UI.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.larissag.epiclist.Application.EpicListApplication;
import com.app.larissag.epiclist.Model.Nivel;
import com.app.larissag.epiclist.Model.Task;
import com.app.larissag.epiclist.R;
import com.app.larissag.epiclist.UI.Activity.TaskActivity;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.io.Serializable;
import java.util.List;

import io.realm.Realm;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context context;
    private List<Task> tasks;
    private EpicListApplication application;

    public ListViewAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        application = (EpicListApplication) context.getApplicationContext();
    }

    public void updateTaskList(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //   YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.done));
                Toast.makeText(context, "Abriy " + position, Toast.LENGTH_SHORT).show();
                Realm realm = Realm.getDefaultInstance();
            //    tasks.remove(position);
                Task task = getItem(position);

                realm.beginTransaction();
                task.removeFromRealm();
                realm.commitTransaction();


                Nivel nivel = realm.where(Nivel.class).equalTo("nroNivel", application.getUserLevel() +1).findFirst();
                if(nivel.getNroAtividades()>application.getUserProgress()){
                    application.changeUserProgress(application.getUserProgress()+1);
                }else{
                    application.changeUserProgress(0);
                    application.changeUserLevel(application.getUserLevel() + 1);
                    Toast.makeText(context, nivel.getTexto(), Toast.LENGTH_SHORT).show();
                }
                application.changeUserProgress(application.getUserProgress());

                notifyDataSetChanged();

            }
        });

        return v;
    }

    @Override
    public void fillValues(final int position, View convertView) {
      convertView.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
              Intent intent = new Intent(context, TaskActivity.class);
              intent.putExtra("chave",getItem(position).getChave());
              context.startActivity(intent);
              return false;
          }
      });

        TextView titulo = (TextView) convertView.findViewById(R.id.titulo_text);
        titulo.setText(getItem(position).getTitulo());
        TextView t = (TextView) convertView.findViewById(R.id.position);
        t.setText((position + 1) + " - ");
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}