package gareth.example.com.opsc7312assignment2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Gareth on 14/08/2014.
 */
public class Q1Help extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1help);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.q1help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // when a menu item is selected, perform the following decision
        switch(item.getItemId())
        {
            case R.id.q1HelpExit:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
