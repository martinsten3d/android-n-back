package se.kth.martsten.lab_1.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Result result);

    @Query("DELETE FROM results_2")
    void deleteAll();

    @Query("SELECT * FROM results_2 ORDER BY id DESC")
    LiveData<List<Result>> getResults();
}
