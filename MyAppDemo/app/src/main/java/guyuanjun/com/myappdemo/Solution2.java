package guyuanjun.com.myappdemo;

/**
 * Created by HP on 2017-12-27.
 */

public class Solution2 {

    public static void main(String[] args){

    }

    public boolean exist(char[][] board, String word) {
        if (word == null || word.length() == 0)
            return true;
        int row = board.length;
        if(row == 0 || board==null) return false;
        int col = board[0].length;
        if (col == 0) return false;

        System.out.println("row="+row+" col"+col);

        boolean[][] visit= new boolean[row][col];
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                if (search(board, word, 0, i, j, visit)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean search(char[][] board, String word, int index, int i, int j, boolean[][] visit){
        if (index == word.length()) return true;
        if (i<0 || j<0 || i>=board.length || j>=board[0].length || visit[i][j] || board[i][j] != word.charAt(index)) return false;
        visit[i][j] = true;
        boolean res = false;
        res = search(board, word, index+1, i-1, j, visit)
                ||search(board, word, index+1, i+1, j, visit)
                ||search(board, word, index+1, i, j-1, visit)
                ||search(board, word, index+1, i, j+1, visit);
        visit[i][j] = false;
        return res;
    }
}
