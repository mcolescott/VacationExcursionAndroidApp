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
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.wgu.mcolesc.vacationapp.Database.Repository;
import edu.wgu.mcolesc.vacationapp.Database.VacationDatabaseBuilder;
import edu.wgu.mcolesc.vacationapp.Entities.Excursion;
import edu.wgu.mcolesc.vacationapp.Entities.Vacation;
import edu.wgu.mcolesc.vacationapp.R;

public class ExcursionDetails extends AppCompatActivity {
    VacationDatabaseBuilder vacationDatabase;
    Repository repository;
    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalendar = Calendar.getInstance();
    Excursion currentExcursion;

    String title;
    String date;
    String vacationStartDate;
    String vacationEndDate;
    int excursionID;
    int vacationID;

    EditText editTitle;
    TextView editDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        vacationDatabase = VacationDatabaseBuilder.getDatabase(getApplicationContext());

        editTitle = findViewById(R.id.edit_excursion_title);
        editDate = findViewById(R.id.edit_excursion_date);

        title = getIntent().getStringExtra("name");
        date = getIntent().getStringExtra("date");
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        vacationStartDate = getIntent().getStringExtra("vacationStartDate");
        vacationEndDate = getIntent().getStringExtra("vacationEndDate");

        editTitle.setText(title);
        editDate.setText(date);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long date = System.currentTimeMillis();
                String info = editDate.getText().toString();

                if (info.isEmpty()) info = sdf.format(date);
                try {
                    myCalendar.setTime(sdf.parse(info));
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(ExcursionDetails.this, startDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Repository repository = new Repository(getApplication());
        if (item.getItemId() == R.id.excursion_save) {
            Excursion excursion;
            if (excursionID == -1) {
                try {
                    if (repository.getmAllExcursions().size() == 0) {
                        excursionID = 1;
                    }
                    else {
                        excursionID = repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcursionID() + 1;
                    }

                    if (compareDate(vacationStartDate, vacationEndDate, editDate.getText().toString())) {
                        excursion = new Excursion(excursionID, editTitle.getText().toString(), editDate.getText().toString(), vacationID);
                        repository.insert(excursion);
                        this.finish();
                    }
                    else {
                        Toast.makeText(ExcursionDetails.this, "Excursion date must be during vacation dates.", Toast.LENGTH_LONG).show();
                        excursionID = -1;
                    }
                }
                catch (InterruptedException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                excursion = new Excursion(excursionID, editTitle.getText().toString(), editDate.getText().toString(), vacationID);
                Vacation vacation = vacationDatabase.vacationDAO().getVacationInfo(vacationID);

                vacationStartDate = vacation.getVacationStartDate();
                vacationEndDate = vacation.getVacationEndDate();

                try {
                    if (compareDate(vacationStartDate, vacationEndDate, editDate.getText().toString())) {
                        repository.update(excursion);
                        this.finish();
                    }
                    else {
                        Toast.makeText(ExcursionDetails.this, "Excursion date must be during vacation dates.", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (item.getItemId() == R.id.excursion_delete) {
            try {
                for (Excursion e : repository.getmAllExcursions()) {
                    if (e.getExcursionID() == excursionID) {
                        currentExcursion = e;
                    }
                }
                repository.delete(currentExcursion);
                Toast.makeText(ExcursionDetails.this, currentExcursion.getExcursionTitle() + " has been deleted.", Toast.LENGTH_LONG).show();
                ExcursionDetails.this.finish();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (item.getItemId() == R.id.excursion_notify) {
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;

            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Long trigger = myDate.getTime();
            Intent intent = new Intent(ExcursionDetails.this, Receiver.class);
            intent.putExtra("key3", "Excursion today!");
            intent.putExtra("excursion_title", editTitle.getText().toString());
            PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert,
                    intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
    }

    public boolean compareDate(String startDate, String endDate, String excDate) throws ParseException {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date castStartDate = sdf.parse(startDate);
        Date castEndDate = sdf.parse(endDate);
        Date castExcDate = sdf.parse(excDate);

        assert castExcDate != null;
        assert castStartDate != null;
        assert castEndDate != null;

        if (castExcDate.getTime() == castStartDate.getTime()) {
            return true;
        }
        else if (castExcDate.getTime() == castEndDate.getTime()) {
            return true;
        }
        else if (castExcDate.before(castStartDate)) {
            return false;
        }
        else if (castExcDate.after(castEndDate)) {
            return false;
        }
        else {
            return true;
        }
    }
}