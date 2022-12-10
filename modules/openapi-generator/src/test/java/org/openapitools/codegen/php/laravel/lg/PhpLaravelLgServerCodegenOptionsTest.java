package org.openapitools.codegen.php.laravel.lg;

import org.openapitools.codegen.AbstractOptionsTest;
import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.languages.PhpLaravelLgServerCodegen;
import org.openapitools.codegen.options.PhpLaravelLgServerCodegenOptionsProvider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PhpLaravelLgServerCodegenOptionsTest extends AbstractOptionsTest {
    private PhpLaravelLgServerCodegen codegen = mock(PhpLaravelLgServerCodegen.class, mockSettings);

    public PhpLaravelLgServerCodegenOptionsTest() {
        super(new PhpLaravelLgServerCodegenOptionsProvider());
    }

    @Override
    protected CodegenConfig getCodegenConfig() {
        return codegen;
    }

    @SuppressWarnings("unused")
    @Override
    protected void verifyOptions() {
        // TODO: Complete options using Mockito
        // verify(codegen).someMethod(arguments)
    }
}

