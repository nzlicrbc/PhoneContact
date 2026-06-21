# Phone Contact

<p align="center">
  A modern Android contacts app — list, search, add, edit contacts, and save them to the device's address book.
</p>

---

## 📱 Screenshots

| Contact List | Search & History | New Contact |
|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/0128b085-4075-4c2d-a9f7-c21495460387" width="250" alt="Contact List"/> | <img src="https://github.com/user-attachments/assets/8f0efa88-49e0-492a-bdb6-b02129820211" width="250" alt="Search and History"/> | <img src="https://github.com/user-attachments/assets/ff4d8703-76c0-4254-9164-65bb06bd79c4" width="250" alt="New Contact"/> |

| Profile Detail | Editing |
|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/500f3eb4-75f8-4424-be64-c2251f72d2c5" width="250" alt="Profile Detail"/> | <img src="https://github.com/user-attachments/assets/848daaaf-4fd1-4215-9183-54f90ec68c04" width="250" alt="Edit Contact"/> |

---

## ✨ Features

- **Contact list** — View all contacts grouped alphabetically
- **Search** — Instant search by name (minimum 2 characters)
- **Search history** — Save recent searches; clear individually or all at once
- **Add / edit contact** — First name, last name, phone number, and profile photo
- **Profile photo** — Pick from camera or gallery, with automatic compression and upload to the API
- **Profile detail** — View, edit, and delete contact information
- **Swipe to delete** — Quickly remove a contact with a swipe gesture
- **Save to device contacts** — Add the contact to the phone's native address book
- **Offline support** — Local database with Room, synced with the API
- **Empty states** — User-friendly UI for "no contacts" / "no results found" scenarios

---

## 🏗️ Architecture

The app follows **Clean Architecture** principles in a layered structure:

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│  (Compose UI · ViewModel · Navigation)  │
├─────────────────────────────────────────┤
│             Domain Layer                │
│     (Use Cases · Repository Interface)  │
├─────────────────────────────────────────┤
│              Data Layer                 │
│  (Room · Retrofit · Repository Impl)    │
└─────────────────────────────────────────┘
```

### Patterns used

| Pattern | Description |
|---------|-------------|
| **MVVM** | UI state managed via ViewModel + StateFlow |
| **Repository Pattern** | Abstracts local (Room) and remote (Retrofit) data sources |
| **Use Case** | Each business rule lives in its own use case class |
| **Dependency Injection** | Modular dependency management with Hilt |
| **Unidirectional Data Flow** | Event → ViewModel → State → UI flow |

---

## 📂 Project Structure

```
app/src/main/java/com/example/phonecontact/
├── data/
│   ├── datasource/
│   │   ├── local/
│   │   └── remote/
│   ├── local/
│   │   ├── dao/
│   │   ├── database/
│   │   └── entity/
│   ├── remote/
│   │   ├── api/
│   │   └── dto/
│   ├── mapper/
│   └── repository/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
├── presentation/
│   ├── contacts/
│   ├── addcontact/
│   ├── profile/
│   ├── components/
│   └── navigation/
├── di/
├── ui/theme/
└── utils/
```
