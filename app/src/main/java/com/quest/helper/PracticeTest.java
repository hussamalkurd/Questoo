package com.quest.helper;

public class PracticeTest  {
    String id,test_name,test_url;

    public String getId() {
        return id;
    }

    public String getTest_name() {
        return test_name;
    }

    public String getTest_url() {
        return test_url;
    }

    public PracticeTest(String id, String test_name, String test_url) {

        this.id = id;
        this.test_name = test_name;
        this.test_url = test_url;
    }
}
