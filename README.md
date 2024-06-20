---

# Permutation Generator Library for Java

## Overview
This Java library provides utilities to generate permutations from a given column source, with support for skipping particular columns and combinations based on user-defined criteria.

## Features
- **Permutation Generation template:** Generates permutations from a specified predefined column source.
- **Permutation Generation:** Generates permutations from a specified column source.
- **Column Skipping:** Allows users to skip specific columns during permutation generation based on defined criteria.
- **Combination Skipping:** Provides functionality to skip combinations of columns based on specified conditions.

## Installation
1. **Download the Library:**
   - Download the latest JAR file from the releases section of the repository.

2. **Include in Your Project:**
   - Add the downloaded JAR file to your Java project's classpath.

## Usage
1. **Prepare the Column source for Permutation Generator:**
   ```java
        List<String> column1Source = Arrays.asList("app1","app2","app3");
        BasicColumnSource<String, List<String>> basicColumn1Source= new BasicColumnSource(column1Source,"column1 source");
        List<String> column2Source = Arrays.asList("resource1","resource2","resource3","resource4");
        BasicColumnSource<String, List<String>> basicColumn2Source= new BasicColumnSource(column2Source,"column2 source");
        List<String> column3Source = Arrays.asList("get","post","put","delete");
        BasicColumnSource<String, List<String>> basicColumn3Source= new BasicColumnSource(column3Source,"column3 source");
   ```

2. **Setup Column Source:**
   - Define your column source from which permutations will be generated.
   ```java
         ColumnCriteriaBuilder<BasicColumnSource<String,List<String>>> skipColum1ByColumn2 = new ColumnCriteriaBuilder<>()
                .setCriteriaColumn(Arrays.asList(basicColumn2Source)).setSkippedColumn(Arrays.asList(basicColumn1Source));
        skipColum1ByColumn2.setPredicate((row)->row.get(basicColumn2Source).equals("resource3"));
   ```
   
3. **Initialize the Permutation Generator:**
   ```java
        QueryableConfigurablePermutationGenerator  queryableConfigurablePermutationGenerator = new QueryableConfigurablePermutationGenerator<BasicColumnSource>() {
            @Override
            public void destinationHook(Map paths) throws Exception {
                System.out.println(paths.values());
            }
        };
   ```
      
5. **Configure Skipping Criteria:**
   - Define criteria for skipping columns and combinations using appropriate methods provided by the library.
   ```java
        queryableConfigurablePermutationGenerator.addSourceColumns(Arrays.asList(basicColumn1Source,basicColumn2Source,basicColumn3Source)); //add column source
        queryableConfigurablePermutationGenerator.addColumnSkipCriteria(skipColum1ByColumn2.build()); //add skip column criteria
        queryableConfigurablePermutationGenerator.generate();
   ```     

6. **Generate Permutations:**
   full generation sample code
   ```java
   // Example code to generate permutations
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

   ```

8. **Handle Output:**
   - By implement destinationHook Process or store generated permutations as required by your application.

## Example Code
you can use, extend or refer predefined template for design your needs. 

```java

public class Main {
    public static void main(String[] args) {
      
      String confToml = "src/test/resources/CsvTemplateInput.toml";
      CSVQueryablePermutationGeneratorTemplate csvQueryablePermutationGeneratorTemplate = new CSVQueryablePermutationGeneratorTemplate(confToml);
      csvQueryablePermutationGeneratorTemplate.generate();
    }

}
```

## Configuration
- Extend the library's methods and classes according to your specific requirements.
- you can find csv template configuration from this location src/test/resources/CsvTemplateInput.toml

## Contributing
Contributions to improve this library are welcome! Please fork the repository and submit pull requests with proposed enhancements.

## License
This library is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments
- Inspiration for this library came from real life case  where need to generate testcase possibility .

---
