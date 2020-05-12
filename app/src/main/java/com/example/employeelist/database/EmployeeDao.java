package com.example.employeelist.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmployeeDao {

    @Query("SELECT * FROM employee ORDER BY id")
    LiveData<List<EmployeeEntry>> loadAllEmployees();

    @Insert
    void insertEmployee(EmployeeEntry employeeEntry);

}
