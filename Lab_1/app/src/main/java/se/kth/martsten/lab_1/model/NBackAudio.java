package se.kth.martsten.lab_1.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * subclass of NBack which handles stimuli in the form of a String
 */
public class NBackAudio extends NBack<String> {

    private final ArrayList<String> strings;
    private final String[] letters = new String[]{ "T", "L", "H", "C", "O", "Q", "K", "R" };

    /**
     * create a new NBack object with audio as stimuli
     * @param n number of nbacks
     * @param timeBetweenEvents time between stimulis
     * @param numberOfEvents how many stimulis will be produced
     */
    public NBackAudio(int n, int timeBetweenEvents, int numberOfEvents) {
        super(Stimuli.AUDIO, n, timeBetweenEvents, numberOfEvents);
        strings = new ArrayList<>();
    }

    @Override
    protected String generateStimuli() {
        String s = letters[new Random().nextInt(letters.length)];
        if(strings.size() >= n && new Random().nextInt(4) == 0)
            s = strings.get(strings.size() - n);
        strings.add(s);
        return s;
    }

    @Override
    public boolean isNBack() {
        if(strings.size() < n + 1) return false;
        return strings.get(strings.size() - 1).equals(strings.get(strings.size() - n - 1));
    }

    public List<String> getStrings() { return new ArrayList<>(strings); }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " Strings: " + strings.toString();
    }
}
