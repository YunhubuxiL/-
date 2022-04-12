package HashMapDemo;

public class Node<K,V> {

    K key;
    V val;
    Node<K, V> next;
    Node(K key,V val,Node<K,V> next){
        this.key = key;
        this.val = val;
        this.next = next;
    }



    public final K getKey(){
        return key;
    }
    public final V getVal(){
        return val;
    }
    @Override
    public final String toString(){
        return key+"="+val;
    }
    //判断是否存在
    @Override
    public final boolean equals(Object key){
        if(key == this.key||key==null) {
            return true;
        }
        return false;
    }

}
