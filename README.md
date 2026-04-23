# EbookMurah Store - Portal Bisnis Digital

Portal bisnis digital lengkap untuk penjualan ebook dengan sistem pembayaran otomatis, program transformasi 90 hari, dan dashboard pengguna.

## 🚀 Fitur Utama

- **Katalog Ebook**: Halaman publik untuk menampilkan ebook dengan narasi 5 fase transformasi
- **Sistem Pembayaran Midtrans**: Integrasi QRIS, Virtual Account, dan E-Wallet
- **Otomasi Pembayaran**: Pembuatan akun otomatis dan pengiriman email setelah pembayaran sukses
- **Program 90 Hari**: Sistem timeboxing dengan progress tracking dan motivasi harian
- **Dashboard Pengguna**: Area privat untuk mengelola progress dan akses ebook
- **Skill Synthesizer**: Fitur analisis potensi diri (coming soon)
- **Keamanan Profesional**: Spring Security dengan enkripsi password BCrypt
- **UI/UX Modern**: Dark mode dengan aksen Gold menggunakan Tailwind CSS

## 📋 Prasyarat

- Java 17 atau更高
- Maven 3.6+
- MySQL 8.0+ atau PostgreSQL 12+ (atau H2 untuk development)
- Akun Midtrans (Server Key & Client Key)
- Akun Email (Gmail atau SMTP server lain)

## 🔧 Instalasi

### 1. Clone Repository

```bash
git clone https://github.com/username/ebookmurah-store.git
cd ebookmurah-store
```

### 2. Konfigurasi Environment Variables

Buat file `.env` atau set environment variables berikut:

```bash
# Database Configuration
DB_DRIVER=com.mysql.cj.jdbc.Driver
DB_URL=jdbc:mysql://localhost:3306/ebookmurah?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Jakarta
DB_USERNAME=root
DB_PASSWORD=your_database_password
DB_DIALECT=org.hibernate.dialect.MySQLDialect

# Untuk PostgreSQL, gunakan:
# DB_DRIVER=org.postgresql.Driver
# DB_URL=jdbc:postgresql://localhost:5432/ebookmurah
# DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# Mail Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# Midtrans Configuration
MIDTRANS_SERVER_KEY=SB-Mid-server-xxxxx
MIDTRANS_CLIENT_KEY=SB-Mid-client-xxxxx
MIDTRANS_API_URL=https://app.sandbox.midtrans.com/snap/v1

# Untuk production, ganti dengan production URL:
# MIDTRANS_API_URL=https://app.midtrans.com/snap/v1

# Callback URLs (sesuaikan dengan domain Anda)
MIDTRANS_NOTIFICATION_URL=https://ebookmurah.store/api/payment/notification
MIDTRANS_FINISH_URL=https://ebookmurah.store/payment/success
MIDTRANS_UNFINISH_URL=https://ebookmurah.store/payment/unfinish
MIDTRANS_ERROR_URL=https://ebookmurah.store/payment/error

# Server Configuration
SERVER_PORT=8080

# JWT Secret (opsional, untuk token generation)
JWT_SECRET=your-secret-key-change-this-in-production
JWT_EXPIRATION=86400000
```

### 3. Build Project

```bash
mvn clean install
```

### 4. Jalankan Aplikasi

```bash
mvn spring-boot:run
```

Atau jalankan JAR file:

```bash
java -jar target/ebookmurah-store-1.0.0.jar
```

### 5. Akses Aplikasi

- **Public**: http://localhost:8080
- **Login**: http://localhost:8080/login
- **Dashboard**: http://localhost:8080/app/dashboard (memerlukan login)

## 📁 Struktur Project

```
ebookmurah-store/
├── src/
│   ├── main/
│   │   ├── java/com/ebookmurah/
│   │   │   ├── config/              # Konfigurasi Spring (Security, Midtrans)
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── entity/              # JPA Entities
│   │   │   ├── repository/          # JPA Repositories
│   │   │   ├── service/             # Business Logic
│   │   │   └── EbookMurahApplication.java
│   │   └── resources/
│   │       ├── templates/           # Thymeleaf Templates
│   │       │   ├── app/             # Private pages
│   │       │   ├── email/           # Email templates
│   │       │   ├── payment/         # Payment pages
│   │       │   ├── layout.html
│   │       │   ├── index.html
│   │       │   ├── login.html
│   │       │   └── register.html
│   │       └── application.properties
├── pom.xml
├── .gitignore
└── README.md
```

## 🗄️ Database

Aplikasi menggunakan JPA Hibernate dengan `ddl-auto=update`, sehingga tabel akan dibuat otomatis saat aplikasi dijalankan pertama kali.

### Tabel Database:

- **users**: Data pengguna
- **ebooks**: Data ebook
- **transactions**: Data transaksi pembayaran
- **timeboxing_progress**: Progress program 90 hari

### Data Awal (Seed Data)

Untuk menambahkan ebook pertama, Anda bisa menggunakan SQL script atau menambahkan data melalui database client:

