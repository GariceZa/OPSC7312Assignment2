package gareth.example.com.opsc7312assignment2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Gareth on 16/08/2014.
 */
public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //when a menu item is clicked perform the following decision
        switch (item.getItemId())
        {
            case R.id.startQ1:
                startActivity(new Intent(this,Question1.class));
                break;
            
            case R.id.startQ2:
                startActivity(new Intent(this,Question2.class));
                break;
            
            case R.id.about:
                startActivity(new Intent(this,MainAbout.class));
                break;
            
            case R.id.mainExit:
                MainExitDialog EDF = MainExitDialog.newInstance("Exit?");
                EDF.show(getFragmentManager(), "dialog");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickLaunchQ1(View v) {
        //start question 1
        startActivity(new Intent(this,Question1.class));
    }

    public void onClickLaunchQ2(View v) {
        //start question 2
        startActivity(new Intent(this,Question2.class));
    }

    public void doPositiveClick() {
        //finish the activity when the user
        // clicks ok on the exit dialog
        finish();
    }

    public void doNegativeClick() {
        //do nothing

    }
}
