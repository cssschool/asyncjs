# Async in ES7

Bitte erst den Artikel zu [generators.md](generators.md) lesen.

Auch mit ES7 sind Promises wichtig. Die Arbeit mit Ihnen wird aber ähnlich den Generatoren, welche
man seit ES6 für asynchrone Zwecke missbrauchen kann, deutlich erleichtert.

Die Keywords lauten mit ES7 `async` und `await`. 

## Async-Funktionen

*ES6 mit Generatoren:*

```javascript
function getSatisfaction (city) {
	return async(function*() {
		return Math.abs(yield getTemperature(city)-25)*50 + yield getTransport(city);
	});
}
```

*Pandant dazu mit ES7:*

```javascript
async function getSatisfaction (city) {
	return Math.abs(await getTemperature(city)-25)*50 + await getTransport(city);
}
```

Als Unterschied erkennt man hier, dass man keine `async`-Funktion mehr zu schreiben braucht um das ganze abzukürzen. Mit der neuen 
Version wird die Magie bereits mit spezifiziert.

Als Einschränkung gilt hier aber, dass ein `await` in einer nested Funktion nicht funktioniert. 

```javascript
async function getSatisfaction (city) {
	await doSomething();
	(function() {
		await asyncOp1();// funktioniert nicht
	})();
}
```