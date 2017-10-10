package cs.dawson.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {

    TextView numberOfAttemptsTextView, results1TextView, results2TextView;
    //
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        numberOfAttemptsTextView = (TextView)findViewById(R.id.numberOfAttempts);
        results1TextView = (TextView)findViewById(R.id.resultScore1);
        results2TextView = (TextView)findViewById(R.id.resultScore2);

        int numberOfAttempts = getIntent().getExtras().getInt("totalAttempts");
        numberOfAttemptsTextView.setText(" " + String.valueOf(numberOfAttempts));

        int correctAnswersOne = getIntent().getExtras().getInt("pastGameOneCorrectAnswers");
        int incorrectAnswersOne = getIntent().getExtras().getInt("pastGameOneIncorrectAnswers");
        results1TextView.setText(getResources().getString(R.string.pastGameOneString) + " " + String.valueOf(correctAnswersOne)
                + " " + getResources().getString(R.string.pastGamesExtraString) + " " + String.valueOf(incorrectAnswersOne));

        int correctAnswersTwo = getIntent().getExtras().getInt("pastGameTwoCorrectAnswers");
        int incorrectAnswersTwo = getIntent().getExtras().getInt("pastGameTwoIncorrectAnswers");
        results2TextView.setText(getResources().getString(R.string.pastGameTwoString) + " " + String.valueOf(correctAnswersTwo)
                + " " + getResources().getString(R.string.pastGamesExtraString) + " " + String.valueOf(incorrectAnswersTwo));
    }

}
