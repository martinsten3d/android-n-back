package se.kth.martsten.lab_1.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import se.kth.martsten.lab_1.db.Result;
import se.kth.martsten.lab_1.db.ResultRepository;

public class ResultViewModel extends AndroidViewModel {

    private final ResultRepository resultRepository;
    private final LiveData<List<Result>> allResults;

    public ResultViewModel(@NonNull Application application) {
        super(application);
        resultRepository = new ResultRepository(application);
        allResults = resultRepository.getAllResults();
    }

    public LiveData<List<Result>> getResults() { return allResults; }

    public void insert(Result result) { resultRepository.insert(result); }
}
