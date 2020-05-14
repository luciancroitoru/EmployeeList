package com.example.employeelist.adapters;

//EmployeeAdapter that creates and binds ViewHolders and displays data in a recyclerview.

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.employeelist.R;
import com.example.employeelist.database.EmployeeEntry;
import com.example.employeelist.utils.DataMng;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.RecyclerViewHolder> {

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds task data and the Context
    private List<EmployeeEntry> employeeEntries;
    private Context mContext;

    /* Constructor for the EmployeeAdapter that initializes Context.
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public EmployeeAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }


    // This is called when ViewHolders are created.
    // @return a new EmployeeViewHolder that holds view for each employee
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the list_item_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_employee, parent, false);

        return new RecyclerViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        // Determine the values of the wanted data
        EmployeeEntry employeeEntry = employeeEntries.get(position);
        String name = employeeEntry.getEmployeeName();
        String birthday = employeeEntry.getBirthday();
        String gender = employeeEntry.getGender();
        String salary = String.valueOf(employeeEntry.getSalary());

        //Set values
        holder.nameView.setText(name);
        holder.birthdayView.setText(birthday);
        holder.genderView.setText(gender);
        holder.salaryView.setText(salary);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (employeeEntries == null) {
            return 0;
        }
        return employeeEntries.size();
    }

    public List<EmployeeEntry> getEmployeeEntries() {
        return employeeEntries;
    }

    //updates the list of employeeEntries
    public void setEmployees(List<EmployeeEntry> entries) {
        employeeEntries = entries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the employee name, birthday, gender and salary TextViews
        TextView nameView;
        TextView birthdayView;
        TextView genderView;
        TextView salaryView;

        /**
         * Constructor for the Employee ViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.recyclerview_content_name);
            birthdayView = itemView.findViewById(R.id.recyclerview_content_birthday);
            genderView = itemView.findViewById(R.id.recyclerview_content_gender);
            salaryView = itemView.findViewById(R.id.recyclerview_content_salary);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = employeeEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
            int clickedPosition = getAdapterPosition();
            DataMng.Name = employeeEntries.get(clickedPosition).getEmployeeName();
            Log.d(DataMng.Name, "name of employee");
        }
    }
}
