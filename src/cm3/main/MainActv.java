package cm3.main;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActv extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_actv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_actv, menu);
        return true;
    }
}
