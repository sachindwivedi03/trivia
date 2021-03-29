package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    private TextView questionTextView;
    private TextView CounterTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton PrevButton;
    private TextView highScore;
    private TextView score;
    private int questionIndex =0;
    private List<Question> questionList;
    private int scored=0;
    private int highScored=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.Question_text);
        CounterTextView = findViewById(R.id.counter_text);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        PrevButton = findViewById(R.id.prev_button);
        highScore = findViewById(R.id.high_score);
        score = findViewById(R.id.score);




        nextButton.setOnClickListener(this);
        PrevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        questionList= new QuestionBank().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> QUESTION_ARRAY_LIST) {

                questionTextView.setText(QUESTION_ARRAY_LIST.get(questionIndex).getAnswer());

                Log.d("Question", "processFinished: "+ QUESTION_ARRAY_LIST);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prev_button:
                if (questionIndex > 0) {
                    questionIndex = (questionIndex - 1) % questionList.size();
                    updateQuestion();
                }
                break;
            case R.id.next_button:
                questionIndex = (questionIndex +1) % questionList.size();
                updateQuestion();
                break;
            case R.id.true_button:
                checkAnswer (true);
                updateQuestion();
                questionIndex = (questionIndex +1) % questionList.size();

                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                questionIndex = (questionIndex +1) % questionList.size();

                break;

        }
    }

    private void checkAnswer (Boolean userChooseCorrect){
        boolean AnswerIsTrue = questionList.get(questionIndex).isAnswerTrue();
        int ToastMessageId =0;
        if (userChooseCorrect == AnswerIsTrue){
            fadeView();
            scored=scored+1;
            ToastMessageId = R.string.correct_Answer;
        } else {
            shakeAnimation();
            if (scored>0){
                scored=scored-1;
            }
            ToastMessageId = R.string.wrong_Answer;
        }

        Toast.makeText(MainActivity.this, ToastMessageId,
                Toast.LENGTH_SHORT).show();
        score.setText("Current Score: "+ scored );


        if (highScored<scored){
            highScored=scored;
        }
        highScore.setText("High Score: "+ highScored);

    }

    private void updateQuestion() {
        String Question = questionList.get(questionIndex).getAnswer();
        questionTextView.setText(Question);
        CounterTextView.setText(1+ questionIndex +" / " + questionList.size());
    }


    private void shakeAnimation() {
        Animation shake= AnimationUtils.loadAnimation(MainActivity.this,
        R.anim.shake_animaiton);
        final CardView cardView = findViewById(R.id.cardView);
       cardView.setAnimation(shake);

       shake.setAnimationListener(new Animation.AnimationListener() {
           @Override
           public void onAnimationStart(Animation animation) {

               cardView.setCardBackgroundColor(Color.RED);

           }

           @Override
           public void onAnimationEnd(Animation animation) {

               cardView.setCardBackgroundColor(Color.WHITE);

           }

           @Override
           public void onAnimationRepeat(Animation animation) {

           }
       });

    }

    private void fadeView(){
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation= new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}