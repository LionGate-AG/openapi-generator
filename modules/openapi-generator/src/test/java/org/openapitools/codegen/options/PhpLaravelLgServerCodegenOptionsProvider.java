package org.openapitools.codegen.options;

import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.languages.PhpLaravelLgServerCodegen;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class PhpLaravelLgServerCodegenOptionsProvider implements OptionsProvider {
    public static final String PROJECT_NAME_VALUE = "PhpLaravelLiongate";

    @Override
    public String getLanguage() {
        return "php-laravel-lg";
    }

    @Override
    public Map<String, String> createOptions() {
        ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<String, String>();
        return builder
                .put(PhpLaravelLgServerCodegen.PROJECT_NAME, PROJECT_NAME_VALUE)
                .build();
    }

    @Override
    public boolean isServer() {
        return false;
    }
}

