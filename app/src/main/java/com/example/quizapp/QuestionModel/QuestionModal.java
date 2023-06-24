package com.example.quizapp.QuestionModel;

public class QuestionModal {
    private String question;
    private int answer;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int selectedOption;
    private boolean isBookMarked=false;

    public QuestionModal(String question, int answer, String option1, String option2, String option3, String option4) {
        this.question = question;
        this.answer = answer;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.isBookMarked=false;
        this.selectedOption=-1;
    }

    public String getQuestion() {
        return this.question;
    }

    public int getAnswer() {
        return this.answer;
    }

    public int getSelectedAnswer() {
        return this.selectedOption;
    }

    public String getOption1() {
        return this.option1;
    }

    public String getOption2() {
        return this.option2;
    }

    public String getOption3() {
        return this.option3;
    }

    public String getOption4() {
        return this.option4;
    }

    public boolean getIsBookMarked(){
        return this.isBookMarked;
    }

    public void setIsBookMarked(){
        this.isBookMarked= !this.isBookMarked;
    }
    public void setSelectedAnswer(int option){
        this.selectedOption=option;
    }
}
