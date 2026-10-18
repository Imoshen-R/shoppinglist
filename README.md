## 🛠 Basis des Projekts & Danksagung

Dieses Projekt basiert auf dem Starter-Template **`WEB-II-3-Shoppinglist.zip`** aus der Lehrveranstaltung *Webanwendungen (Kapitel II.3: Backend-Technologien)* an der ASW gGmbH (Dozent: Prof. Dr. Andreas Karrenbauer).

### Übernommene Bestandteile aus der Vorlage:
* Grundlegendes Spring-Boot-Projekt-Setup (Maven `pom.xml` mit Web-, JPA- und Thymeleaf-Abhängigkeiten).
* Entität `ShoppingListItem` (Basisattribute) und `ShoppingListItemRepository` (Spring Data JPA Interface).
* Grundgerüst des `ShoppingListController` mit Methoden zum Laden der Liste und Hinzufügen von Elementen.


# Einkaufslisten-Projekt: Anforderungen, Features & Design

## 1. User Stories

### US-01: Artikel anlegen (Erfassen)
* **Als:** Benutzer  
* **Möchte ich:** neue Artikel mit Namen und Menge über ein Formular hinzufügen können,  
* **Damit:** ich beim Einkaufen nichts vergesse.  
* **Akzeptanzkriterien:**
  - Der Artikelname ist ein Pflichtfeld und darf nicht leer sein.
  - Nach dem Hinzufügen wird die Seite neu geladen und der Artikel steht auf der Liste.
  - Das Eingabefeld wird nach dem Absenden geleert.

### US-02: Artikel als erledigt markieren (Abhaken / Durchstreichen)
* **Als:** Benutzer im Supermarkt  
* **Möchte ich:** einen eingekauften Artikel mit einem Klick als erledigt markieren können,  
* **Damit:** der Text durchgestrichen wird und ich sehe, was schon im Einkaufswagen liegt.  
* **Akzeptanzkriterien:**
  - Jeder Listeneintrag besitzt einen Button zum Abhaken.
  - Erledigte Artikel werden optisch deutlich durchgestrichen dargestellt.
  - Die Markierung bleibt auch beim Neuladen der Seite erhalten.

### US-03: Erledigten Artikel wiederherstellen (Rückgängig)
* **Als:** Benutzer, der sich verklickt hat  
* **Möchte ich:** einen durchgestrichenen Artikel wieder als offen markieren können,  
* **Damit:** versehentlich abgehakte Artikel wieder normal auf der Liste stehen.  
* **Akzeptanzkriterien:**
  - Bei durchgestrichenen Artikeln wird ein Wiederherstellen-Button angeboten.
  - Ein Klick setzt den Zustand zurück auf offen (nicht durchgestrichen).

### US-04: Endgültiges Löschen
* **Als:** Benutzer  
* **Möchte ich:** einen Artikel endgültig von der Einkaufsliste entfernen können,  
* **Damit:** die Liste nicht mit alten Einträgen überfüllt wird.  
* **Akzeptanzkriterien:**
  - Über ein Papierkorb-Symbol kann der Artikel mit einem Klick komplett aus der Datenbank gelöscht werden.

### US-05: Mobile Nutzung & Barrierefreiheit
* **Als:** Smartphone-Nutzer  
* **Möchte ich:** die Liste einhändig bedienen können und klare visuelle Rückmeldungen sehen,  
* **Damit:** ich das Smartphone im Laden problemlos mit einer Hand nutzen kann.  
* **Akzeptanzkriterien:**
  - Buttons und Eingabefelder haben eine Mindestgröße für Touch-Bedienung.
  - Das Formular bricht auf schmalen Bildschirmen sauber um.
  - Alle interaktiven Elemente besitzen einen sichtbaren Tastaturfokus (`:focus-visible`) und `aria-label`-Attribute für Screenreader.
 
### US-06: Schnelle Mengenanpassung und Statuswechsel
- **Als:** Smartphone-Nutzer im Supermarkt  
- **Möchte ich:** die Menge von Artikeln mit einem Tippen schrittweise erhöhen oder verringern können,  
- **Damit:** ich die Liste einhändig bedienen kann und keine Tipparbeit für Zahlen nötig ist.
- **Akzeptanzkriterien:**
  - Über einen Plus-Button (`+`) wird der Bedarf (`demand`) um 1 erhöht.
  - Über einen Minus-Button (`-`) wird der Bedarf (`demand`) um 1 verringert.
  - Fällt die Menge durch das Dekrementieren auf 0, wird der Artikel automatisch als erledigt markiert (visuell durchgestrichen).
  - Wird bei einem bereits erledigten (durchgestrichenen) Artikel das Minus (`-`) betätigt, wird er endgültig aus der Liste gelöscht. (Hat sich als verwirrend herausgestellt und wird stattdessen ausgeblendet; 18.10.26)
  - Wird bei einem bereits erledigten Artikel das Plus (`+`) betätigt, wird er wiederhergestellt (Status offen, Menge 1). (Hat sich als verwirrend herausgestellt und wird stattdessen ausgeblendet; 18.10.26)

