package br.com.delogic.jnerator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.delogic.jnerator.test.entities.Additional;
import br.com.delogic.jnerator.test.entities.AdditionalOrderItem;
import br.com.delogic.jnerator.test.entities.Address;
import br.com.delogic.jnerator.test.entities.CashPaymentMode;
import br.com.delogic.jnerator.test.entities.Category;
import br.com.delogic.jnerator.test.entities.City;
import br.com.delogic.jnerator.test.entities.CreditCardPaymentMode;
import br.com.delogic.jnerator.test.entities.DeliveryRegionByCity;
import br.com.delogic.jnerator.test.entities.DeliveryRegionByZipCode;
import br.com.delogic.jnerator.test.entities.HomeDelivered;
import br.com.delogic.jnerator.test.entities.ItemProduct;
import br.com.delogic.jnerator.test.entities.LocalClient;
import br.com.delogic.jnerator.test.entities.OnlineClient;
import br.com.delogic.jnerator.test.entities.OnlineClientAddress;
import br.com.delogic.jnerator.test.entities.Order;
import br.com.delogic.jnerator.test.entities.PhoneClient;
import br.com.delogic.jnerator.test.entities.Product;
import br.com.delogic.jnerator.test.entities.ProductOrderItem;
import br.com.delogic.jnerator.test.entities.StoreDelivered;
import br.com.delogic.jnerator.test.entities.Tenent;
import br.com.delogic.jnerator.test.entities.TenentAddress;
import br.com.delogic.jnerator.test.entities.TenentPaymentsAccepted;
import br.com.delogic.jnerator.test.entities.TenentStorePersonalization;
import br.com.delogic.jnerator.test.entities.TenentWorkingHours;
import br.com.delogic.jnerator.util.ReflectionUtils;

public class JNeratorTest extends Assert {

    private JNerator jNerator;
    private int      amount = 100;

    @Before
    public void init() {
        jNerator = new JNeratorImpl();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test() {

        // Collection<Address> addresses = null;
        // jNerator.prepare(Tenent.class).forAttr("address").use(addresses)
        // .forAttr("abc").use("entity")
        // .forAttr("abc").use(addresses)
        // .forAttr("abc").use("entity1", "entity2")
        // .forAttr("abc").use(10L, 20d)
        // .forAttr("abc").useSequentially("entity1", "entity2")
        // .forAttr("abc").useLoremIpsum(500)
        // .forRelationship("cars", Arrays.asList(HomeDelivered.class,
        // StoreDelivered.class))
        // .generate(50);

//        jNerator.prepare(Tenent.class).forAttr("teste").use(new AttributeGenerator(){
//
//            public <T> Object generate(int index, AttributeConfiguration<T> attributeConfiguration, Object instance) {
//                // TODO Auto-generated method stub
//                return null;
//            }}).generate(10);

        assertHasData(jNerator.prepare(Tenent.class).generate(amount));
        assertHasData(jNerator.prepare(Category.class).generate(amount));
        assertHasData(jNerator.prepare(Product.class).generate(amount));
        assertHasData(jNerator.prepare(City.class).generate(amount));
        assertHasData(jNerator.prepare(Address.class).generate(amount));
        assertHasData(jNerator.prepare(TenentAddress.class).generate(amount));
        assertHasData(jNerator.prepare(TenentPaymentsAccepted.class).generate(amount));
        assertHasData(jNerator.prepare(TenentStorePersonalization.class).generate(amount));
        assertHasData(jNerator.prepare(TenentWorkingHours.class).generate(amount));
        assertHasData(jNerator.prepare(LocalClient.class).generate(amount));
        assertHasData(jNerator.prepare(OnlineClient.class).generate(amount));
        assertHasData(jNerator.prepare(OnlineClientAddress.class).generate(amount));
        assertHasData(jNerator.prepare(PhoneClient.class).generate(amount));
        assertHasData(jNerator.prepare(Additional.class).generate(amount));
        assertHasData(jNerator.prepare(DeliveryRegionByCity.class).generate(amount));
        assertHasData(jNerator.prepare(DeliveryRegionByZipCode.class).generate(amount));

        InstanceGenerator<Order> orderGenerator = jNerator.prepare(Order.class);

        orderGenerator.forRelationship("deliveryMode", Arrays.asList(HomeDelivered.class, StoreDelivered.class));
        orderGenerator.forRelationship("paymentMode", Arrays.asList(CashPaymentMode.class, CreditCardPaymentMode.class));

        InstanceGenerator<ItemProduct> itemProductsGenerators = orderGenerator
            .forRelationship("orderItens", ItemProduct.class);
        itemProductsGenerators.forRelationship("additionals", AdditionalOrderItem.class);
        itemProductsGenerators.forRelationship("products", ProductOrderItem.class);

        List<Order> orders = orderGenerator.generate(amount);
        assertHasData(orders);

    }

    private void assertHasData(List<?> objects) {
        assertNotNull(objects);
        assertTrue(objects.size() >= amount);

        List<Field> allFields = ReflectionUtils.getAllDeclaredFields(objects.get(0).getClass());

        for (Object obj : objects) {
            for (Field field : allFields) {
                try {
                    field.setAccessible(true);
                    assertNotNull(field.get(obj));
                } catch (Exception e) {
                    throw new RuntimeException("Error when trying to get field value for assert", e);
                }
            }
            toString(obj);
        }
    }

    private void toString(Object object) {
        System.out.println(ToStringBuilder.reflectionToString(object, ToStringStyle.DEFAULT_STYLE));
    }

}
