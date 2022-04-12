package HashMapDemo;

public class HashMapDemo<K,V> {

    // 数组初始长度
    private static int tar = 16;

    // 阈值
    static final float threshold = 0.75f;
    //
    static int size = 0 ;

    // 初始化参数
    public HashMapDemo() {

    }
    //自己设置数组长度
    public HashMapDemo(int x) {
        tar = x;
    }
    private Node<K,V>[] table;

    //put方法
    public V put(K key, V val){
        if(size==0){
            table = new Node[tar];
        }

        int h = hash(key)%tar;
        if(table[h]==null){
            table[h] = new Node<K, V>(key,val,null);
        }else{
            Node<K,V> temp = table[h];
            while(temp.next!=null){
                if(temp.key==key){
                    temp.val = val;
                    break;
                }
                temp = temp.next;
            }
            if(temp.key!=key){
                temp.next = new Node<K, V>(key,val,null);
            }
        }
        size++;
        if(size>=tar*threshold){
            expansion();
        }
        return val;
    }
    //扩容方法
    private void expansion(){
        tar*=2;
        Node[] temp = new Node[tar];
        for(int i =0;i<tar/2;i++){
            if(table[i]!=null){
                Node node = table[i];
                while(node!=null){
                    int h = hash((K) node.key)%tar;
                    if(temp[h]==null){
                        temp[h] = new Node(node.key,node.val,null);
                    }else{
                        Node node1 =  temp[h];
                        while(node1.next!=null){
                            node1 = node1.next;
                        }
                        node1.next = new Node(node.key,node.val,null);
                    }
                    node = node.next;
                }
            }
        }

        table = temp;
    }
    //添加方法
    public V get(K key){
      int h = hash(key)%tar;
      if(table[h]!=null){
            Node temp = table[h];
            while(temp!=null){
                if(temp.key==key){
                    return (V) temp.val;
                }
                temp = temp.next;
            }
      }
        return null;
    }
    // 获hash值
    private int hash(Object key){
        if(key==null)return 0;
        int x = key.hashCode();
        x = x^(x>>>16);

        return x<0? x*=-1 :x;
    }
    //返回个数
    public int size(){
        return size;
    }
    //判断key是否存在
    public boolean containsKey(K key){
        if(size==0)return false;
        int h = hash(key)%tar;
        if(table[h]!=null){
            Node temp = table[h];
            while(temp!=null){
                if(temp.key==key)return true;
                temp = temp.next;
            }
        }
        return false;
    }
    //
    @Override
    public String toString(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{");
        for(int i =0;i<tar;i++){
            if (table[i]!=null){
                Node temp = table[i];
                while(temp!=null){
                    stringBuffer.append(temp);
                    stringBuffer.append(",");
                    temp = temp.next;
                }
            }
        }
        stringBuffer.delete(stringBuffer.length()-1,stringBuffer.length());
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}