## 2. Features & Priorisierung (MoSCoW)

### Must-Have (Kernfunktionen)
* **[F-01] Übersicht der Einkaufsliste:** Anzeige aller gespeicherten Artikel mit Name und Menge.
* **[F-02] Artikel anlegen:** Formular zur Eingabe neuer Artikel inklusive Validierung (keine leeren Namen).
* **[F-03] Artikel abhaken (Durchstreichen) & Wiederherstellen:** Ändern des Bearbeitungsstatus per Mausklick/Tap.
* **[F-04] Endgültiges Löschen:** Entfernen von Artikeln aus der Datenbank.


### Should-Have (Gute Usability & Stabilität)
* **[F-05] Mobile-First CSS:** Responsives Layout basierend auf Flexbox.
* **[F-06] Barrierefreie Bedienelemente:** Kontrastreicher Tastaturfokus und Screenreader-Labels für Symbol-Buttons.
* **[F-07] Schnellanpassung der Artikelmenge:** Touch-optimierte Stepper-Buttons (`+` / `-`) je Listeneintrag mit automatischer Status-Koppelung (Erledigen bei 0, Reaktivieren/Löschen bei erledigten Artikeln).

### Could-Have (Optionale Erweiterung)
* **[F-08] Schneller Filter:** Umschalter in der Ansicht, um erledigte/durchgestrichene Artikel bei Bedarf temporär auszublenden.
* **[F-09] Automatische Sortierung: Erledigte Artikel werden ans Ende der Liste einsortiert. (Neu 18.10.26)

## 3. Anfängliche Design- und Architektur-Entscheidungen

### 3.1 Architektur: Server-Side Rendering (SSR) mit Spring Boot & Thymeleaf
* **Entscheidung:** Wir nutzen Spring Web MVC mit der Template-Engine Thymeleaf.
* **Begründung:** 
  - Die gesamte HTML-Seite wird serverseitig gerendert. Das reduziert die Komplexität im Frontend drastisch, da kein asynchrones JavaScript (Fetch API) und keine manuelle DOM-Manipulation nötig sind.
  - Nach Änderungen (Hinzufügen, Abhaken, Löschen) wird das bewährte Post-Redirect-Get-Muster (PRG) verwendet, um doppelte Formularabsendungen beim Neuladen zu verhindern.

### 3.2 Datenhaltung & Persistenz: Spring Data JPA mit H2-Datenbank
* **Entscheidung:** Die Einkaufsliste wird in einer lokalen H2-In-Memory-Datenbank über Spring Data JPA verwaltet.
* **Begründung:** 
  - Die Entität `ShoppingListItem` wird über einfache Annotationen (`@Entity`, `@Id`) gemappt.
  - Das `CrudRepository`-Interface von Spring Data stellt alle Datenbankoperationen (`save`, `deleteById`, `findAll`) out-of-the-box bereit, ohne dass eigenes SQL geschrieben werden muss.
  - Die Daten liegen zentral im Backend und sind für alle anfragenden Clients einheitlich verfügbar (anders als bei isoliertem Browser-Speicher).

### 3.3 Validierung: Serverseitige Bean Validation
* **Entscheidung:** Eingaben werden im Backend über Bean Validation (`@NotBlank`, `@Min`) in Kombination mit `BindingResult` geprüft.
* **Begründung:** 
  - Reine HTML5-Browserprüfungen (`required`) können umgangen werden. Eine serverseitige Validierung stellt die Datenintegrität sicher und liefert verständliche Fehlermeldungen direkt im Formular zurück.

### 3.4 Frontend-Gestaltung: Semantisches HTML5 und schlankes Flexbox-CSS
* **Entscheidung:** Verzicht auf schwere CSS-Frameworks; stattdessen eigenes, semantisches CSS.
* **Begründung:** 
  - **Semantik & Barrierefreiheit:** Die Verwendung von Standard-Tags (`<main>`, `<form>`, `<label>`, `<button>`) sorgt dafür, dass Screenreader und Tastaturnutzer die Struktur natürlich erfassen können.
  - **Flexbox:** Ermöglicht ein sauberes, elastisches Layout für Formulare und Listenzeilen, das sich nahtlos an Smartphone-Bildschirme anpasst.

## 4. Iterative Design- und Architektur-Entscheidungen im Laufe des Entwicklungsprozesses

