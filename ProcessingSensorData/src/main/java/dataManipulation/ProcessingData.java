package dataManipulation;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import data.MonitoredData;
import fileWriter.FileOperations;


public class ProcessingData {
	private ArrayList<MonitoredData> sirDate = new ArrayList<MonitoredData>();

	public ArrayList<MonitoredData> getSirDate() {
		return sirDate;
	}

	public void setSirDate(ArrayList<MonitoredData> sirDate) {
		this.sirDate = sirDate;
	}

	public static ArrayList<String> splitLine(String str){
		return (ArrayList<String>) Stream.of(str.split("  "))
				.map (elem -> new String(elem))
				.collect(Collectors.toList());
	}
	
	public void taskOne(String path) {
		ArrayList<String> list = new ArrayList<String>();

		try (Stream<String> stream = Files.lines(Paths.get(path))) {
			list = (ArrayList<String>) stream.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<String> replacedList = (ArrayList<String>) list.stream()
				.map(s -> s.replaceAll("\\s{2,}", "  ").trim())
				.collect(Collectors.toList());

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		FourFunction<Date, Date, String, MonitoredData> funct4 = (start, end, name)-> new MonitoredData(start, end, name);

		for(String x : replacedList) {
			ArrayList<String> parts = splitLine(x);
			try {
				sirDate.add(funct4.apply(format.parse(parts.get(0)), format.parse(parts.get(1)), parts.get(2)));

			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
		}
		OutputStream out = FileOperations.deschidereFisier(Paths.get("task_1.txt"));
		FileOperations.scriereFisier(out, "TASK 1\r\n");
		sirDate.stream().forEach(s -> FileOperations.scriereFisier(out, s.toString()));
		FileOperations.inchidereFisier(out);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) 
	{
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public void taskTwo() {
		long count = sirDate.stream()
				.filter( distinctByKey(p -> p.getDistinctStartDay())) 
				.count(); 

		long count2 =  sirDate.stream()
				.filter( distinctByKey(p -> p.getDistinctEndDay())) 
				.count(); 

		long val = count>count2 ? count : count2;

		OutputStream out = FileOperations.deschidereFisier(Paths.get("task_2.txt"));
		String rez = "The number of distinct days is : " + val;
		FileOperations.scriereFisier(out, "TASK 2\r\n");
		FileOperations.scriereFisier(out, rez);
		FileOperations.inchidereFisier(out);
	}

	public Map<String, Long> taskThree() {
		Map<String, Long> map = sirDate.stream()
				.collect(Collectors.groupingBy(MonitoredData::getActivity, Collectors.counting()));

		OutputStream out = FileOperations.deschidereFisier(Paths.get("task_3.txt"));
		FileOperations.scriereFisier(out, "TASK 3\r\n");
		map.entrySet().stream().forEach(s -> FileOperations.scriereFisier(out, s.getKey() + ", " + s.getValue() + "\r\n"));
		FileOperations.inchidereFisier(out);
		
		return map;
	}

	public  Map<String, Map<String, Long>> taskFour() {

		 ArrayList<MonitoredData> sirS = (ArrayList<MonitoredData>) sirDate.stream()
	                .map(p -> new MonitoredData(p.getStart(), p.getStart(), p.getActivity()))
	                .collect(Collectors.toList());
		 
		 ArrayList<MonitoredData> sirE = (ArrayList<MonitoredData>) sirDate.stream()
				    .filter(p -> !(p.getStartDate().equals(p.getEndDate())))
	                .map(p -> new MonitoredData(p.getEnd(), p.getEnd(), p.getActivity()))
	                .collect(Collectors.toList());
		 
		 Map<String, Map<String, Long>> map = Stream.concat(sirS.stream(), sirE.stream())	
					.collect(Collectors.groupingBy( MonitoredData::getStartDate, Collectors.groupingBy(MonitoredData::getActivity, Collectors.counting())));
		 
		OutputStream out = FileOperations.deschidereFisier(Paths.get("task_4.txt"));
		FileOperations.scriereFisier(out, "TASK 4\r\n");
		map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(s -> FileOperations.scriereFisier(out, s.getKey() + ", " + s.getValue() + "\r\n"));
		FileOperations.inchidereFisier(out);
		
		return map;
	}


	public Map<String, Duration> taskFive() {
		Map<String, Long> map = sirDate.stream()
				.collect(Collectors.groupingBy( MonitoredData::getActivity, Collectors.summingLong(MonitoredData::getDifferenceBetweenDates)));

		Map<String, Duration> newMap = map.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> Duration.ofMillis(e.getValue())));

		OutputStream out = FileOperations.deschidereFisier(Paths.get("task_5.txt"));
		FileOperations.scriereFisier(out, "TASK 5\r\n");
		newMap.entrySet().stream().forEach(s -> FileOperations.scriereFisier(out, s.getKey() + ", " + 
		        String.format("%d:%02d:%02d", 
				s.getValue().getSeconds()/3600, 
				(s.getValue().getSeconds()%3600)/60, 
				(s.getValue().getSeconds()%3600)%60) + "\r\n"));
		FileOperations.inchidereFisier(out);
		
		return newMap;
	}

	public ArrayList<String> taskSix() {
		Map<String, Long> map = sirDate.stream()
				.collect(Collectors.groupingBy(MonitoredData::getActivity, Collectors.counting()));

		Map<String, Long> mapLess5 = sirDate.stream()
				.filter(p -> p.getDifferenceBetweenDates()/60000 < 5)
				.collect(Collectors.groupingBy(MonitoredData::getActivity, Collectors.counting()));

		BiPredicate<Long, Long> predicate1 = (x, y) -> y > 0.9 * x; 

		ArrayList<String> list =(ArrayList<String>) mapLess5.keySet().stream()
				.filter(k ->predicate1.test(map.get(k),mapLess5.get(k)))
				.collect(Collectors.toList());

		OutputStream out = FileOperations.deschidereFisier(Paths.get("task_6.txt"));
		FileOperations.scriereFisier(out, "TASK 6\r\n");
		list.stream().forEach(s -> FileOperations.scriereFisier(out, s.toString()));
		FileOperations.inchidereFisier(out);
		
		return list;
	}


	public static void main(String args[]) {
		ProcessingData proc = new ProcessingData();
		String path = args[0];
		proc.taskOne(path);
		proc.taskTwo();
		proc.taskThree();
		proc.taskFour();
		proc.taskFive();
		proc.taskSix();

	}

}
