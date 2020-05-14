
interface DatabaseInspector {
	
	List<String> getTables();
	
	List<String> getColumnsForTable(String tableName);
	
	List<List<Object>> getEntries(String tableName);
	
	List<List<Object>> getEntries(String tableName, List<String> columns);
	
}