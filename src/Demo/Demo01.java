package Demo;

import HashMapDemo.HashMapDemo;

import java.util.HashMap;
import java.util.UUID;

public class Demo01 {
    public static void main(String[] args) {
        HashMapDemo<String,Integer> map = new HashMapDemo<String, Integer>();
        for(int i =0;i<10;i++){
            map.put(UUID.randomUUID().toString(),i);
        }
        map.put(null,2);
        System.out.println(map.get(null));
        System.out.println(map);

    }
}
