package cs.dawson.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Marc-Daniel on 2017-09-29.
 */

public class AboutActivity extends Activity {

    TextView numberOfAttemptsTextView, results1TextView, results2TextView;

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
        results1TextView.setText("Previous past score -> Correct: " + String.valueOf(correctAnswersOne) + ", Incorrect: " + String.valueOf(incorrectAnswersOne));

        int correctAnswersTwo = getIntent().getExtras().getInt("pastGameTwoCorrectAnswers");
        int incorrectAnswersTwo = getIntent().getExtras().getInt("pastGameTwoIncorrectAnswers");
        results2TextView.setText("Most recent past score -> Correct: " + String.valueOf(correctAnswersTwo) + ", Incorrect: " + String.valueOf(incorrectAnswersTwo));
    }

}
