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

### 241201 51 N皇后

```code
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<List<String>> solveNQueens(int n) {
        //实现n皇后问题
        List<List<String>> res = new ArrayList<>();
        // 创建一个二维数组，用于存储皇后的位置
        char[][] board = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = '.';
            }
        }
        //打印出来棋盘
        System.out.println("棋盘：");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        //递归函数，用于判断当前位置是否可以放置皇后
        solve(res, board, 0);
        return res;
    }
    public void solve(List<List<String>> res, char[][] board, int row) {
        // 递归终止条件，当行数等于n时，说明已经放置了n个皇后，将当前状态加入结果集
        if (row == board.length) {
            List<String> list = new ArrayList<>();
            for (char[] chars : board) {
                list.add(new String(chars));
            }
            res.add(list);
            return;
        }
        // 遍历当前行，尝试放置皇后
        for (int col = 0;col < board.length; col++) {
            if (isValid(board, row, col)) {
                board[row][col] = 'Q';
                solve(res, board, row + 1);
                board[row][col] = '.';
            }
        }
    }
    public boolean isValid(char[][] board, int row, int col) {
        // 判断当前位置是否可以放置皇后
        // 遍历当前列，判断是否有皇后
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 'Q') {
                return false;
            }
        }
        // 遍历左上角，判断是否有皇后
        for(int i=row-1, j=col-1; i>=0 && j>=0; i--, j--){
            if(board[i][j] == 'Q'){
                return false;
            }
        }
        // 遍历右上角，判断是否有皇后
        for(int i=row-1, j=col+1; i>=0 && j<board.length; i--, j++){
            if(board[i][j] == 'Q'){
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        Solution solution = new Solution();
        List<List<String>> res = solution.solveNQueens(4);
        System.out.println(res);
    }
}
```

### 241202 52 N皇后 II

```code
class Solution {
    public int totalNQueens(int n) {
        // 实现n皇后问题
        List<List<String>> res = new ArrayList<>();
        // 创建一个二维数组，用于存储皇后的位置
        char[][] board = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = '.';
            }
        }
        // 递归函数，用于判断当前位置是否可以放置皇后
        solve(res, board, 0);
        return res.size();
    }

    public void solve(List<List<String>> res, char[][] board, int row) {
        // 递归终止条件，当行数等于n时，说明已经放置了n个皇后，将当前状态加入结果集
        if (row == board.length) {
            List<String> list = new ArrayList<>();
            for (char[] chars : board) {
                list.add(new String(chars));
            }
            res.add(list);
            return;
        }
        // 遍历当前行，尝试放置皇后
        for (int col = 0; col < board.length; col++) {
            if (isValid(board, row, col)) {
                board[row][col] = 'Q';
                solve(res, board, row + 1);
                board[row][col] = '.';
            }
        }
    }

    public boolean isValid(char[][] board, int row, int col) {
        // 判断当前位置是否可以放置皇后
        // 遍历当前列，判断是否有皇后
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 'Q') {
                return false;
            }
        }
        // 遍历左上角，判断是否有皇后
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 'Q') {
                return false;
            }
        }
        // 遍历右上角，判断是否有皇后
        for (int i = row - 1, j = col + 1; i >= 0 && j < board.length; i--, j++) {
            if (board[i][j] == 'Q') {
                return false;
            }
        }
        return true;
    }
}
```

241203 3274. 检查棋盘方格颜色是否相同

```code
class Solution {
    public static boolean checkTwoChessboards(String coordinate1, String coordinate2) {
        // 判断是否属于同一组
        if (Judge(coordinate1.charAt(0), coordinate2.charAt(0))) {
            if (JudgeNum(coordinate1.charAt(1), coordinate2.charAt(1))) {
                return true;
            }
            return false;
        } else {
            if (JudgeNum(coordinate1.charAt(1), coordinate2.charAt(1)))
                return false;
            return true;
        }
    }

    public static boolean Judge(char a, char b) {
        String s1 = "aceg";
        String s2 = "bdfh";
        if (a == b)
            return true;
        if (s1.contains(String.valueOf(a)) && s2.contains(String.valueOf(b)))
            return false;
        if (s1.contains(String.valueOf(b)) && s2.contains(String.valueOf(a)))
            return false;
        return true;
    }

    public static boolean JudgeNum(char a, char b) {
        if (a == b)
            return true;
        String s1 = "1357";
        String s2 = "2468";
        if (s1.contains(String.valueOf(a)) && s1.contains(String.valueOf(b)))
            return true;
        if (s2.contains(String.valueOf(a)) && s2.contains(String.valueOf(b)))
            return true;
        return false;
    }
}
```

### 241204  2056 棋盘上有效移动组合的数目

自己穷举 报错 需要重新做

官方题解

### 241205 3001. 捕获黑皇后需要的最少移动次数

```java
class Solution {
    public int minMovesToCaptureTheQueen(int a, int b, int c, int d, int e, int f) {
        if (a == e) {
            // 不能跳过其他棋子
            if (a == c && judge(d, b, f))
                return 2;
            return 1;
        }
        if (b == f) {
            if (d == b && judge(c, a, e))
                return 2;
            return 1;
        }
        if (juedui(c,e)==juedui(d,f)){
            // 判断a b 也在斜线上
            if(b==d || b==f) return 1;
            double result = (double) (a - c) / (b - d);
            double result2 = (double) (a - e) / (b - f);
            if(result==result2){
                if(judge(a,c,e)){
                    System.out.println("yes");
                    return 2;
                }
            }
            return 1;
        }
        return 2;
    }

    public boolean judge(int x1, int x2, int x3) {
        if (x1 < x3 && x1 > x2)
            return true;
        if (x1 > x3 && x1 < x2)
            return true;
        return false;
    }
    public int juedui(int a,int b){
        if(a>b){
            return a-b;
        }else{
            return b-a;
        }
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        System.out.println(s.minMovesToCaptureTheQueen(3,8,1,2,4,5));
    }
}
```

