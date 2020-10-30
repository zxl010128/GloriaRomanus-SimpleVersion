package unsw.gloriaromanus.backend;

public class TrainingRecord {
    private Unit unit;
    private int finishTurn;
    private boolean finished;

    public TrainingRecord(Unit unit, int finishTurn) {
        this.unit = unit;
        this.finishTurn = finishTurn;
        this.finished = false;
    }

    public Unit getUnit() {
        return unit;
    }

    public int getFinishTurn() {
        return finishTurn;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean getFinished(){
        return finished;
    }
    
}
