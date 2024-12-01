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