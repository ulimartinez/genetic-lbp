package sensors;
/**
 * Created by ulimartinez on 11/15/17.
 */
public class GeneratorTest {
    public static void main(String[] args){
        TempGenerator temps = new TempGenerator(25, 150);
        temps.generateValues(10);
    }
}