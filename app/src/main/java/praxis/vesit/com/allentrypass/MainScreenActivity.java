package praxis.vesit.com.allentrypass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainScreenActivity extends Activity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    Button btnRegisterForEvent;
    Button btnAddEvent;
    Button btnCreateStudentPass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        if(getIntent().getStringExtra("message")!=null){
            Context context = getApplicationContext();
            CharSequence text = getIntent().getStringExtra("message");
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        // Buttons
        btnRegisterForEvent = (Button)findViewById(R.id.btnRegisterForEvent);
        btnAddEvent = (Button)findViewById(R.id.btnAddEvent);
        btnCreateStudentPass = (Button)findViewById(R.id.btnCreateStudentPass);

        // view products click event
        btnCreateStudentPass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), CreateStudentPassActivity.class);
                startActivity(i);

            }
        });

        // view products click event
        btnAddEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), AddEventActivity.class);
                startActivity(i);

            }
        });

        btnRegisterForEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), RegisterForEventActivity.class);
                startActivity(i);

            }
        });
    }
}
