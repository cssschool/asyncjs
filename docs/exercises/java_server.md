# Java server

1. Server used in exercises is written in Java.
2. It has main method - in *WeatherServer.java*.
3. It uses *Ratpack* library to declare http services.
4. It has fully non blocking - architecture.
5. You can check that even tough it uses only few threads it can server  
it can serve thousands of concurrent connections.
6. You can change how good it works changing this line in *WeatherServer* 
```
private final Destabilizer destabilizer = new Destabilizer(200, 0.0, 0.0);
```
Destabilizer can make server slow and unstable. Which you can use to 
test error handling in JavaScript.
For instance
```
private final Destabilizer destabilizer = new Destabilizer(500, 0.1, 0.02);
```
means that server that every request will be processed about 500ms,
and 10% of request will fail, and  2% of requests will  hang forever.
