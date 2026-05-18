# I-Wish — Client

The desktop frontend of **I-Wish**, a networked wish list application. Built with Java 11 and JavaFX, it provides a graphical interface for users to create and manage their wish lists, connecting to the I-Wish server over TCP sockets and communicating via JSON messages.

> 🎥 [Demo Video](https://drive.google.com/file/d/1UVbPUfKGFjUPwXu1YrR6LaXkL6Ery_HV/view) &nbsp;|&nbsp; 📋 [Trello Board](https://trello.com/b/ebfz0hDL/i-wish-project-%F0%9F%A5%B3) &nbsp;|&nbsp; 🖥️ [Server Repository](https://github.com/Amira-Mostafa/I-Wish-Server)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 11 |
| UI Framework | JavaFX 13 (FXML + CSS) |
| Networking | Java Sockets (`java.net`) |
| Message Format | JSON (`org.json`) |
| Build Tool | Maven |

---

## Project Structure

```
I-Wish-Client/
├── src/main/
│   ├── java/com/example/
│   │   ├── client/          # App entry point & server connection
│   │   ├── controller/      # JavaFX controllers (one per screen)
│   │   └── model/           # Data model classes (Wish, User, etc.)
│   └── resources/
│       ├── fxml/            # FXML layout files
│       └── css/             # Stylesheets
└── pom.xml
```

---

## Getting Started

### Prerequisites

- Java 11+
- Maven
- The [I-Wish Server](https://github.com/Amira-Mostafa/I-Wish-Server) running and reachable

### Configure the Server Address

Update the server host and port in the client connection class:

```java
String host = "localhost"; // or server IP
int port = 5000;           // must match server config
```

### Run the Client

```bash
mvn clean javafx:run
```

Make sure the server is already running before launching the client.

---

## How It Works

1. On startup, the client opens a TCP socket connection to the server.
2. User actions in the JavaFX UI (creating a wish, viewing lists, etc.) are serialized into JSON and sent to the server.
3. The server processes the request and sends back a JSON response.
4. The client deserializes the response and updates the UI accordingly.

All application state lives on the server — the client is purely a presentation and interaction layer.

---

## Related

- **Server:** [Amira-Mostafa/I-Wish-Server](https://github.com/Amira-Mostafa/I-Wish-Server)
- **Demo:** [Watch on Google Drive](https://drive.google.com/file/d/1UVbPUfKGFjUPwXu1YrR6LaXkL6Ery_HV/view)
- **Project Board:** [Trello](https://trello.com/b/ebfz0hDL/i-wish-project-%F0%9F%A5%B3)
