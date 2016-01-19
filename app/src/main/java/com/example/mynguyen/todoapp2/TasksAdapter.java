package com.example.mynguyen.todoapp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by My on 1/16/2016.
 */
// this class defines a custom ArrayAdapter so that a Task isn't restricted to one TextView
// displayed in an android.R.layout.simple_list_item_1. instead each Task can have multiple fields
// as defined by its own XML
public class TasksAdapter extends ArrayAdapter<Task> {
   public TasksAdapter(Context context, List<Task> tasks) {
      super(context, 0, tasks);
   }

   @Override
   public View getView(int position, View view, ViewGroup parent) {
      if (view == null)
         // inflate from custom XML
         view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_task, parent, false);
      // display custom fields: "Task Name" label and "Priority" label
      Task task = getItem(position);
      TextView name = (TextView)view.findViewById(R.id.task_name);
      name.setText(task.name);
      TextView priority = (TextView)view.findViewById(R.id.task_priority);
      priority.setText(task.priority);
      return view;
   }
}
