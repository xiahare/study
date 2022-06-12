package projemp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class WorkScheduling {
	static private HashMap map = new HashMap();
	static {
		map.put("0", "日");
		map.put("1", "一");
		map.put("2", "二");
		map.put("3", "三");
		map.put("4", "四");
		map.put("5", "五");
		map.put("6", "六");
	}
	public static void main(String[] args) {
		
		// first one
		LinkedPerson root = new LinkedPerson();
		root.name = 'A';
		LinkedPerson p = root;
		
		// 9 persons
		int personNumber= 9;
		for (char i = 66; i< 65+personNumber; i++) {
			LinkedPerson t = new LinkedPerson();
			t.name = i;
			p.next = t;
			
			p = p.next;
			
			// last one
			if(i==65+personNumber-1) {
				p.next = root;
			}
		}
		
		String str = "hello";
		int idx = str.indexOf("h");
		System.out.println("str.indexOf(\"h\") : " + idx);
		char char2 = str.charAt(2);
		System.out.println("str.charAt(2) : " + char2);
		String substr = str.substring(1, 3);
		System.out.println("str.substring(1, 3) : " + substr);
		
//		// test link
//		p = root;
//		for (int i=0; i< 20; i++ ) {
//			
//			System.out.println(p.name);
//			p = p.next;
//		}
		
		// schedule
		Date d = new Date();  //now
		SimpleDateFormat df = new SimpleDateFormat("MM-dd");
		
		p = root;
		
		for(int j = 0 ; j<365 ; j++) {
			d = new Date( d.getTime()+ 24*60*60*1000+1000 );
			String o = df.format(d) ;
			System.out.print(o + "|" + map.get(String.valueOf(d.getDay())));
			for(int i = 0 ; i<4 ; i++) {
				System.out.print(",");
				System.out.print(p.name);
				p = p.next;
			}
			System.out.println();
		}
		
	}
	
}

class LinkedPerson {
	public char name;
	public LinkedPerson next;
}