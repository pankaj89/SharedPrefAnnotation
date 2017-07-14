package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Pankaj Sharma on 13/7/17.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Item {
}
