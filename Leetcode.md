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


