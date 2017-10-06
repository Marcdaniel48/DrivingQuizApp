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

    // String for LogCat documentation
    private final static String METHOD_TAG = "GameActivity";

    // Displays amount of questions answered out of the number of total questions
    TextView progressTextView;

    // Displays the amount of answers that the user got correct or incorrect
    TextView correctIncorrectTextView;

    // The sentence that describes one of the four images of the ImageButtons
    TextView questionSentenceTextView;

    // Displays whether the user answered correctly or incorrectly
    TextView answerResultTextView;

    // Image buttons
    ImageButton imageButton1;
    ImageButton imageButton2;
    ImageButton imageButton3;
    ImageButton imageButton4;

    // The other buttons
    Button nextButton;
    Button playAgainButton;
    Button hintButton;

    // Arrays
    ArrayList<Integer> answers = new ArrayList<Integer>();
    String[] questionsArray = new String[14];
    private int[] imagesArray;
    private int[] happyFacesArray;
    private int[] sadFacesArray;

    //Variables to control the logic of the game
    int locationOfCorrectImage;
    int correctAnswers = 0;
    int incorrectAnswers = 0;
    int questions = 0;
    int chances = 0;
    int numberOfQuestions = 0;
    int previousNumber = -1;

    //Variables to hold the id of images from resources
    int button1ImageRes;
    int button2ImageRes;
    int button3ImageRes;
    int button4ImageRes;

    //Variables to control the flow of the game
    boolean disable = false;
    boolean disableNextButton = false;
    boolean disablePlayAgainButton = true;
    String question;
    int questionPos;

    boolean answeredQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "Entered the onStart() method");

        // Questions Array from Resource folder
        questionsArray = getResources().getStringArray(R.array.questions_array);

        // Traffic Signs Images Array Id from Resource Folder
        imagesArray = new int[]
        {
                R.drawable.pic_0,   R.drawable.pic_1,   R.drawable.pic_2,
                R.drawable.pic_3,   R.drawable.pic_4,   R.drawable.pic_5,
                R.drawable.pic_6,   R.drawable.pic_7,   R.drawable.pic_8,
                R.drawable.pic_9,   R.drawable.pic_10,  R.drawable.pic_11,
                R.drawable.pic_12,  R.drawable.pic_13,  R.drawable.pic_14
        };

        // Happy Faces Images Array Id from Resource Folder
        happyFacesArray = new int[]
        {
                R.drawable.happyface_1, R.drawable.happyface_2,
                R.drawable.happyface_3, R.drawable.happyface_4
        };

        //Sad Faces Images Array Id from Resource Folder
        sadFacesArray = new int[]
        {
                R.drawable.sadface_1,   R.drawable.sadface_2,   R.drawable.sadface_3
        };

        //Main ImageButtons for the game
        imageButton1 = (ImageButton)findViewById(R.id.button0);
        imageButton2 = (ImageButton)findViewById(R.id.button1);
        imageButton3 = (ImageButton)findViewById(R.id.button2);
        imageButton4 = (ImageButton)findViewById(R.id.button3);

        //main buttons of the game
        nextButton = (Button)findViewById(R.id.nextButton);

        playAgainButton = (Button)findViewById(R.id.playAgainButton);

        hintButton = (Button)findViewById(R.id.hintButton);

        //Components to control the progress of the game

        progressTextView = (TextView)findViewById(R.id.progressTextView);
        answerResultTextView = (TextView)findViewById(R.id.resultTextView);

        questionSentenceTextView = (TextView)findViewById(R.id.questionTextView);
        correctIncorrectTextView = (TextView)findViewById(R.id.scoreTextView);

        createNextQuestion();

        //Restoring the main components of the game
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        // Shared preferences loading
        if(prefs.getInt("button1ImageRes", 0) != 0 && prefs.getInt("button1ImageRes", 0) != 0 && prefs.getInt("button1ImageRes", 0)!=0 && prefs.getInt("button1ImageRes", 0)!=0)
        {
            // Getting
            button1ImageRes = prefs.getInt("button1ImageRes", 0); // WORKS
            button2ImageRes = prefs.getInt("button2ImageRes", 0); // WORKS
            button3ImageRes = prefs.getInt("button3ImageRes", 0); // WORKS
            button4ImageRes = prefs.getInt("button4ImageRes", 0); // WORKS

            question = prefs.getString("question", null); // WORKS
            questions = prefs.getInt("questions", 0);
            questionPos = prefs.getInt("questionPos", 0);
            chances = prefs.getInt("chances", 0);
            disable = prefs.getBoolean("disable", false);
            disableNextButton = prefs.getBoolean("disableNextButton", false);
            disablePlayAgainButton = prefs.getBoolean("disablePlayAgainButton", false);
            correctAnswers = prefs.getInt("correctAnswers", 0); // WORKS
            incorrectAnswers = prefs.getInt("incorrectAnswers", 0); // WORKS

            answeredQuestion = prefs.getBoolean("answeredQuestion", false);

            // Setting
            imageButton1.setImageResource(button1ImageRes);
            imageButton2.setImageResource(button2ImageRes);
            imageButton3.setImageResource(button3ImageRes);
            imageButton4.setImageResource(button4ImageRes);
            questionSentenceTextView.setText(question);
            progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));
            correctIncorrectTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers)); // WORKS



            if(disableNextButton)
                nextButton.setClickable(false);
            else
                nextButton.setClickable(true);

            if(disablePlayAgainButton)
                playAgainButton.setClickable(false);
            else playAgainButton.setClickable(true);

            if(disable)
                disableButtons();
            else
                enableButtons();
        }


        if(savedInstanceState != null )
        {
            if
            (
                savedInstanceState.getInt("button1ImageRes")!= 0 && savedInstanceState.getInt("button2ImageRes")!= 0 &&
                savedInstanceState.getInt("button3ImageRes")!= 0 && savedInstanceState.getInt("button4ImageRes")!= 0
            )
            {
                button1ImageRes = savedInstanceState.getInt("button1ImageRes");
                button2ImageRes = savedInstanceState.getInt("button2ImageRes");
                button3ImageRes = savedInstanceState.getInt("button3ImageRes");
                button4ImageRes = savedInstanceState.getInt("button4ImageRes");
            }

            if(savedInstanceState.getString("question") != null)
            {
                question = savedInstanceState.getString("question");
            }

            if(savedInstanceState.getInt("questions") != 0)
            {
                questions = savedInstanceState.getInt("questions");
            }

            if(savedInstanceState.getInt("questionPos") != 0)
                questionPos = savedInstanceState.getInt("questionPos");

            chances = savedInstanceState.getInt("chances");

            disable = savedInstanceState.getBoolean("disable",false);
            disableNextButton = savedInstanceState.getBoolean("disableNextButton", true);
            disablePlayAgainButton = savedInstanceState.getBoolean("disablePlayAgainButton",true);

            if(savedInstanceState.getInt("correctAnswers") != 0 && savedInstanceState.getInt("incorrectAnswers") != 0)
            {
                correctAnswers = savedInstanceState.getInt("correctAnswers");
                incorrectAnswers = savedInstanceState.getInt("incorrectAnswers");
            }

            imageButton1.setImageResource(button1ImageRes);
            imageButton2.setImageResource(button2ImageRes);
            imageButton3.setImageResource(button3ImageRes);
            imageButton4.setImageResource(button4ImageRes);

            questionSentenceTextView.setText(question);

            correctIncorrectTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers));
            progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));

            if(disable)
                disableButtons();
            else
                enableButtons();

            if(disableNextButton)
                nextButton.setClickable(false);
            else
                nextButton.setClickable(true);

            if(disablePlayAgainButton)
                playAgainButton.setClickable(false);
            else
                playAgainButton.setClickable(true);
        }

        Log.d("DISABLE_IMAGE_BUTTONS", String.valueOf(disable));
        Log.d("DISABLE_NEXT_BUTTON", String.valueOf(disableNextButton));
    }

    /**
     * The nextButton method invokes the createNextQuestion.
     * @param view
     */
    public void nextButton(View view)
    {
        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "nextButton method Invoked");
        createNextQuestion();
    }

    /**
     * The PlayAgain method resets the components of the Game.
     * @param view
     */
    public void playAgain(View view){
        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "playAgain method() invoked");

        incorrectAnswers = 0;
        correctAnswers = 0;
        questions = 0;
        chances = 0;
        numberOfQuestions = 0;
        progressTextView.setText("0/0");
        correctIncorrectTextView.setText("");

        disablePlayAgainButton = true;
        playAgainButton.setClickable(false);


        nextButton.setClickable(false);
        disableNextButton = true;

        disable = false;
        disableButtons();

        //progressTextView.setBackground(null);
        progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));
        correctIncorrectTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers));

        createNextQuestion();
    }

    /**
     * The createNextQuestion method generates a random question
     * with its equivalent image and three other random images
     * and sets the images on the screen.
     */
    public void createNextQuestion() {

        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "createNextQuestion() method Invoked");

        progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));

        correctIncorrectTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers));

        chances = 0;

        disableNextButton = true;
        nextButton.setClickable(false);

        disablePlayAgainButton = true;
        playAgainButton.setClickable(false);

        answerResultTextView.setText("");

        enableButtons();

        int incorrectAnswer;

        previousNumber = -1;

        answers.clear();

        //Create a random object
        Random rand = new Random();

        questionPos = rand.nextInt(14);

        question = questionsArray[questionPos];
        questionSentenceTextView.setText(question);

        locationOfCorrectImage = rand.nextInt(4);

        for (int i=0; i<4; i++)
        {
            if (i == locationOfCorrectImage)
            {
                answers.add(imagesArray[questionPos]);
            }
            else
            {
                incorrectAnswer = rand.nextInt(14);
                while (incorrectAnswer == questionPos || incorrectAnswer == previousNumber)
                {
                    incorrectAnswer = rand.nextInt(14);
                }
                previousNumber = incorrectAnswer;
                answers.add(imagesArray[incorrectAnswer]);
            }
        }

        //holds the image id from the resource folder.
        button1ImageRes = answers.get(0);
        button2ImageRes = answers.get(1);
        button3ImageRes = answers.get(2);
        button4ImageRes = answers.get(3);

        imageButton1.setImageResource(button1ImageRes);
        imageButton2.setImageResource(button2ImageRes);
        imageButton3.setImageResource(button3ImageRes);
        imageButton4.setImageResource(button4ImageRes);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "onSaveInstanceState() method Invoked");

        outState.putInt("button1ImageRes",button1ImageRes);
        outState.putInt("button2ImageRes",button2ImageRes);
        outState.putInt("button3ImageRes",button3ImageRes);
        outState.putInt("button4ImageRes",button4ImageRes);

        outState.putInt("questionPos", questionPos);
        outState.putString("question", question);
        outState.putInt("questions", questions);
        outState.putBoolean("disable", disable);
        outState.putBoolean("disableNextButton", disableNextButton);
        outState.putBoolean("disablePlayAgainButton", disablePlayAgainButton);
        outState.putInt("chances", chances);
        outState.putInt("correctAnswers", correctAnswers);
        outState.putInt("incorrectAnswers", incorrectAnswers);

        outState.putBoolean("answeredQuestion", answeredQuestion);
    }

    /**
     * The disableButtons method disable
     * the main ImageButtons on the screen and
     * also disable the playAgain Button.
     */
    public void disableButtons(){
        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "disableButtons() method Invoked");

        imageButton1.setClickable(false);
        imageButton2.setClickable(false);
        imageButton3.setClickable(false);
        imageButton4.setClickable(false);
    }

    /**
     * The enableButtons method enable the
     * the main Image Buttons of the Game
     */
    public void enableButtons(){

        imageButton1.setClickable(true);
        imageButton2.setClickable(true);
        imageButton3.setClickable(true);
        imageButton4.setClickable(true);
    }

    /**
     * The chooseAnswer method checks which Image Button
     * was clicked by the user and sets the appropriate
     * image on screen along with the updated score and
     * progress of questions.
     * @param view
     */
    public void chooseAnswer(View view) {
        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "chooseAnswer() method Invoked");

        Random rand = new Random();
        //condition used when the user clicks in the correct image
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectImage)))
        {
            correctAnswers++;

            answerResultTextView.setTextColor(Color.GREEN);
            answerResultTextView.setText(R.string.correct);

            int happyFace = rand.nextInt(3);

            switch (view.getTag().toString())
            {
                case "0":
                    button1ImageRes = happyFacesArray[happyFace];
                    imageButton1.setImageResource(button1ImageRes);
                    break;
                case "1":
                    button2ImageRes = happyFacesArray[happyFace];
                    imageButton2.setImageResource(button2ImageRes);
                    break;
                case "2":
                    button3ImageRes = happyFacesArray[happyFace];
                    imageButton3.setImageResource(button3ImageRes);
                    break;
                case "3":
                    button4ImageRes = happyFacesArray[happyFace];
                    imageButton4.setImageResource(button4ImageRes);
                    break;
                default:
                    break;
            }

            disable = true;
            disableButtons();

            questions++;
            correctIncorrectTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers));
            progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));

            if(questions >= 4)
            {
                answerResultTextView.setText(R.string.correct_finish);

                nextButton.setClickable(false);
                disableNextButton = true;

                disableButtons();

                playAgainButton.setClickable(true);
                disablePlayAgainButton = false;

            }
            else
            {
                nextButton.setClickable(true);
                disableNextButton = false;
            }


        }
        else
        {
            chances++;
            int sadFace = rand.nextInt(2);
            switch (view.getTag().toString())
            {
                case "0":
                    button1ImageRes = sadFacesArray[sadFace];
                    imageButton1.setImageResource(button1ImageRes);
                    imageButton1.setClickable(false);
                    break;
                case "1":
                    button2ImageRes = sadFacesArray[sadFace];
                    imageButton2.setImageResource(button2ImageRes);
                    imageButton2.setClickable(false);
                    break;
                case "2":
                    button3ImageRes = sadFacesArray[sadFace];
                    imageButton3.setImageResource(button3ImageRes);
                    imageButton3.setClickable(false);
                    break;
                case "3":
                    button4ImageRes = sadFacesArray[sadFace];
                    imageButton4.setImageResource(button4ImageRes);
                    imageButton4.setClickable(false);
                    break;
                default:
                    break;
            }
            if(chances > 1)
            {
                incorrectAnswers++;
                answerResultTextView.setTextColor(Color.RED);
                answerResultTextView.setText(R.string.wrong);

                disable = true;
                disableButtons();

                playAgainButton.setClickable(false);
                disablePlayAgainButton = true;

                disableNextButton = false;
                nextButton.setClickable(true);
                
                questions++;
                correctIncorrectTextView.setText("Correct: " + Integer.toString(correctAnswers) + " Incorrect: " + Integer.toString(incorrectAnswers));
                progressTextView.setText("Questions:" + Integer.toString(questions) + "/" + Integer.toString(4));
            }
            else
            {
                answerResultTextView.setTextColor(Color.RED);
                answerResultTextView.setText(R.string.wrong_tryagain);
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
        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "showHint() method Invoked");

        String query = questionSentenceTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        startActivity(intent);
    }

    public void onPause()
    {
        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "onPause() method Invoked");
        super.onPause();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Current game session
        editor.putInt("button1ImageRes", button1ImageRes);
        editor.putInt("button2ImageRes", button2ImageRes);
        editor.putInt("button3ImageRes", button3ImageRes);
        editor.putInt("button4ImageRes", button4ImageRes);
        editor.putString("question", question);
        editor.putInt("questions", questions);
        editor.putInt("questionsPos", questionPos);
        editor.putInt("chances", chances);
        editor.putBoolean("disable", disable);
        editor.putBoolean("disableNextButton", disableNextButton);
        editor.putBoolean("disablePlayAgainButton", disablePlayAgainButton);
        editor.putInt("correctAnswers", correctAnswers);
        editor.putInt("incorrectAnswers", incorrectAnswers);

        editor.putBoolean("answeredQuestion", answeredQuestion);


        editor.commit();
    }
}