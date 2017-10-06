package cs.dawson.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        numberOfAttemptsTextView.setText(getIntent().getExtras().getString("totalAttempts"));
    }

}
