package se.kth.martsten.lab_1.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ResultRepository {

    private final ResultDao resultDao;
    private final LiveData<List<Result>> allResults;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public ResultRepository(Application application) {
        ResultRoomDatabase db = ResultRoomDatabase.getDatabase(application);
        resultDao = db.resultDao();
        allResults = resultDao.getResults();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Result>> getAllResults() {
        return allResults;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Result result) {
        ResultRoomDatabase.databaseWriteExecutor.execute(() -> {
            resultDao.insert(result);
        });
    }
}
