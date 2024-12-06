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

```code
官方题解：
class Solution {
    static int[][] rookDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    static int[][] bishopDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    static int[][] queenDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    String[] pieces;
    int[][] positions;
    int n;
    int res = 0;
    List<Movement> stack = new ArrayList<Movement>();

    public int countCombinations(String[] pieces, int[][] positions) {
        this.pieces = pieces;
        this.positions = positions;
        this.n = pieces.length;
        dfs(0);
        return res;
    }

    public void dfs(int u) {
        if (u == n) {
            res++;
            return;
        }
        int[][] directions;
        if ("rook".equals(pieces[u])) {
            directions = rookDirections;
        } else if ("queen".equals(pieces[u])) {
            directions = queenDirections;
        } else {
            directions = bishopDirections;
        }

        // 处理第 u 个棋子原地不动的情况
        stack.add(new Movement(positions[u][0], positions[u][1], positions[u][0], positions[u][1], 0, 0));
        if (check(u)) {
            dfs(u + 1);
        }
        stack.remove(stack.size() - 1);

        // 枚举第 u 个棋子在所有方向、所有步数的情况
        for (int i = 0; i < directions.length; i++) {
            for (int j = 1; j < 8; j++) {
                int x = positions[u][0] + directions[i][0] * j;
                int y = positions[u][1] + directions[i][1] * j;
                if (x < 1 || x > 8 || y < 1 || y > 8) {
                    break;
                }
                stack.add(new Movement(positions[u][0], positions[u][1], x, y, directions[i][0], directions[i][1]));
                if (check(u)) {
                    dfs(u + 1);
                }
                stack.remove(stack.size() - 1);
            }
        }
    }

    // 判断第 u 个棋子是否之前的棋子在移动过程中相遇
    public boolean check(int u) {
        for (int v = 0; v < u; v++) {
            if (stack.get(u).cross(stack.get(v))) {
                return false;
            }
        }
        return true;
    }
}

class Movement {
    public final int START_X;
    public final int START_Y;
    public final int END_X;
    public final int END_Y;
    public final int DX;
    public final int DY;
    public int curX;
    public int curY;

    public Movement(int startX, int startY, int endX, int endY, int dx, int dy) {
        this.START_X = startX;
        this.START_Y = startY;
        this.END_X = endX;
        this.END_Y = endY;
        this.DX = dx;
        this.DY = dy;
        this.curX = startX;
        this.curY = startY;
    }

    public void reset() {
        curX = START_X;
        curY = START_Y;
    }

    public boolean stopped() {
        return curX == END_X && curY == END_Y;
    }

    public void advance() {
        if (!stopped()) {
            curX += DX;
            curY += DY;
        }
    }

    public boolean cross(Movement oth) {
        // 每次判断是否相遇时需要重置 cur
        reset();
        oth.reset();
        while (!stopped() || !oth.stopped()) {
            advance();
            oth.advance();
            if (curX == oth.curX && curY == oth.curY) {
                return true;
            }
        }
        return false;
    }
}

```
### 241205 3001. 捕获黑皇后需要的最少移动次数
```code
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
```code
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