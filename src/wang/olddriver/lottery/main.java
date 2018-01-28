package wang.olddriver.lottery;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class main {
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, ParseException, InterruptedException {
		while(true) {
			List<Map<String,String>> matchList = Crawler.FetchMatches(null);
			saveMatchList(matchList);

			List<Map<String,String>> matchResultList = Crawler.fetchDateResults(null);
			saveMatchResultList(matchResultList);
			
			fetchHistoryMatch();
			
			Thread.sleep(3600);
		}
	}
	
	public static void saveMatchList(List<Map<String,String>> matchList) throws ClassNotFoundException, SQLException {
		for(Map<String,String> m:matchList) 
		{
			Map<String, String> mi = BaseDao.findMatch(m.get("MatchId"));
			
			// 比赛
			if (mi == null) {
				BaseDao.insertMatch(m);
			}
			else {
				BaseDao.updateMatch(m);
			}
			
			// 主队ID
			Map<String, String> tm = BaseDao.findTeam(m.get("HomeId"));
			if (tm == null) {
				BaseDao.insertTeam(tm);
			}
			else {
				BaseDao.updateTeam(tm);
			}
			//客队ID
			tm = BaseDao.findTeam(m.get("AwayId"));
			if (tm == null) {
				BaseDao.insertTeam(tm);
			}
			else {
				BaseDao.updateTeam(tm);
			}
			//赛事
			Map<String, String> lg = BaseDao.findLeague(m.get("LeagueId"));
			if (lg == null) {
				BaseDao.insertLeague(m);
			}
			else {
				BaseDao.updateLeague(m);
			}
			//赔率
			BaseDao.appendOdds(m);
			
		}
	}
	
	
	public static void saveMatchResultList(List<Map<String,String>> matchResultList) throws IOException, ClassNotFoundException, SQLException, ParseException {
		for(Map<String,String> m:matchResultList) {
			int res = BaseDao.updateMatchResult(m);
			
			if(res==0) {
				//10-03 23:30
				Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				String[] temp = m.get("MatchTime").trim().split(" ");
				String dt = year+"-"+temp[0];
				
				List<Map<String,String>> matchList = Crawler.FetchMatches(dt);
				saveMatchList(matchList);
				SimpleDateFormat dte = new SimpleDateFormat("yyyy-MM-dd");
				Date date = dte.parse(dt);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date); 
				calendar.add(calendar.DATE,-1);
				dt = dte.format(calendar.getTime());				
				
				matchList = Crawler.FetchMatches(dt);
				saveMatchList(matchList);
				BaseDao.updateMatchResult(m);
			}
		}
	}

	public static void fetchHistoryMatch() throws ClassNotFoundException, SQLException, ParseException, IOException {
		String condition = "MatchTime ORDER BY MatchTime LIMIT 1;";
		
		List<Map<String,String>> mlist = BaseDao.findMatches("MatchTime",condition);
		if(mlist.size()==1) {
			String dt = mlist.get(0).get("MatchTime");
			SimpleDateFormat dte = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dte.parse(dt);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date); 
			calendar.add(calendar.DATE,-1);
			dt = dte.format(calendar.getTime());
			
			System.out.println(dt);
			List<Map<String,String>> matchResultList = Crawler.fetchDateResults(dt);
			saveMatchResultList(matchResultList);
			
		}
		
	}
	
}
