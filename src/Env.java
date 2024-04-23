import java.util.ArrayList;
import java.util.HashMap;

public class Env
{
    public String funcId;
    public boolean hasRet;
    public Env prev;
    public HashMap<String, Object> m;
    public Env(Env prev)
    {
        this.prev = prev;
        m = new HashMap<>();
    }
    public void Put(String name, Object value) {
//        System.out.println(String.format("Adding %s %s to symtab",name,value));
        m.put(name, value);
    }
    public Object Get(String name)
    {
        if(m.containsKey(name)){
            return m.get(name);
        }
        if(prev!=null){
            return prev.Get(name);
        }
        return null;
    }
}