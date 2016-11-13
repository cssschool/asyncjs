# Sources

Please open two files:
src/main/webapp/js/weatherController.js
src/main/webapp/js/weatherService.js

# Application
You look for a place to fly on the weekend - 
it must be affordable and have good enough weather for you.
So You select desired weather... and you should see which destinations are OK. 


Our page displays if Temperature and Travel costs are acceptable.
To use it user has to select desired temperature (like 20')  and cost per difference
  like 20CHF. 
  
So if for instance there is 19' in London and travel cost there is 150CHF then
  total cost would be: 190 = 150CHF + (21-19)*20CHF.
  
We assume that acceptable cost is 500CHF.

# Goal
  weatherService.js
  there are 2 ready functions that call server:
  getTemperature()
  and getTransport()
  
  You goal is to implement function
  getSatisfaction() using both above (or you may call server directly).
  
  Function getSatisfaction() should simply give number for given city Id.
  Number should be as fallows:
   = |idealTemperaure - PredictedTemperatureInCity| * costPerGrade + travelCost
   
   
  
  
  





