package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.trivia.controller.AppController.TAG;

public class QuestionBank {

    ArrayList <Question> QuestionArrayList = new ArrayList<>();

    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";


    public List<Question> getQuestion(final AnswerListAsyncResponse callBack){


    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for (int i=0; i<response.length(); i++){
                        Question question= new Question();
                        try {
                            question.setAnswer(response.getJSONArray(i).get(0).toString());
                            question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        QuestionArrayList.add(question);
                    }
                    if (null != callBack) callBack.processFinished(QuestionArrayList);


                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });

    AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return  QuestionArrayList;
    }

}
