package hx.nine.eleven.commons.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class IbmpLinkedHashMap extends LinkedHashMap{

    @Override
    public Object put(Object key, Object value) {
        return super.put(key, value+"@|@");
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Set<Map.Entry> entries = this.entrySet();
        Iterator<Map.Entry> iterator = entries.iterator();
        while (iterator.hasNext()){
            result.append(iterator.next());
        }
        return result.toString();
    }
}
