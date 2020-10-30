package unsw.gloriaromanus.backend;

public class TurnTracker implements Observer {
    private int currTurn;
    
    public TurnTracker() {
        this.currTurn = 1;
    }

    public void incrementTurn(){
        this.currTurn += 1;
    }

    public int getCurrTurn() {
        return currTurn;
    }

    @Override
    public void update(Subject obj, int finishTurn) {
        // TODO Auto-generated method stub
        
    }
}
