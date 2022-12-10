package org.openapitools.codegen.php.laravel.lg;

import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.PhpLaravelLgServerCodegen;
import io.swagger.models.*;
import io.swagger.models.properties.*;

import org.testng.Assert;
import org.testng.annotations.Test;

@SuppressWarnings("static-method")
public class PhpLaravelLgServerCodegenModelTest {

    @Test(description = "convert a simple java model")
    public void simpleModelTest() {
        final Model model = new ModelImpl()
                .description("a sample model")
                .property("id", new LongProperty())
                .property("name", new StringProperty())
                .required("id")
                .required("name");
        final DefaultCodegen codegen = new PhpLaravelLgServerCodegen();

        // TODO: Complete this test.
        Assert.fail("Not implemented.");
    }

}

