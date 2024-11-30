 ### 241125 743 网络延迟时间

```code
class Solution {
    public int networkDelayTime(int[][] times, int n, int k) {
        // 所有节点 总共n个 从k出发？这是中序遍历
        // 构建图？ 构建一个邻接矩阵
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(graph[i], Integer.MAX_VALUE);
        }
        for (int[] time : times) {
            graph[time[0] - 1][time[1] - 1] = time[2];
        }

        // 初始化距离数组
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k - 1] = 0;

        // Bellman-Ford算法
        for (int i = 0; i < n - 1; i++) {
            for (int u = 0; u < n; u++) {
                if (dist[u] == Integer.MAX_VALUE)
                    continue; // 如果当前节点不可达，跳过
                for (int v = 0; v < n; v++) {
                    if (graph[u][v] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) {
                        dist[v] = dist[u] + graph[u][v];
                    }
                }
            }
        }

        // 找到最大距离
        int maxDist = Arrays.stream(dist).max().getAsInt();
        return maxDist == Integer.MAX_VALUE ? -1 : maxDist;
    }
}
```
### 241126 3206 交替组
```code
class Solution {
    public int numberOfAlternatingGroups(int[] colors) {
        int num=0;
        int size=colors.length;
        for(int i=0;i<size;i++){
            if(colors[i%size]!=colors[(i+size+1)%size]&&colors[i%size]!=colors[(i+size-1)%size]){
                num++;
            }
        }
        return num;
    }
}
```

### 241127 3208 交替组
```code 
// 下面代码 会导致超时
class Solution {
    public int numberOfAlternatingGroups(int[] colors, int k) {
        // k个的话 连续k个不一样
        int num = 0;
        int size = colors.length;
        for (int i = 0; i < size; i++) {
            // 连续k个不相等
            int time = 1;
            // 从当前节点开始 所以只需要再验证k-1个节点！！！
            for (int j = i; j < k + i - 1; j++) {
                if (colors[j % size] == colors[(j + 1) % size]) {
                    break;
                }
                time--;
            }
            if (time == 1) {
                num++;
            }
        }
        // int j = i;
        // while (time < k) {
        // if (colors[j % size] == colors[(j + 1) % size]) {
        // break;
        // }
        // j++;
        // }
        return num;
    }
}
//优化： 去除没必要的比较
class Solution {
    public int numberOfAlternatingGroups(int[] colors, int k) {
        int n = colors.length;
        int res = 0, cnt = 1;
        for (int i = -k + 2; i < n; i++) {
            if (colors[(i + n) % n] != colors[(i - 1 + n) % n]) {
                cnt += 1;
            } else {
                cnt = 1;
            }
            if (cnt >= k) {
                res += 1;
            }
        }
        return res;
    }
}
```

### 241128 3250 单调数组队的数目
```code 
class Solution {
    public int countOfPairs(int[] nums) {
        int mod = 1000000007;
        int n = nums.length;
        // 用于计数
        int[] count = new int[1]; // 使用数组以便在递归中修改
        // 从 0 开始生成 arr1 和 arr2
        int pri=0;
        generatePairs(nums, new int[n], new int[n], 0, count, mod,pri);

        return count[0];
    }

    private void generatePairs(int[] nums, int[] arr1, int[] arr2, int index, int[] count, int mod,int pri) {
        int n = nums.length;

        // 递归终止条件
        if (index == n) {
            if (isValid(arr1, arr2)) {
                count[0] = (count[0] + 1) % mod;
            }
            return;
        }
        // 枚举 arr1[index] 的所有可能值
        for (int x = pri; x <= nums[index]; x++) {
            arr1[index] = x;
            arr2[index] = nums[index] - x;
            pri=arr1[index];
            // 递归生成下一步
            generatePairs(nums, arr1, arr2, index + 1, count, mod,pri);
        }
    }

    // 判断 arr1 和 arr2 是否满足单调条件
    private boolean isValid(int[] arr1, int[] arr2) {
        for (int i = 0; i < arr1.length - 1; i++) {
            if (arr1[i] > arr1[i + 1]) {
                return false;
            }
        }
        for (int i = 0; i < arr2.length - 1; i++) {
            if (arr2[i] < arr2[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
```
### 241129 3250 单调数组队的数目
```code 
优化： 使用动态规划
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int countOfPairs(int[] nums) {
        int mod = 1000000007;
        int n = nums.length;

        // 使用 Map 记录状态：key 为 (index, prev1, prev2)，value 为 count
        Map<String, Integer> memo = new HashMap<>();
        
        // 从索引 0 开始递归
        return dfs(nums, 0, 0, nums[0], memo, mod);
    }

    private int dfs(int[] nums, int index, int prev1, int prev2, Map<String, Integer> memo, int mod) {
        // 如果到达数组末尾，说明是一个有效组合
        if (index == nums.length) {
            return 1;
        }

        // 构造唯一的状态 key
        String key = index + "," + prev1 + "," + prev2;

        // 如果已经计算过这个状态，直接返回结果
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        int count = 0;
        // 遍历 arr1[index] 的所有可能值
        for (int x = prev1; x <= nums[index]; x++) {
            int y = nums[index] - x; // arr2[index] 的值
            // 剪枝：确保 arr2[index] 不小于 prev2
            if (y > prev2) {
                continue;
            }
            // 递归到下一个索引
            count = (count + dfs(nums, index + 1, x, y, memo, mod)) % mod;
        }

        // 保存结果到缓存中
        memo.put(key, count);

        return count;
    }
}
```

### 241130 3232 判断是否可以赢得游戏
```code 
class Solution {
    public boolean canAliceWin(int[] nums) {
        // 判断是否是个位数
        int count1 = 0;
        int count2 = 0;
        for(int i=0;i<nums.length;i++){
            if(nums[i]<10){
                count1+=nums[i];
            }else{
                count2+=nums[i];
            }
        }
        if(count1==count2){
            return false;
        }
        return true;
    }
}
```


