package praxis.vesit.com.allentrypass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import praxis.vesit.com.allentrypass.util.JSONParser;
import praxis.vesit.com.config.AppConfig;

/**
 * Created by shruti.mantri on 20/07/15.
 */
public class CreateStudentPassActivity extends Activity{
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputCardPassNumber;
    EditText inputStudentName;
    EditText inputStudentEmail;
    EditText inputStudentContact;
    EditText inputStudentCollege;

    // url to create new product
    private static String url_create_product = "http://" + AppConfig.HOST + ":" + AppConfig.PORT + "/allEntryPass/create_student_pass.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_student_pass);

        // Edit Text
        inputCardPassNumber = (EditText) findViewById(R.id.inputCardPassNumber);
        inputStudentName = (EditText) findViewById(R.id.inputStudentName);
        inputStudentEmail = (EditText) findViewById(R.id.inputStudentEmail);
        inputStudentContact = (EditText) findViewById(R.id.inputStudentContact);
        inputStudentCollege = (EditText) findViewById(R.id.inputStudentCollege);

        // Create button
        Button btnCreatePass = (Button) findViewById(R.id.btnCreatePass);

        // button click event
        btnCreatePass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewPass().execute();
            }
        });

        // Back button
        Button btnBack = (Button) findViewById(R.id.btnBackFromCreatePass);

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
        }
        ((EditText) findViewById(R.id.inputCardPassNumber)).setText(getIntent().getStringExtra("passNumber"));
        ((EditText) findViewById(R.id.inputStudentName)).setText(getIntent().getStringExtra("name"));
        ((EditText) findViewById(R.id.inputStudentEmail)).setText(getIntent().getStringExtra("email"));
        ((EditText) findViewById(R.id.inputStudentContact)).setText(getIntent().getStringExtra("phone"));
        ((EditText) findViewById(R.id.inputStudentCollege)).setText(getIntent().getStringExtra("college"));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(i);
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewPass extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateStudentPassActivity.this);
            pDialog.setMessage("Creating Pass..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String passId = inputCardPassNumber.getText().toString();
            String name = inputStudentName.getText().toString();
            String email = inputStudentEmail.getText().toString();
            String phone = inputStudentContact.getText().toString();
            String college = inputStudentCollege.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("passId", passId));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("phone", phone));
            params.add(new BasicNameValuePair("college", college));

            // getting JSON Object
            // Note that create product url accepts POST method
            Log.d("URL:",url_create_product);
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            if(json==null){
                Intent i = new Intent(getApplicationContext(), CreateStudentPassActivity.class);
                i.putExtra("message", "Please enter required field(s)");
                i.putExtra("passNumber", passId);
                i.putExtra("name", name);
                i.putExtra("phone", phone);
                i.putExtra("email", email);
                i.putExtra("college", college);
                startActivity(i);
                return null;
            }
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    pDialog.dismiss();
                    Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                    i.putExtra("message", "Pass created successfully!");
                    startActivity(i);
                    // closing this screen

                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), CreateStudentPassActivity.class);
                    i.putExtra("message", json.getString("message"));
                    i.putExtra("passNumber", passId);
                    i.putExtra("name", name);
                    i.putExtra("phone", phone);
                    i.putExtra("email", email);
                    i.putExtra("college", college);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}