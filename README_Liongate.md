# Getting Started

```sh
    git clone https://github.com/LionGate-AG/openapi-generator
    cd openapi-generator
```

## Maven install

```sh
    ./mvnw clean install -DskipTests -Dadditionalparam=-Xdoclint:none -DadditionalJOption=-Xdoclint:none
```

## Genearte lumen code from OpenAPI

```sh
java -jar modules/openapi-generator-cli/target/openapi-generator-cli.jar generate \
   -i https://raw.githubusercontent.com/openapitools/openapi-generator/master/modules/openapi-generator/src/test/resources/3_0/petstore.yaml \
   -g php-lava-lumen \
   -o /path/to/outputdir/
```

```sh
java -jar modules/openapi-generator-cli/target/openapi-generator-cli.jar generate \
    -i https://raw.githubusercontent.com/openapitools/openapi-generator/master/modules/openapi-generator/src/test/resources/3_0/petstore.yaml \
    -g php-lava-lumen \
    --global-property models,modelDocs,modelTests,apis,apiTests,apiDocs,supportingFiles \
    --invoker-package Modules \
    --api-package {API_NAME}\\Http\\Controllers \
    --model-package {API_NAME}\\Http\\Models \
    --additional-properties=customPathPrefix={CUSTOM_PREFIX_FOR_ROUTES} \
    -o /path/to/output/
```
