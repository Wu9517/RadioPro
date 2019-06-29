package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 桶排序：
 * 给定一个非空的数组，返回其中出现频率前k高的元素
 *
 * @author wzy
 */
public class Solution1 {
    public static List<Integer> topKFrequent(int[] t, int k) {
        List<Integer> res = new ArrayList<>();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (Integer num : t) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }
        //桶排序
        List<Integer>[] list = new List[t.length + 1];
        for (Integer key : map.keySet()) {
            //获取出现的次数作为下标
            int i = map.get(key);
            if (list[i] == null) {
                list[i] = new ArrayList<>();
            }
            list[i].add(key);
        }
        //倒序遍历数组获取出现顺序从大到小的排列
        for (int i = list.length - 1; i >= 0 && res.size() < k; i--) {
            if (list[i] == null) {
                continue;
            }
            res.addAll(list[i]);
        }
        return res;
    }

    public static void main(String[] args) {
        int[] array = {1, 1, 1, 2, 2, 3};
        List<Integer> result = Solution1.topKFrequent(array, 2);
        System.out.println(result);
    }
}
