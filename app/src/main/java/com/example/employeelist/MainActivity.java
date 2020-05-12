package com.example.employeelist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeelist.adapters.EmployeeAdapter;
import com.example.employeelist.database.AppDatabase;
import com.example.employeelist.database.EmployeeEntry;
import com.example.employeelist.viewmodels.EmployeeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements EmployeeAdapter.ItemClickListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.recycler_view_employee_details)
    RecyclerView mRecyclerView;
    List<EmployeeEntry> employeeList;
    private Intent intent;
    private EmployeeAdapter mAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setTitle(getString(R.string.action_bar_employees_list));

        //database call
        mDb = AppDatabase.getInstance(getApplicationContext());

        //viewmodel setup call
        setupViewModel();

        // linear layout setup for the RecyclerView, sets items in a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // this intializes the adapter and attaches it to the RecyclerView
        mAdapter = new EmployeeAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        // fab opens activity for adding a new employee
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, EmployeeDetailsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setupViewModel() {
        EmployeeViewModel employeeViewModel = ViewModelProviders.of(this).get(EmployeeViewModel.class);
        employeeViewModel.getEmployees().observe(this, new Observer<List<EmployeeEntry>>() {
            @Override
            public void onChanged(@Nullable List<EmployeeEntry> employeeEntries) {
                //updates the list of employees from livedata to viewmodel
                mAdapter.setEmployees(employeeEntries);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {

    }
}
