package edu.wgu.mcolesc.vacationapp.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.wgu.mcolesc.vacationapp.Database.Repository;
import edu.wgu.mcolesc.vacationapp.Entities.Excursion;
import edu.wgu.mcolesc.vacationapp.Entities.Vacation;
import edu.wgu.mcolesc.vacationapp.R;

public class VacationDetails extends AppCompatActivity {
    Repository repository;
    DatePickerDialog.OnDateSetListener pickStartDate;
    DatePickerDialog.OnDateSetListener pickEndDate;
    final Calendar myCalendar = Calendar.getInstance();

    Vacation currentVacation;
    int numExcursions;

    String title;
    String hotel;
    String startDate;
    String endDate;
    int vacationID;

    EditText editTitle;
    EditText editHotel;
    TextView editStartDate;
    TextView editEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTitle = findViewById(R.id.edit_vacation_title);
        editHotel = findViewById(R.id.edit_vacation_hotel);
        editStartDate = findViewById(R.id.edit_vacation_start_date);
        editEndDate = findViewById(R.id.edit_vacation_end_date);

        title = getIntent().getStringExtra("title");
        hotel = getIntent().getStringExtra("hotel");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        vacationID = getIntent().getIntExtra("id", -1);

        editTitle.setText(title);
        editHotel.setText(hotel);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info = editStartDate.getText().toString();

                try {
                    myCalendar.setTime(sdf.parse(info));
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(VacationDetails.this, pickStartDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info = editEndDate.getText().toString();

                try {
                    myCalendar.setTime(sdf.parse(info));
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(VacationDetails.this, pickEndDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        pickStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStartDate();
            }
        };

        pickEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEndDate();
            }
        };

        FloatingActionButton addExcursionFAB = findViewById(R.id.add_excursions_fab);
        addExcursionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                intent.putExtra("vacationStartDate", startDate);
                intent.putExtra("vacationEndDate", endDate);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.vacation_details_recycler);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Excursion> filteredExcursions = new ArrayList<>();
        try {
            for (Excursion e : repository.getmAllExcursions()) {
                if (e.getVacationID() == vacationID) filteredExcursions.add(e);
            }
            excursionAdapter.setExcursions(filteredExcursions);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLabelStartDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelEndDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editEndDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vacation_save) {
            Vacation vacation;
            if (vacationID == -1) {
                try {
                    if (repository.getmAllVacations().isEmpty()) {
                        vacationID = 1;
                    }
                    else {
                        vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() -1).getVacationID() + 1;
                    }

                    if (compareDate(editStartDate.getText().toString(), editEndDate.getText().toString())) {
                        vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                        repository.insert(vacation);
                        this.finish();
                    }
                    else {
                        Toast.makeText(VacationDetails.this, "Start date must be before end date.", Toast.LENGTH_LONG).show();
                        vacationID = -1;
                    }


                } catch (InterruptedException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                try {
                    if (compareDate(editStartDate.getText().toString(), editEndDate.getText().toString())) {
                        repository.update(vacation);
                        this.finish();
                    }
                    else {
                        Toast.makeText(VacationDetails.this, "Start date must be before end date.", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (item.getItemId() == R.id.vacation_delete) {
            try {
                for (Vacation v : repository.getmAllVacations()) {
                    if (v.getVacationID() == vacationID) {
                        currentVacation = v;
                    }
                }
                numExcursions = 0;

                for (Excursion exc : repository.getmAllExcursions()) {
                    if (exc.getVacationID() == vacationID) {
                        ++numExcursions;
                    }
                }

                if (numExcursions == 0) {
                    repository.delete(currentVacation);
                    Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle() + " has been deleted", Toast.LENGTH_LONG).show();
                    VacationDetails.this.finish();
                }
                else {
                    Toast.makeText(VacationDetails.this, "Cannot delete a vacation with excursions", Toast.LENGTH_LONG).show();
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (item.getItemId() == R.id.vacation_notify) {
            Intent intent = new Intent(VacationDetails.this, Receiver.class);

            String startDateOnScreen = editStartDate.getText().toString();
            String endDateOnScreen = editEndDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myStartDate = null;
            Date myEndDate = null;

            try {
                myStartDate = sdf.parse(startDateOnScreen);
                myEndDate = sdf.parse(endDateOnScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Long startTrigger = myStartDate.getTime();
            Long endTrigger = myEndDate.getTime();
            Long current = System.currentTimeMillis();
            String currentDay = sdf.format(current);

            if (startDateOnScreen.equals(currentDay)) {
                //Intent intent = new Intent(VacationDetails.this, Receiver.class);
                intent.putExtra("key1", "Vacation starts today!");
                intent.putExtra("vacation_title", editTitle.getText().toString());
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert,
                        intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, startTrigger, sender);
            }
            else if (endDateOnScreen.equals(currentDay)) {
                //Intent intent = new Intent(VacationDetails.this, Receiver.class);
                intent.putExtra("key2", "Vacation ends today...");
                intent.putExtra("vacation_title", editTitle.getText().toString());
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert,
                        intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, endTrigger, sender);
            }

            return true;
        }

        if (item.getItemId() == R.id.vacation_share) {
            Intent sentIntent = new Intent();

            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TEXT, "Vacation: " + editTitle.getText().toString()
                    + "\nStaying at: " + editHotel.getText().toString()
                    + "\nVacation dates: " + editStartDate.getText().toString() + " - " + editEndDate.getText().toString());
            sentIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sentIntent, null);

            startActivity(shareIntent);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
    }

    public boolean compareDate(String startDate, String endDate) throws ParseException {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date castStartDate = sdf.parse(startDate);
        Date castEndDate = sdf.parse(endDate);

        assert castStartDate != null;
        if (castStartDate.after(castEndDate)) {
            return false;
        }
        else {
            return true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView recyclerView = findViewById(R.id.vacation_details_recycler);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);

        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Excursion> filteredExcursions = new ArrayList<>();

        try {
            for (Excursion e : repository.getmAllExcursions()) {
                if (e.getVacationID() == vacationID) filteredExcursions.add(e);
            }
            excursionAdapter.setExcursions(filteredExcursions);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}