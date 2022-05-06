# Getting Started

```sh
    git clone https://github.com/LionGate-AG/openapi-generator
    cd openapi-generator
```

## Maven install

```sh
    ./mvnw clean install -DskipTests -Dadditionalparam=-Xdoclint:none -DadditionalJOption=-Xdoclint:none
```

## Generate code from OpenAPI

Available generators:
1. php-lava-lumen
2. php-lava-laravel

```sh
java -jar modules/openapi-generator-cli/target/openapi-generator-cli.jar generate \
   -i https://raw.githubusercontent.com/openapitools/openapi-generator/master/modules/openapi-generator/src/test/resources/3_0/petstore.yaml \
   -g {GENERATOR_NAME} \
   -o /path/to/outputdir/
```

```sh
java -jar modules/openapi-generator-cli/target/openapi-generator-cli.jar generate \
    -i https://raw.githubusercontent.com/openapitools/openapi-generator/master/modules/openapi-generator/src/test/resources/3_0/petstore.yaml \
    -g {GENERATOR_NAME} \
    --global-property models,modelDocs,modelTests,apis,apiTests,apiDocs,supportingFiles \
    --invoker-package Modules \
    --api-package {API_NAME}\\Http\\Controllers\\Generated \
    --model-package {API_NAME}\\Http\\Models\\Generated \
    --additional-properties=customPathPrefix={CUSTOM_PREFIX_FOR_ROUTES} \
    -o /path/to/output/
```

## Adjusting generators

Templates path: ```/modules/openapi-generator/src/main/resources/{GENERATOR_NAME}/```

Code processor path: ```/modules/openapi-generator/src/main/java/org/openapitools/codegen/languages/```
