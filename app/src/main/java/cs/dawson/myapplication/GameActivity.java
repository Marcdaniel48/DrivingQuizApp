package cs.dawson.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    Button startButton;
    TextView progressTextView;
    TextView scoreTextView;
    TextView questionTextView;
    //Added by Marc
    TextView hintTextView;
    TextView resultTextView;

    ImageButton button0;
    ImageButton button1;
    ImageButton button2;
    ImageButton button3;
    Button nextButton;

    Button playAgainButton;
    RelativeLayout gameRelativeLayout;

    ArrayList<Integer> answers = new ArrayList<Integer>();
    String[] questionsArray = new String[14];
    //Added by Marc
    String[] hintsArray = new String[14];
    private int[] images;
    private int[] happyFaces;
    int locationOfCorrectImage;
    int score = 0;
    int questions = 0;
    int chances = 0;
    int numberOfQuestions = 0;
    int previousNumber = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Questions Array from Resource folder
        questionsArray = getResources().getStringArray(R.array.questions_array);
        // Added by Marc
        hintsArray = getResources().getStringArray(R.array.hint_array);

        //Traffic Signs Images Array Id from Resource Folder
        images = new int[]{R.drawable.pic_0, R.drawable.pic_1, R.drawable.pic_2,
                R.drawable.pic_3, R.drawable.pic_4, R.drawable.pic_5,
                R.drawable.pic_6, R.drawable.pic_7, R.drawable.pic_8,
                R.drawable.pic_9, R.drawable.pic_10, R.drawable.pic_11,
                R.drawable.pic_12, R.drawable.pic_13, R.drawable.pic_14};

        //Happy Faces Images Array Id from Resource Folder
        happyFaces = new int[]{R.drawable.happyface_1,R.drawable.happyface_2,
                R.drawable.happyface_3,R.drawable.happyface_4,};

        button0 = (ImageButton)findViewById(R.id.button0);
        button1 = (ImageButton)findViewById(R.id.button1);
        button2 = (ImageButton)findViewById(R.id.button2);
        button3 = (ImageButton)findViewById(R.id.button3);
        nextButton = (Button)findViewById(R.id.nextButton);
        progressTextView = (TextView)findViewById(R.id.progressTextView);
        resultTextView = (TextView)findViewById(R.id.resultTextView);
        questionTextView = (TextView)findViewById(R.id.questionTextView);
        //Added by Marc
        hintTextView = (TextView)findViewById(R.id.hintTextView);
        scoreTextView = (TextView)findViewById(R.id.scoreTextView);
        playAgainButton = (Button)findViewById(R.id.playAgainButton);
        //gameRelativeLayout = (RelativeLayout)findViewById(R.id.gameRelativeLayout);

        createNextQuestion();

    }
    public void playAgain(View view) {

        playAgain();

    }
    public void playAgain() {

        score = 0;
        questions = 0;
        chances = 0;
        numberOfQuestions = 0;
        progressTextView.setText("0/0");
        scoreTextView.setText("");
        playAgainButton.setClickable(false);
        nextButton.setClickable(false);
        progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));
        scoreTextView.setText("Your score: " + Integer.toString(score));
        createNextQuestion();

    }
    public void createNextQuestion() {
        chances = 0;
        int incorrectAnswer;
        previousNumber = -1;
        answers.clear();
        nextButton.setClickable(false);
        resultTextView.setText("");

        //Create a random object
        Random rand = new Random();

        int questionPos = rand.nextInt(14);

        String question = questionsArray[questionPos];
        questionTextView.setText(question);
        // Added by Marc
        hintTextView.setText(hintsArray[questionPos]);
        hintTextView.setVisibility(View.INVISIBLE);

        locationOfCorrectImage = rand.nextInt(4);

        for (int i=0; i<4; i++) {

            if (i == locationOfCorrectImage) {
                answers.add(images[questionPos]);
            } else {
                incorrectAnswer = rand.nextInt(14);
                while (incorrectAnswer == questionPos || incorrectAnswer == previousNumber){
                    incorrectAnswer = rand.nextInt(14);
                }
                previousNumber = incorrectAnswer;
                answers.add(images[incorrectAnswer]);
            }
        }
        unableButtons();
        button0.setImageResource(answers.get(0));
        button1.setImageResource(answers.get(1));
        button2.setImageResource(answers.get(2));
        button3.setImageResource(answers.get(3));
    }
    public void nextQuestion(View view){
        createNextQuestion();
    }
    public void changeFaces(){

        button0.setImageResource(happyFaces[0]);
        button1.setImageResource(happyFaces[1]);
        button2.setImageResource(happyFaces[2]);
        button3.setImageResource(happyFaces[3]);
    }
    public void disableButtons(){

        button0.setClickable(false);
        button1.setClickable(false);
        button2.setClickable(false);
        button3.setClickable(false);
    }
    public void unableButtons(){

        button0.setClickable(true);
        button1.setClickable(true);
        button2.setClickable(true);
        button3.setClickable(true);
    }

    public void chooseAnswer(View view) {

        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectImage))) {
            score++;
            questions++;
            resultTextView.setText("Correct!");
            //resultTextView.setBackgroundResource(R.color.colorGreen);
            resultTextView.setTextColor(Color.GREEN);
            changeFaces();
            disableButtons();
            scoreTextView.setText("Score:" + Integer.toString(score));
            progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));
            if(questions == 4){
                resultTextView.setText("Correct!Test Finished!");
            }else{
                nextButton.setClickable(true);
            }


        } else {
            chances++;
            resultTextView.setText("Wrong! Try One More Time!");
            resultTextView.setBackgroundResource(R.color.colorRed);
            if(chances > 1){
                resultTextView.setText("Wrong!");
                disableButtons();
                progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));
                if(questions == 4){
                    resultTextView.setText("Wrong!Test Finished!");
                }else{
                    nextButton.setClickable(true);
                }
            }else{
                nextButton.setClickable(false);
            }
        }
    }

    public void start(View view) {
        super.onStart();
        playAgain();
    }

    public void showHint(View v)
    {
        hintTextView.setVisibility(View.VISIBLE);
    }
}