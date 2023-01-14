package se.kth.martsten.lab_1.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "results_2")
public class Result {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "stimuli")
    private final String stimuli;

    @ColumnInfo(name = "n")
    private final int n;

    @ColumnInfo(name = "timeBetweenEvents")
    private final int timeBetweenEvents;

    @ColumnInfo(name = "numberOfEvents")
    private final int numberOfEvents;

    @ColumnInfo(name = "perfectScore")
    private final int perfectScore;

    @ColumnInfo(name = "score")
    private final int score;

    public Result(@NonNull String stimuli, int n, int timeBetweenEvents, int numberOfEvents, int perfectScore, int score) {
        this.stimuli = stimuli;
        this.n = n;
        this.timeBetweenEvents = timeBetweenEvents;
        this.numberOfEvents = numberOfEvents;
        this.perfectScore = perfectScore;
        this.score = score;
    }

    @NonNull
    public String getStimuli() { return stimuli; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getN() {
        return n;
    }

    public int getTimeBetweenEvents() {
        return timeBetweenEvents;
    }

    public int getNumberOfEvents() {
        return numberOfEvents;
    }

    public int getPerfectScore() {
        return perfectScore;
    }

    public int getScore() {
        return score;
    }
}
