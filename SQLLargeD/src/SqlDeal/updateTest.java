package SqlDeal;

import java.util.ArrayList;
import java.util.List;

public class updateTest {
	public static void main(String[] args) throws Exception {
		jdbcUtil jdbcUtil = new jdbcUtil();
		
		jdbcUtil.getConnection();
		String sql = "insert into USRTEST(USR,USR_NM,DES,PASSWORD) value(?,?,?,?)";
		List<Object> params = new ArrayList<Object>();
		params.add("mok");
		params.add("poook");
		params.add("description");
		params.add("123456");
		
		try {
			long startTime1 = System.currentTimeMillis();
			long endTime1 = System.currentTimeMillis();
			long costTime1 = endTime1-startTime1;
			long startTime = System.currentTimeMillis();
//			boolean result = jdbcUtil.updateByPreparadStatement(sql, params);
			long endTime = System.currentTimeMillis();
//			System.out.println("add result: "+result);
			System.out.println("time cost: "+ (endTime-startTime-costTime1));
			
			long startTime2 = System.currentTimeMillis();
			jdbcUtil.batchUpdateByPreparadStatement(sql, params);
			long endTime2 = System.currentTimeMillis();
			System.out.println("time cost: "+ ((endTime2-startTime2-costTime1)*0.001) + "s");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			jdbcUtil.relessConnection();
		}
	}
}