### 241206 999 可以被一步捕获的棋子数

```java
class Solution {
    public int numRookCaptures(char[][] board) {
        // 先找到白色的车
        int x = 0, y = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'R') {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        int num = 0;
        // 遍历上下左右 看是否有卒 或者白色的象
        for (int i = x - 1; i >= 0; i--) {
            if (board[i][y] == 'p') {
                num++;
                break;
            } else if (board[i][y] == 'B')
                break;
        }
        for (int i = x + 1; i < 8; i++) {
            if (board[i][y] == 'p') {
                num++;
                break;
            } else if (board[i][y] == 'B')
                break;
        }
        for (int i = y - 1; i >= 0; i--) {
            if (board[x][i] == 'p') {
                num++;
                break;
            } else if (board[x][i] == 'B')
                break;
        }
        for (int i = y + 1; i < 8; i++) {
            if (board[x][i] == 'p') {
                num++;
                break;
            } else if (board[x][i] == 'B')
                break;
        }
        return num;
    }
}
```

3238 求出胜利玩家的数目

```java
class Solution {
    public int winningPlayerCount(int n, int[][] pick) {
        int[][] num = new int[10][11];
        // 初始化
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 11; j++) {
                num[i][j] = 0;
            }
        }

        for (int i = 0; i < pick.length; i++) {
            // 统计每个玩家 获得的每种球的数目
            num[pick[i][0]][pick[i][1]]++;
        }
        // 判断每个玩家
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 11; j++) {
                if (num[i][j] > i) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}
```

### 241209 1812 判断国际象棋棋盘中一个格子的颜色

```java
class Solution {
    public boolean squareIsWhite(String coordinates) {
        // 判断棋盘颜色
        char x = coordinates.charAt(0);
        char y = coordinates.charAt(1);
        return (x - 'a') % 2 != (y - '1') % 2;
    }
}
```
### 241210 935 骑士拨号器
```java
超时:
class Solution {
    int[][] next = {
            { 4, 6 }, // 0
            { 6, 8 }, // 1
            { 7, 9 }, // 2
            { 4, 8 }, // 3
            { 0, 3, 9 }, // 4
            {}, // 5
            { 0, 1, 7 }, // 6
            { 2, 6 }, // 7
            { 1, 3 }, // 8
            { 4, 6 } // 9
    };
    int count = 0;

    public int knightDialer(int n) {
        // 可以先将每一个数可以到达的下一个数都列出来
        // 从0开始 直到长度到n-;
        for (int i = 0; i < 10; i++) {
            Diguin(i, n, 1);
        }
        return count % 1000000007;
    }

    void Diguin(int i, int n, int len) {
        if (len == n){
            count++;
            return;
        }
        for (int j : next[i]) {
            Diguin(j, n, len + 1);
        }
    }
}

官方题解：动态规划
class Solution {
    private static final int MOD = 1_000_000_007;
    private static final int[][] next = {
        {4, 6}, // 0
        {6, 8}, // 1
        {7, 9}, // 2
        {4, 8}, // 3
        {3, 9, 0}, // 4
        {}, // 5
        {1, 7, 0}, // 6
        {2, 6}, // 7
        {1, 3}, // 8
        {2, 4} // 9
    };

    public int knightDialer(int n) {
        if (n == 1) return 10; // 如果只需要按一次，那么有10种可能

        long[] counts = new long[10];
        Arrays.fill(counts, 1); // 每个数字开始时都有1种方式

        for (int step = 1; step < n; ++step) {
            long[] nextCounts = new long[10];
            for (int i = 0; i < 10; ++i) {
                for (int j : next[i]) {
                    nextCounts[j] = (nextCounts[j] + counts[i]) % MOD;
                }
            }
            counts = nextCounts;
        }

        long sum = 0;
        for (long count : counts) {
            sum = (sum + count) % MOD;
        }
        return (int) sum;
    }
}
```

### 241211 2717 半有序排列
```java
class Solution {
    public int semiOrderedPermutation(int[] nums) {
        // 半有序排列
        int num=0;
        int num1=-1,num2=-1;
        //找出1 和 n
        for(int i=0;i<nums.length;i++){
            if(nums[i]==1){
                num1=i;
            }
            if(nums[i]==nums.length){
                num2=i;
            }
        }
        //进行交换
        if(num1<num2){
            num=num1+nums.length-num2-1;
        }else{
            num=num1+nums.length-num2-2;
        }
        return num;
    }
}
```
### 241212 2931. 购买物品的最大开销
```java
class Solution {
    public long maxSpending(int[][] values) {
        int[] num = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            num[i] = values[i].length - 1;
        }

        long sum = 0;
        int day = 1;
        int minIndex = 0;

        while (day <= values.length * values[0].length) {
            // 初始化 minIndex 为第一个有效的索引
            for (int i = 0; i < values.length; i++) {
                if (num[i] >= 0) {
                    minIndex = i;
                    break;
                }
            }

            // 找出最小的数
            for (int i = 0; i < values.length; i++) {
                if (num[i] < 0) {
                    continue;
                }
                if (values[i][num[i]] < values[minIndex][num[minIndex]]) {
                    minIndex = i;
                }
            }

            sum += (long) values[minIndex][num[minIndex]] * day;
            num[minIndex]--;
            day++;
        }

        return sum;
    }
}
```
