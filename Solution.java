public class Solution {
    public int networkDelayTime(int[][] times, int n, int k) {
        // 所有节点 总共n个 从k出发？这是中序遍历
        // 构建图？ 构建一个邻接矩阵
        int[][] graph = new int[n][n];
        for (int i = 0; i < times.length; i++) {
            graph[times[i][0] - 1][times[i][1] - 1] = times[i][2];
        }
        int[] dist = new int[n];
        // 根据邻接矩阵去计算最短路径
        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[k - 1] = 0;
        // 从k节点出发 遍历所有节点
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
        int num = dist[0];
        for (int i = 0; i < n; i++) {
            if (dist[i] > num) {
                num = dist[i];
            }
        }
        if (num == Integer.MAX_VALUE) {
            return -1;
        }
        return num;
    }
}
