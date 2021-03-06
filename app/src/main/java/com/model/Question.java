package com.model;

public class Question {
    private String answer;
    public boolean answerTrue;

    public Question() {
    }

    public Question(String answer, boolean answerTrue) {
        this.answer = answer;
        this.answerTrue = answerTrue;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }



    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", answerTrue=" + answerTrue +
                '}';
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;

    }
}
