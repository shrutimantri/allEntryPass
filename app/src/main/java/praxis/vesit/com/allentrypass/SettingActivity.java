package praxis.vesit.com.allentrypass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import praxis.vesit.com.allentrypass.MainScreenActivity;
import praxis.vesit.com.allentrypass.R;
import praxis.vesit.com.config.AppConfig;

/**
 * Created by shruti.mantri on 22/07/15.
 */
public class SettingActivity extends Activity {
    
    EditText host;
    EditText port;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        // Edit Text
        host = (EditText) findViewById(R.id.inputHost);
        port = (EditText) findViewById(R.id.inputPort);

        // Create button
        Button btnSubmitIP = (Button) findViewById(R.id.btnSubmitIP);

        // button click event
        btnSubmitIP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AppConfig.setHOST(host.getText().toString());
                AppConfig.setPORT(port.getText().toString());

                Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                i.putExtra("message", "Host and Port registered");
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(i);
    }
}
