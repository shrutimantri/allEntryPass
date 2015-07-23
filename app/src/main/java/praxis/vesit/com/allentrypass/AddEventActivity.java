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
public class AddEventActivity extends Activity {
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputEventName;

    // url to create new product
    private static String url_create_product = "http://" + AppConfig.HOST + ":" + AppConfig.PORT + "/allEntryPass/create_event.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        // Edit Text
        inputEventName = (EditText) findViewById(R.id.inputEventName);

        // Create button
        Button btnCreateEvent = (Button) findViewById(R.id.btnCreateEvent);

        // button click event
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewEvent().execute();
            }
        });

        // Back button
        Button btnBack = (Button) findViewById(R.id.btnBackFromAddEvent);

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

        ((EditText) findViewById(R.id.inputEventName)).setText(getIntent().getStringExtra("eventName"));

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(i);
    }

    /**
     * Background Async Task to Create new product
     * */
    class CreateNewEvent extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddEventActivity.this);
            pDialog.setMessage("Creating Event..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String eventName = inputEventName.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("eventName", eventName));

            // getting JSON Object
            // Note that create product url accepts POST method
            Log.d("URL:", url_create_product);
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                if(json==null){
                    Intent i = new Intent(getApplicationContext(), AddEventActivity.class);
                    i.putExtra("message", "Please enter required field(s)");
                    startActivity(i);
                    return null;
                }
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    pDialog.dismiss();
                    Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                    i.putExtra("message", "Event created successfully!");
                    startActivity(i);
                    // closing this screen

                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), AddEventActivity.class);
                    i.putExtra("message", json.getString("message"));
                    i.putExtra("eventName", eventName);
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
