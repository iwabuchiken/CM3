package cm3.items;

public class AI {
	/*----------------------------
	 * Class fields
		----------------------------*/
	String file_name;
	String title;
	String memo;
	long last_played_at;
	String table_name;
	
	/*----------------------------
	 * Constructor
		----------------------------*/
	public AI(String file_name, String title, String memo, long last_played_at, String table_name) {

		this.file_name = file_name;
		this.title = title;
		this.memo = memo;
		this.last_played_at = last_played_at;
		this.table_name = table_name;
		
	}//public ThumbnailItem(long fileId, String file_path, long date_added, long date_modified)

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public long getLast_played_at() {
		return last_played_at;
	}

	public void setLast_played_at(long last_played_at) {
		this.last_played_at = last_played_at;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	
	/*----------------------------
	 * Methods
		----------------------------*/
	
}//public class ThumbnailItem
