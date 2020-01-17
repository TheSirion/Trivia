package com.bawp.trivia;

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

import com.bawp.trivia.data.AnswerListAsyncResponse;
import com.bawp.trivia.data.QuestionBank;
import com.bawp.trivia.model.Question;
import com.bawp.trivia.model.Score;
import com.bawp.trivia.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextview;
    private TextView questionCounterTextview;
    private TextView scoreTextview;
    private Score score = new Score();
    private int scoreCounter = 0;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new Prefs(MainActivity.this);
        ImageButton nextButton = findViewById(R.id.next_button);
        ImageButton prevButton = findViewById(R.id.prev_button);
        Button trueButton = findViewById(R.id.true_button);
        Button falseButton = findViewById(R.id.false_button);
        questionCounterTextview = findViewById(R.id.counter_text);
        questionTextview = findViewById(R.id.question_textview);
        scoreTextview = findViewById(R.id.scoreTextView);
        TextView highScoreTextview = findViewById(R.id.HighScoreTextView);


        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextview.setText(questionArrayList.get(currentQuestionIndex).getQuestion());
                updateQuestion();
            }
        });

        highScoreTextview.setText(MessageFormat.format("HIGHSCORE: {0}", prefs.getHighScore()));
    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prev_button:
                if (currentQuestionIndex > 0)
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                updateQuestion();
                break;
            case R.id.next_button:
                updateQuestion();
                break;
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;
        }
    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId = 0;
        if (userChooseCorrect == answerIsTrue) {
            fadeView();
            toastMessageId = R.string.correct_answer;
            scoreCounter += 100;
            score.setScore(scoreCounter);
        } else {
            shakeAnimation();
            toastMessageId = R.string.wrong_answer;
            if (scoreCounter > 0) scoreCounter -= 100;
            score.setScore(scoreCounter);
        }
        Toast.makeText(getApplicationContext(), toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion() {
        questionTextview.setText(questionList.get(currentQuestionIndex).getQuestion());
        String questionCounter = (currentQuestionIndex + 1) + " / " + (questionList.size() + 1);
        String scoreString = "SCORE: " + score.getScore();

        scoreTextview.setText(scoreString);
        questionCounterTextview.setText(questionCounter);
    }

    private void fadeView() {
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
