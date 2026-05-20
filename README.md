# Phone Contact

<p align="center">
  Modern bir Android kişi rehberi uygulaması — kişileri listele, ara, ekle, düzenle ve cihaz rehberine kaydet.
</p>

---

## 📱 Ekran Görüntüleri

| Kişi Listesi | Arama & Geçmiş | Yeni Kişi |
|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/0128b085-4075-4c2d-a9f7-c21495460387" width="250" alt="Kişi Listesi"/> | <img src="https://github.com/user-attachments/assets/8f0efa88-49e0-492a-bdb6-b02129820211" width="250" alt="Arama ve Geçmiş"/> | <img src="https://github.com/user-attachments/assets/ff4d8703-76c0-4254-9164-65bb06bd79c4" width="250" alt="Yeni Kişi"/> |

| Profil Detay | Düzenleme |
|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/500f3eb4-75f8-4424-be64-c2251f72d2c5" width="250" alt="Profil Detay"/> | <img src="https://github.com/user-attachments/assets/848daaaf-4fd1-4215-9183-54f90ec68c04" width="250" alt="Kişi Düzenleme"/> |

---

## ✨ Özellikler

- **Kişi listesi** — Alfabetik gruplama ile tüm kişileri görüntüleme
- **Arama** — İsme göre anlık arama (minimum 2 karakter)
- **Arama geçmişi** — Son aramaları kaydetme, tek tek veya toplu temizleme
- **Kişi ekleme / düzenleme** — Ad, soyad, telefon ve profil fotoğrafı
- **Profil fotoğrafı** — Kamera veya galeriden seçim, otomatik sıkıştırma ve API'ye yükleme
- **Profil detay** — Kişi bilgilerini görüntüleme, düzenleme ve silme
- **Kaydırarak silme** — Swipe ile hızlı kişi silme
- **Cihaz rehberine kaydet** — Kişiyi telefon rehberine ekleme
- **Çevrimdışı destek** — Room ile yerel veritabanı, API ile senkronizasyon
- **Boş durum ekranları** — Kişi yok / sonuç bulunamadı durumları için kullanıcı dostu UI

---

## 🏗️ Mimari

Uygulama **Clean Architecture** prensiplerine göre katmanlı yapıda geliştirilmiştir:

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
### Kullanılan desenler

| Desen | Açıklama |
|-------|----------|
| **MVVM** | UI state yönetimi ViewModel + StateFlow ile |
| **Repository Pattern** | Yerel (Room) ve uzak (Retrofit) veri kaynaklarını soyutlama |
| **Use Case** | Her iş kuralı ayrı use case sınıfında |
| **Dependency Injection** | Hilt ile modüler bağımlılık yönetimi |
| **Unidirectional Data Flow** | Event → ViewModel → State → UI akışı |

---

## 📂 Proje Yapısı

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
