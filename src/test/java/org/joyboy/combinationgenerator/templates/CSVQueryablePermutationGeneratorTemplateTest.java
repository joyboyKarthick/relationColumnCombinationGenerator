package org.joyboy.combinationgenerator.templates;

import org.junit.jupiter.api.Test;

class CSVQueryablePermutationGeneratorTemplateTest {

    @Test
    public void CSVQueryablePermutationGeneratorTemplateTest() throws Exception {
        String confToml = "src/test/resources/CsvTemplateInput.toml";
        CSVQueryablePermutationGeneratorTemplate csvQueryablePermutationGeneratorTemplate = new CSVQueryablePermutationGeneratorTemplate(confToml);
        csvQueryablePermutationGeneratorTemplate.generate();

    }

}