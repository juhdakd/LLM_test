class Solution {
    public long maxSpending(int[][] values) {
        // 输入参数有效性检查
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

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] values = { { 8, 5, 2 }, { 6, 4, 1 }, { 9, 7, 3 } };
        System.out.println(solution.maxSpending(values));
    }
}