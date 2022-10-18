package com.common.common.zhangqiang.java8stream;


import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.java8stream
 * @ClassName: stream
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-10-18  16:55
 * @UpdateDate: 2022-10-18  16:55
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class stream {
    public static void main(String[] args) {
/*//        1.查找集合中的第一个对象
        Optional<Object> first= ObjectList.stream() .filter(a -> "hanmeimei".equals(a.getUserName())) .findFirst();
//        2.返回符合查询条件的集合
//所有名字叫hanmeimei的集合
        List<Object> firstObject= ObjectList.stream() .filter(a -> "hanmeimei".equals(a.getUserName())) .collect(Collectors.toList());
//所有名字为空的集合
        List<Object> firstObject= ObjectList.stream() .filter(a -> StringUtils.isEmpty(a.getUserName())) .collect(Collectors.toList());*/
//        filter 过滤
         List<Integer> integers = Lists.newArrayList(30, 40, 10, 20);
        Set<Integer> collect = integers.stream()
                .filter(i -> i > 20).collect(Collectors.toSet());
//        assertEquals(Sets.newTreeSet(30, 40), collect);
        System.out.println(collect);
//        map 对每个元素执行操作,不会改变原本数量
        List<Integer> integers1 = Lists.newArrayList(30, 40, 10, 20);
        List<String> collect1 = integers1.stream().map(i -> i + "呵呵").collect(Collectors.toList());
        System.out.println(collect1);
//        3. mapToInt 转成IntSream
        ArrayList<Integer> list = Lists.newArrayList(2, 5, 1, 6);
        IntStream intStream = list.stream().mapToInt(i -> i);
        List<Integer> collect3 = intStream.boxed()
                .collect(Collectors.toList()); //  boxed() 把int转成Integer??
        System.out.println(collect3);
//    mapToDouble mapToLong 同理
//        4. flatMap 类似把多个集合压平成一个?
        ArrayList<ArrayList<Integer>> list1 = Lists.newArrayList(Lists.newArrayList(1, 2, 3), Lists.newArrayList(1, 2, 3));
        Stream<Integer> integerStream = list1.stream().flatMap(Collection::stream);
        List<Integer> collect4 = integerStream.collect(Collectors.toList());
        System.out.println(collect4);//[1, 2, 3, 1, 2, 3]
//    list.stream().flatMapToInt() TODO
//        5. distinct 去重
        List<Integer> integers2 = Lists.newArrayList(30, 40, 10, 20, 20, 10, 50, 20);
        List<Integer> collect5 = integers2.stream().distinct().collect(Collectors.toList());
        System.out.println(collect5);//[30, 40, 10, 20, 50]
//        6. sorted 排序
        collect5 = integers2.stream().sorted().collect(Collectors.toList());
        System.out.println(collect);//  [10, 10, 20, 20, 20, 30, 40, 50]
        collect5 = integers2.stream().sorted((x, y) -> {//这里认为40 不吉利 自定义排到最后
            if (y == 40) {
                return -1;  //  注意 排序比较时 结果 1排在前,-1排在后
            }else if (x==40){
                return 1;
            }
            return x.compareTo(y);
        }).collect(Collectors.toList());
        System.out.println(collect);// [10, 10, 20, 20, 20, 30, 50, 40]
//        7. peek 不影响主流的情况下 对当前流元素进行提取
        List<String> list2 = Lists.newArrayList("刘德华", "x蔡徐坤", "x吴亦凡", "朱大胖", "外星人");
        List<String> earth = new ArrayList<>();
        List<String> noFat = new ArrayList<>();
        List<String> noBadMan =
                list2.stream().filter(i -> !"外星人".equals(i)).peek(earth::add)
                        .filter(i -> !i.equals("朱大胖")).peek(noFat::add).filter(i -> !i.startsWith("x"))
                        .collect(Collectors.toList());
        System.out.println("earth : " + earth); // earth : [刘德华, x蔡徐坤, x吴亦凡, 朱大胖]
        System.out.println("noFat : " + noFat); // noFat : [刘德华, x蔡徐坤, x吴亦凡]
        System.out.println("noBadMan : " + noBadMan); // noBadMan : [刘德华]
//        8. limit 限定流元素数量
        List<Integer> integers3 = Lists.newArrayList(30, 40, 10, 20, 20, 10, 50, 20);
        List<Integer> collect6 = integers3.stream().limit(3).collect(Collectors.toList());
        System.out.println(collect6); // [30, 40, 10]
//        9. skip 跳过多少个元素
        List<Integer> q = Lists.newArrayList(30, 40, 10, 20, 20, 10, 50, 20);
        List<Integer> collect7 = q.stream().skip(2).collect(Collectors.toList());
        System.out.println(collect7); // [10, 20, 20, 10, 50, 20]
//        10. foreach foreachOrdered 遍历
        Stream<Integer> limit = Stream.iterate(0,x->x+1).limit(100);
        List list3 = new ArrayList();
//    limit.parallel().forEachOrdered(list::add);// [0, 1, 2, 3, 4...
        limit.parallel().forEach(list3::add);// [62, 63, 64, 65,...
        System.out.println(list3);
//        11. reduce 合并?累加
        List<Integer> integers4 = Lists.newArrayList(30, 40, 10, 20, 20, 10, 50, 20);

        Integer reduce = integers4.stream().reduce(0, (x, y) -> x + y);
        Integer integer = integers4.stream().reduce((x, y) -> x + y).orElse(-1);
        Integer reduce1 = integers4.stream().reduce(0, (x, y) -> x + y,Integer::sum);
        System.out.println(reduce); // 200
        System.out.println(integer); // 200
        System.out.println(reduce1); // 200
//        12. collect 收集
        List<Integer> integers5 = Lists.newArrayList(30, 40, 10, 20, 20, 10, 50, 20);

        ArrayList<Object> collect8 = integers5.stream()
                .collect(ArrayList::new, ArrayList::add, ArrayList::add);
        // 在combiner使用时，你的Stream是平行的，因为在这种情况下，
        // 多个线程收集的元素Stream到最终输出的子列表ArrayList，
        // 并且这些子列表必须被组合以产生最终的ArrayList。

        System.out.println(collect8);// [30, 40, 10, 20, 20, 10, 50, 20]
//        13. min 最小 count总数
         integers = Lists.newArrayList(30, 40, 10, 20, 20, 10, 50, 20);

        Integer integer1 = integers.stream().min(Integer::compareTo).orElse(-1);
        System.out.println(integer1); //10
        long count = integers.stream().count();
        System.out.println(count);//8
//        14. findFirst 第一个 findAny 随便一个
        integers = Lists.newArrayList(30, 40, 10, 20, 20, 10, 50, 20);

        Integer integer2 = integers.stream().findFirst().orElse(-1);
        System.out.println(integer2);
        Integer integer3 = integers.stream().findAny().orElse(-1);
        System.out.println(integer3);//30
//        15. Stream.iterate Stream.generate 生成一个无限流后者可以自定义实现方法//必须有limit限制,否则无限
      /*  collect3 = Stream.iterate(0, (x) -> ++x).limit(100).collect(Collectors.toList());
        System.out.println(collect3);

         collect4 = Stream.generate(MyStreamTest::get).limit(200)
                .collect(Collectors.toList());
        System.out.println(collect4);


        private static Integer get() {
            return RandomUtils.nextInt(1, 1000);
        }*/
//        16. Stream.concat 组合俩流
        /*Stream<Integer> limit = Stream.generate(MyStreamTest::get).limit(10).sorted();
        Stream<Integer> limit1 = Stream.generate(MyStreamTest::get).limit(10).sorted();
        List<Integer> collect = Stream.concat(limit, limit1).collect(Collectors.toList());
        System.out.println(collect);*/

    }
}
