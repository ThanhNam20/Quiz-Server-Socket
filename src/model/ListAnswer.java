package model;

import java.util.ArrayList;

public class ListAnswer {
    private ArrayList<Answer> answerArrayList ;

    public ListAnswer(ArrayList<Answer> answerArrayList) {
        this.answerArrayList = answerArrayList;
    }

    public ArrayList<Answer> getAnswerArrayList() {
        return answerArrayList;
    }

    public void setAnswerArrayList(ArrayList<Answer> answerArrayList) {
        this.answerArrayList = answerArrayList;
    }
}
