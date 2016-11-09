package org.cresst.sb.irp.cat.service;

import static org.junit.Assert.*;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintCondition;
import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.junit.Before;
import org.junit.Test;

public class BlueprintStatementTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void test_BluePrintCondition() {
        BlueprintStatement statement = new BlueprintStatement("Test", 1, 1, 1, new BlueprintCondition() {

            @Override
            public boolean test(PoolItem item) {
                return true;
            }
        });
        assertNotNull(statement);
        assertTrue(statement.test(null));
    }
}
