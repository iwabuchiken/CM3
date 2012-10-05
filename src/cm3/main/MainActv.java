package cm3.main;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import cm3.utils.DBUtils;
import cm3.utils.Methods;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;

public class MainActv extends Activity {

	/*********************************
	 * <DB>
	 * 1. Table
	 * 		1. ai (audio item)
	 * 		2. Main table
	 * 2. DB name
	 * 
	 *********************************/
	/*********************************
	 * 1.1. Table: ai (audio item)
	 *********************************/
	public static String tName_ai = "ai";
	
	public static String[] cols_main = 
		{"file_name", 	"file_path",	"title", "memo",
			"last_played_at",	"table_name"};
	
	public static String[] col_types_main =
		{"TEXT", 		"TEXT", 		"TEXT",	"TEXT",
			"INTEGER",			"TEXT"};
	
	/*********************************
	 * 1.2. Main table
	 *********************************/
	public static String tName_main = "CM3";
	
	/*********************************
	 * 2. DB name, others
	 *********************************/
	public static String dbName = "cm3.db";
	
	public static String tName_separator = "__";
	
	/*********************************
	 * <Paths and file names>
	 * 1. 
	 *********************************/
	/*********************************
	 * 1.
	 *********************************/
	public static String fName_tapeatalk = "tapeatalk_records";

	public static String dPath_ExternalStorage_card = "/mnt/sdcard-ext";
	
	public static String dPath_ExternalStorage = "/mnt/sdcard";

	public static String dName_mainFolder = "CM3";

	
	/*********************************
	 * <Others>
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

        //debug
        initial_setup();
        
        
        
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

    private void initial_setup() {
		/*********************************
		 * 1. Create table
		 * 
		 * 2. Insert data => "tapeatalk"
		 *********************************/
//    	Methods.drop_table(this, MainActv.dbName, MainActv.tName_ai);
    	
    	
    	boolean res = create_table(
    					MainActv.dbName, MainActv.tName_main,
    					MainActv.cols_main, MainActv.col_types_main);
    	
    	
    	if (res == false) {
			
    		// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "res == false");
			
			return;
    		
		}//if (res == false)
    	
    	/*********************************
		 * 2. Insert data => "tapeatalk"
		 *********************************/
    	File[] f_list = Methods.get_file_list(StringUtils.join(
						new String[]{MainActv.dPath_ExternalStorage, MainActv.fName_tapeatalk},
						File.separator));
    	
    	String f_full_path = StringUtils.join(
					new String[]{
							MainActv.dPath_ExternalStorage,
							MainActv.fName_tapeatalk},
					File.separator);
    	
    	res = Methods.refresh_db(this, MainActv.dbName, MainActv.tName_main, f_full_path);
    	
    	
	}//private void initial_setup()
    

	private boolean create_table(
					String dbName, String table_name,
					String[] cols, String[] col_types) {
		/*********************************
		 * memo
		 *********************************/
		DBUtils dbu = new DBUtils(this, dbName);
		
		//
		SQLiteDatabase rdb = dbu.getReadableDatabase();

		boolean res = dbu.tableExists(rdb, table_name);
		
		if (res == true) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + table_name);
			
			rdb.close();
			
			return true;

		} else {//if (condition)
			
			SQLiteDatabase wdb = dbu.getWritableDatabase();
			
			res = dbu.createTable(
					wdb, 
					table_name,
					cols,
					col_types);

			if (res == true) {
				// Log
				Log.d("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: " + table_name);
				
				wdb.close();
				
				return true;
				
			} else {//if (res == true)
				// Log
				Log.d("MainActv.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]",
						"Create table failed: " + table_name);
				
				wdb.close();
				
				return false;
				
			}//if (res == true)
		}//if (condition)
				
	}//private boolean create_table()
	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_actv, menu);
        return true;
    }

}//public class MainActv extends Activity
