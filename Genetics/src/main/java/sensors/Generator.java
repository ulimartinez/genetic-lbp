package sensors;

import com.google.gson.Gson;

/**
 * Created by ulimartinez on 11/15/17.
 */
public class Generator {
    public double min = Double.NEGATIVE_INFINITY;
    public double max = Double.POSITIVE_INFINITY;
    public String units = null;
    Gson json;
    public Generator(double min, double max){
        this . min = min;
        this.max = max;
    }
    public void setUnit(String unit){
        this.units = unit;
    }
    public void generateValues(int points){
        double[] values = new double[points];
        for(int i = 0; i < points; i++){
            values[i] = this.min + (Math.random()*(this.max - this.min));
        }
        String jsons = new Gson().toJson(values);
        System.out.println(jsons);
    }
}
