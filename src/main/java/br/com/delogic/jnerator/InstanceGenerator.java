package br.com.delogic.jnerator;

import java.util.List;

public interface InstanceGenerator<T> {

    List<T> generate(int amount);

    List<T> getCachedInstances();

    <E> InstanceGenerator<T> setAttributeGenerator(String attributeName, AttributeGenerator<E, T> attributeGenerator);

}
