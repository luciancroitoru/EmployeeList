package com.example.employeelist.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.employeelist.database.AppDatabase;
import com.example.employeelist.database.EmployeeEntry;

import java.util.List;

//ViewModel implementation
public class EmployeeViewModel extends AndroidViewModel {
    // Constant for logging
    private static final String TAG = EmployeeViewModel.class.getSimpleName();

    private LiveData<List<EmployeeEntry>> stories;

    public EmployeeViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the stories from Database");
        stories = database.employeeDao().loadAllEmployees();
    }

    public LiveData<List<EmployeeEntry>> getEmployees() {
        return stories;
    }
}
