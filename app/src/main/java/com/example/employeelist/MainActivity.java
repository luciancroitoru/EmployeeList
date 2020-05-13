package com.example.employeelist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements EmployeeAdapter.ItemClickListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.recycler_view_employee_details)
    RecyclerView mRecyclerView;
    @BindView(R.id.analytics_text_view)
    TextView analyticsTextView;
    List<EmployeeEntry> employeeList;
    String finalAnalytics;
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

        //calculates the sum of the selected transactions
        try {
            finalAnalytics = analytics();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        analyticsTextView.setText(finalAnalytics);

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

    private String analytics() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {

                employeeList = mDb.employeeDao().loadEmployeesForAnalytics();
                int listSize = employeeList.size();
                int ageSum = 0;
                List<Integer> ageList = new ArrayList<>();

                for (int i = 0; i < listSize; i++) {

                    String databaseDate = employeeList.get(i).getBirthday();

                    //Getting the default zone id
                    ZoneId defaultZoneId = ZoneId.systemDefault();

                    Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(databaseDate);
                    //Converting the current Date to Instant
                    Instant instantBirthDate = birthDate.toInstant();
                    //Converting the current Date to LocalDate
                    LocalDate localBirthDate = instantBirthDate.atZone(defaultZoneId).toLocalDate();

                    Date newDate = new Date();
                    //Converting the current Date to Instant
                    Instant instantCurrentDate = newDate.toInstant();
                    //Converting the current Date to LocalDate
                    LocalDate localCurrentDate = instantCurrentDate.atZone(defaultZoneId).toLocalDate();

                    // Calculate age using Period function
                    // https://howtodoinjava.com/java/calculate-age-from-date-of-birth/
                    Period p = Period.between(localBirthDate, localCurrentDate);
                    int age = p.getYears();

                    ageSum += age;

                    ageList.add(age);
                }

                double ageSumDouble = ageSum;
                double averageAge = ageSumDouble / listSize;
                double roundedOneDigitX = Math.round(averageAge * 10) / 10.0;

                // code inspiration for comparing list items and calculate median
                // https://stackoverflow.com/questions/42658403/get-the-median-length-from-an-arraylist
                Collections.sort(ageList, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer i1, Integer i2) {
                        return i1 - i2;
                    }
                });

                double middle;

                if (ageList.size() % 2 == 1) {
                    middle = ageList.get(ageList.size() / 2);
                } else {
                    middle = (ageList.get(ageList.size() / 2)
                            + ageList.get(ageList.size() / 2 - 1)) / 2.0;
                }

                String analyticsString = "Average age: " + roundedOneDigitX +
                        "\nMedian age: " + middle + " ";

                return analyticsString;
            }
        };

        Future<String> future = executorService.submit(callable);
        String finalAnalyticsString = future.get();
        executorService.shutdown();
        return finalAnalyticsString;
    }

    @Override
    public void onItemClickListener(int itemId) {

    }
}
