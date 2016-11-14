package org.cresst.sb.irp.cat.service.lib;

import java.util.ArrayList;
import java.util.List;

import org.cresst.sb.irp.cat.domain.analysis.BlueprintCondition;
import org.cresst.sb.irp.cat.domain.analysis.BlueprintStatement;
import org.cresst.sb.irp.cat.domain.analysis.PoolItem;
import org.cresst.sb.irp.cat.service.CATAnalysisServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlueprintSpecs {
    private final static Logger logger = LoggerFactory.getLogger(CATAnalysisServiceImpl.class);

    public static List<BlueprintStatement> getGradeBlueprints(int grade) {
        if (grade != 3) {
            logger.error("Only grade 3 implemented");
            return null;
        }

        List<BlueprintStatement> statements = new ArrayList<>();

        BlueprintStatement blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1: Reading");
        blueprint.setMin(14);
        blueprint.setMax(16);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("1");
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1, DOK=2");
        blueprint.setMin(7);
        blueprint.setMax(Integer.MAX_VALUE);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("1") && item.getDok().equals("2");
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1, DOK>=3");
        blueprint.setMin(2);
        blueprint.setMax(Integer.MAX_VALUE);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                int dok = Integer.parseInt(item.getDok());
                return item.getClaim().equals("1") && dok >= 3;
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1, Literary");
        blueprint.setMin(0);
        blueprint.setMax(8);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                String target = item.getTarget();
                return (item.getClaim().equals("1") && target.matches("|1|2|3|4|5|6|7"));

            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1 Informational");
        blueprint.setMin(0);
        blueprint.setMax(8);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                String target = item.getTarget();
                return (item.getClaim().equals("1") && target.matches("8|9|10|11|12|13|14"));

            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1 (Literary), Target 2");
        blueprint.setMin(1);
        blueprint.setMax(2);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                String target = item.getTarget();
                return (item.getClaim().equals("1") && target.equals("2"));

            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1 (Literary), Target 4");
        blueprint.setMin(1);
        blueprint.setMax(2);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                String target = item.getTarget();
                return (item.getClaim().equals("1") && target.equals("4"));

            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1 (Literary), Target 9");
        blueprint.setMin(1);
        blueprint.setMax(2);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                String target = item.getTarget();
                return (item.getClaim().equals("1") && target.equals("9"));

            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 1 (Informational), Target 11");
        blueprint.setMin(1);
        blueprint.setMax(2);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                String target = item.getTarget();
                return (item.getClaim().equals("1") && target.equals("11"));

            }
        });
        statements.add(blueprint);


        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 2: Writing");
        blueprint.setMin(10);
        blueprint.setMax(10);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("2");
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 2, DOK=2");
        blueprint.setMin(5);
        blueprint.setMax(5);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("2") && item.getDok().equals("2");
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 2, DOK>=3");
        blueprint.setMin(1);
        blueprint.setMax(Integer.MAX_VALUE);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                int dok = Integer.parseInt(item.getDok());
                return item.getClaim().equals("2") && dok >= 3;
            }
        });
        statements.add(blueprint);

        // Claim 2 (At least one O/P item)
        // Target 1a/3a/6a & 1b/3b/6b
        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 2 (At least one O/P item)\nTarget 1a/3a/6a & 1b/3b/6b");
        blueprint.setMin(1);
        blueprint.setMax(2);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("2") && item.getTarget().matches("1aO|3aO|6aO|1bO|3bO|6bO");
            }
        });
        statements.add(blueprint);

        // Claim 2 (At least one E/E item)
        // Target 1a/3a/6a & 1b/3b/6b
        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 2 (At least one E/E item)\nTarget 1a/3a/6a & 1b/3b/6b");
        blueprint.setMin(1);
        blueprint.setMax(2);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("2") && item.getTarget().matches("1aE|3aE|6aE|1bE|3bE|6bE");
            }
        });
        statements.add(blueprint);

        // Claim 2 (Exactly 3 O/P and E/E items)
        // Target 1a/3a/6a & 1b/3b/6b
        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 2 (Exactly 3 O/P and E/E items)\nTarget 1a/3a/6a & 1b/3b/6b");
        blueprint.setMin(3);
        blueprint.setMax(3);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("2") && item.getTarget().matches("1aO|3aO|6aO|1bO|3bO|6bO|1aE|3aE|6aE|1bE|3bE|6bE");
            }
        });
        statements.add(blueprint);

        // Claim 2 (Exactly 1 Write Brief text)
        // Target 1a/3a/6a
        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 2 (Exactly 1 Write Brief text)\nTarget 1a/3a/6a");
        blueprint.setMin(1);
        blueprint.setMax(1);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("2") && item.getTarget().matches("1aO|3aO|6aO|1aE|3aE|6aE");
            }
        });
        statements.add(blueprint);

        // Claim 2 (Only 2 revise brief allowed)
        // Target 1a/3a/6a
        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 2 (Exactly 1 Write Brief text)\nTarget 1b/3b/6b");
        blueprint.setMin(2);
        blueprint.setMax(2);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("2") && item.getTarget().matches("1bO|3bO|6bO|1bE|3bE|6bE");
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 3: Speaking/Listening");
        blueprint.setMin(8);
        blueprint.setMax(9);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("3");
            }
        });
        statements.add(blueprint);

        blueprint = new BlueprintStatement();
        blueprint.setSpecification("Claim 4: Research");
        blueprint.setMin(6);
        blueprint.setMax(6);
        blueprint.setCondition(new BlueprintCondition() {
            @Override
            public boolean test(PoolItem item) {
                return item.getClaim().equals("4");
            }
        });
        statements.add(blueprint);

        return statements;
    }
}
