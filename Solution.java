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