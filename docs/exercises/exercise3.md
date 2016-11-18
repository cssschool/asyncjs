# Sources

Please open two files:
src/main/webapp/js/weatherController.js
src/main/webapp/js/weatherService.js


# Goal

Solve problem from exercise2 using promises.

# Hint
Try to use Promise.all() - that will be resolved when both parts are resolved.


# Promises
You create promise with:
```
   var result = new Promise( function(resolve, reject) {
   ...
        resolve( 123); // resolves promise with 123 as result
   ...     
    //reject("error");  - resolves with error
   });
```
   
See
[Promises ](https://developer.mozilla.org/pl/docs/Web/JavaScript/Reference/Global_Objects/Promise)
   for more.


