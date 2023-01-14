package se.kth.martsten.lab_1.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * subclass of NBack which handles stimuli in the form of an Integer
 */
public class NBackPosition extends NBack<Integer> {

    private final ArrayList<Integer> ints;

    /**
     * create a new NBack object with position as stimuli
     * @param n number of nbacks
     * @param timeBetweenEvents time between stimulis
     * @param numberOfEvents how many stimulis will be produced
     */
    public NBackPosition(int n, int timeBetweenEvents, int numberOfEvents) {
        super(Stimuli.POSITION, n, timeBetweenEvents, numberOfEvents);
        ints = new ArrayList<>();
    }

    @Override
    protected Integer generateStimuli() {
        int randInt = new Random().nextInt(9);
        if(ints.size() >= n && new Random().nextInt(4) == 0)
            randInt = ints.get(ints.size() - n);
        ints.add(randInt);
        return randInt;
    }

    @Override
    public boolean isNBack() {
        if(ints.size() < n + 1) return false;
        return ints.get(ints.size() - 1).equals(ints.get(ints.size() - n - 1));
    }

    public List<Integer> getInts() { return new ArrayList<>(ints); }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " Integers: " + ints.toString();
    }
}
