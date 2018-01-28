package wang.olddriver.lottery;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Results {
	public static int getpage(int page) throws IOException {
		int nextpage = 0;
		String url = "http://cp.zgzcw.com/dc/getKaijiangFootBall.action?";
		Connection con = Jsoup.connect(url);		
		Map<String,String> data = new HashMap<String,String>();
		data.put("jumpPage", ""+page);
		con.data(data);		
		Document doc = con.post();
		Elements trlist = doc.select("table tbody tr");
		String line,league,matchid,time,home,soccer,away,result,rq,rqesult1,totalballs,halfall;
		for(Element element:trlist) {
			league = element.select("td").get(1).text();
			line = league+" ";
			matchid = element.select("td").get(2).attr("tid");
			line += matchid+" ";
			time = element.select("td").get(2).text();
			line += time+" ";
			home = element.select("td").get(3).text();
			line += home+" ";
			soccer = element.select("td").get(4).text();
			line += soccer+" ";
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
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int page = 1;
		
		while(page!=0) {
			System.out.println("正在取的页数："+page);
			page = getpage(page);
			System.out.println("页数："+page);
		}

	}
}

