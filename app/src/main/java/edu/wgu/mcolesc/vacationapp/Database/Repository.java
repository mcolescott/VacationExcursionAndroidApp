package edu.wgu.mcolesc.vacationapp.Database;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.wgu.mcolesc.vacationapp.DAO.ExcursionDAO;
import edu.wgu.mcolesc.vacationapp.DAO.VacationDAO;
import edu.wgu.mcolesc.vacationapp.Entities.Excursion;
import edu.wgu.mcolesc.vacationapp.Entities.Vacation;

public class Repository {
    private VacationDAO mVacationDAO;
    private ExcursionDAO mExcursionDAO;

    private List<Vacation> mAllVacations;
    private List<Excursion> mAllExcursions;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mVacationDAO = db.vacationDAO();
        mExcursionDAO = db.excursionDAO();
    }

    public List<Vacation> getmAllVacations() throws InterruptedException {
        databaseExecutor.execute(() -> {
            mAllVacations = mVacationDAO.getAllVacations();
        });

        Thread.sleep(1000);
        return mAllVacations;
    }

    public List<Excursion> getmAllExcursions() throws InterruptedException {
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAllExcursions();
        });

        Thread.sleep(1000);
        return mAllExcursions;
    }

    public List<Excursion> getAssociatedExcursions(int vacationID) throws InterruptedException {
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAssociatedExcursions(vacationID);
        });

        Thread.sleep(1000);
        return mAllExcursions;
    }

    public void insert(Vacation vacation) throws InterruptedException {
        databaseExecutor.execute(() ->{
            mVacationDAO.insert(vacation);
        });

        Thread.sleep(1000);
    }

    public void insert(Excursion excursion) throws InterruptedException {
        databaseExecutor.execute(() ->{
            mExcursionDAO.insert(excursion);
        });

        Thread.sleep(1000);
    }

    public void update(Vacation vacation) throws InterruptedException {
        databaseExecutor.execute(() ->{
            mVacationDAO.update(vacation);
        });

        Thread.sleep(1000);
    }

    public void update(Excursion excursion) throws InterruptedException {
        databaseExecutor.execute(() ->{
            mExcursionDAO.update(excursion);
        });

        Thread.sleep(1000);
    }

    public void delete(Vacation vacation) throws InterruptedException {
        databaseExecutor.execute(() -> {
            mVacationDAO.delete(vacation);
        });

        Thread.sleep(1000);
    }

    public void delete(Excursion excursion) throws InterruptedException {
        databaseExecutor.execute(() ->{
            mExcursionDAO.delete(excursion);
        });

        Thread.sleep(1000);
    }


}
