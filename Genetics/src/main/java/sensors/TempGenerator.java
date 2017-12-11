package sensors;

import sensors.Generator;

/**
 * Created by ulimartinez on 11/15/17.
 */
public class TempGenerator extends Generator {
    public TempGenerator(double min, double max){
        super(min, max);
        super.setUnit("C");
    }
}
