package speedmeter.fly.speed;

public interface AsyncResponse {//response send to main to see if its the result we want
    void processFinish(String output);
}