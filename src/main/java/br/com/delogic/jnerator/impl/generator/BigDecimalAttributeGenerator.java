package br.com.delogic.jnerator.impl.generator;

import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.delogic.jnerator.AttributeConfiguration;
import br.com.delogic.jnerator.AttributeGenerator;

public class BigDecimalAttributeGenerator implements AttributeGenerator<BigDecimal> {

    public BigDecimal generate(int index, AttributeConfiguration attributeConfiguration) {
        return new BigDecimal(new BigInteger((attributeConfiguration.getName() + index).getBytes()));
    }

}