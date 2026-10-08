# Einkaufsliste: MVC, JPA, Validierung und Servlet

Java 17 / Spring Boot 4.1.1; Anwendungspaket `de.aswggmbh.demo`.
Dieses Projekt ergänzt die verkürzten Folienbeispiele um Imports,
Zugriffsmethoden, Template, Abhängigkeiten und den Validierungsfehlerpfad.

## Import und Start

Eclipse wie in der beigefügten `ECLIPSE.md` einrichten. Über **File → Import →
General → Existing Projects into Workspace → Select archive file** das
`WEB-II-3-Shoppinglist.zip` importieren. Maven lädt die Abhängigkeiten;
bei Bedarf **Maven → Update Project** ausführen. `DemoApplication` als
**Java Application** starten oder ihre mitgelieferte Startkonfiguration
unter **Run Configurations → Java Application** wählen.

`http://localhost:8080/` zeigt Liste und Formular. Einen Artikel hinzufügen;
GET, POST und Redirect sind in den Browser-Entwicklerwerkzeugen sichtbar.
Die H2-Datenbank liegt im Arbeitsspeicher und wird beim Beenden verworfen.
Nur einen der beiden Webserver gleichzeitig auf Port 8080 starten.
Alternativ: ZIP entpacken, **Maven → Existing Maven Projects** importieren
oder im Projektordner `mvn spring-boot:run` verwenden.

## Ungültige Eingaben ausprobieren

Die HTML-Eingabefelder begrenzen bereits im Browser leere/negative Werte.
Um die zusätzliche serverseitige Validierung zu beobachten, in den
Entwicklerwerkzeugen am Formular vorübergehend `novalidate` ergänzen oder
einen eigenen HTTP-POST mit `name=&demand=-1&supply=0` senden. Das Formular
zeigt Feldfehler und behält Eingaben sowie vorhandene Einträge; es wird
nichts Neues gespeichert. Nichtnumerische Mengen erzeugen ebenfalls
einen Bindungsfehler. Erfolgreiche Eingaben werden über JPA gespeichert.

## Servlet und Filter

`http://localhost:8080/list` liefert die feste JSON-Antwort `{"items":[]}`
des unabhängigen Servlet-Beispiels. Es ist keine REST-Schnittstelle für
die Datenbank. `@ServletComponentScan` registriert Servlet und Filter;
`LoggingFilter` protokolliert Anfragen und Statuscodes in der Console.

Der POM bringt Web MVC, Thymeleaf, Data JPA, Bean Validation und H2 mit.
Die automatisierten HTTP-Beispiele unter `src/test/java` lassen sich
mit **Run As → Maven test** bzw. `mvn test` ausführen.
