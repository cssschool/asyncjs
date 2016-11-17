# JavaScript - minimum that You need for workshop

# Basics

## Decalare variable
```
var myString = "my variable";
var myEmptyObject = {};
var myObjectWithSomeProperty = { 
   property1: "valueOfProperty1", 
   property2: 2
   };
var myArray = [1, 2, 3, 4];
```
## Use objects
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

Objects in JavaScripts are like HashMap in Java. 
### Warning
Never forget putting ```var``` before your variable.
Because You will get punished in the most cruel way:
Your variable will become *global*. 


## Functions
Declare function:

```function fun1 (arg1, arg2) {
   var x = arg1 + 7 * arg2;
   return 'whatever' + x;
}
```
Or

```var fun1 = function(arg1, arg2) {
  var x = arg1 + 7 * arg2;
   return 'whatever' + x;
}```

Or (ECMA 6) 
```var fun1 = (arg1, arg2) => 'whatever' + (arg1 + 7*arg2);```



## Arrays
 
 ```var arrayOf = [
      {name}
  ];
  
  ```
