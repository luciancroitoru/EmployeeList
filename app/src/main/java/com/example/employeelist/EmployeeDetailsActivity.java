package com.example.employeelist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeDetailsActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    @BindView(R.id.button_add_new_employee)
    Button button;
    @BindView(R.id.answer_name)
    EditText answerName;
    @BindView(R.id.answer_birthday)
    EditText answerBirthday;
    @BindView(R.id.answer_gender)
    RadioGroup answerGender;
    @BindView(R.id.answer_salary)
    EditText answerSalary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        ButterKnife.bind(this);

        setTitle(getString(R.string.action_bar_employee_details));

        //solution to add a datepicker for birthday found here:
        // https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            //method to set the birthday date once the date is selected and confirmed
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }

        };

        answerBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // on EditText click generate a DatePicker Dialog box
                new DatePickerDialog(EmployeeDetailsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    // method to update the EditText view with the birthday in SimpleDate format
    private void updateDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        answerBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    // method to check which RadioButton from the RadioGroup was clicked
    // RadioButton usage: https://developer.android.com/guide/topics/ui/controls/radiobutton
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_male:
                if (checked)
                    // Rule if male button clicked
                    break;
            case R.id.radio_female:
                if (checked)
                    // Rule if female button clicked
                    break;
        }
    }
}
