package org.cresst.sb.irp.cat.service.lib;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class StatsTest {
    private final double epsilon = .000001;
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test_bias_values_equals_parameter() {
        Map<String, Double> values = new HashMap<>();
        values.put("1", 1.0);
        assertEquals(0.0, Stats.averageBias(values, values), epsilon);
    }

    @Test
    public void test_bias_values_different() {
        Map<String, Double> parameters = new HashMap<>();
        Map<String, Double> values = new HashMap<>();

        parameters.put("1", 1.0);
        values.put("1", 0.5);
        assertEquals(0.5, Stats.averageBias(parameters, values), epsilon);

        parameters.put("2", 2.0);
        values.put("2", 1.0);
        assertEquals(0.75, Stats.averageBias(parameters, values), epsilon);

        parameters.put("3", 1.0);
        values.put("3", 0.5);
        assertEquals(2/3.0, Stats.averageBias(parameters, values), epsilon);

        parameters.put("4", 3.0);
        values.put("4", 3.0);
        assertEquals(0.5, Stats.averageBias(parameters, values), epsilon);
    }
}
