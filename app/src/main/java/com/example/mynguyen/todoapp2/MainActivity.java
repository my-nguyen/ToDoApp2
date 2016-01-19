package com.example.mynguyen.todoapp2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DetailFragment.TaskSaver {
   private List<Task>         mTasks;
   private int                mPosition;
   private ArrayAdapter<Task> mAdapter;
   private final int          REQUEST_CODE = 17;
   public static final String KEY_EXTRA = "task";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // load items from database
      mTasks = Task.getAll();
      // set adapter to feed from List<Task>
      mAdapter = new TasksAdapter(this, mTasks);

      // set up ListView
      ListView listView = (ListView)findViewById(R.id.main_list_view);
      listView.setAdapter(mAdapter);

      // on long click, remove from ListView the task at clicked position
      listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            // remove task from database
            mTasks.get(position).delete();
            // remove task from ArrayAdapter
            mTasks.remove(position);
            // force screen refresh
            mAdapter.notifyDataSetChanged();
            return true;
         }
      });

      // on (short) click, create and transfer control to EditActivity
      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mPosition = position;
            showEditDialog();
         }
      });
   }

   @Override
   // this method inflates/populates/shows menu items on the ActionBar (not ToolBar)
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_main, menu);
      return true;
   }

   @Override
   // this method handles ActionBar (menu) clicks
   public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId() == R.id.menu_item_new_task) {
         Task task = new Task();
         // save task in database
         task.save();
         // add Task to ListView
         mTasks.add(task);
         // new Task is at the last position
         mPosition = mTasks.size() - 1;
         showEditDialog();
      }
      return super.onOptionsItemSelected(item);
   }

   public void save(Task task) {
      // save to the database
      Log.d("NGUYEN", "MainActivity: task="+task);
      task.save();
      // update the data at a previously selected position
      mTasks.set(mPosition, task);
      // refresh the ListView
      mAdapter.notifyDataSetChanged();
   }

   private void showEditDialog() {
      DetailFragment detailFragment = DetailFragment.newInstance(mTasks.get(mPosition));
      // set the Dialog to occupy the whole screen. this is an alternative to calling setStyle()
      // inside DetailFragment.onCreate()
      detailFragment.setStyle(DetailFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light);
      detailFragment.show(getSupportFragmentManager(), "fragment_edit_name");
   }
}
