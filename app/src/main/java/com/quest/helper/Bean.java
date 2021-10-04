package com.quest.helper;

/**
 * Created by CIS on 7/10/18.
 */

 public class Bean {
     String id,category,image;
    boolean isselected;
      int answer;
    String transaction_date,
            amount,
            subcat_id,cr_dr;

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSubcat_id() {
        return subcat_id;
    }

    public void setSubcat_id(String subcat_id) {
        this.subcat_id = subcat_id;
    }

    public String getCr_dr() {
        return cr_dr;
    }

    public void setCr_dr(String cr_dr) {
        this.cr_dr = cr_dr;
    }

    public Bean(String transaction_date, String amount, String subcat_id, String cr_dr) {

        this.transaction_date = transaction_date;
        this.amount = amount;
        this.subcat_id = subcat_id;
        this.cr_dr = cr_dr;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public Bean(boolean isselected) {
        this.isselected = isselected;
    }

    public boolean isIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }

    String question_id,question,option_one,option_two,option_three,option_four,chooseanswer;





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


    public String getChooseanswer() {
        return chooseanswer;
    }

    public void setChooseanswer(String chooseanswer) {
        this.chooseanswer = chooseanswer;
    }

    public Bean(String question_id, String question, String option_one, String option_two, String option_three, String option_four, String chooseanswer) {

        this.question_id = question_id;
        this.question = question;
        this.option_one = option_one;
        this.option_two = option_two;
        this.option_three = option_three;
        this.option_four = option_four;
        this.chooseanswer=chooseanswer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Bean(String id, String category,String image) {

        this.id = id;
        this.category = category;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
