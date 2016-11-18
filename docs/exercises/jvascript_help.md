# JavaScript - minimum that You need for workshop

# Basics

## Declare variable
```
var myString = "my variable";
var myEmptyObject = {};
var myObjectWithSomeProperty = { 
   property1: "valueOfProperty1", 
   property2: 2
   };
var myArray = [1, 2, 3, 4];
```
## Objects
There are no classes. Only objects!

``` var myEmptyObject = {};```
### Set property;
```myEmptyObject.prop1 = 7;```
or 
```myEmptyObject['prop1'] = 7;```

### Read property
``` var a = myObject.prop1```
or
``` var a = myObject['prop1']```

*Objects in JavaScripts are like Map in Java.*
 
 
### Warning
Never forget putting ```var``` before your variable.
Otherwise You will get punished in the most cruel way:
Your variable will become *global*. 


## Functions
Declare function:

```function fun1 (arg1, arg2) {
   var x = arg1 + 7 * arg2;
   return 'whatever' + x;
}
```
Or

```
var fun1 = function(arg1, arg2) {
  var x = arg1 + 7 * arg2;
   return 'whatever' + x;
}
```

Or (ECMA 6) 
```
var fun1 = (arg1, arg2) => 'whatever' + (arg1 + 7*arg2);
```

Function is also an object.

### Pitfalls
1. There is no overloading.
 ```
 function a(arg1) {
 }
 
 function a(arg1,arg2) {
  }
 
```
will create only one function that uses  both arguments.

2. You can declare that a function has 2 parameters, you can still call it
with 5 parameters and ... you can even use those 5 in body.
(_JavaScript does not ask silly questions. JavaScript understands_)

3. Beware of *this*
*this* keyword exists in JavaScript but behaves very unintuitive. It is best to avoid it unless you read
exactly how it works.


## Arrays
 
```
 var arrayOfObjects = [
      {name: "n1"} , {name: "n2"} 
  ];
```

### Array operations
```
arrayOfObjects.forEach ( function(element) {
    console.log( element.name);
});
```

```
var newArray = arrayOfObjects.map ( function(element) {
    return element.name.length;
});
```

# Ajax

We use zepto.js which is simplified version of jQuery.

```
$.get("/services/transport/" + cityId).done(function (data) {
                    console.log(data);//callback called  on received answer from server 
                }).fail( function() {
                    console.log("oops"); //callback called  on error received from server
                });
```