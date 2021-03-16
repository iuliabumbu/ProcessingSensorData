package data;
import java.util.Calendar;
import java.util.Date;


public class MonitoredData {
	private Date start;
	private Date end;
	private String activity;
	
	public MonitoredData(Date start, Date end, String activity) {
		this.start = start;
		this.end = end;
		this.activity = activity;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Override
	public String toString() {
		return "MonitoredData [start=" + start + ", end=" + end + ", activity=" + activity + "]\r\n";
	}
	
	public int getDistinctStartDay() {
		int nr = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		int zi = calendar.get(Calendar.DAY_OF_MONTH);
		int luna = calendar.get(Calendar.MONTH);
		nr = luna * 31 + zi;
		
		return nr;
	}
	
	public int getDistinctEndDay() {
		int nr = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(end);
		int zi = calendar.get(Calendar.DAY_OF_MONTH);
		int luna = calendar.get(Calendar.MONTH);
		nr = luna * 31 + zi;
		
		return nr;
	}
	
	public String getStartDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		int zi = calendar.get(Calendar.DAY_OF_MONTH);
		int luna = calendar.get(Calendar.MONTH) + 1;
		int an = calendar.get(Calendar.YEAR);
		String szi, sluna;
		if(zi >= 10) {
			szi = zi +"";
		}
		else {
			szi = "0" + zi;
		}
		if(luna >= 10) {
			sluna = luna +"";
		}
		else {
			sluna = "0" + luna;
		}
		String data = szi + ":" + sluna + ":" + an;
		return data;
	}
	public String getEndDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(end);
		int zi = calendar.get(Calendar.DAY_OF_MONTH);
		int luna = calendar.get(Calendar.MONTH) + 1;
		int an = calendar.get(Calendar.YEAR);
		String szi, sluna;
		if(zi >= 10) {
			szi = zi +"";
		}
		else {
			szi = "0" + zi;
		}
		if(luna >= 10) {
			sluna = luna +"";
		}
		else {
			sluna = "0" + luna;
		}
		String data = szi + ":" + sluna + ":" + an;
		return data;
	}
	
	
	public Long getDifferenceBetweenDates() {
		Long diffInMillies = end.getTime() - start.getTime();
	
		return diffInMillies;
	}
	

	

}
