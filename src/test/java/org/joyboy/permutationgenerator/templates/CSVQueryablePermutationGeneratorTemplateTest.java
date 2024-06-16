package org.joyboy.permutationgenerator.templates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVQueryablePermutationGeneratorTemplateTest {

    @Test
    public void CSVQueryablePermutationGeneratorTemplateTest() throws Exception {
        String confToml = "src/test/resources/CsvTemplateInput.toml";
        CSVQueryablePermutationGeneratorTemplate csvQueryablePermutationGeneratorTemplate = new CSVQueryablePermutationGeneratorTemplate(confToml);
        csvQueryablePermutationGeneratorTemplate.generate();

    }

}