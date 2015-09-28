package io.mikesir87.bacabs.service;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A Qualifier that specifies an injection based on application configuration.
 *
 * Examples of use are the JiraClient, where configuration can determine if a
 * Basic or Cas authenticated client should be used.
 *
 * @author Michael Irwin
 */
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface ChosenPerConfiguration {
}
