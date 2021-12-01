package com.shsxt.flink;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 向kafka中生产数据
 *
 * @author root
 */
public class GenerateTestData extends Thread {

    static String[] behaviors = new String[]{
            "pv", "cart", "buy", "fav"
    };

    static String[] itemID = new String[]{"10000", "Register"};

    static  Map<String,String> map = new HashMap();

    static String ymd = "2019-10-01";


    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;
    private static Random random = new Random();


    public static void main(String[] args) throws ParseException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30000; i++) {
            list.add(userlogs());
        }

        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String time1 = o1.split(",")[4];
                String time2 = o2.split(",")[4];
                return time1.compareTo(time2);
            }
        });

        for(String line:list){
            System.out.println(line );
        }

    }


    //生成随机数据
    private static String userlogs() throws ParseException {

        StringBuffer userLogBuffer = new StringBuffer("");
        String behavior = behaviors[random.nextInt(4)];

        String itemID = random.nextInt(100) + "";

        String userID = random.nextInt(1000) + "";

        String categaryID = map.get(itemID);

        String time = ymd + " " + random.nextInt(2) + ":" +  random.nextInt(60) + ":" + random.nextInt(60) ;

        long timestap = format.parse(time).getTime();


        if(categaryID==null){
            categaryID = random.nextInt(10) + "";
            map.put(itemID,categaryID);
        }

        userLogBuffer.append(userID)
                .append(",")
                .append(itemID)
                .append(",")
                .append(categaryID)
                .append(",")
                .append(behavior)
                .append(",")
                .append(timestap);


        return userLogBuffer.toString();
    }

}
