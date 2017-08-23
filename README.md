# boxfish-commons-web-model

![Boxfish Logo](https://raw.github.com/bxfsh/boxfish-commons-web-model/master/src/main/resources/boxfish-logo.jpg)

By [Boxfish](http://www.boxfish.com)

[![Build Status](https://travis-ci.org/bxfsh/boxfish-commons-web-model.svg?branch=master)](https://travis-ci.org/bxfsh/boxfish-commons-web-model)

Minimalist Rails-like HashMap to be used as Input/Update/Query models that scrutinises the input.

* It binds the request input from body AND querystring
* Allows you to *permit* or ignore inbound data
* Allows inline specification of requirements and validation rules
* Provides a model that can be used as body to express the 400 errors
* Removes the need for a InputForm and UpdateForms
* Removes the need for a Validator
* Takes the "magic" of 400 error exception handling magic away
* Standardise input field names, helping compatibility cross camelCase and snake_case

Depends on:

* Nothing! it's a pure Java HashMap specialised to operate inbound input.

## Why a HashModel rather than an InputForm or the Entity?

For a little background, read: [How Homakov hacked GitHub and the line of code that could have prevented it] (https://gist.github.com/peternixey/1978249)

This problem is very similar the one you have if you do this.

```java

    // Controller.java
    @RequestMapping(value = "", method = POST)
    public ResponseEntity<?> create(final @RequestBody @Valid Entity input) {
        final EntityDTO entity = repo.save(input);
        return ResponseEntity
            .created(URI.create("resources/" + entity.getId()))
            .body(entity);
    }

```

The common solution to the problem is creating an *InputForm* (for PUT operations, another model *UpdateForm* may be required as well). Following some good SRP, you will end up having:

* *EntityInputForm:* only allows the wanted input to be bound
* *EntityMapper:* copies the data from your input form into your entity prior to save
* *EntityValidator:* that goes beyond the @Valid annotation and is often required

This *boxfish-commons-web-model* allows the 3 items above to be all performed in-line, reducing the amount of code repetition, keeping our entity clean and ultimately making the controller code a lot more fluent where it takes care of the inbound input.

## Getting started

Install the dependency

```gradle
compile 'com.boxfish:boxfish-commons-web-model:1.0.4'
```

Then update your classpath

```console
gradle eclipse build --refresh-dependencies
```

Include the HashModel as your model in your controller

```java

    @RequestMapping(path = "", method = POST)
    public ResponseEntity<?> create(
            final @RequestBody RestModel input) throws Exception {

        input.require("name");
        if (input.isValid()) {
            final LabelDTO dto = service.create(groupId, input);
        final String resource = "groups/" + groupId + "/labels/" + dto.getId();
            return created(URI.create(resource)).body(dto);
    }
    else
            return badRequest().body(input.errors());
    }

```

And finally

```java

    // Controller.java
    @RequestMapping(path = "", method = POST)
    public ResponseEntity<?> create(
            final @RequestBody RestModel input) throws Exception {

        input
            .require("tags")
            .rulesOnEachChildOf("tags", validateTagStructure("tags"));

        if (input.isValid())
            return ok(service.create(input));
        else
            return badRequest().body(input.errors());
    }

```

And you can update your entity model like this:

```java

    // Service.java
    public TagDTO create(final HashModel input) throws Exception {
        if (input.isValid()) {
            final TagEntity tag = new TagEntity();
            tag.setName(input.get("name").asString());
            return TagDTO.builderFor(tag).build();
        }
        return null;
    }

```

You may use your validation within your service.

However, I believe that worrying about the quality of the input is part of the controller, which will also enable you to control imperatively the Response Status Code based on the quality of your input.

## Features

Find each of the extensions provided by the `RestModel` to the standard java hash map.

### Field Name Normalization

`display_name`, `displayName` for the `RestModel` are the same. It normalizes the input casing and facilitates varying standards of consumers.
E.g.: Javascript will preffer camel case (especially if ESLint is being used), whereas nativaly ruby and python would
have their models in snake case.

### Factory Methods

| Operation                            | Description                                                                                                      |
| ------------------------------------ | ---------------------------------------------------------------------------------------------------------------- |
| `newRestModel()`                     | Simple factory for a new empty `RestModel`, usage: `newRestModel()`                                              |
| `restModelFrom(Map<String, Object>)` | Creates a restModel form an existing map, used specially for injesting querystring, usage: `restModelFrom(map)`. |

### Basic Operations

All the operations implemented by the HashMap<String, Object> are avaiable in the `RestModel`.
We placed here only the operations that extend those of the originally available in the HashMap<,> in any way.

| Operation                                     | Description                                                                                                                                                        |
| --------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `require(field...)`                           | makes a field required, failing during validation `isValid()` if not provided by the user; required fields are automatically permitted.                            |
| `permit(field...)`                            | authorizes a a field to be found/retrieved in the restModel; only permitted fields are visible in the `has(field)`, `containsKey(field)` and `value(field)`        |
| `permitAll()`                                 | permits any field, not requiring each `permit(field)` to be invoked individually.                                                                                  |
| `baseline(field, value)`                      | defines default values for the field, which is only returned when no other value is provided through `value(field)`, or even during the construction of the map.   |
| `isAccepted(field)`                           | `true` if the field is accepted (either required or permitted), `false` otherwise, usage: `isAccepted(field)`                                                      |
| `isValid()`                                   | `restModel.isValid()` returns `true` if all the required fields were provided and all the rules passed, returns `false` otherwise.                                 |
| `errors()`                                    | provides a JSON-friendly POJO loaded with validation errors, usage: `badRequest().body(restModel.errors())`                                                        |
| `get(field)`                                  | returns the RestValue wrapper for the field, and can be used exactly the same way as the `value(field)` method.                                                    |
| `value(field)`                                | `value(field)` is an alias to the method `get(field)` and returns the value, usage: `value(field).asDouble()` for example.                                         |
| `containsValue(field)`                        | *unsupported*, one of the very few exceptions, the rest model does not support the `containsValue`operation.                                                       |
| `has(field)`                                  | checks if the field is available, not null, not empty and *not blank*, usage: `has(field)`.                                                                        |
| `rules(field, validatorBuilder)`              | allow performing extended validation that goes beyond the `required(field)` rule; for usage, see the validation section to learn how to use the validation builder |
| `rulesOnEachChildOf(field, validatorBuilder)` | allow performing extended validation that goes beyond the `required(field)` rule; for usage, see the validation section to learn how to use the validation builder |

### Validation

The `RestModel` provides a simple API for simple validation of inbound fields.

#### Required fields

Use `required(String... fields)` to define the fields that must be provided

```java

    // let input be loaded by @RequestBody in a @RestController (spring)
    // input.errors() will return an error model that can be returned as response body.
    boolean valid = input
        .required("email", "password", "firstName", "lastName")
        .permit("dateOfBirth")
        .isValid();

```

#### Rules

It's also possible to define more complex validation rules using anonymous functions (expressions)
in a very convenient way. Say that only 18+ years old are allowed to acquire a particular service:

```java

    // let input be loaded by @RequestBody in a @RestController (spring)
    // input.errors() will return an error model that can be returned as response body.
    boolean valid = input
        .require("dateOfBirth")
        .rules("dateOfBirth", (c) -> c
            .forType(Instant.class)
            .ifValueFailsOn((model, dateOfBirth) -> now().minus(18, YEARS).isBefore(dateOfBirth))
            .warnWith("the user is required to be 18+ years old."))
        .isValid();

    UserEntity entity = UserEntity.builderFor(input).build();
    if (valid)
        return ok(dao.save(entity));
    else
        return badRequest().body(input.errors());

```

#### Rules on Each Child

Likewise, you can run the same `rules` with a few exceptions. See the example below for more information.

```java

    // let input be loaded by @RequestBody in a @RestController (spring)
    // input.errors() will return an error model that can be returned as response body.
    boolean valid = input
        .require("datesOfArrival")
        .rulesOnEachChildOf("datesOfArrival", (c) -> c
            .ifValueFailsOn((model, rawDateOfArrival) -> now().minus(2, YEARS).isBefore(rawDateOfArrival.asInstant()))
            .warnWith("one of the arrivals happened more than 2 years ago"))
        .isValid();

```

## Castless Conversions

All values injested by a `RestModel`, on the way out are wrapped by a class called `RestValue`. This class has operations for most of the
default types, all their generic `List<T>` and to the `RestModel` itself, which ultimately allow limitless nesting of the structure.

The types supported are the following:

* `asString()`
* `asLong()`
* `asInteger()`
* `asShort()`
* `asBoolean()`
* `asBigDecimal()`
* `asFloat()`
* `asDouble()`
* `asInstant()`
* `asTimestamp()`
* `asModel()`
* `asList()`
* `asListOf(T)`