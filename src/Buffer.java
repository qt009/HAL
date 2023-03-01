import static java.lang.System.exit;

public class Buffer {
    private boolean available = false;
    private double data;
    private Interpreter iptInterpreter;
    private Interpreter outInterpreter;
    public int IO_to_Buf;
    public int Buf_to_IO;
    public Buffer(){};

    public Interpreter getIptInterpreter(){
        return this.iptInterpreter;
    }
    public Interpreter getOutInterpreter(){
        return this.outInterpreter;
    }
    public void setIptInterpreter(Interpreter newIptInterpreter){
        this.iptInterpreter = newIptInterpreter;
    }
    public void setOutInterpreter(Interpreter newOutInterpreter){
        this.outInterpreter = newOutInterpreter;
    }

    public synchronized void put(double x) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                exit(1);
            }
        }

        data = x;
        available = true;
        notifyAll();
    }

    public synchronized double get() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                exit(1);
            }
        }
        available = false;
        notifyAll();
        return data;

    }
    public double getData(){
        return this.data;
    }
}
