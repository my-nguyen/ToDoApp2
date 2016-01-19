package com.example.mynguyen.todoapp2;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

// public class EditActivity extends AppCompatActivity {
public class DetailFragment extends DialogFragment {
   private TextView  mDueDate;
   private Task      mTask;
   private TaskSaver mTaskSaver;
   private static final int REQUEST_DATE = 100;
   private static final String NEW_BUNDLE = "Bundle_Task";

   public interface TaskSaver {
      public void save(Task task);
   }

   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);
      mTaskSaver = (TaskSaver)activity;
   }

   @Override
   public void onDetach() {
      super.onDetach();
      mTaskSaver = null;
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
      return inflater.inflate(R.layout.fragment_detail, container);
   }

   @Override
   public void onViewCreated(View view, @Nullable Bundle bundle) {
      super.onViewCreated(view, bundle);

      // extract the Task object stashed in via newInstance(). note the Bundle object is retrieved
      // via getArguments() and not from the parameter above
      mTask = (Task)getArguments().getSerializable(NEW_BUNDLE);

      // set up the Task Name
      EditText taskName = (EditText)view.findViewById(R.id.task_name);
      // set the text in EditText with the data passed from parent MainActivity
      taskName.setText(mTask.name);
      // save text after user edits it
      taskName.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         }

         @Override
         // record the text entered by user
         public void onTextChanged(CharSequence s, int start, int before, int count) {
            mTask.name = s.toString();
         }

         @Override
         public void afterTextChanged(Editable s) {
         }
      });

      // set up the Due Date textfield
      mDueDate = (TextView)view.findViewById(R.id.task_due_date);
      setDueDateTextView(mTask.dueDate);

      // set up the Due Date change button
      Button changeDate = (Button)view.findViewById(R.id.task_change_due_date);
      // show another fragment (DatePickerFragment) on top of the current fragment (DetailFragment)
      // in order to obtain a new Due Date
      changeDate.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view)
         {
            // DatePickerFragment dialog = new DatePickerFragment();
            DatePickerFragment dialog = DatePickerFragment.newInstance(mTask.dueDate);
            // set up DetailFragment as target to receive data back from DatePickerFragment
            dialog.setTargetFragment(DetailFragment.this, REQUEST_DATE);
            dialog.show(getFragmentManager(), "DialogDate");
         }
      });

      // set up the Priority Spinner
      Spinner prioritySpinner = (Spinner)view.findViewById(R.id.task_priority);
      // create an ArrayAdapter using priority_array and a default spinner layout
      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
            R.array.priority_array, android.R.layout.simple_spinner_item);
      // specify the layout to use when the list of choices appears
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      // apply the adapter to the spinner
      prioritySpinner.setAdapter(adapter);
      // pre-select the existing priority
      if (mTask.priority != null && !mTask.priority.isEmpty())
         prioritySpinner.setSelection(adapter.getPosition(mTask.priority));
      // listen for new selected priority
      prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mTask.priority = parent.getItemAtPosition(position).toString();
         }
         @Override
         public void onNothingSelected(AdapterView<?> parent) {
         }
      });

      // set up the Save Button
      Button saveButton = (Button)view.findViewById(R.id.save_button);
      // send the saved edited text back to parent MainActivity
      saveButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            mTaskSaver.save(mTask);
            dismiss();
         }
      });
   }

   @Override
   // this method receives data (a Calendar object) sent back from DatePickerFragment and sets
   // the Due Date TextView accordingly.
   public void onActivityResult(int request, int result, Intent intent)
   {
      if (result == Activity.RESULT_OK && request == REQUEST_DATE) {
         // set the due-date textfield with the Calendar object sent back from DatePickerFragment
         mTask.dueDate = (Calendar) intent.getSerializableExtra(DatePickerFragment.RESULT_DATEPICKER);
         setDueDateTextView(mTask.dueDate);
      }
   }

   /*
   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      super.onCreateOptionsMenu(menu, inflater);
      inflater.inflate(R.menu.menu_edit, menu);
   }

   @Override
   public void onCreate(Bundle bundle) {
      super.onCreate(bundle);
      setHasOptionsMenu(true);
   }
   */

   public DetailFragment() {
   }

   public static DetailFragment newInstance(Task task) {
      DetailFragment fragment = new DetailFragment();
      Bundle bundle = new Bundle();
      bundle.putSerializable(NEW_BUNDLE, task);
      fragment.setArguments(bundle);
      return fragment;
   }

   // this method sets the Due Date TextView with the contents of the Calendar parameter
   private void setDueDateTextView(Calendar date) {
      int year = date.get(Calendar.YEAR);
      int month = date.get(Calendar.MONTH);
      int day = date.get(Calendar.DAY_OF_MONTH);
      Log.d("NGUYEN", "onViewCreated(), day:" + day + ", month:" + month + ", year:" + year);
      mDueDate.setText(new StringBuilder().append(month+1).append("/").append(day).append("/").append(year));
   }
}
