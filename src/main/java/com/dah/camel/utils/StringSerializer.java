package com.dah.camel.utils;

import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class StringSerializer extends StringRedisSerializer {
}
