# 📚 Bookish2025

**Bookish2025** is an Android application built with **Jetpack Compose** that allows users to search for books using the **Google Books API**, view book details, and save selected books to a local **SQLite database** via **Room** and a custom **ContentProvider**.

---

## ✨ Features

- 🔍 **Search Books Online**: Search by title using Google Books API.  
- 📥 **Save Books Locally**: Store selected books in a local SQLite database.  
- 🧾 **View Book Details**: View title, authors, publisher, description, categories, and thumbnail image.  
- 🗃️ **Content Provider Integration**: Enables accessing book data through a `ContentProvider`.  
- 📂 **Filter Local Data**: If the search field is empty, local data is displayed instead of fetching online.  
- 🗑️ **Remove Books**: Delete saved books directly from the local database.  
- 🎨 **Modern UI**: Built with Jetpack Compose and Material3 design system.  

---

## 🏗️ Architecture & Technologies

- **Jetpack Compose** – UI toolkit  
- **Room (SQLite)** – Local persistence  
- **Google Books API** – RESTful book search  
- **ContentProvider** – For accessing local data from external apps or components  
- **Retrofit** – HTTP client for network operations  
- **Coil** – Image loading library  
- **MVVM-inspired architecture** – Lightweight, repository pattern  

---

## 🔧 How It Works

- On app start:
  - If search input is empty → local books are loaded via ContentProvider.  
  - If input is present → results are fetched via Google Books API and displayed.  
- Books can be saved locally by tapping on them.  
- On tapping a book, details are shown with an option to delete it from local storage.  
- Local data is stored using Room and exposed via ContentProvider for future extensibility.  

---

## 📌 Prerequisites

- Android Studio (Flamingo or newer)  
- Internet connection (for Google Books API)  
- Minimum SDK: 24 (Android 7.0)  

---

## 🖼️ Screenshots


<img width="376" height="826" alt="Screenshot 1" src="https://github.com/user-attachments/assets/b0ff556e-5d4f-48bc-8153-06b0ceef42a4" />
<br/>
<img width="367" height="807" alt="Screenshot 2" src="https://github.com/user-attachments/assets/63afad1e-55b4-40c5-9f76-cf727a64e4d3" />
<br/>
<img width="370" height="828" alt="Screenshot 3" src="https://github.com/user-attachments/assets/3901275d-3a2e-410e-b77e-8c4b80544cf2" />


