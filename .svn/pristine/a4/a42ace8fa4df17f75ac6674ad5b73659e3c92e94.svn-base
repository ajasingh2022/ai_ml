package com.skvader.rsp.cft.common.datainterface.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.skvader.rsp.cft.common.datainterface.database.connection.iface.TransactionManager;
import com.skvader.rsp.cft.common.datainterface.database.connection.impl.DatabaseMode;
import com.skvader.rsp.cft.common.exception.StoredProcedureException;

public class ConfigDatabaseUtils {

	
	private static void appendStandardParams(TransactionManager tm, StringBuilder sql){
		if(DatabaseMode.isSqlServer(tm)){
			sql.append("?,?,?,?,?)}"); //Standard Parameters for Error handling			
		} else {
			sql.append("?,?,?,?)}"); 
		}
		
	}
	
	public static ResultSet executeProcedure(TransactionManager tm,String procedureName,Object... arguments) throws SQLException, StoredProcedureException{
		Connection conn = tm.getConnection(); 
		CallableStatement cs = null;
		 ResultSet rs=null;
			StringBuilder sql = new StringBuilder();
			sql.append("{call ").append(procedureName).append("(");
			for(int i = 0; i < arguments.length; i++) {
				sql.append("?,");
			}
			appendStandardParams(tm, sql);
			cs = conn.prepareCall(sql.toString());
			int i;
			for(i = 1; i <= arguments.length; i++) {
				Object argument = arguments[i - 1];
				 if(argument instanceof String) {
					cs.setString(i, (String)argument);	
				 }
			}
			tm.setStandardParameters(i, cs, tm.getMode());
			rs = cs.executeQuery();
			return rs;
			 
			}
}
