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

## Creating a Release

This repository is configured to compile and upload the ```openapi-generator-cli.jar``` every time a push to the branch ```liongate``` occurs. But those files are only accessible under workflows and are automatically deleted after a while. To permanently and more easily have access to the ```openapi-generator-cli.jar``` you may create a release. 

If you want to create a new release, it is easiest to just create a new tag. Upon tag creation this repository is configured to automatically create a new release, generate the ```openapi-generator-cli.jar``` and upload it as a release asset. The name of the release will be "Release {TAG_NAME}".
