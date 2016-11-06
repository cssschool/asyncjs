# Generators

## Einleitung

Mit ES6 gibt es neu Generatoren. Diese ähneln sehr den Iteratoren welche wir aus Java kennen.

Bisher brauchen wir für eine Iteration ein Array, da dies iterable war.

```javascript
	function count(n) {
		return Array.from(new Array(n)).map((x, i) => i);
	}

	for (var i of count(5)) {
		console.log(i);
	}
```

Die Generatoren sind jedoch selbst iterable, so dass wir nun kein Array als Hilfsobjekt mehr brauchen. 

```javascript
	function* count(n){
	  for (var x = 0; x < n; x++) {
	    yield x
	  }
	}

	for (var x of count(5)) {
	  console.log(x)
	}
```

Eine Generator-Funktion erkennt man an dem Sternchen `function* count`.
Beinhalten tut diese ausserdem ein oder mehrfach das Keyword yield. Dieses ist ein haltepunkt, bei dem der Generator auf die nächste iteration wartet.

```javascript
	function* count(n){
	  for (var x = 0; x < n; x++) {
	    yield x
	  }
	}

	var iterator = count(5);
	console.log(iterator.next()); // {value: 0, done: false}
	console.log(iterator.next()); // {value: 1, done: false}
	console.log(iterator.next()); // {value: 2, done: false}
	console.log(iterator.next()); // {value: 3, done: false}
	console.log(iterator.next()); // {value: 4, done: false}
	console.log(iterator.next()); // {value: undefined, done: true}
```

Man sieht, dass das iterator.next() immer ein Objekt mit einem `value` und dem booleaschen Parameter `done` zurückgibt. Somit können wir feststellen, ob der Generator sein "Ende" erreicht hat oder ob weitere Iterationen folgen.
An dem Beispiel sieht man jedoch auch, dass der Generator bei dem Keyword `yield` stehen bleibt und wartet.

## Async mit Generatoren

Da die Generatoren warten, bis ein `.next()` aufgerufen wird, bevor sie fortlaufen, kann man dies benutzen um asynchrone Aufrufe wie Synchrone aussehen zu lassen. Als Beispiel nehmen wir die Funktion `getSatisfaction(city)`. Diese beinhaltet zwei asynchrone Aufrufe, welche parallel oder seriell bearbeitet werden müssen, bevor die Funktion das Ergebnis zurückgeben kann.

```javascript
	function getSatisfaction (city) {
        return Math.abs(getTemperature(city)-25)*50 + getTransport(city);
    }
```

In diesem Kurs haben wir ja bereits das Thema Promises behandelt und die Funktion sieht jetzt ungefähr so aus:

*Seriell:*

```javascript
	function getSatisfaction (city) {
		return getTemperature(city).then(temp => {
			return getTransport(city).then(transport => {
				return Math.abs(temp-25) * 50 + transport;
			});
		});
    }
```

*Parallel:*

```javascript
	function getSatisfaction (city) {
		var temp;
		var transport;
		return Promise.all([
				getTemperature(city).then(value => temp = value),
				getTransport(city).then(value => transport = value)
			]).then( () => {
	        	return Math.abs(temp-25)*50 + transport;
        	});
    }
```

Wenn wir nun die Generatoren zur Hilfe nehmen, könnte das wie folgt aussehen:

*Seriell:*

```javascript
	function getSatisfaction (city) {
		return async(function*() {
			return Math.abs(yield getTemperature(city)-25)*50 + yield getTransport(city);
		});
    }
```


*Parallel:*

```javascript
	function getSatisfaction (city) {
		return asnyc(function*() {
			var temp;
			var transport;
			yield Promise.all([
					getTemperature(city).then(value => temp = value),
					getTransport(city).then(value => transport = value)
				])
		    return Math.abs(temp-25)*50 + transport;
		});
    }
```

Wie man erkennt ist das ganze (gerade das Serielle) viel einfacher und kürzer geschrieben. Callback Code kann dadurch komplett entfernt werden, was die Wartbarkeit erheblich verbessert. Verschachtelung wird auf eine Ebene reduziert.

Aber was verbirgt sich hinter dieser `async`-Funktion?

## Die `async`-Funktion

Nun wird es etwas kniffliger. Um das genannte zu erreichen, braucht man eine Funktion welche den Iterator, welcher übergeben wird, auflöst. In unserem Beispiel haben wir einen anonymen Generator, welcher übergeben wird.

```javascript
	function async(makeGenerator){
		var generator = makeGenerator();

		function handle(result){

			if (result.done) {
				return Promise.resolve(result.value);
			}
			
			return Promise.resolve(result.value).then(
				(res) => {
					return handle(generator.next(res));
				}, 
				(err) => {
					return handle(generator.throw(err));
				}
			);

	    }

	    try {
	      return handle(generator.next());
	    } catch (ex) {
	      return Promise.reject(ex);
	    }
	}
```	

Zuerst wird der Generator "instanziert". In der `handle`-Funktion geschieht die Magie. Diese prüft zuerst ob bei dem übergebenen 
Result der Boolean done auf true steht und gibt, falls ja, ein `Promise.resolve(result.value)` zurück. Dies hat zur Folge, dass die 
ganze Asny-Funktion beendet wird.
Wenn nein, wird ebenfalls ein Promise erzeugt, welches wiederum beim resolven oder bei einem Fehler die handle Funktion aufruft 
mit dem fehler oder dem erfolgreichen Result.
Initial wird die `handle`-Funktion mit dem ersten `generator.next()`-Result aufgerufen. Im Falle eines Fehlers wird die Promise
rejected. 
Wichtig bei dem Vorgehen ist jedoch, dass man nur Promises bei einem yield verwendet, da ansonsten die `.then`-Methode nicht vorhanden
ist.
