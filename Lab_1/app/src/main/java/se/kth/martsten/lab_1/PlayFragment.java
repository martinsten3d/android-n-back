package se.kth.martsten.lab_1;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import se.kth.martsten.lab_1.db.Result;
import se.kth.martsten.lab_1.model.NBack;
import se.kth.martsten.lab_1.model.NBackAudio;
import se.kth.martsten.lab_1.model.NBackPosition;
import se.kth.martsten.lab_1.utils.AnimationUtil;
import se.kth.martsten.lab_1.utils.DialogUtil;
import se.kth.martsten.lab_1.utils.TextToSpeechUtil;
import se.kth.martsten.lab_1.view.ResultViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView titleView, eventsView, scoreView;

    private ImageView[] imageView;
    private final int[] boxIDs = new int[] {
            R.id.box0,
            R.id.box1,
            R.id.box2,
            R.id.box3,
            R.id.box4,
            R.id.box5,
            R.id.box6,
            R.id.box7,
            R.id.box8
    };

    private View waitingView, playingView;
    private Button positionButton, audioButton;
    private final ArrayList<NBack> nBacks;

    private TextToSpeechUtil textToSpeechUtil;

    private CountDownTimer outerTimer, innerTimer;

    private int config_shortAnimTime, config_longAnimTime;

    private ResultViewModel resultViewModel;

    public PlayFragment() {
        // Required empty public constructor
        nBacks = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayFragment newInstance(String param1, String param2) {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // NB! Cancel the current and queued utterances, then shut down the service to
    // de-allocate resources
    @Override
    public void onPause() {
        textToSpeechUtil.shutdown();
        super.onPause();
    }

    // Initialize the text-to-speech service - we do this initialization
    // in onResume because we shutdown the service in onPause
    @Override
    public void onResume() {
        super.onResume();
        textToSpeechUtil.initialize(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cancel timers when changing fragment
        if(innerTimer != null) innerTimer.cancel();
        if(outerTimer != null) outerTimer.cancel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    // Fragment view created, safe to call findViewById()
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // References
        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        textToSpeechUtil = new TextToSpeechUtil();
        config_shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        config_longAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);

        // Setup info text
        titleView = view.findViewById(R.id.text_title);
        eventsView = view.findViewById(R.id.text_events);
        scoreView = view.findViewById(R.id.text_score);

        // Setup grid and boxes
        imageView = new ImageView[boxIDs.length];
        for(int i = 0; i < boxIDs.length; i++) {
            imageView[i] = view.findViewById(boxIDs[i]);
            imageView[i].setVisibility(View.INVISIBLE);
        }

        // Setup buttons
        waitingView = view.findViewById(R.id.waiting);
        playingView = view.findViewById(R.id.playing);
        playingView.setVisibility(View.INVISIBLE);

        positionButton = view.findViewById(R.id.button_position);
        audioButton = view.findViewById(R.id.button_audio);

        view.findViewById(R.id.button_start).setOnClickListener(this::startNBack);
        view.findViewById(R.id.button_position).setOnClickListener(this::guessPosition);
        view.findViewById(R.id.button_audio).setOnClickListener(this::guessAudio);

        // Show correct UI
        AnimationUtil.fadeInView(view, config_shortAnimTime);
        AnimationUtil.fadeInView(waitingView, config_longAnimTime);

        newNBack();
        updateStateInfo();
    }

    private void startNBack(View view) {
        positionButton.setVisibility(View.GONE);
        audioButton.setVisibility(View.GONE);
        for(NBack nBack : nBacks) {
            if (nBack instanceof NBackPosition)
                positionButton.setVisibility(View.VISIBLE);
            if(nBack instanceof NBackAudio)
                audioButton.setVisibility(View.VISIBLE);
        }

        AnimationUtil.fadeOutView(waitingView, config_shortAnimTime);
        AnimationUtil.fadeInView(playingView, config_shortAnimTime);

        runEvent();
    }

    private void endNBack() {
        // Find result of round
        int score = 0;
        int perfectScore = 0;
        for(NBack nBack : nBacks) {
            score += nBack.getScore();
            perfectScore += nBack.getPerfectScore();
        }

        // Save data
        resultViewModel.insert(new Result(getStimuliString(), nBacks.get(0).getN(), nBacks.get(0).getTimeBetweenEvents(),
                nBacks.get(0).getNumberOfEvents(), perfectScore, score));

        // Show result dialog
        String msg = getString(R.string.dialog_result, score, perfectScore, perfectScore == 0 ? 100f : (float)score/(float)perfectScore * 100f);
        DialogUtil.createDialog(getActivity(),getString(R.string.dialog_title), msg).show();

        // Show correct UI
        AnimationUtil.fadeOutView(playingView, config_shortAnimTime);
        AnimationUtil.fadeInView(waitingView, config_shortAnimTime);
    }

    private void guessPosition(View view) {
        for(NBack nBack : nBacks)
            if(nBack instanceof NBackPosition)
                animateButtonColor(positionButton, nBack.guess());
        updateStateInfo();
    }

    private void guessAudio(View view) {
        for(NBack nBack : nBacks)
            if(nBack instanceof NBackAudio)
                animateButtonColor(audioButton, nBack.guess());
        updateStateInfo();
    }

    private void animateButtonColor(Button button, NBack.Guess guess) {
        if(guess == NBack.Guess.CORRECT)
            AnimationUtil.blinkViewColor(button,
                    getResources().getColor(com.google.android.material.R.color.design_default_color_primary),
                    getResources().getColor(R.color.green),
                    config_shortAnimTime);
        else if(guess == NBack.Guess.WRONG)
            AnimationUtil.blinkViewColor(button,
                    getResources().getColor(com.google.android.material.R.color.design_default_color_primary),
                    getResources().getColor(R.color.red),
                    config_shortAnimTime);
    }

    private void runEvent() {
        int randInt = 0;
        for(NBack nBack : nBacks) {
            if(nBack instanceof NBackPosition) {
                randInt = ((NBackPosition) nBack).newEvent();
                imageView[randInt].setVisibility(View.VISIBLE);
            }
            else if(nBack instanceof NBackAudio) {
                String randLetter = ((NBackAudio) nBack).newEvent();
                textToSpeechUtil.speakNow(randLetter);
            }
        }

        updateStateInfo();

        // Create two new delays to hide the stimuli and then wait for a delay before showing the next stimuli
        int finalRandInt = randInt;
        outerTimer = new CountDownTimer(500, 1) {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                imageView[finalRandInt].setVisibility(View.INVISIBLE);

                innerTimer = new CountDownTimer(nBacks.get(0).getTimeBetweenEvents(), 1) {
                    @Override
                    public void onTick(long l) { }

                    @Override
                    public void onFinish() {
                        if(nBacks.get(0).getCurrentEvent() > 0)
                            runEvent();
                        else {
                            endNBack();
                            newNBack();
                            updateStateInfo();
                        }
                    }
                }.start();
            }
        }.start();
    }

    private void updateStateInfo() {
        if(getContext() == null) return;

        titleView.setText(getString(R.string.title, getStimuliString(), nBacks.get(0).getN()));
        eventsView.setText(getString(R.string.events, nBacks.get(0).getCurrentEvent()));

        int score = 0;
        for(NBack nBack : nBacks)
            score += nBack.getScore();
        scoreView.setText(getString(R.string.score, score));
    }

    // Create a new NBack game based on the users preferences
    private void newNBack() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireView().getContext());
        int n = Integer.parseInt(sp.getString("n", "1"));
        int timeBetweenEvents = Integer.parseInt(sp.getString("events_time", "2500"));
        int numberOfEvents = Integer.parseInt(sp.getString("events_number", "20"));

        nBacks.clear();
        String stimuli = sp.getString("stimuli", "position");
        if(stimuli.contains("position"))
            nBacks.add(new NBackPosition(n, timeBetweenEvents, numberOfEvents));
        if(stimuli.contains("audio"))
            nBacks.add(new NBackAudio(n, timeBetweenEvents, numberOfEvents));
    }

    private String getStimuliString() {
        String s = "";
        if(nBacks.size() > 1)
            s = getString(R.string.dual);
        else if(nBacks.get(0).getStimuli() == NBack.Stimuli.POSITION)
            s = getString(R.string.position);
        else
            s = getString(R.string.audio);
        return s;
    }
}
