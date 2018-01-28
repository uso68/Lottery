package wang.olddriver.lottery;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	public static List<Map<String,String>> FetchMatches(String dt) throws IOException{
		List<Map<String,String>> matchList = new ArrayList<Map<String,String>>();
		String url = "";
		if(dt==null) {
			url = "http://cp.zgzcw.com/lottery/jchtplayvsForJsp.action?lotteryId=47&type=jcmini";
		}else {
			url = "http://cp.zgzcw.com/lottery/jchtplayvsForJsp.action?lotteryId=47&type=jcmini&issue="+dt;
		}
		
		            
		Document doc = Jsoup.connect(url).get();
		Elements tr = doc.select("table.mb tbody tr");
		String matchtime,matchid,leagueid,matchclass,homerank,homename,homeid,awayname,awayid,awayrank,
		rodds3,rodds1,rodds0,euodds3,euodds1,euodds0;
		String line;
		
		for(Element element : tr) {
			Map<String,String> match = new HashMap<String,String>();
			
			matchtime = element.select("tr").attr("t");
			match.put("MatchTime", matchtime);
			matchid = element.select("td.wh-1").select("a").attr("id").replace("show_", "");
			match.put("MatchId", matchid);
			
			matchclass = element.select("tr").attr("m");
			match.put("MatchClass", matchclass);
			
			leagueid = element.select("td.wh-2").select("a").attr("href").replace("http://saishi.zgzcw.com/soccer/league/","");
			if(leagueid.contains("cup")) {
				leagueid = element.select("td.wh-2").select("a").attr("href").replace("http://saishi.zgzcw.com/soccer/cup/","");
			}
			match.put("LeagueId", leagueid);
			
			homerank = element.select("td.wh-4").select("em").text();
			match.put("HomeRank", homerank);
			
			homename = element.select("td.wh-4").select("a").text();
			
			homeid = element.select("td.wh-4").select("a").attr("href").replace("http://saishi.zgzcw.com/soccer/team/","");
			match.put("HomeId", homeid);
			
			awayid = element.select("td.wh-6").select("a").attr("href").replace("http://saishi.zgzcw.com/soccer/team/","");
			match.put("AwayId", awayid);
			
			awayname = element.select("td.wh-6").select("a").text();
			awayrank = element.select("td.wh-6").select("em").text();
			match.put("AwayRank", awayrank);
			
			Elements fodds = element.select("td.wh-8 div.frq a");
			
			String odds = "";	
			
			if(fodds.size()==3) {
				String odds3 = fodds.get(0).text();
				match.put("ODDS3",odds3);
				String odds1 = fodds.get(1).text();
				match.put("ODDS1",odds1);
				String odds0 = fodds.get(2).text();
				match.put("ODDS0",odds0);
				odds = odds3+" "+odds1+" "+odds0;
				//System.out.println(odds);
				
				}
			
			Elements rodds = element.select("td.wh-8 div.rqq a");
			String rrodds = "";
			if(rodds.size()==3) {
				match.put("RQ", element.select("td.wh-8 div.rqq em").first().text());	
				rodds3 = rodds.get(0).text();
				match.put("RODDS3",rodds3);
				rodds1 = rodds.get(1).text();
				match.put("RODDS1",rodds1);
				rodds0 = rodds.get(2).text();
				match.put("RODDS0",rodds0);
				rrodds = rodds3+" "+rodds1+" "+rodds0;
				//System.out.println(rrodds);
			}
			String euodds = "";
			euodds3 = element.select("td.wh-11 div").select("span").get(1).text();
			match.put("EUODDS3",euodds3);
			euodds1 = element.select("td.wh-11 div").select("span").get(2).text();
			match.put("EUODDS1",euodds1);
			euodds0 = element.select("td.wh-11 div").select("span").get(0).text();
			match.put("EUODDS0",euodds0);
			euodds = euodds3+" "+euodds1+" "+euodds0;
			
			
			line = matchtime+" "+matchid+" "+leagueid+" "+matchclass+" "+homerank+" "+"("+homeid+")"+" "+
			homename+" VS "+awayname+" "+"("+awayid+")"+" "+awayrank+" "+odds+","+rrodds+","+euodds;
			System.out.println(line);
			
			
			matchList.add(match);
		}
		
		return matchList;
		
	}
	public static int getpage(int page) throws IOException {
		int nextpage = 0;
		String url = "http://cp.zgzcw.com/dc/getKaijiangFootBall.action?";
		Connection con = Jsoup.connect(url);		
		Map<String,String> data = new HashMap<String,String>();
		data.put("jumpPage", ""+page);
		con.data(data);		
		Document doc = con.post();
		Elements trlist = doc.select("table tbody tr");
		String line,league,matchid,time,home,score,away,result,rq,rqesult1,totalballs,halfall;
		for(Element element:trlist) {
			league = element.select("td").get(1).text();
			line = league+" ";
			matchid = element.select("td").get(2).attr("tid");
			line += matchid+" ";
			time = element.select("td").get(2).text();
			line += time+" ";
			home = element.select("td").get(3).text();
			line += home+" ";
			score = element.select("td").get(4).text();
			if(score.isEmpty()) {
				continue;
			}
			line += score+" ";
			away = element.select("td").get(5).text();
			line += away+" ";
			result = element.select("td").get(6).text();
			line += result+" ";
			rq = element.select("td").get(7).text();
			line += rq+" ";
			rqesult1 = element.select("td").get(8).text();
			line += rqesult1+" ";
			totalballs = element.select("td").get(10).text();
			line += totalballs+" ";
			halfall = element.select("td").get(11).text();
			line += halfall;
			System.out.println(line);
		}
		
		page += 1;
		
		//javascript:page(8)
		Element a = doc.select("div.page-left a").last();
		System.out.println("javascript:page("+page+")");
		System.out.println(a.attr("href"));
		if(a.attr("href").equals("javascript:page("+page+")")) {
			nextpage = page;
			
		}
		
		return nextpage;
	}
	
	public static List<Map<String,String>> fetchDateResults(String dt) throws ParseException, IOException{
		List<Map<String,String>> rlist = new ArrayList<Map<String,String>>();
		
		if(dt==null) {
			SimpleDateFormat dte = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			//startTime=2016-02-01&endTime=2017-10-02&league=
			dt = dte.format(calendar.getTime());
		}
		String url = String.format("http://cp.zgzcw.com/dc/getKaijiangFootBall.action?startTime=%s&endTime=%s&league=",
				dt,dt);
		Connection con = Jsoup.connect(url);
		int page = 1;
		Map<String,String> data = new HashMap<String,String>();
		data.put("jumpPage", ""+page);
		con.data(data);
		while(true) {
			Document doc = con.post();
			Elements trlist = doc.select("table tbody tr");
			String line,league,matchid,time,home,score,hometotalscore,awaytotalscore,homehalfscore,awayhalfscore,away,result,rq,RQresult,totalballs,halfall;
			for(Element element:trlist) {
				Map<String,String> r = new HashMap<String,String>();
				league = element.select("td").get(1).text();
				//r.put("MatchClass", league);
				line = league+" ";
				matchid = element.select("td").get(2).attr("tid");
				r.put("MatchId", matchid);
				line += matchid+" ";
				time = element.select("td").get(2).text();
				r.put("MatchTime", time);
				line += time+" ";
				home = element.select("td").get(3).text();
				//r.put("HomeName", home);
				line += home+" ";
				score = element.select("td").get(4).text();
				// 0:0(0:0)
				score = score.replaceAll(" ", "");
				String[] temp = score.split("\\(");
				String[] temp1 = temp[0].split(":");
				hometotalscore = temp1[0];
				r.put("HomeTotalScore", hometotalscore);
				awaytotalscore = temp1[1];
				r.put("AwayTotalScore", awaytotalscore);
				String[] temp2 = temp[1].split(":");
				homehalfscore = temp2[0];
				r.put("HomeHalfScore", homehalfscore);
				awayhalfscore = temp2[1].replace(")","");
				r.put("AwayHalfScore", awayhalfscore);
				line += score+" ";
				away = element.select("td").get(5).text();
				line += away+" ";
				result = element.select("td").get(6).text();
				if(result.isEmpty()==false) {
					r.put("Status", "4");
				};
				r.put("Result", result);
				line += result+" ";
				rq = element.select("td").get(7).text();
				line += rq+" ";
				RQresult = element.select("td").get(8).text();
				r.put("RQResult", RQresult);
				line += RQresult+" ";
				totalballs = element.select("td").get(10).text();
				r.put("TotalGoals", totalballs);
				line += totalballs+" ";
				halfall = element.select("td").get(11).text();
				line += halfall;
				System.out.println(line);
				rlist.add(r);
				
				
				
			}
			// 查查 a.pre, 如果 a.text() equals "下一页", 则 page += 1，继续，否则 break
			Element pagelink = doc.select("div.page-left a.pre").last();
			if (pagelink !=null&&pagelink.text().equals("下一页")) {
				page += 1;
			}else {
				break;
			}
		}
		return rlist;
	}
	
	public static List<Map<String,String>> fetchResults(int page) throws IOException{
		List<Map<String,String>> results = new ArrayList<Map<String,String>>();
		String url = "http://cp.zgzcw.com/dc/getKaijiangFootBall.action?";
				   // http://cp.zgzcw.com/dc/getKaijiangFootBall.action?startTime=2017-10-03&endTime=2017-10-03&league=
		Connection con = Jsoup.connect(url);		
		for(int i=0;i<results.size();i++) {
		Map<String,String> data = new HashMap<String,String>();
		data.put("jumpPage", ""+page);
		con.data(data);		
		Document doc = con.post();
		Elements trlist = doc.select("table tbody tr");
		String line,league,matchid,time,home,score,hometotalscore,awaytotalscore,homehalfscore,awayhalfscore,away,result,rq,RQresult,totalballs,halfall;
		for(Element element:trlist) 
		{
			Map<String,String> r = new HashMap<String,String>();
			league = element.select("td").get(1).text();
			r.put("MatchClass", league);
			line = league+" ";
			matchid = element.select("td").get(2).attr("tid");
			r.put("MatchId", matchid);
			line += matchid+" ";
			time = element.select("td").get(2).text();
			r.put("MatchTime", time);
			line += time+" ";
			home = element.select("td").get(3).text();
			r.put("HomeName", home);
			line += home+" ";
			score = element.select("td").get(4).text();
			// 0:0(0:0)
			score = score.replaceAll(" ", "");
			String[] temp = score.split("\\(");
			String[] temp1 = temp[0].split(":");
			hometotalscore = temp1[0];
			r.put("HomeTotalScore", hometotalscore);
			awaytotalscore = temp1[1];
			r.put("AwayTotalScore", awaytotalscore);
			String[] temp2 = temp[1].split(":");
			homehalfscore = temp2[0];
			r.put("HomeHalfScore", homehalfscore);
			awayhalfscore = temp2[1].replace(")","");
			r.put("AwayHalfScore", awayhalfscore);
			line += score+" ";
			away = element.select("td").get(5).text();
			line += away+" ";
			result = element.select("td").get(6).text();
			r.put("Result", result);
			line += result+" ";
			rq = element.select("td").get(7).text();
			line += rq+" ";
			RQresult = element.select("td").get(8).text();
			r.put("RQResult", RQresult);
			line += RQresult+" ";
			totalballs = element.select("td").get(10).text();
			r.put("TotalGoals", totalballs);
			line += totalballs+" ";
			halfall = element.select("td").get(11).text();
			line += halfall;
			System.out.println(line);
			results.add(r);
			
			}
		
		}
		return results;
	}
		
	public static Map<String,String> FetchTeam(String id){
		Map<String,String> tm = new HashMap<String,String>();
		return tm;
		
	}
}
