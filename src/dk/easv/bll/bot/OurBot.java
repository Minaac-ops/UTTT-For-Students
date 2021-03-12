package dk.easv.bll.bot;

import dk.easv.bll.field.Field;
import dk.easv.bll.field.IField;
import dk.easv.bll.game.GameState;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;
import dk.easv.bll.move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OurBot implements IBot {

    Field field;
    IGameState state;
    private static final String BOTNAME = "OurBot";
    private Random rand = new Random();
    protected int[][] firstMove = {
            {2, 2}, {1, 1}, {0, 0}, {1, 1}, {1, 0}, {2, 0}, {0, 1}, {1, 1}, {0, 2}, {1, 2}, {2, 2}, //isolate the component to play in {2,1}
            {2, 0}
    };

    public OurBot() {
        field = new Field();
        state = new GameState();
    }

    /**
     * Makes a turn. Implement this method to make your dk.easv.bll.bot do something.
     *
     * @param state the current dk.easv.bll.game state
     * @return The column where the turn was made.
     */
    @Override
    public IMove doMove(IGameState state) {
        List<IMove> winMoves = getWinningMoves(state);

            for (int[] move : firstMove)
            {
                if (!winMoves.isEmpty()){
                    return winMoves.get(0);
                }
                else if (state.getField().getMacroboard()[move[0]][move[1]].equals(IField.AVAILABLE_FIELD))
                {
                    for (int[] selectedMove : firstMove)
                    {
                        int x = move[0] * 3 + selectedMove[0];
                        int y = move[1] * 3 + selectedMove[1];
                        if (state.getField().getBoard()[x][y].equals(Field.EMPTY_FIELD))
                        {
                            return new Move(x, y);
                        }
                    }
                }
            }
        return null;
    }

    private boolean isWinningMove(IGameState state, IMove move, String player) {

        String[][] board = state.getField().getBoard();
        boolean isRowWin = true;
        // checking row
        int startX = move.getX()-(move.getX()%3);
        int endX = startX + 2;
        for (int x = startX; x <= endX; x++) {
            if(x!=move.getX())
                if(!board[x][move.getY()].equals(player))
                    isRowWin = false;
        }

        boolean isColumnWin=true;
        //checking column
        int startY = move.getY()-(move.getY()%3);
        int endY = startY + 2;
        for (int y = startY; y <= endY; y++) {
            if(y!=move.getY())
                if(!board[move.getX()][y].equals(player))
                    isColumnWin = false;
        }


        boolean isDiagWin = true;

        // checking diagonal left-top to right-bottom
        if(!(move.getX()==startX && move.getY()==startY))
            if(!board[startX][startY].equals(player))
                isDiagWin=false;
        if(!(move.getX()==startX+1 && move.getY()==startY+1))
            if(!board[startX+1][startY+1].equals(player))
                isDiagWin=false;
        if(!(move.getX()==startX+2 && move.getY()==startY+2))
            if(!board[startX+2][startY+2].equals(player))
                isDiagWin=false;

        boolean isOppositeDiagWin = true;
        // checking the opposite diagonal
        if(!(move.getX()==startX && move.getY()==startY+2))
            if(!board[startX][startY+2].equals(player))
                isOppositeDiagWin=false;
        if(!(move.getX()==startX+1 && move.getY()==startY+1))
            if(!board[startX+1][startY+1].equals(player))
                isOppositeDiagWin=false;
        if(!(move.getX()==startX+2 && move.getY()==startY))
            if(!board[startX+2][startY].equals(player))
                isOppositeDiagWin=false;

        return isColumnWin || isDiagWin || isOppositeDiagWin || isRowWin;
    }


    private List<IMove> getWinningMoves(IGameState state){
        String player = "1";
        if(state.getMoveNumber()%2==0)
            player="0";

        List<IMove> avail = state.getField().getAvailableMoves();

        List<IMove> winningMoves = new ArrayList<>();
        for (IMove move:avail) {
            if(isWinningMove(state,move,player))
                winningMoves.add(move);
        }
        return winningMoves;
    }


    @Override
    public String getBotName() {
        return BOTNAME;
    }
}
