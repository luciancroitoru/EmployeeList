// http://www.vancura.cz/tutorial-google-custom-search-in-android-app/

package com.example.employeelist;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.employeelist.utils.DataMng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends Activity {

    private static final String TAG = "searchActivity";
    static String result = null;
    @BindView(R.id.search_edit_text)
    EditText mEditText;
    @BindView(R.id.button_search)
    Button mButton;
    @BindView(R.id.response_view)
    TextView mTextView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.search_question)
    TextView mSearchQuestion;

    Context context;
    Integer responseCode = null;
    String responseMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        context = getApplicationContext();

        String hint = DataMng.Name;
        String question = "Interested in the employee " + hint + "? Place your search text below";
        mSearchQuestion.setText(question);

        // button onClick
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String searchString = mEditText.getText().toString();
                String text = "Searching for : " + searchString;
                Log.d(TAG, text);
                mTextView.setText(text);

                // hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                // remove spaces
                String searchStringNoSpaces = searchString.replace(" ", "+");

                // Google API key
                // Replace with your own key value
                String key = ""; // your Google API key

                // Google Search Engine ID
                // Replace with your own key value
                String cx = ""; //Google search engine API key

                String urlString = "https://www.googleapis.com/customsearch/v1?q=" + searchStringNoSpaces + "&key=" + key + "&cx=" + cx + "&alt=json";
                URL url = null;
                try {
                    url = new URL(urlString);
                } catch (MalformedURLException e) {
                    Log.e(TAG, "ERROR converting String to URL " + e.toString());
                }
                Log.d(TAG, "Url = " + urlString);


                // start AsyncTask
                GoogleSearchAsyncTask searchTask = new GoogleSearchAsyncTask();
                searchTask.execute(url);
            }
        });

    }


    private class GoogleSearchAsyncTask extends AsyncTask<URL, Integer, String> {

        protected void onPreExecute() {

            Log.d(TAG, "AsyncTask - onPreExecute");
            // show mProgressBar
            mProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(URL... urls) {

            URL url = urls[0];
            Log.d(TAG, "AsyncTask - doInBackground, url=" + url);

            // Http connection
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                Log.e(TAG, "Http connection ERROR " + e.toString());
            }


            try {
                responseCode = conn.getResponseCode();
                responseMessage = conn.getResponseMessage();
            } catch (IOException e) {
                Log.e(TAG, "Http getting response code ERROR " + e.toString());

            }

            Log.d(TAG, "Http response code =" + responseCode + " message=" + responseMessage);

            try {

                if (responseCode != null && responseCode == 200) {

                    // response OK
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = rd.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    rd.close();

                    conn.disconnect();

                    result = sb.toString();

                    Log.d(TAG, "result=" + result);

                    String jsonString = result;
                    JSONObject obj = new JSONObject(jsonString);

                    String titlesResult = "";
                    JSONArray arr = obj.getJSONArray("items");
                    for (int i = 0; i < arr.length(); i++) {
                        String title = arr.getJSONObject(i).getString("title");
                        if (i < 5) {
                            titlesResult = titlesResult + "\n" + title;
                        }
                    }

                    return titlesResult;

                } else {

                    // response problem
                    String errorMsg = "Http ERROR response " + responseMessage + "\n" + "Is the phone connected to internet? " + "\n" + "Make sure to replace in code your own Google API key and Search Engine ID";
                    Log.e(TAG, errorMsg);
                    result = errorMsg;
                    return result;

                }
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Http Response ERROR " + e.toString());
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

            Log.d(TAG, "AsyncTask - onProgressUpdate, progress=" + progress);

        }

        protected void onPostExecute(String result) {

            Log.d(TAG, "AsyncTask - onPostExecute, result=" + result);

            // hide mProgressBar
            mProgressBar.setVisibility(View.GONE);

            // make TextView scrollable
            mTextView.setMovementMethod(new ScrollingMovementMethod());
            // show result
            mTextView.setText(result);
        }
    }
}
