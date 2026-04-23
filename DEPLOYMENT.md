# Panduan Deployment EbookMurah Store

Panduan lengkap untuk deploy aplikasi EbookMurah Store agar dapat diakses public.

## 📋 Prasyarat Sebelum Deployment

1. **Akun GitHub** - Untuk hosting code
2. **Akun Railway** (Rekomendasi) atau VPS - Untuk hosting aplikasi
3. **Domain** - ebookmurah.store (opsional, bisa pakai subdomain Railway)
4. **Database** - Railway menyediakan PostgreSQL gratis
5. **Akun Midtrans Production** - Untuk pembayaran live
6. **Akun Email** - Untuk pengiriman email otomatis

---

## 🚀 Cara 1: Deployment ke Railway (Paling Mudah & Gratis)

### Langkah 1: Push Code ke GitHub

```bash
cd c:/Users/USER/Documents/ebookMurah
git init
git add .
git commit -m "Initial commit: EbookMurah Store complete project"
git branch -M main
```

Buat repository baru di GitHub (https://github.com/new), kemudian:

```bash
git remote add origin https://github.com/USERNAME/ebookmurah-store.git
git push -u origin main
```

### Langkah 2: Buat Project di Railway

1. Buka https://railway.app
2. Login dengan GitHub
3. Klik **"New Project"** → **"Deploy from GitHub repo"**
4. Pilih repository `ebookmurah-store`
5. Railway akan otomatis mendeteksi Spring Boot project

### Langkah 3: Konfigurasi Environment Variables di Railway

Di dashboard Railway project, klik tab **"Variables"** dan tambahkan:

**Database (Railway menyediakan PostgreSQL otomatis):**
```
DB_DRIVER=org.postgresql.Driver
DB_URL=${RAILWAY_POSTGRES_URL}
DB_USERNAME=${RAILWAY_POSTGRES_USER}
DB_PASSWORD=${RAILWAY_POSTGRES_PASSWORD}
DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect
```

**Email:**
```
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

**Midtrans Production:**
```
MIDTRANS_SERVER_KEY=Mid-server-xxxxx (Production Key)
MIDTRANS_CLIENT_KEY=Mid-client-xxxxx (Production Key)
MIDTRANS_API_URL=https://app.midtrans.com/snap/v1
MIDTRANS_NOTIFICATION_URL=https://ebookmurah-store-production.up.railway.app/api/payment/notification
MIDTRANS_FINISH_URL=https://ebookmurah-store-production.up.railway.app/payment/success
MIDTRANS_UNFINISH_URL=https://ebookmurah-store-production.up.railway.app/payment/unfinish
MIDTRANS_ERROR_URL=https://ebookmurah-store-production.up.railway.app/payment/error
```

**Server:**
```
SERVER_PORT=8080
```

### Langkah 4: Deploy

1. Klik **"Deploy"** di Railway
2. Tunggu proses build (sekitar 2-5 menit)
3. Setelah selesai, Railway akan memberikan URL seperti:
   - `https://ebookmurah-store-production.up.railway.app`

### Langkah 5: Setup Custom Domain (Opsional)

1. Di Railway project, klik **"Settings"** → **"Domains"**
2. Klik **"Add Domain"**
3. Masukkan domain: `ebookmurah.store`
4. Railway akan memberikan CNAME record untuk ditambahkan di DNS provider

**DNS Configuration di Cloudflare/Namecheap/dll:**
```
Type: CNAME
Name: ebookmurah (atau @ untuk root domain)
Value: ebookmurah-store-production.up.railway.app
TTL: 3600
```

---

## 🖥️ Cara 2: Deployment ke VPS (Ubuntu/Debian)

### Langkah 1: Siapkan VPS

Beli VPS dari provider seperti:
- DigitalOcean ($5/bulan)
- Linode ($5/bulan)
- Vultr ($5/bulan)
- AWS EC2 (Free tier tersedia)

Pilih OS: **Ubuntu 22.04 LTS** atau **Debian 12**

### Langkah 2: Connect ke VPS

```bash
ssh root@your_vps_ip
```

### Langkah 3: Install Java 17

```bash
sudo apt update
sudo apt install openjdk-17-jdk -y
java -version
```

### Langkah 4: Install MySQL Server

```bash
sudo apt install mysql-server -y
sudo mysql_secure_installation
```

Buat database dan user:

```bash
sudo mysql -u root -p
```

```sql
CREATE DATABASE ebookmurah;
CREATE USER 'ebookmurah'@'localhost' IDENTIFIED BY 'StrongPassword123!';
GRANT ALL PRIVILEGES ON ebookmurah.* TO 'ebookmurah'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Langkah 5: Clone Repository

```bash
cd /var/www
git clone https://github.com/USERNAME/ebookmurah-store.git
cd ebookmurah-store
```

### Langkah 6: Build Project

```bash
sudo apt install maven -y
mvn clean package -DskipTests
```

### Langkah 7: Setup Environment Variables

Buat file `.env`:

```bash
nano .env
```

Isi dengan:

```bash
DB_DRIVER=com.mysql.cj.jdbc.Driver
DB_URL=jdbc:mysql://localhost:3306/ebookmurah?useSSL=false&serverTimezone=Asia/Jakarta
DB_USERNAME=ebookmurah
DB_PASSWORD=StrongPassword123!
DB_DIALECT=org.hibernate.dialect.MySQLDialect

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

MIDTRANS_SERVER_KEY=Mid-server-xxxxx
MIDTRANS_CLIENT_KEY=Mid-client-xxxxx
MIDTRANS_API_URL=https://app.midtrans.com/snap/v1
MIDTRANS_NOTIFICATION_URL=https://ebookmurah.store/api/payment/notification
MIDTRANS_FINISH_URL=https://ebookmurah.store/payment/success
MIDTRANS_UNFINISH_URL=https://ebookmurah.store/payment/unfinish
MIDTRANS_ERROR_URL=https://ebookmurah.store/payment/error

SERVER_PORT=8080
```

Simpan dengan `Ctrl+X`, `Y`, `Enter`

### Langkah 8: Jalankan Aplikasi dengan Systemd

Buat service file:

```bash
sudo nano /etc/systemd/system/ebookmurah.service
```

Isi dengan:

```ini
[Unit]
Description=EbookMurah Store Application
After=syslog.target network.target

[Service]
Type=simple
User=root
WorkingDirectory=/var/www/ebookmurah-store
EnvironmentFile=/var/www/ebookmurah-store/.env
ExecStart=/usr/bin/java -jar target/ebookmurah-store-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Simpan dan jalankan:

```bash
sudo systemctl daemon-reload
sudo systemctl start ebookmurah
sudo systemctl enable ebookmurah
sudo systemctl status ebookmurah
```

### Langkah 9: Setup Nginx Reverse Proxy

Install Nginx:

```bash
sudo apt install nginx -y
```

Konfigurasi Nginx:

```bash
sudo nano /etc/nginx/sites-available/ebookmurah
```

Isi dengan:

```nginx
server {
    listen 80;
    server_name ebookmurah.store www.ebookmurah.store;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Enable site:

```bash
sudo ln -s /etc/nginx/sites-available/ebookmurah /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### Langkah 10: Setup SSL dengan Let's Encrypt

```bash
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d ebookmurah.store -d www.ebookmurah.store
```

Ikuti instruksi untuk setup SSL gratis.

### Langkah 11: Setup Firewall

```bash
sudo ufw allow 22
sudo ufw allow 80
sudo ufw allow 443
sudo ufw enable
```

---

## 🔧 Cara 3: Deployment ke Docker

### Langkah 1: Buat Dockerfile

File ini sudah ada di project, atau buat baru:

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/ebookmurah-store-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Langkah 2: Build Docker Image

```bash
mvn clean package
docker build -t ebookmurah-store .
```

### Langkah 3: Run Container

```bash
docker run -d \
  -p 8080:8080 \
  --env-file .env \
  --name ebookmurah \
  ebookmurah-store
```

### Langkah 4: Deploy ke Docker Hub (Opsional)

```bash
docker tag ebookmurah-store USERNAME/ebookmurah-store:latest
docker push USERNAME/ebookmurah-store:latest
```

---

## 🌐 Cara 4: Deployment ke Render.com (Alternatif Railway)

### Langkah 1: Push ke GitHub (Sama seperti Railway)

### Langkah 2: Buat Project di Render

1. Buka https://render.com
2. Login dengan GitHub
3. Klik **"New +"** → **"Web Service"**
4. Connect repository GitHub
5. Render akan otomatis mendeteksi Spring Boot

### Langkah 3: Konfigurasi Build

- **Build Command**: `mvn clean package -DskipTests`
- **Start Command**: `java -jar target/ebookmurah-store-1.0.0.jar`

### Langkah 4: Setup Environment Variables

Sama seperti Railway, sesuaikan dengan URL Render.

### Langkah 5: Deploy

Render akan otomatis deploy setiap push ke GitHub.

---

## ✅ Verifikasi Deployment

Setelah deployment selesai:

1. **Cek URL aplikasi** - Buka domain yang sudah disetup
2. **Test halaman public** - `/` (katalog ebook)
3. **Test halaman login** - `/login`
4. **Test pembayaran** - Beli ebook dengan Midtrans Sandbox dulu
5. **Test email** - Pastikan email terkirim setelah pembayaran sukses
6. **Test dashboard** - Login dan akses `/app/dashboard`
7. **Test program 90 hari** - Akses `/app/timeboxing`

---

## 🔍 Troubleshooting

### Aplikasi tidak bisa diakses

**Railway:**
- Cek tab "Logs" di dashboard Railway
- Pastikan environment variables sudah benar
- Cek apakah build berhasil

**VPS:**
```bash
sudo systemctl status ebookmurah
sudo journalctl -u ebookmurah -f
```

### Database connection error

- Pastikan database sudah dibuat
- Cek kredensial database di environment variables
- Untuk Railway, gunakan `${RAILWAY_POSTGRES_URL}`

### Email tidak terkirim

- Pastikan App Password Gmail sudah benar
- Cek apakah 2FA sudah aktif di akun Gmail
- Cek folder spam di email penerima

### Midtrans error

- Pastikan Server Key dan Client Key benar
- Untuk testing, gunakan Sandbox environment
- Cek apakah callback URL sudah benar

### Port 8080 blocked

**VPS:**
```bash
sudo ufw allow 8080
```

Atau gunakan Nginx reverse proxy (port 80/443)

---

## 📊 Monitoring

### Railway
- Otomatis menyediakan monitoring di dashboard
- Cek tab "Metrics" untuk CPU, Memory, dan Network

### VPS
Install monitoring tools:
```bash
sudo apt install htop -y
htop
```

---

## 💰 Biaya Estimasi

**Railway:**
- Free tier: $5/bulan (limit usage)
- Pro: $20/bulan (unlimited)

**VPS:**
- DigitalOcean: $5/bulan
- Linode: $5/bulan
- Vultr: $5/bulan

**Domain:**
- Namecheap: ~$10/tahun
- Cloudflare: Gratis (jika sudah punya domain)

**Midtrans:**
- Free untuk development
- Transaction fee untuk production

---

## 🎯 Rekomendasi

Untuk pemula, gunakan **Railway** karena:
- Gratis untuk testing
- Otomatis handle SSL
- Mudah setup
- Auto-deploy dari GitHub
- Built-in database

Untuk production dengan traffic tinggi, gunakan **VPS** karena:
- Lebih fleksibel
- Full control
- Lebih murah untuk long-term
- Bisa hosting multiple apps

---

## 📞 Support

Jika mengalami masalah:
1. Cek logs di platform hosting
2. Pastikan environment variables benar
3. Test di local environment dulu
4. Baca dokumentasi Spring Boot dan platform hosting

---

**Selamat deploying! 🚀**
