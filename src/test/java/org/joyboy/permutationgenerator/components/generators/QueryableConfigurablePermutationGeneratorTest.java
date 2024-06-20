package org.joyboy.permutationgenerator.components.generators;

import org.joyboy.permutationgenerator.components.columnsources.BasicColumnSource;
import org.joyboy.permutationgenerator.components.criteria.ColumnCriteriaBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QueryableConfigurablePermutationGeneratorTest {

    @Test
    public void testGenerator() throws Exception {

        // source columns
        List<String> column1Source = Arrays.asList("app1","app2","app3");
        BasicColumnSource<String, List<String>> basicColumn1Source= new BasicColumnSource(column1Source,"column1 source");
        List<String> column2Source = Arrays.asList("resource1","resource2","resource3","resource4");
        BasicColumnSource<String, List<String>> basicColumn2Source= new BasicColumnSource(column2Source,"column2 source");
        List<String> column3Source = Arrays.asList("get","post","put","delete");
        BasicColumnSource<String, List<String>> basicColumn3Source= new BasicColumnSource(column3Source,"column3 source");

        // criteria creation
        ColumnCriteriaBuilder<BasicColumnSource<String,List<String>>> skipColum1ByColumn2 = new ColumnCriteriaBuilder<>()
                .setCriteriaColumn(Arrays.asList(basicColumn2Source)).setSkippedColumn(Arrays.asList(basicColumn1Source));
        skipColum1ByColumn2.setPredicate((row)->row.get(basicColumn2Source).equals("resource3"));

        QueryableConfigurablePermutationGenerator  queryableConfigurablePermutationGenerator = new QueryableConfigurablePermutationGenerator<BasicColumnSource>() {
            @Override
            public void destinationHook(Map paths) throws Exception {
                System.out.println(paths.values());
            }
        };

        queryableConfigurablePermutationGenerator.addSourceColumns(Arrays.asList(basicColumn1Source,basicColumn2Source,basicColumn3Source)); //add column source
        queryableConfigurablePermutationGenerator.addColumnSkipCriteria(skipColum1ByColumn2.build()); //add skip column criteria
        queryableConfigurablePermutationGenerator.generate();


    }

}