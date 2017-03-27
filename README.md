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
compile 'com.boxfish:boxfish-commons-web-model:1.0.1'
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
