package com.bawp.trivia.data;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bawp.trivia.controller.AppController;
import com.bawp.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.bawp.trivia.controller.AppController.TAG;

public class QuestionBank {

    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url =
            "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Question question = new Question();
                            question.setQuestion(response.getJSONArray(i).get(0).toString());
                            question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                            // Add question objects to list
                            questionArrayList.add(question);
//                            Log.d("Hello", "onResponse: " + question);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (null != callBack) callBack.processFinished(questionArrayList);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: " + error.getMessage());
                }
            }
        );
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;
    }
}