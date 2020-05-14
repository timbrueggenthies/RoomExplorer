
class SqLiteDatabaseInspector implements DatabaseInspector {
	
	private SupportSQLiteDatabase database;
	
	public SqLiteDatabaseInspector(SupportSQLiteDatabase db) {
		this.database = db;
	}
	
	List<String> getTables() {
		String tableQuery = "SELECT name _id FROM sqlite_master WHERE type ='table'";
		Cursor cursor = database.query(tableQuery);
		List<List<Object>> entries;
		try(cursor) {
			entries = getAllColumns(cursor)
		} catch (Exception e) {
			
		}
		
		List<String> result = new ArrayList(entries.size());
		for (List<Object> column in entries) {
			if (column.size() != 1) throw new RuntimeException();
			result.add((String) column.get(0));
		}
		return result;
	}
	
	List<String> getColumnsForTable(String tableName) {
		String tableQuery = "SELECT * FROM " + tableName + " LIMIT 0";
		Cursor cursor = database.query(tableQuery);
		String[] columnNames;
		try(cursor) {
			columnNames = cursor.getColumnNames();
		} catch (Exception e) {
			
		}
		List<String> result = new ArrayList(columnNames.length());
		for (String columnName in columnNames) {
			result.add(columnName);
		}
		return result;
	}
	
	List<List<Object>> getEntries(String tableName) {
		String tableQuery = "SELECT * FROM " + tableName;
		Cursor cursor = database.query(tableQuery);
		List<List<Object>> result;
		try(cursor) {
			result = getAllColumns(cursor);
		} catch (Exception e) {
			
		}
		return result;
	}
	
	List<List<Object>> getEntries(String tableName, List<String> columns) {
		String columnString = "";
		for (int i = 0; i < columns.size() - 1; i++) {
			columnString += columns.get(i) + ", ";
		}
		columnString += columns.get(columns.size() - 1);
		String tableQuery = "SELECT " + columnString + " FROM " + tableName;
		Cursor cursor = database.query(tableQuery);
		List<List<Object>> result;
		try(cursor) {
			result = getAllColumns(cursor);
		} catch (Exception e) {
			
		}
		return result;
	}
	
	private List<List<Object>> getAllColumns(Cursor cursor) {
		List<List<Object>> result = new ArrayList(cursor.getCount());
		boolean contentAvailable = cursor.moveToFirst();
		while(contentAvailable) {
			result.add(getCurrentColumn(cursor));
			cursor.moveToNext();
		}
		return result;
	}
	
	private List<Object> getCurrentColumn(Cursor cursor) {
		int columnCount = cursor.getColumnCount();
		List<Object> result = new ArrayList(columnCount);
		for(int column = 0; column < columnCount; column++) {
			int columnType = cursor.getType(column)
			Object entry;
			switch(columnType) {
				case Cursor.FIELD_TYPE_FLOAT:
					entry = new Float(getFloat(column));
					break;
				case Cursor.FIELD_TYPE_INTEGER:
					entry = new Integer(getInteger(column));
					break;
				case Cursor.FIELD_TYPE_STRING:
					entry = getString(column);
					break;
				case Cursor.FIELD_TYPE_NULL:
					entry = null;
					break;
			}
			result.add(entry);
		}
		return result;
	}
	
}