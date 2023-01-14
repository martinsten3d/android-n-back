package se.kth.martsten.lab_1.model;

import androidx.annotation.NonNull;

/**
 * Abstract class for creating subclasses of different types of N-Backs, e.g. position or audio.
 * @param <T> type of object the player should recognize
 */
public abstract class NBack<T> {

    protected final int n;
    private final int timeBetweenEvents;
    private final int numberOfEvents;

    private int perfectScore;
    private int currentEvent;
    private boolean guessed;
    private int score;

    public enum Stimuli { POSITION, AUDIO }
    private final Stimuli stimuli;
    public enum Guess { CORRECT, WRONG, NONE }

    public NBack(Stimuli stimuli, int n, int timeBetweenEvents, int numberOfEvents) {
        this.stimuli = stimuli;
        this.n = n;
        this.timeBetweenEvents = timeBetweenEvents;
        this.numberOfEvents = numberOfEvents;

        perfectScore = 0;
        currentEvent = numberOfEvents;
        guessed = false;
        score = 0;
    }

    public NBack() {
        this(Stimuli.POSITION, 1, 2500, 30);
    }

    /**
     * creates a new event and returns a newly generated stimuli
     * @return a new stimuli of the N-Backs stimuli type
     */
    public T newEvent() {
        currentEvent--;
        guessed = false;
        if(isNBack()) perfectScore++;
        return generateStimuli();
    }

    /**
     * generates a new stimuli, based on the type of stimuli the subclass defines
      * @return a new stimuli
     */
    protected abstract T generateStimuli();

    /**
     * called when the player guesses that the latest generated stimuli is the same as the one n times back
     * @return none if the player has already guessed, correct or wrong.
     */
    public Guess guess() {
        if(guessed) return Guess.NONE;
        guessed = true;

        if(isNBack()) {
            score++;
            return Guess.CORRECT;
        }

        return Guess.WRONG;
    }

    /**
     * returns true or false based on if the last generated stimuli is the same as the one n times back
     * @return true or false
     */
    public abstract boolean isNBack();

    public int getN() { return n; }
    public int getTimeBetweenEvents() { return timeBetweenEvents; }
    public int getNumberOfEvents() { return numberOfEvents; }
    public int getPerfectScore() { return perfectScore; }
    public int getCurrentEvent() { return currentEvent; }
    public boolean getGuessed() { return guessed; }
    public int getScore() { return score; }
    public Stimuli getStimuli() { return stimuli; }

    @NonNull
    @Override
    public String toString() {
        return "Stimuli: " + stimuli + ", N: " + n + ", Time between events: " + timeBetweenEvents + ", Number of events: " + numberOfEvents;
    }
}