### 4.1 Zustandsabhängige Aktionssteuerung und Trennung von Status und Menge
* **Problem:** Beim Abhaken eines Artikels darf die Bedarfsmenge (`demand`) nicht auf 0 überschrieben werden, da sonst beim Wiederherstellen die ursprüngliche Anzahl verloren ginge. Gleichzeitig führten zu viele sichtbare Aktionsbuttons in der mobilen Tabellenansicht zu Fehlklicks.
* **Entscheidung:**
  - Der Erledigt-Status wird über ein eigenes Flag (`boolean completed`) geführt, wodurch `demand` unverändert konserviert wird.
  - Die Benutzeroberfläche schaltet die Formulare über `th:if` und `th:unless` zustandsabhängig um: Bei offenen Artikeln werden ausschließlich der Abhaken-Button (`✔`) und die Stepper (`-` / `+`) eingeblendet. Bei erledigten Artikeln werden die Mengen-Buttons verborgen und stattdessen die Aktionen für Löschen (`🗑`) und Wiederherstellen (`↺`) angeboten.
  - Die visuelle Markierung erfolgt rein deklarativ über `th:classappend="${item.completed} ? 'done' : ''"`, wodurch der Status im Stylesheet (`.done { text-decoration: line-through; opacity: 0.7; }`) formatiert wird, ohne HTML-Struktur oder Datenmodell zu verändern.

### 4.2 Defensive Mengenbegrenzung und Klick-Schutz (Obergrenze 9999)
* **Problem:** Bei schrittweiser Erhöhung über den Stepper-Button (`+`) können unbegrenzte Klicks zu Integer-Überläufen in der Datenbank führen oder lange Zahlenkolonnen das Tabellenraster zerstören. Eine reine Fehlermeldung bei jedem Klick würde jedoch den Bedienfluss stören.
* **Entscheidung:**
  - Auf Persistenzebene wird der Höchstwert über `@Max(value = 9999, message = "Soviel braucht doch kein Mensch!")` in der Entität `ShoppingListItem` abgesichert.
  - Das Hinzufügen-Formular schränkt den Wertebereich im Browser über `min="1"` und `max="9999"` ein.
  - Der Erhöhen-Button in der Tabelle wird bei Erreichen des Höchstwerts über `th:disabled="${item.demand == 9999}"` automatisch deaktiviert. Dies verhindert ungültige Requests direkt in der UI ohne störende Fehlerseiten.

### 4.3 Tabellen-Layout und kontrollierter Textumbruch für lange Artikelnamen
* **Problem:** Sehr lange, zusammenhängende Eingaben ohne Leerzeichen drücken die Tabellenspalten auseinander und zerstören die mobile Lesbarkeit.
* **Entscheidung:**
  - Der Artikelname wird in der Entität via `@Size(max = 50)` und im Eingabefeld via `maxlength="50"` begrenzt.
  - Die Artikelspalte wird in CSS über `td:nth-child(2) { max-width: 25ch; overflow-wrap: break-word; }` formatiert. Dies erzwingt bei Erreichen von ca. 25 Zeichenbreiten einen Zeilenumbruch selbst bei wortweisen Zeichenketten ohne Trennstellen.
  - Aktions-Buttons in Tabellenzellen erhalten mit `td button { font-size: 2rem; }` und `form {	min-width: 4rem; }` gut erkennbare Beschriftung und eine minimale Größe für eine einhändige Bedienung auf Smartphones.

### 4.4 Gezielte Responsive-Entkopplung und Tastatur-Fokussierung
* **Problem:** Eine pauschale Media-Query auf das `form`-Tag würde auch die winzigen Formulare der Tabellen-Aktionsbuttons in eine Zeile zwingen und deren Ausrichtung beschädigen.
* **Entscheidung:**
  - Das Erfassungsformular wird im HTML über `class="add"` semantisch separiert.
  - Die Media Query `@media (min-width: 35rem)` adressiert ausschließlich `form.add`, um das Formular ab 35 rem Breite von `column` auf `row` umzuschalten, während die Tabellenformulare unverändert bleiben.
  - Der globale Box-Modell-Reset (`*, *::before, *::after { box-sizing: border-box; }`) stellt sicher, dass Innenabstände (`padding`) und Rahmen (`border`) nicht zur definierten Elementbreite addiert werden.
  - Alle interaktiven Elemente erhalten über `a:focus-visible, button:focus-visible, input:focus-visible` eine kontrastreiche Umrandung (`outline: 3px solid #174b7a; outline-offset: 2px;`), um die Barrierefreiheit für Tastaturnutzer ohne störende Maus-Klickränder sicherzustellen.
