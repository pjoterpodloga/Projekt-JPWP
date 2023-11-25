package src;

public class Utilis {
    static double[] linspace(double start, double stop, int n)
    {
        double[] values = new double[n];

        values[0] = start;
        values[n - 1] = stop;

        double increment = (stop - start) / (n - 1.);

        for (int i = 1; i < n - 1; i += 1)
        {
            values[i] = values[i - 1] + increment;
        }

        return values;
    }
}
