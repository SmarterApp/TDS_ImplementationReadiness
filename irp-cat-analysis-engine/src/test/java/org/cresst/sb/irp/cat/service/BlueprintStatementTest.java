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
                return item.getClaim().equals("1");
            }
        });

        assertNotNull(statement);
        assertTrue(statement.test(new PoolItem() {

            @Override
            public String getItemId() {
                return null;
            }

            @Override
            public String getClaim() {
                return "1";
            }

            @Override
            public String getDok() {
                return null;
            }
        }));

        assertFalse(statement.test(new PoolItem() {

            @Override
            public String getItemId() {
                return null;
            }

            @Override
            public String getClaim() {
                return "2";
            }

            @Override
            public String getDok() {
                return null;
            }
        }));
    }
}
