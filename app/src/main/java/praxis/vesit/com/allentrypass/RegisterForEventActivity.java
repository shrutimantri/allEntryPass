package praxis.vesit.com.allentrypass;

import android.app.Activity;
import android.app.Application;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import praxis.vesit.com.allentrypass.util.JSONParser;
import praxis.vesit.com.config.AppConfig;

/**
 * Created by shruti.mantri on 20/07/15.
 */
public class RegisterForEventActivity extends ListActivity {
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputPassId;
    TextView studentNameText;
    TextView studentEmailText;
    TextView studentContactText;
    TextView studentCollegeText;
    Button btnSubmit;
    Spinner eventDropdown;
    List<String> eventsName;
    // url to create new product
    private static String url_get_student = "http://" + AppConfig.HOST + ":" + AppConfig.PORT + "/allEntryPass/get_student_details.php";
    private static String url_register_event = "http://" + AppConfig.HOST + ":" + AppConfig.PORT + "/allEntryPass/register_event.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_event);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Edit Text
        inputPassId = (EditText) findViewById(R.id.inputPassId);
        studentNameText = (TextView) findViewById(R.id.studentNameText);
        studentEmailText = (TextView) findViewById(R.id.studentEmailText);
        studentContactText = (TextView) findViewById(R.id.studentContactText);
        studentCollegeText = (TextView) findViewById(R.id.studentCollegeText);
        eventDropdown = (Spinner) findViewById(R.id.eventDropdown);
        btnSubmit = (Button) findViewById(R.id.btnRegisterForEventSubmit);
        // Create button
        Button btnGo = (Button) findViewById(R.id.btnGo);

        // button click event
        btnGo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new GetStudentDetails().execute();
            }
        });

        // Back button
        Button btnBack = (Button) findViewById(R.id.btnBackFromRegisterEvent);

        // button click event
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                startActivity(i);
            }
        });

        if(getIntent().getStringExtra("message")!=null){
            Context context = getApplicationContext();
            CharSequence text = getIntent().getStringExtra("message");
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }/*
        ((EditText) findViewById(R.id.inputCardPassNumber)).setText(getIntent().getStringExtra("passNumber"));
        ((EditText) findViewById(R.id.inputStudentName)).setText(getIntent().getStringExtra("name"));
        ((EditText) findViewById(R.id.inputStudentEmail)).setText(getIntent().getStringExtra("email"));
        ((EditText) findViewById(R.id.inputStudentContact)).setText(getIntent().getStringExtra("phone"));
        ((EditText) findViewById(R.id.inputStudentCollege)).setText(getIntent().getStringExtra("college"));*/
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new RegisterPassForEvent().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class GetStudentDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterForEventActivity.this);
            pDialog.setMessage("Loading student details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    eventsName = new ArrayList<String>();
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("passId", inputPassId.getText().toString()));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_get_student, "GET", params);

                        // check your log for json response
                        Log.d("Single Student Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray studentObj = json
                                    .getJSONArray("student"); // JSON Array

                            // get first product object from JSON Array
                            JSONObject student = studentObj.getJSONObject(0);

                            // display product data in EditText
                            studentNameText.setText(student.getString("name"));
                            studentEmailText.setText(student.getString("email"));
                            studentContactText.setText(student.getString("phone"));
                            studentCollegeText.setText(student.getString("college"));
                            studentNameText.setVisibility(TextView.VISIBLE);
                            studentEmailText.setVisibility(TextView.VISIBLE);
                            studentContactText.setVisibility(TextView.VISIBLE);
                            studentCollegeText.setVisibility(TextView.VISIBLE);
                            if(student.has("events")){
                                JSONArray spinnerArray =  student.getJSONArray("events");
                                for(int i=0; i<spinnerArray.length(); i++) {
                                    eventsName.add(spinnerArray.getString(i));
                                }

                            }
                            setListAdapter(new ArrayAdapter<String>(getBaseContext(),
                                    R.layout.list_event, R.id.eventNameInList, eventsName));
                        }else{
                            if(json.get("message").equals("No student found")){
                                Intent i = new Intent(getApplicationContext(), RegisterForEventActivity.class);
                                i.putExtra("message", "This pass is not registered!");
                                startActivity(i);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    // updating listview

                }
            });
        }
    }


    class RegisterPassForEvent extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterForEventActivity.this);
            pDialog.setMessage("Registering for event...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("passId", inputPassId.getText().toString()));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_get_student, "GET", params);

                        // check your log for json response
                        Log.d("Single Student Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray studentObj = json
                                    .getJSONArray("student"); // JSON Array

                            // get first product object from JSON Array
                            JSONObject student = studentObj.getJSONObject(0);

                            // display product data in EditText
                            studentNameText.setText(student.getString("name"));
                            studentEmailText.setText(student.getString("email"));
                            studentContactText.setText(student.getString("phone"));
                            studentCollegeText.setText(student.getString("college"));
                            studentNameText.setVisibility(TextView.VISIBLE);
                            studentEmailText.setVisibility(TextView.VISIBLE);
                            studentContactText.setVisibility(TextView.VISIBLE);
                            studentCollegeText.setVisibility(TextView.VISIBLE);

                        }else{
                            if(json.get("message").equals("No student found")){
                                Intent i = new Intent(getApplicationContext(), RegisterForEventActivity.class);
                                i.putExtra("message", "This pass is not registered!");
                                startActivity(i);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }
}
