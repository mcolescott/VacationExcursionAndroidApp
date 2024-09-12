package edu.wgu.mcolesc.vacationapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import edu.wgu.mcolesc.vacationapp.DAO.ExcursionDAO;
import edu.wgu.mcolesc.vacationapp.DAO.VacationDAO;
import edu.wgu.mcolesc.vacationapp.Entities.Excursion;
import edu.wgu.mcolesc.vacationapp.Entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 6, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {

    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    private static volatile VacationDatabaseBuilder INSTANCE;

    public static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VacationDatabaseBuilder.class, "VacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
