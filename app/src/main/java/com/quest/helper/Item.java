package com.quest.helper;

/**
 * Created by CIS on 7/11/18.
 */

public class Item {

    String question_id,question,option;
    String option_one,option_two,option_three,option_four;
    Integer answer;

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public String getOption_one() {
        return option_one;
    }

    public void setOption_one(String option_one) {
        this.option_one = option_one;
    }

    public String getOption_two() {
        return option_two;
    }

    public void setOption_two(String option_two) {
        this.option_two = option_two;
    }

    public String getOption_three() {
        return option_three;
    }

    public void setOption_three(String option_three) {
        this.option_three = option_three;
    }

    public String getOption_four() {
        return option_four;
    }

    public void setOption_four(String option_four) {
        this.option_four = option_four;
    }

    public Item(String option_one, String option_two, String option_three, String option_four) {

        this.option_one = option_one;
        this.option_two = option_two;
        this.option_three = option_three;
        this.option_four = option_four;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Item(String question_id, String question, String option) {

        this.question_id = question_id;
        this.question = question;
        this.option = option;
    }
}
