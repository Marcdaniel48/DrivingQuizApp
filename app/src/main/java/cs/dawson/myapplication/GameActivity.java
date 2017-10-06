package cs.dawson.myapplication;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView progressTextView;
    TextView scoreTextView;
    TextView questionTextView;
    TextView hintTextView;
    TextView resultTextView;

    Button startButton;
    ImageButton button0;
    ImageButton button1;
    ImageButton button2;
    ImageButton button3;
    Button nextButton;
    Button playAgainButton;
    Button hintButton;


    ArrayList<Integer> answers = new ArrayList<Integer>();
    String[] questionsArray = new String[14];
    private int[] images;
    private int[] happyFaces;
    private int[] sadFaces;
    int locationOfCorrectImage;
    int score = 0;
    int correctAnswers = 0;
    int incorrectAnswers = 0;
    int questions = 0;
    int chances = 0;
    int numberOfQuestions = 0;
    int previousNumber = -1;

    //Variables to hold the id of images from resources
    int button_0;
    int button_1;
    int button_2;
    int button_3;

    //Variables to control the flow of the game
    boolean disable = false;
    boolean disableNextButton = false;
    boolean disablePlayAgainButton = true;
    String question;
    int questionPos;
    String hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Questions Array from Resource folder
        questionsArray = getResources().getStringArray(R.array.questions_array);

        //Traffic Signs Images Array Id from Resource Folder
        images = new int[]{R.drawable.pic_0, R.drawable.pic_1, R.drawable.pic_2,
                R.drawable.pic_3, R.drawable.pic_4, R.drawable.pic_5,
                R.drawable.pic_6, R.drawable.pic_7, R.drawable.pic_8,
                R.drawable.pic_9, R.drawable.pic_10, R.drawable.pic_11,
                R.drawable.pic_12, R.drawable.pic_13, R.drawable.pic_14};

        //Happy Faces Images Array Id from Resource Folder
        happyFaces = new int[]{R.drawable.happyface_1,R.drawable.happyface_2,
                R.drawable.happyface_3,R.drawable.happyface_4};

        //Sad Faces Images Array Id from Resource Folder
        sadFaces = new int[]{R.drawable.sadface_1,R.drawable.sadface_2, R.drawable.sadface_3};

        //Main ImageButtons for the game
        button0 = (ImageButton)findViewById(R.id.button0);
        button1 = (ImageButton)findViewById(R.id.button1);
        button2 = (ImageButton)findViewById(R.id.button2);
        button3 = (ImageButton)findViewById(R.id.button3);

        //main buttons of the game
        nextButton = (Button)findViewById(R.id.nextButton);
        playAgainButton = (Button)findViewById(R.id.playAgainButton);
        hintButton = (Button)findViewById(R.id.hintButton);

        //Components to control the progress of the game
        progressTextView = (TextView)findViewById(R.id.progressTextView);
        resultTextView = (TextView)findViewById(R.id.resultTextView);
        questionTextView = (TextView)findViewById(R.id.questionTextView);
        hintTextView = (TextView)findViewById(R.id.hintTextView);
        scoreTextView = (TextView)findViewById(R.id.scoreTextView);

        createNextQuestion();

        //Restoring the main components of the game

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        if(prefs.getInt("button_0", 0) != 0 && prefs.getInt("button_0", 0) != 0 && prefs.getInt("button_0", 0)!=0 && prefs.getInt("button_0", 0)!=0)
        {
            button_0 = prefs.getInt("button_0", 0); // WORKS
            button_1 = prefs.getInt("button_1", 0); // WORKS
            button_2 = prefs.getInt("button_2", 0); // WORKS
            button_3 = prefs.getInt("button_3", 0); // WORKS
            question = prefs.getString("question", null); // WORKS
            questions = prefs.getInt("questions", 0);
            questionPos = prefs.getInt("questionPos", 0);
            chances = prefs.getInt("chances", 0);
            disable = prefs.getBoolean("disable", false);
            disableNextButton = prefs.getBoolean("disableNextButton", false);
            disablePlayAgainButton = prefs.getBoolean("disablePlayAgainButton", false);
            score = prefs.getInt("score", 0);
            correctAnswers = prefs.getInt("correctAnswers", 0); // WORKS
            incorrectAnswers = prefs.getInt("incorrectAnswers", 0); // WORKS


            button0.setImageResource(button_0);
            button1.setImageResource(button_1);
            button2.setImageResource(button_2);
            button3.setImageResource(button_3);
            questionTextView.setText(question);
            scoreTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers)); // WORKS
            progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));
            nextButton.setClickable(true);
            playAgainButton.setClickable(true);
            if(disable){
                disableButtons();
            }
            if(disableNextButton){
                nextButton.setClickable(false);
            }
            if(disablePlayAgainButton){
                playAgainButton.setClickable(false);
            }
        }
        if(savedInstanceState != null ){
            // restore my counter
            if(savedInstanceState.getInt("button_0")!=0 && savedInstanceState.getInt("button_1")!=0
                    && savedInstanceState.getInt("button_2")!=0 && savedInstanceState.getInt("button_3")!=0) {
                button_0 = savedInstanceState.getInt("button_0");
                button_1 = savedInstanceState.getInt("button_1");
                button_2 = savedInstanceState.getInt("button_2");
                button_3 = savedInstanceState.getInt("button_3");
            }
            if(savedInstanceState.getString("question") != null) {
                question = savedInstanceState.getString("question");
            }
            if(savedInstanceState.getInt("questions") != 0)
            {
                questions = savedInstanceState.getInt("questions");
            }
            questionPos = savedInstanceState.getInt("questionPos");
            chances = savedInstanceState.getInt("chances");
            disable = savedInstanceState.getBoolean("disable",false);
            disableNextButton = savedInstanceState.getBoolean("disableNextButton", true);
            disablePlayAgainButton = savedInstanceState.getBoolean("disablePlayAgainButton",true);
            if(savedInstanceState.getInt("correctAnswers") != 0 && savedInstanceState.getInt("incorrectAnswers") != 0) {
                correctAnswers = savedInstanceState.getInt("correctAnswers");
                incorrectAnswers = savedInstanceState.getInt("incorrectAnswers");
            }

            button0.setImageResource(button_0);
            button1.setImageResource(button_1);
            button2.setImageResource(button_2);
            button3.setImageResource(button_3);
            questionTextView.setText(question);
            scoreTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers));
            progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));

            if(disable){
                disableButtons();
            }
            if(disableNextButton){
                nextButton.setClickable(false);
            }
            if(disablePlayAgainButton){
                playAgainButton.setClickable(false);
            }
        }

        if(questions >= 4)
        {
            playAgainButton.setClickable(true);
        }
    }

    /**
     * The nextButton method invokes the createNextQuestion.
     * @param view
     */
    public void nextButton(View view) {
        createNextQuestion();
    }

    /**
     * The PlayAgain method resets the components of the Game.
     * @param view
     */
    public void playAgain(View view){
        score = 0;
        incorrectAnswers = 0;
        correctAnswers = 0;
        questions = 0;
        chances = 0;
        numberOfQuestions = 0;
        progressTextView.setText("0/0");
        scoreTextView.setText("");
        playAgainButton.setClickable(false);
        disablePlayAgainButton = true;
        nextButton.setClickable(false);
        disableNextButton = true;
        progressTextView.setBackground(null);
        progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));
        scoreTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers));
        createNextQuestion();
    }

    /**
     * The createNextQuestion method generates a random question
     * with its equivalent image and three other random images
     * and sets the images on the screen.
     */
    public void createNextQuestion() {
        scoreTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers));
        progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));
        hintButton.setText(R.string.hint);
        chances = 0;
        int incorrectAnswer;
        previousNumber = -1;
        answers.clear();
        nextButton.setClickable(false);
        disableNextButton = true;
        resultTextView.setText("");

        //Create a random object
        Random rand = new Random();

        questionPos = rand.nextInt(14);

        question = questionsArray[questionPos];
        questionTextView.setText(question);

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
        enableButtons();
        //holds the image id from the resource folder.
        button_0 = answers.get(0);
        button_1 = answers.get(1);
        button_2 = answers.get(2);
        button_3 = answers.get(3);

        button0.setImageResource(button_0);
        button1.setImageResource(button_1);
        button2.setImageResource(button_2);
        button3.setImageResource(button_3);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("button_0",button_0);
        outState.putInt("button_1",button_1);
        outState.putInt("button_2",button_2);
        outState.putInt("button_3",button_3);
        outState.putInt("questionPos", questionPos);
        outState.putString("question", question);
        outState.putString("hint", hint);
        outState.putInt("questions", questions);
        outState.putBoolean("disable", disable);
        outState.putBoolean("disableNextButton", disableNextButton);
        outState.putBoolean("disablePlayAgainButton", disablePlayAgainButton);
        outState.putInt("chances", chances);
        outState.putInt("score", score);
        outState.putInt("correctAnswers", correctAnswers);
        outState.putInt("incorrectAnswers", incorrectAnswers);


    }

    /**
     * The disableButtons method disable
     * the main ImageButtons on the screen and
     * also disable the playAgain Button.
     */
    public void disableButtons(){

        button0.setClickable(false);
        button1.setClickable(false);
        button2.setClickable(false);
        button3.setClickable(false);
        playAgainButton.setClickable(false);
        disablePlayAgainButton = true;

    }

    /**
     * The enableButtons method enable the
     * the main Image Buttons of the Game
     */
    public void enableButtons(){

        button0.setClickable(true);
        button1.setClickable(true);
        button2.setClickable(true);
        button3.setClickable(true);
    }

    /**
     * The chooseAnswer method checks which Image Button
     * was clicked by the user and sets the appropriate
     * image on screen along with the updated score and
     * progress of questions.
     * @param view
     */
    public void chooseAnswer(View view) {
        Random rand = new Random();
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectImage))) {
            score++;
            correctAnswers++;
            resultTextView.setTextColor(Color.GREEN);
            resultTextView.setText(R.string.correct);
            int happyFace = rand.nextInt(3);
            switch (view.getTag().toString()){
                case "0":
                    button_0 = happyFaces[happyFace];
                    button0.setImageResource(button_0);
                    break;
                case "1":
                    button_1 = happyFaces[happyFace];
                    button1.setImageResource(button_1);
                    break;
                case "2":
                    button_2 = happyFaces[happyFace];
                    button2.setImageResource(button_2);
                    break;
                case "3":
                    button_3 = happyFaces[happyFace];
                    button3.setImageResource(button_3);
                    break;
                default:
                    break;
            }
            disable = true;
            disableButtons();
            questions++;
            scoreTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers));
            progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));
            if(questions >= 4){
                resultTextView.setText(R.string.correct_finish);
                nextButton.setClickable(false);
                disableNextButton = true;
                disableButtons();
                playAgainButton.setClickable(true);
                disablePlayAgainButton = false;
            }else{
                nextButton.setClickable(true);
                disableNextButton = false;
            }


        } else {
            chances++;
            int sadFace = rand.nextInt(2);
            switch (view.getTag().toString()){
                case "0":
                    button_0 = sadFaces[sadFace];
                    button0.setImageResource(button_0);
                    button0.setClickable(false);
                    break;
                case "1":
                    button_1 = sadFaces[sadFace];
                    button1.setImageResource(button_1);
                    button1.setClickable(false);
                    break;
                case "2":
                    button_2 = sadFaces[sadFace];
                    button2.setImageResource(button_2);
                    button2.setClickable(false);
                    break;
                case "3":
                    button_3 = sadFaces[sadFace];
                    button3.setImageResource(button_3);
                    button3.setClickable(false);
                    break;
                default:
                    break;
            }
            if(chances > 1){
                incorrectAnswers++;
                resultTextView.setTextColor(Color.RED);
                resultTextView.setText(R.string.wrong);
                disable = true;
                disableButtons();
                playAgainButton.setClickable(false);
                disablePlayAgainButton = true;
                questions++;
                scoreTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers));
                progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));
                if(questions == 4){
                    resultTextView.setText(R.string.wrong_finished);
                    nextButton.setClickable(false);
                    disableNextButton = true;
                    disableButtons();
                    playAgainButton.setClickable(true);
                    disablePlayAgainButton = false;
                }else{
                    nextButton.setClickable(true);
                    disableNextButton = false;
                }
            }else{
                resultTextView.setTextColor(Color.RED);
                resultTextView.setText(R.string.wrong_tryagain);
                nextButton.setClickable(false);
                playAgainButton.setClickable(false);
                disablePlayAgainButton = true;
                disableNextButton = true;
                disable = false;
            }
        }
    }

    public void showHint(View v)
    {
        String query = questionTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        startActivity(intent);
    }

    public void onPause()
    {
        super.onPause();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Current game session
        editor.putInt("button_0", button_0);
        editor.putInt("button_1", button_1);
        editor.putInt("button_2", button_2);
        editor.putInt("button_3", button_3);
        editor.putString("question", question);
        editor.putInt("questions", questions);
        editor.putInt("questionsPos", questionPos);
        editor.putInt("chances", chances);
        editor.putBoolean("disable", disable);
        editor.putBoolean("disableNextButton", disableNextButton);
        editor.putBoolean("disablePlayAgainButton", disablePlayAgainButton);
        editor.putInt("correctAnswers", correctAnswers);
        editor.putInt("incorrectAnswers", incorrectAnswers);


        editor.commit();
    }
}