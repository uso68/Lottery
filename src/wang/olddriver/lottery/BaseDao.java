package wang.olddriver.lottery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class BaseDao {
	private static String driver = "org.mariadb.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/lottery";
	private static String user = "root";
	private static String password = "sn1234";
	private static Stack<Connection> pool = new Stack<Connection>();
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		if(pool.size()!=0) {
			return pool.pop();
			
		}
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	
	public static int backConnection(Connection conn) {
		if(pool.size()>=10) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			pool.push(conn);
		}
		
		return pool.size();
		
	}
	// 比赛
	public static void insertMatch(Map<String,String> m) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		String sql = String.format("INSERT INTO tb_match "
				+ "(MatchId,MatchTime,MatchClass,"
				+ "HomeId,AwayId,Status,"
				+ "ODDS3,ODDS1,ODDS0,"
				+ "RQ,RODDS3,RODDS1,RODDS0,"
				+ "EUODDS3,EUODDS1,EUODDS0,"
				+ "HomeTotalScore,AwayTotalScore,HomeHalfScore,AwayHalfScore,"
				+ "Result,RQResult,TotalGoals) VALUES (%s,'%s','%s',%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)",
				m.get("MatchId"), 
				m.get("MatchTime"),
				m.get("MatchClass"),
				m.get("HomeId"),
				m.get("AwayId"),
				m.get("Status"),
				m.get("ODDS3"),
				m.get("ODDS1"),
				m.get("ODDS0"),
				m.get("RQ"),
				m.get("RODDS3"),
				m.get("RODDS1"),
				m.get("RODDS0"),
				m.get("EUODDS3"),
				m.get("EUODDS1"),
				m.get("EUODDS0"),
				m.get("HomeTotalScore"),
				m.get("AwayTotalScore"),
				m.get("HomeHalfScore"),
				m.get("AwayHalfScore"),
				m.get("Result"),
				m.get("RQResult"),
				m.get("TotalGoals"));
		stmt.executeQuery(sql);
		backConnection(conn);
	}
	public static Map<String,String> findMatch(String id) throws ClassNotFoundException, SQLException {
		Map<String,String> m = null; //new HashMap<String,String>();
		String sql = String.format("SELECT * FROM tb_match WHERE MatchId = %s", id);
		
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		
		if (rs.next()) {
			m = new HashMap<String,String>();
			ResultSetMetaData rsmd = rs.getMetaData();
			int count=rsmd.getColumnCount();
			for(int i = 1;i<=count;i++) {
				String name = rsmd.getColumnName(i);
				m.put(name, rs.getString(i));
				
			}
			rs.close();
		}
		else {
			//return null;
		}
		backConnection(conn);
		
		return m;
	}
	
	public static List<Map<String,String>> findMatches(String fieldlist,String condition) throws ClassNotFoundException, SQLException {
		List<Map<String,String>> mlist = new ArrayList<Map<String,String>>();
		String sql = String.format("SELECT %s FROM tb_match WHERE %s", fieldlist,condition);
		
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		
		while (rs.next()) {
			Map<String,String> m = new HashMap<String,String>();
			ResultSetMetaData rsmd = rs.getMetaData();
			int count=rsmd.getColumnCount();
			for(int i = 1;i<=count;i++) {
				String name = rsmd.getColumnName(i);
				m.put(name, rs.getString(i));
				
			}
			mlist.add(m);
			
		}
		rs.close();
		backConnection(conn);
		
		return mlist;
	}
	
	public static void updateMatch(Map<String,String> m) throws ClassNotFoundException, SQLException {
		String sql = String.format("UPDATE tb_match SET "
				+ "MatchTime='%s',MatchClass='%s',"
				+ "HomeId=%s,HomeRank=%s,AwayId=%s,AwayRank=%s,"
				+ "ODDS3=%s,ODDS1=%s,ODDS0=%s,"
				+ "RQ=%s,RODDS3=%s,RODDS1=%s,RODDS0=%s,"
				+ "EUODDS3=%s,EUODDS1=%s,EUODDS0=%s "
				+ "WHERE MatchId=%s",
				m.get("MatchTime")
				,m.get("MatchClass")
				,m.get("HomeId")
				,0//m.get("HomeRank")
				,m.get("AwayId")
				,0//m.get("AwayRank")
				,m.get("ODDS3")
				,m.get("ODDS1")
				,m.get("ODDS0")
				,m.get("RQ")
				,m.get("RODDS3")
				,m.get("RODDS1")
				,m.get("RODDS0")
				,m.get("EUODDS3")
				,m.get("EUODDS1")
				,m.get("EUODDS0")
				,m.get("MatchId"));
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		try
		{
			int rs = stmt.executeUpdate(sql);	
		}
		catch(Exception e) {
			System.out.println(sql);
		}

		backConnection(conn);
	}
	// 球队
	public static void insertTeam(Map<String,String> m) {
		
	}
	public static Map<String,String> findTeam(String id) {
		Map<String,String> m = new HashMap<String,String>();
		return m;
	}
	public static void updateTeam(Map<String,String> m) {
		
	}
	
	// 赔率
	public static void appendOdds(Map<String,String> m) {
		
	}
	
	// 联赛
	public static void insertLeague(Map<String,String> m) {
		
	}
	public static Map<String,String> findLeague(String id) {
		Map<String,String> m = new HashMap<String,String>();
		return m;
	}
	public static void updateLeague(Map<String,String> m) {
		
	}
	public static int updateMatchResult(Map<String,String> m) throws ClassNotFoundException, SQLException {
		Connection conn = getConnection();
		Statement stmt = conn.createStatement();
		if(m.get("Result")=="胜") {
			m.replace("Result", "3");
		}else if(m.get("Result")=="平"){
			m.replace("Result", "1");
		}else {
			m.replace("Result", "0");
		}
		if(m.get("RQResult")=="胜") {
			m.replace("RQResult", "3");
		}else if(m.get("RQResult")=="平"){
			m.replace("RQResult", "1");
		}else {
			m.replace("RQResult", "0");
		}
		String status = m.get("Status");
		
		
		String sql = String.format("UPDATE tb_match SET HomeTotalScore = %s,"
				+ "AwayTotalScore=%S,"
				+ "HomeHalfScore=%s,"
				+ "AwayHalfScore=%s,"
				+ "Status=%s,"
				+ "Result=%s,"
				+ "RQResult=%s,"
				+ "TotalGoals=%s WHERE MatchId=%s"
				,m.get("HomeTotalScore")
				,m.get("AwayTotalScore")
				,m.get("HomeHalfScore")
				,m.get("AwayHalfScore")
				,status
				,m.get("Result")
				,m.get("RQResult")
				,m.get("TotalGoals")
				,m.get("MatchId"));
		int res = stmt.executeUpdate(sql);
		backConnection(conn);
		
		return res;
	}
	
	
}
