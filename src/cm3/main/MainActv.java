package cm3.main;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.view.Menu;

public class MainActv extends Activity {

	/*********************************
	 * Others
	 *********************************/
	public static Vibrator vib;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/*********************************
		 * 1. super
		 * 2. Set content
		 * 2-2. Set title
		 * 3. Initialize => vib
		 * 
		 *  4. Set list
		 *  5. Set listener => Image buttons
		 *  6. Set path label
		 *  
		 *  7. Initialize preferences
		 *  
		 *  8. Refresh DB

		 *********************************/
    	/*********************************
		 * 1. super
		 * 2. Set content
		 *********************************/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_actv);
        
        /*********************************
		 * 2-2. Set title
		 *********************************/
		this.setTitle(this.getClass().getName());
        
        vib = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);

//        /*********************************
//		 * 4. Set list
//		 *********************************/
//        set_initial_dir_list();
//        
//        /*********************************
//		 * 5. Set listener => Image buttons
//		 *********************************/
//		set_listeners();
//		
//		/*********************************
//		 * 6. Set path label
//		 *********************************/
//		Methods.updatePathLabel(this);
//		
//		/*********************************
//		 * 7. Initialize preferences
//		 *********************************/
//		init_prefs();
		

        
    }//public void onCreate(Bundle savedInstanceState)

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_actv, menu);
        return true;
    }

}//public class MainActv extends Activity
