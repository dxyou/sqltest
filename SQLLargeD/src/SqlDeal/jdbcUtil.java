package SqlDeal;

import java.lang.reflect.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.*;

@SuppressWarnings("unused")
public class jdbcUtil {
	
	private final String USERNAME = "MENO";
	private final String PASSWORD = "12345";
	private final String LINKURL = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=GMT%2B8";
	private final String DRIVE = "com.mysql.cj.jdbc.Driver";
	
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	public jdbcUtil() throws ClassNotFoundException {
		Class.forName(DRIVE);
	}
	
	/**
	 * 
	 * @Description: ��ȡ�����ݿ������
	 * @date 2018��8��31������10:52:15
	 * @return Connection
	 * @return
	 */
	public Connection getConnection() {

		try {
			connection =  DriverManager.getConnection(LINKURL, USERNAME, PASSWORD);
		}catch(Exception e){
			e.printStackTrace();
		}
		return connection;
	}
	
	/*
	 * �ر�����
	 * @throws SQLException
	 */
	public void relessConnection() throws SQLException {
		if(resultSet!=null) {
			resultSet.close();
		}
		if(preparedStatement!=null) {
			preparedStatement.close();
		}
		if(connection!=null) {
			connection.close();
		}
	}
	
	/**
	 * 
	 * @Description: ��ɾ��
	 * @date 2018��8��31������10:58:52
	 * @return Boolean
	 * @param sql	 sql���
	 * @param params ����
	 * @return
	 * @throws SQLException
	 */
	public boolean updateByPreparadStatement(String sql, List<Object> params) throws SQLException {
		boolean flag = false;
		int result = -1;
		int index = 1;
		preparedStatement = connection.prepareStatement(sql); //��ȡsql���
		if(params != null && !params.isEmpty()) {
			for(int i=0;i<params.size();i++) {
				preparedStatement.setObject(index++, params.get(i)); //�����������
			}
		}
		
		result = preparedStatement.executeUpdate();  //ִ��sql
		System.out.println("result: "+result);
		flag = result >0 ? true : false;
		return flag;
		
	}
	
	/**
	 * 
	 * @Description: ��������ɾ��
	 * @date 2018��8��31������3:30:37
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public void batchUpdateByPreparadStatement(String sql, List<Object> params) throws SQLException {
		boolean flag = false;
		int result = -1;
		int index = 1;
		int batchSize = 5000;
		int count = 50000;
		String url = LINKURL.concat("&rewriteBatchedStatements=true");
		connection =  DriverManager.getConnection(url, USERNAME, PASSWORD);
		
		connection.setAutoCommit(false);
		preparedStatement = connection.prepareStatement(sql); //��ȡsql���
		for(int j=0;j<=count;j++) {
			int i;
			index = 1;
			if(params != null && !params.isEmpty()) {
				for(i=0;i<params.size();i++) {
					preparedStatement.setObject(index++, params.get(i)); //�����������
				}
			}
			preparedStatement.addBatch();
			if(j % batchSize == 0) {
				preparedStatement.executeBatch();
				connection.commit();
				System.out.println("add "+ batchSize +" recodes");
			}
		}
		
		if((count)%batchSize !=0) {
			preparedStatement.executeBatch();
			connection.commit();
		}
		
		this.relessConnection();
	}
	
	
	/**
	 * 
	 * @Description: ��ѯ����
	 * @date 2018��8��31������11:26:17
	 * @return Map<String,Object>
	 * @param sql
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> findSimpleResult(String sql, List<Object> param) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		preparedStatement = connection.prepareStatement(sql);
		int index = 1;
		if(param != null && !param.isEmpty()) {
			for(int i = 0;i<param.size();i++) {
				preparedStatement.setObject(index++, param.get(i));
			}
		}
		resultSet = preparedStatement.executeQuery(); //��ѯ���ص�����
		ResultSetMetaData metaData = preparedStatement.getMetaData(); //��ȡ�н��
		int cols_len = metaData.getColumnCount(); //������
		
		while (resultSet.next()) {
			for(int i = 0;i<cols_len;i++) {
				String col_nm = metaData.getColumnName(i+1);
				Object col_value = resultSet.getObject(col_nm);
				if(col_value == null) {
					col_value = "";
				}
				map.put(col_nm, col_value);
			}
		}
		return map;
		
	}
	
	/**
	 * 
	 * @Description: ������ѯ
	 * @date 2018��8��31������11:33:19
	 * @return List<Map<String,Object>>
	 * @param sql
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> findMultiResult(String sql, List<Object> param) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		preparedStatement = connection.prepareStatement(sql);
		int index = 1;
		if(param != null && !param.isEmpty()) {
			for(int i = 0;i<param.size();i++) {
				preparedStatement.setObject(index++, param.get(i));
			}
		}
		resultSet = preparedStatement.executeQuery(); //��ѯ���ص�����
		ResultSetMetaData metaData = preparedStatement.getMetaData(); //��ȡ�н��
		
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			int cols_len = metaData.getColumnCount(); //������
			for(int i = 0;i<cols_len;i++) {
				String col_nm = metaData.getColumnName(i+1);  	//����
				Object col_value = resultSet.getObject(col_nm);	//��ֵ
				if(col_value == null) {
					col_value = "";
				}
				map.put(col_nm, col_value);
			}
			list.add(map);
		}
		return list;
		
	}
}
