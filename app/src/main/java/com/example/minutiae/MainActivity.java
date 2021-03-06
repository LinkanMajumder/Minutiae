package com.example.minutiae;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.controller.AppController;
import com.data.AnswerListAsyncResponse;
import com.data.Repository;
import com.example.minutiae.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import com.model.Question;
import com.model.Score;
import com.util.Prefs;

import org.json.JSONArray;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    List<Question> questionList;
    private ActivityMainBinding binding;
    private int currentQuestionIndex = 0;
    private int scoreCounter = 0;
    private Score score;
    private Prefs prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        score = new Score();
        prefs = new Prefs(MainActivity.this);
        Log.d("TAG", "onCreate: "+prefs.getHighestScore());


        binding.highestScoreText.setText(MessageFormat.format("Highest: {0}", String.valueOf(prefs.getHighestScore())));
        binding.scoreText.setText(MessageFormat.format("Current Score: {0}",
                String.valueOf(score.getScore())));

        questionList = new Repository().getQuestions(questionArrayList ->{
            binding.questionTextview.setText(questionArrayList.get(currentQuestionIndex)
                    .getAnswer());

                    extracted(questionArrayList);
                }

        );

        binding.buttonNext.setOnClickListener(view -> {
            currentQuestionIndex = (currentQuestionIndex +1) %  questionList.size();
            updateQuestion();



        });
        binding.buttonTrue.setOnClickListener(view -> {
            checkAnswer(true);
            updateQuestion();

        });
        binding.buttonFalse.setOnClickListener(view -> {
            checkAnswer(false);
            updateQuestion();


        });


    }

    private void checkAnswer(boolean userChoseCorrect) {
        boolean answer = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMessageID = 0;
        if (userChoseCorrect == answer) {
            snackMessageID = R.string.correct_answer;
            fadeAnimation();
            addPoints();
        }else {
            deductPoints();
            snackMessageID = R.string.incorrect;
            shakeAnimation();
        }
        Snackbar.make(binding.cardView, snackMessageID, Snackbar.LENGTH_SHORT)
                .show();

    }

    private void extracted(ArrayList<Question> questionArrayList) {
        binding.textViewOutOf.setText(String.format("Question:%d/%d", currentQuestionIndex, questionArrayList.size()));
    }
    private void fadeAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.GREEN);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }
        });


    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        binding.questionTextview.setText(question);
        extracted((ArrayList<Question>) questionList);
    }
    private  void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void deductPoints(){

        if (scoreCounter > 0) {
            scoreCounter -= 100;
            score.setScore(scoreCounter);
            binding.scoreText.setText(MessageFormat.format("Current Score: {0}",
                    String.valueOf(score.getScore())));



        }else {
            scoreCounter = 0;
            score.setScore(scoreCounter);


        }

    }
    private void addPoints(){
        scoreCounter += 100;
        score.setScore(scoreCounter);
        binding.scoreText.setText(String.valueOf(score.getScore()));
        binding.scoreText.setText(MessageFormat.format("Current Score: {0}",
                String.valueOf(score.getScore())));

    }

    @Override
    protected void onPause() {
        prefs.saveHighestScore(score.getScore());
        Log.d("Pause", "onPause: saveing score "+ prefs.getHighestScore());
        Log.d("Pause", "onPause: saving score " + prefs.getHighestScore() );
        super.onPause();
    }
}
