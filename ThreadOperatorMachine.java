import java.util.*;

public class ThreadOperatorMachine {

    public static void main(String[] args) throws InterruptedException {
        Machine m = new Machine();
        Operator o = new Operator(m);

        m.start();
        o.start();

        o.join();
        m.interrupt();

        System.out.println("Exit");

    }
}

class Machine extends Thread{
    private final Deque<String> tasks= new ArrayDeque<>();

    void addTask(String task){
        synchronized (tasks) {
            tasks.add(task);
            tasks.notify();
        }
    }

    public void run(){
        outerLoop:
        while(true){
            synchronized (tasks){
                while(tasks.isEmpty()){
                    try {
                        tasks.wait();
                    } catch (InterruptedException e) {
                        break outerLoop;
                    }
                }

                String s = tasks.pop();
                System.out.println("Machine processed "+s);
            }
        }

    }
}

class Operator extends Thread{
    private final Machine m;

    Operator(Machine m) {
        this.m = m;
    }

    public void run() {
        System.out.println("Write file:");
        try(Scanner in = new Scanner(System.in)) {
            while(true){
                String s = in.nextLine();
                if (s.equals("x")) break;
                m.addTask(s);
            }
        }
    }
}
