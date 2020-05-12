package com.example.employeelist.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//class for a EmployeeEntity object to be stored in the database, with getter and setter methods
@Entity(tableName = "employee")
public class EmployeeEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String employeeName;
    private String birthday;
    private String gender;
    private double salary;

    @Ignore
    public EmployeeEntry(String employeeName, String birthday, String gender, double salary) {
        this.employeeName = employeeName;
        this.birthday = birthday;
        this.gender = gender;
        this.salary = salary;
    }

    public EmployeeEntry(int id, String employeeName, String birthday, String gender, double salary) {
        this.id = id;
        this.employeeName = employeeName;
        this.birthday = birthday;
        this.gender = gender;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String userName) {
        this.employeeName = employeeName;
    }

    public String getBirthday() { return birthday; }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String title) {
        this.gender = title;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double storyContent) {
        this.salary = storyContent;
    }
}


