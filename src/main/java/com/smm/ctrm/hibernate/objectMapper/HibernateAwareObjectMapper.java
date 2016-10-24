package com.smm.ctrm.hibernate.objectMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
 
public class HibernateAwareObjectMapper extends ObjectMapper {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -8433399942817917225L;

	public HibernateAwareObjectMapper() {
		
		Hibernate5Module hm = new Hibernate5Module();
        super.setPropertyNamingStrategy(new MyPropertyNamingStrategy());
//        super.enable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
//        super.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        registerModule(hm);
    }
}