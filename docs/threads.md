# Threads in JavaScript


## Single threaded
Simple answer is : There is only one single thread in JavaScript.

It means:
There is no possiblity to pause  function for a while, and wait for sth else... because nothing else can happen.

All IO operations however are provided by browser. And require callbacks.
It means you have to pass function that will be called when response comes.

```
$.get('http://my_url').done(  function(data) {
        this is my callback
 });
```

```
setTimeout(  function(data) {
        this is my callback
 }, 2000);
```
# Browsers use threads internally
In order to process JavaScript timeouts, ajax requests - browser may create internally
more threads - those are however invisible from JavaScript.
All code executes on a single thread.

This means you do not have to synchronize variables etc.

# WebWorkers
In HTML5 introduced concept of webworkers.
Web worker is like additional JavaScript engine - that also work on a single thread
and has NO access to the main JavaScript, and cannot access page (manipulate DOM).

WebWorkers can communicate with server and may send and receive events from main thread.
WebWorkers are like Actors!



