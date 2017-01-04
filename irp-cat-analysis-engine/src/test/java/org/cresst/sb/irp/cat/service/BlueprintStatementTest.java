package org.cresst.sb.irp.cat.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintCondition;
import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.junit.Before;
import org.junit.Test;

public class BlueprintStatementTest {
    private BlueprintStatement statement;
    private PoolItem poolItem1;
    private PoolItem poolItem2;

    @Before
    public void setUp() throws Exception {
        statement = new BlueprintStatement("Test", 1, 1, new BlueprintCondition() {

            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("1");
            }
        });

        poolItem1 = new PoolItem() {

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

            @Override
            public String getTarget() {
                return null;
            }

            @Override
            public String getItemGrade() {
                return null;
            }

			@Override
			public String getPoolGrade() {
				return null;
			}
        };

        poolItem2 = new PoolItem() {

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

            @Override
            public String getTarget() {
                return null;
            }

            @Override
            public String getItemGrade() {
                return null;
            }

			@Override
			public String getPoolGrade() {
				return null;
			}
        };
    }

    @Test
    public void test_BluePrintCondition() {
        assertNotNull(statement);
        assertTrue(statement.test(poolItem1));

        assertFalse(statement.test(poolItem2));
    }

    @Test
    public void test_BluePrint_updateViolations() {
        assertNotNull(statement);

        statement.setMin(1);
        statement.setMax(1);
        statement.incMatch();
        statement.updateViolations();
        assertEquals(1, statement.getViolationCount().getMatch());
        statement.incMatch();
        statement.incMatch();
        statement.updateViolations();
        assertEquals(1, statement.getViolationCount().getOver());
        assertEquals(1, statement.getViolationCount().getMatch());
    }
}
