package com.example.mynguyen.todoapp2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by My on 11/10/2015.
 */
public class DatePickerFragment extends DialogFragment {
   private static final String NEW_BUNDLE = "Bundle_Calendar";
   public static final String RESULT_DATEPICKER = "Result_DatePicker";

   @Override
   public Dialog onCreateDialog(Bundle bundle)
   {
      // use the DatePicker layout
      View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date, null);
      final DatePicker datePicker = (DatePicker)view.findViewById(R.id.dialog_date_date_picker);
      // extract the Calendar object stashed in via newInstance()
      Calendar calendar = (Calendar)getArguments().getSerializable(NEW_BUNDLE);
      // initialize the DatePicker dialog with the date values in year, month and day
      datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

      // create and return AlertDialog
      return new AlertDialog.Builder(getActivity())
            // configure dialog to display View between the title and buttons
            .setView(view)
            .setTitle(R.string.date_picker_title)
            // when the user presses the positive button in the dialog, retrieve the Date from
            // DatePicker and send the result back to DetailFragment
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                  // send the selected Date back to DetailFragment
                  Fragment target = getTargetFragment();
                  if (target != null) {
                     Intent intent = new Intent();
                     Calendar date = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                     intent.putExtra(RESULT_DATEPICKER, date);
                     target.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                  }
               }
            })
            .create();
   }

   // this convenient method is called by DateButton.onClick() in DetailFragment. it creates a new
   // DatePickerFragment object and stashes a Date object in that DatePickerFragment
   public static DatePickerFragment newInstance(Calendar date)
   {
      Bundle bundle = new Bundle();
      bundle.putSerializable(NEW_BUNDLE, date);
      DatePickerFragment fragment = new DatePickerFragment();
      fragment.setArguments(bundle);
      return fragment;
   }
}
