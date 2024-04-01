package org.comlkz.common.utils.jacksonModules;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description:
 * @Author: likongzhu
 * @Date: 2022-08-24 09:38
 * @Version： 1.0
 **/
public class CustomJacksonModule extends SimpleModule {

     public CustomJacksonModule(){
         this.addDeserializer(LocalDateTime.class, CustomLocalDateTimeDeserializer.INSTANCE);
         this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
         this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
         this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
         this.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
         this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
         this.addSerializer(Long.class, ToStringSerializer.instance);
         //this.addSerializer(Long.TYPE, ToStringSerializer.instance);
         this.addSerializer(BigInteger.class, ToStringSerializer.instance);
         this.addSerializer(BigDecimal.class, ToStringSerializer.instance);
         //this.addSerializer(BaseEnum.class, EnumSerializer.INSTANCE);
     }
}
