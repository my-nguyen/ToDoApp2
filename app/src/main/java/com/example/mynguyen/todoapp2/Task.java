package com.example.mynguyen.todoapp2;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by My on 1/15/2016.
 */
@Table(name = "Items")
// this class represents the database model for a Task; it is required by ActiveAndroid. it also
// implements Serializable so its object can be passed in a Bundle from one activity to another.
public class Task extends Model implements Serializable {
   @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
   public long remoteId;
   @Column(name = "Name")
   public String name;
   @Column(name = "due_date")
   public Calendar dueDate;
   @Column(name = "Priority")
   public String priority;

   private static final long serialVersionUID = 5177222050535318633L;

   public Task() {
      super();
      // generate a long unique ID; a static long wouldn't work because it will be reset to zero
      // every time the app is restarted, which would lead to repeating remoteID in the database,
      // which would cause the database to override existing record with the same ID.
      remoteId = UUID.randomUUID().getLeastSignificantBits();
      name = "";
      dueDate = Calendar.getInstance();
      priority = "High";
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      int month = dueDate.get(Calendar.MONTH);
      int day = dueDate.get(Calendar.DAY_OF_MONTH);
      int year = dueDate.get(Calendar.YEAR);
      builder.append("ID:").append(remoteId).append(", Name:").append(name).append(", Due:")
            .append(month + 1).append("/").append(day).append("/").append(year).append(" ").append(priority);
      return builder.toString();
   }

   public static List<Task> getAll() {
      return new Select().from(Task.class).execute();
   }
}
