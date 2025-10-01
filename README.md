Cloth – Backend & Android (kortguide)
Backend (Spring Boot)

Krav: Java 17+, inget DB-install behövs (H2 används i dev).

Starta:

# Gradle
./gradlew bootRun
# eller Maven
./mvnw spring-boot:run


Testa att den lever (från DATORN):
http://localhost:8080/api/health → ska visa OK.

API (just nu):

GET /api/health → OK

GET /api/items?userId=demo → hämta kläd-state

POST /api/items/move → flytta plagg
Body-exempel:

{"userId":"demo","itemId":"hat","location":"PRESCHOOL"}


Databas (dev): H2 in-memory. Rensas vid omstart.

Android-appen

Bas-URL för emulatorn:
I app/src/main/res/values/strings.xml:

<string name="backend_base_url">http://10.0.2.2:8080/</string>


10.0.2.2 = din dator sett från emulatorn.

Kör appen: Starta backend först, kör appen i Android Studio (emulator).
Snabbtest i emulatorns Chrome: http://10.0.2.2:8080/api/health → OK.

Felsökning (kort)

Timeout från appen/emulatorn?
– Backend måste vara igång.
– Använd 10.0.2.2 (inte localhost).

Postman på datorn? Använd http://localhost:8080/ (inte 10.0.2.2).

Kan ändå inte nå? Tillåt port 8080 i Windows-brandväggen.

Flöde i ett nötskal

Appen (Retrofit) → HTTP till backend → backend sparar/läser i DB → svarar JSON.