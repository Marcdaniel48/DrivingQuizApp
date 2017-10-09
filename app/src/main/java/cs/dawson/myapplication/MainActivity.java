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
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // String for LogCat documentation
    private final static String METHOD_TAG = "MainActivity";

    // Displays amount of questions answered out of the number of total questions
    TextView progressTextView;
    // Displays the amount of answers that the user got correct or incorrect
    TextView correctIncorrectTextView;
    // The sentence that describes one of the four images of the ImageButtons
    TextView questionSentenceTextView;
    // Displays whether the user answered correctly or incorrectly
    TextView answerResultTextView;

    // Image buttons
    ImageButton imageButton1, imageButton2, imageButton3, imageButton4;

    // The other buttons
    Button nextButton, playAgainButton, hintButton;

    // Arrays
    ArrayList<Integer> answers = new ArrayList<Integer>();
    String[] questionsArray = new String[14];
    private int[] imagesArray;
    // Display a happy face on correct answer
    private int[] happyFacesArray;
    // Display a sad face on incorrect answer
    private int[] sadFacesArray;

    // A List that will be used to remember the questions that have already been asked.
    List<String> questionsAsked = new ArrayList<String>();

    //Variables to control the logic of the game
    int locationOfCorrectImage;
    int correctAnswers = 0;
    int incorrectAnswers = 0;
    int questions = 0;
    int chances = 0;
    int numberOfQuestions = 0;
    // This int represents an index that will be used to set the images of the image buttons
    // that are not the correct answer button.
    int incorrectAnswer = -1;

    //Variables to hold the id of images from resources
    int button1ImageRes;
    int button2ImageRes;
    int button3ImageRes;
    int button4ImageRes;

    //Variables to control the flow of the game
    boolean disableImageButtons = false;
    boolean disableNextButton = false;
    boolean disablePlayAgainButton = true;
    String question;
    String answerResult;
    int questionPos;


    // Data that's going to be saved using SharedPreferences and that will appear in the 'about' activity
    int totalAttempts = 0;
    int pastGameOneCorrectAnswers = 0;
    int pastGameOneIncorrectAnswers = 0;
    int pastGameTwoCorrectAnswers = 0;
    int pastGameTwoIncorrectAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //Components that tell the user the state of the game
        progressTextView = (TextView)findViewById(R.id.progressTextView);
        answerResultTextView = (TextView)findViewById(R.id.resultTextView);
        questionSentenceTextView = (TextView)findViewById(R.id.questionTextView);
        correctIncorrectTextView = (TextView)findViewById(R.id.scoreTextView);

        Log.d("DISABLE_B4_CREATENEXT", String.valueOf(disableImageButtons));
        // Generate a question
        createNextQuestion();
        Log.d("DISABLE_AFTER_CREATE", String.valueOf(disableImageButtons));

        // Everything under here is SharedPreferences and savedInstanceState getting and loading

        // Shared preferences loading
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        // Checks to see if all of the buttons are not 0 (null). If they aren't, then start retrieving saved SharedPreferences data.
        if(prefs.getInt("button1ImageRes", 0) != 0 && prefs.getInt("button1ImageRes", 0) != 0 && prefs.getInt("button1ImageRes", 0)!=0 && prefs.getInt("button1ImageRes", 0)!=0)
        {
            // Getting
            button1ImageRes = prefs.getInt("button1ImageRes", 0);
            button2ImageRes = prefs.getInt("button2ImageRes", 0);
            button3ImageRes = prefs.getInt("button3ImageRes", 0);
            button4ImageRes = prefs.getInt("button4ImageRes", 0);

            answerResult = prefs.getString("answerResult", null);
            question = prefs.getString("question", null);
            questions = prefs.getInt("questions", 0);
            questionPos = prefs.getInt("questionPos", 0);
            chances = prefs.getInt("chances", 0);
            disableImageButtons = prefs.getBoolean("disableImageButtons", false);
            disableNextButton = prefs.getBoolean("disableNextButton", false);
            disablePlayAgainButton = prefs.getBoolean("disablePlayAgainButton", false);
            correctAnswers = prefs.getInt("correctAnswers", 0);
            incorrectAnswers = prefs.getInt("incorrectAnswers", 0);

            // Setting
            answerResultTextView.setText(answerResult);
            imageButton1.setImageResource(button1ImageRes);
            imageButton2.setImageResource(button2ImageRes);
            imageButton3.setImageResource(button3ImageRes);
            imageButton4.setImageResource(button4ImageRes);
            questionSentenceTextView.setText(question);
            correctIncorrectTextView.setText(getResources().getString(R.string.correct) + " " + Integer.toString(correctAnswers) + " " + getResources().getString(R.string.wrong)
                    + " " + Integer.toString(incorrectAnswers));
            progressTextView.setText(getResources().getString(R.string.questions) + " " + Integer.toString(questions) + "/" + Integer.toString(4));

            if(disableNextButton)
                nextButton.setEnabled(false);
            else
                nextButton.setEnabled(true);

            Log.d("DISABLE_IMGBTN_SHARED", String.valueOf(disableImageButtons));

            if(disablePlayAgainButton)
                playAgainButton.setEnabled(false);
            else playAgainButton.setEnabled(true);

            if(disableImageButtons)
                disableButtons();
            else
                enableButtons();

            totalAttempts = prefs.getInt("totalAttempts", 0);
            pastGameOneCorrectAnswers = prefs.getInt("pastGameOneCorrectAnswers", 0);
            pastGameOneIncorrectAnswers = prefs.getInt("pastGameOneIncorrectAnswers", 0);
            pastGameTwoCorrectAnswers = prefs.getInt("pastGameTwoCorrectAnswers", 0);
            pastGameTwoIncorrectAnswers = prefs.getInt("pastGameTwoIncorrectAnswers", 0);
        }

        // Bundle saved instance state loading
        // Checks if savedInstanceState is null, then more ifs that check if certain elements are not null or 0.
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

            disableImageButtons = savedInstanceState.getBoolean("disableImageButtons",false);
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

            correctIncorrectTextView.setText(getResources().getString(R.string.correct) + " " + Integer.toString(correctAnswers) + " " + getResources().getString(R.string.wrong)
                    + " " + Integer.toString(incorrectAnswers));
            progressTextView.setText(getResources().getString(R.string.questions) + " " + Integer.toString(questions) + "/" + Integer.toString(4));

            if(disableImageButtons)
                disableButtons();
            else
                enableButtons();

            Log.d("DISABLE_IMGBTN_INSTANCE", String.valueOf(disableImageButtons));

            if(disableNextButton)
                nextButton.setEnabled(false);
            else
                nextButton.setEnabled(true);

            if(disablePlayAgainButton)
                playAgainButton.setEnabled(false);
            else
                playAgainButton.setEnabled(true);
        }

        Log.d("DISABLE_IMAGE_BUTTONS", String.valueOf(disableImageButtons));
        Log.d("DISABLE_NEXT_BUTTON", String.valueOf(disableNextButton));
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

        disableImageButtons = false;
        disableButtons();

        //progressTextView.setBackground(null);
        correctIncorrectTextView.setText(getResources().getString(R.string.correct) + " " + Integer.toString(correctAnswers) + " " + getResources().getString(R.string.wrong)
                + " " + Integer.toString(incorrectAnswers));
        progressTextView.setText(getResources().getString(R.string.questions) + " " + Integer.toString(questions) + "/" + Integer.toString(4));

        questionsAsked.clear();

        createNextQuestion();
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
     * The createNextQuestion method generates a random question
     * with its equivalent image and three other random images
     * and sets the images on the screen.
     */
    public void createNextQuestion() {

        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "createNextQuestion() method Invoked");

        correctIncorrectTextView.setText(getResources().getString(R.string.correct) + " " + Integer.toString(correctAnswers) + " " + getResources().getString(R.string.wrong)
                + " " + Integer.toString(incorrectAnswers));
        progressTextView.setText(getResources().getString(R.string.questions) + " " + Integer.toString(questions) + "/" + Integer.toString(4));

        chances = 0;
        questions++;

        disableNextButton = true;
        nextButton.setEnabled(false);

        disablePlayAgainButton = true;
        playAgainButton.setEnabled(false);

        answerResult = "";
        answerResultTextView.setText(answerResult);

        disableImageButtons = false;
        enableButtons();

        answers.clear();
        List<Integer> previousNumbers = new ArrayList<Integer>();
        incorrectAnswer = -1;

        //Create a random object
        Random rand = new Random();

        // questionPos will be used as an index for the questionsArray to assign the
        // quiz question.
        questionPos = rand.nextInt(14);
        question = questionsArray[questionPos];
        // This while loop should make sure that the same question per quiz attempt won't be asked again.
        while(questionsAsked.contains(question))
        {
            questionPos = rand.nextInt(14);
            question = questionsArray[questionPos];
        }
        questionsAsked.add(question);
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

                // Makes sure the correct-answer image doesn't get placed on the wrong bubtton
                // And prevents image duplication.
                while (incorrectAnswer == questionPos || previousNumbers.contains(incorrectAnswer))
                {
                    incorrectAnswer = rand.nextInt(14);
                    Log.i("INC_ANSWERS","incorrect" + incorrectAnswer);
                    Log.i("INC_ANSWERS","previous" + incorrectAnswer);
                }

                previousNumbers.add(incorrectAnswer);
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
        outState.putBoolean("disableImageButtons", disableImageButtons);
        outState.putBoolean("disableNextButton", disableNextButton);
        outState.putBoolean("disablePlayAgainButton", disablePlayAgainButton);
        outState.putInt("chances", chances);
        outState.putInt("correctAnswers", correctAnswers);
        outState.putInt("incorrectAnswers", incorrectAnswers);

    }

    /**
     * The disableButtons method disable
     * the main ImageButtons on the screen and
     * also disable the playAgain Button.
     */
    public void disableButtons(){
        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "disableButtons() method Invoked");

        imageButton1.setEnabled(false);
        imageButton2.setEnabled(false);
        imageButton3.setEnabled(false);
        imageButton4.setEnabled(false);
    }

    /**
     * The enableButtons method enable the
     * the main Image Buttons of the Game
     */
    public void enableButtons(){
        imageButton1.setEnabled(true);
        imageButton2.setEnabled(true);
        imageButton3.setEnabled(true);
        imageButton4.setEnabled(true);
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

            answerResult = getResources().getString(R.string.correct);;
            answerResultTextView.setTextColor(Color.GREEN);
            answerResultTextView.setText(answerResult);

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
            //boolean to be saved in the bundle to keep track of disabled buttons
            disableImageButtons = true;
            disableButtons();
            correctIncorrectTextView.setText(getResources().getString(R.string.correct) + " " + Integer.toString(correctAnswers) + " " + getResources().getString(R.string.wrong)
                    + " " + Integer.toString(incorrectAnswers));
            progressTextView.setText(getResources().getString(R.string.questions) + " " + Integer.toString(questions) + "/" + Integer.toString(4));

            //if questions is greater than or equal to 4 game finished with a correct answer
            if(questions >= 4)
            {
                totalAttempts++;

                // pastGameTwo is the score of the user's latest quiz attempt. So we want to push the
                // score of the attempt prior that that attempt to pastGameOne.
                if(pastGameTwoCorrectAnswers != 0 || pastGameTwoIncorrectAnswers != 0)
                {
                    pastGameOneCorrectAnswers = pastGameTwoCorrectAnswers;
                    pastGameOneIncorrectAnswers = pastGameTwoIncorrectAnswers;

                    pastGameTwoCorrectAnswers = correctAnswers;
                    pastGameTwoIncorrectAnswers = incorrectAnswers;
                }
                else if(pastGameTwoCorrectAnswers == 0 && pastGameTwoIncorrectAnswers == 0)
                {
                    pastGameTwoCorrectAnswers = correctAnswers;
                    pastGameTwoIncorrectAnswers = incorrectAnswers;
                }
                else if(pastGameOneCorrectAnswers == 0 && pastGameOneIncorrectAnswers == 0)
                {
                    pastGameOneCorrectAnswers = correctAnswers;
                    pastGameOneIncorrectAnswers = incorrectAnswers;
                }


                // Keeps track of the past game scores
                Log.d("CORRECT_ANSWERS", String.valueOf(correctAnswers));
                Log.d("INCORRECT_ANSWERS", String.valueOf(incorrectAnswers));
                Log.d("PAST_GAME_ONE_CORRECT", String.valueOf(pastGameOneCorrectAnswers));
                Log.d("PAST_GAME_ONE_INCORRECT", String.valueOf(pastGameOneIncorrectAnswers));
                Log.d("PAST_GAME_TWO_CORRECT", String.valueOf(pastGameTwoCorrectAnswers));
                Log.d("PAST_GAME_TWO_INCORRECT", String.valueOf(pastGameTwoIncorrectAnswers));

                answerResult = getResources().getString(R.string.correct_finish);
                answerResultTextView.setText(answerResult);
                nextButton.setEnabled(false);
                disableNextButton = true;

                disableButtons();

                playAgainButton.setEnabled(true);
                disablePlayAgainButton = false;

            }
            else
            {
                nextButton.setEnabled(true);
                disableNextButton = false;
            }
        }
        else
        {
            //user clicked the wrong image
            chances++;
            Log.i("CHANCES","" + chances);
            int sadFace = rand.nextInt(2);
            switch (view.getTag().toString())
            {
                case "0":
                    button1ImageRes = sadFacesArray[sadFace];
                    imageButton1.setImageResource(button1ImageRes);
                    imageButton1.setEnabled(false);
                    break;
                case "1":
                    button2ImageRes = sadFacesArray[sadFace];
                    imageButton2.setImageResource(button2ImageRes);
                    imageButton2.setEnabled(false);
                    break;
                case "2":
                    button3ImageRes = sadFacesArray[sadFace];
                    imageButton3.setImageResource(button3ImageRes);
                    imageButton3.setEnabled(false);
                    break;
                case "3":
                    button4ImageRes = sadFacesArray[sadFace];
                    imageButton4.setImageResource(button4ImageRes);
                    imageButton4.setEnabled(false);
                    break;
                default:
                    break;
            }
            //checking if the user clicked the wrong image for the second time
            if(chances > 1)
            {
                incorrectAnswers++;
                disableImageButtons = true;
                disableButtons();
                correctIncorrectTextView.setText(getResources().getString(R.string.correct) + " " + Integer.toString(correctAnswers) + " " + getResources().getString(R.string.wrong)
                        + " " + Integer.toString(incorrectAnswers));
                progressTextView.setText(getResources().getString(R.string.questions) + " " + Integer.toString(questions) + "/" + Integer.toString(4));

                //if questions is greater than or equal to 4 game finished.
                if(questions >= 4)
                {
                    totalAttempts++;

                    if(pastGameTwoCorrectAnswers != 0 || pastGameTwoIncorrectAnswers != 0)
                    {
                        pastGameOneCorrectAnswers = pastGameTwoCorrectAnswers;
                        pastGameOneIncorrectAnswers = pastGameTwoIncorrectAnswers;

                        pastGameTwoCorrectAnswers = correctAnswers;
                        pastGameTwoIncorrectAnswers = incorrectAnswers;
                    }
                    else if(pastGameOneCorrectAnswers == 0 && pastGameOneIncorrectAnswers == 0)
                    {
                        pastGameOneCorrectAnswers = correctAnswers;
                        pastGameOneIncorrectAnswers = incorrectAnswers;
                    }
                    else if(pastGameTwoCorrectAnswers == 0 && pastGameTwoIncorrectAnswers == 0)
                    {
                        pastGameTwoCorrectAnswers = correctAnswers;
                        pastGameTwoIncorrectAnswers = incorrectAnswers;
                    }

                    // Keeps track of the past game scores
                    Log.d("CORRECT_ANSWERS", String.valueOf(correctAnswers));
                    Log.d("INCORRECT_ANSWERS", String.valueOf(incorrectAnswers));
                    Log.d("PAST_GAME_ONE_CORRECT", String.valueOf(pastGameOneCorrectAnswers));
                    Log.d("PAST_GAME_ONE_INCORRECT", String.valueOf(pastGameOneIncorrectAnswers));
                    Log.d("PAST_GAME_TWO_CORRECT", String.valueOf(pastGameTwoCorrectAnswers));
                    Log.d("PAST_GAME_TWO_INCORRECT", String.valueOf(pastGameTwoIncorrectAnswers));

                    answerResult = getResources().getString(R.string.wrong_finished);
                    answerResultTextView.setText(answerResult);
                    answerResultTextView.setTextColor(Color.RED);

                    nextButton.setEnabled(false);
                    disableNextButton = true;
                    disableButtons();
                    playAgainButton.setEnabled(true);
                    disablePlayAgainButton = false;

                }
                else
                {
                    Log.i("CHANCES","" + chances);

                    answerResult = getResources().getString(R.string.wrong);
                    answerResultTextView.setText(answerResult);
                    answerResultTextView.setTextColor(Color.RED);
                    disableImageButtons = true;
                    disableButtons();

                    playAgainButton.setEnabled(false);
                    disablePlayAgainButton = true;

                    disableNextButton = false;
                    nextButton.setEnabled(true);
                    correctIncorrectTextView.setText(getResources().getString(R.string.correct) + " " + Integer.toString(correctAnswers) + " " + getResources().getString(R.string.wrong)
                            + " " + Integer.toString(incorrectAnswers));
                    progressTextView.setText(getResources().getString(R.string.questions) + " " + Integer.toString(questions) + "/" + Integer.toString(4));
                }
            }
            else
            {
                answerResult = getResources().getString(R.string.wrong_tryagain);
                answerResultTextView.setTextColor(Color.RED);
                answerResultTextView.setText(answerResult);
                nextButton.setEnabled(false);
                playAgainButton.setEnabled(false);
                disablePlayAgainButton = true;
                disableNextButton = true;
                disableImageButtons = false;
            }
        }
    }

    public void showHint(View v)
    {
        // TODO: Emit LogCat message
        Log.i(METHOD_TAG, "showHint() method Invoked");

        String query = getResources().getString(R.string.hint_prefix) + " " + questionSentenceTextView.getText().toString();
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

        editor.putString("answerResult", answerResult);
        editor.putString("question", question);
        editor.putInt("questions", questions);
        editor.putInt("questionsPos", questionPos);
        editor.putInt("chances", chances);

        Log.d("DISABLE_ONPAUSE", String.valueOf(disableImageButtons));
        editor.putBoolean("disableImageButtons", disableImageButtons);
        editor.putBoolean("disableNextButton", disableNextButton);
        editor.putBoolean("disablePlayAgainButton", disablePlayAgainButton);

        editor.putInt("correctAnswers", correctAnswers);
        editor.putInt("incorrectAnswers", incorrectAnswers);

        editor.putInt("totalAttempts", totalAttempts);

        editor.putInt("pastGameOneCorrectAnswers", pastGameOneCorrectAnswers);
        editor.putInt("pastGameOneIncorrectAnswers", pastGameOneIncorrectAnswers);

        editor.putInt("pastGameTwoCorrectAnswers", pastGameTwoCorrectAnswers);
        editor.putInt("pastGameTwoIncorrectAnswers", pastGameTwoIncorrectAnswers);

        editor.commit();
    }

    public void launchAboutActivity(View v)
    {

        Intent i = new Intent(this, AboutActivity.class);

        Log.d("ATTEMPTS", String.valueOf(totalAttempts));

        i.putExtra("totalAttempts", totalAttempts);

        i.putExtra("pastGameOneCorrectAnswers", pastGameOneCorrectAnswers);
        i.putExtra("pastGameOneIncorrectAnswers", pastGameOneIncorrectAnswers);

        i.putExtra("pastGameTwoCorrectAnswers", pastGameTwoCorrectAnswers);
        i.putExtra("pastGameTwoIncorrectAnswers", pastGameTwoIncorrectAnswers);

        startActivity(i);
    }
}