```sql
INSERT INTO ebooks (slug, title, description, price, phase1_narrative, phase2_narrative, phase3_narrative, phase4_narrative, phase5_narrative, active, created_at, updated_at)
VALUES (
    'transformasi-diri',
    'Transformasi Diri: Menjadi Orang yang Dicari Dunia',
    'Panduan lengkap untuk transformasi diri melalui 5 fase: Menggali Luka, Instal Mindset, Strategi Potensi, Titik Balik, dan Transformasi.',
    99000,
    'Fase 1: Menggali Luka - Temukan dan akhiri luka masa lalu yang menghambat potensi Anda.',
    'Fase 2: Instal Mindset - Bangun mindset growth yang kuat dan positif.',
    'Fase 3: Strategi Potensi - Identifikasi dan kembangkan potensi unik Anda.',
    'Fase 4: Titik Balik - Ubah keputusan hidup Anda menuju arah yang benar.',
    'Fase 5: Transformasi - Menjadi orang yang dicari dan dihargai dunia.',
    true,
    NOW(),
    NOW()
);
```

## 🔐 Keamanan

- **Password Encryption**: Menggunakan BCrypt untuk enkripsi password
- **Spring Security**: Konfigurasi keamanan dengan role-based access control
- **Environment Variables**: Semua sensitive data disimpan di environment variables
- **CSRF Protection**: Enabled untuk form submissions
- **Session Management**: Secure session configuration

## 💳 Integrasi Midtrans

### Setup Midtrans

1. Daftar akun di [Midtrans](https://midtrans.com)
2. Dapatkan Server Key dan Client Key dari dashboard
3. Set environment variables `MIDTRANS_SERVER_KEY` dan `MIDTRANS_CLIENT_KEY`
4. Untuk development, gunakan Sandbox environment
5. Untuk production, ganti API URL ke production endpoint

### Flow Pembayaran

1. User memilih ebook dan metode pembayaran
2. Sistem membuat transaksi di Midtrans
3. User diarahkan ke halaman pembayaran Midtrans
4. Setelah pembayaran sukses, Midtrans mengirim notification webhook
5. Sistem memproses notification dan mengirim email ke user
6. User diarahkan ke halaman success

## 📧 Konfigurasi Email

### Gmail Setup

1. Aktifkan 2-Factor Authentication di akun Gmail
2. Buat App Password di Google Account Settings
3. Gunakan App Password sebagai `MAIL_PASSWORD`
4. Set `MAIL_HOST=smtp.gmail.com` dan `MAIL_PORT=587`

### SMTP Server Lain

Sesuaikan konfigurasi sesuai dengan provider email Anda.

## 🚀 Deployment

### Railway

1. Push code ke GitHub
2. Buat project baru di Railway
3. Connect repository GitHub
4. Set environment variables di Railway dashboard
5. Deploy otomatis akan berjalan

### VPS (Ubuntu/Debian)

```bash
# Install Java 17
sudo apt update
sudo apt install openjdk-17-jdk

# Install MySQL
sudo apt install mysql-server

# Setup database
mysql -u root -p
CREATE DATABASE ebookmurah;
CREATE USER 'ebookmurah'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON ebookmurah.* TO 'ebookmurah'@'localhost';
FLUSH PRIVILEGES;
EXIT;

# Build dan run
mvn clean package
nohup java -jar target/ebookmurah-store-1.0.0.jar > app.log 2>&1 &
```

### Docker (Opsional)

Buat `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/ebookmurah-store-1.0.0.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

Build dan run:

```bash
docker build -t ebookmurah-store .
docker run -p 8080:8080 --env-file .env ebookmurah-store
```

## 🧪 Testing

```bash
# Run semua tests
mvn test

# Run specific test
mvn test -Dtest=UserServiceTest
```

## 📝 Environment Variables Reference

| Variable | Deskripsi | Default |
|----------|-----------|---------|
| DB_DRIVER | Database driver | com.mysql.cj.jdbc.Driver |
| DB_URL | Database connection URL | jdbc:mysql://localhost:3306/ebookmurah |
| DB_USERNAME | Database username | root |
| DB_PASSWORD | Database password | - |
| MAIL_HOST | SMTP host | smtp.gmail.com |
| MAIL_PORT | SMTP port | 587 |
| MAIL_USERNAME | Email username | - |
| MAIL_PASSWORD | Email password | - |
| MIDTRANS_SERVER_KEY | Midtrans Server Key | - |
| MIDTRANS_CLIENT_KEY | Midtrans Client Key | - |
| MIDTRANS_API_URL | Midtrans API URL | https://app.sandbox.midtrans.com/snap/v1 |
| SERVER_PORT | Server port | 8080 |

## 🤝 Kontribusi

Kontribusi sangat diterima! Silakan:

1. Fork repository
2. Buat branch fitur (`git checkout -b feature/AmazingFeature`)
3. Commit perubahan (`git commit -m 'Add some AmazingFeature'`)
4. Push ke branch (`git push origin feature/AmazingFeature`)
5. Buka Pull Request

## 📄 Lisensi

Proyek ini dilisensikan under MIT License.

## 👥 Kontak

- **Website**: https://ebookmurah.store
- **Email**: support@ebookmurah.store

## 🙏 Acknowledgments

- Spring Boot Team
- Midtrans Payment Gateway
- Tailwind CSS
- Thymeleaf Team

---

Dibuat dengan ❤️ oleh EbookMurah Store Team
