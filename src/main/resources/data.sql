-- Sample Ebook Data
INSERT INTO ebooks (slug, title, description, price, stock, active, phase1_narrative, phase2_narrative, phase3_narrative, phase4_narrative, phase5_narrative, created_at, updated_at) VALUES 
(
    'transformasi-diri',
    'Transformasi Diri: Menjadi Orang yang Dicari Dunia',
    'Panduan lengkap untuk transformasi diri melalui 5 fase: Menggali Luka, Instal Mindset, Strategi Potensi, Titik Balik, dan Transformasi. Ebook ini akan membantu Anda menemukan versi terbaik dari diri sendiri.',
    99000,
    999,
    true,
    'Fase 1: Menggali Luka - Di fase ini, Anda akan diajak untuk menggali ke dalam dan menemukan luka-luka masa lalu yang selama ini menghambat potensi Anda. Tanpa menyembuhkan luka, transformasi sejati tidak mungkin terjadi. Anda akan belajar mengakui, menerima, dan melepaskan beban emosional yang selama ini Anda bawa.',
    'Fase 2: Instal Mindset - Setelah luka disembuhkan, saatnya menginstal mindset growth yang kuat. Anda akan belajar mengubah pola pikir dari fixed mindset ke growth mindset. Fase ini akan membantu Anda membangun kepercayaan diri, optimisme, dan mentalitas yang tidak mudah menyerah menghadapi tantangan.',
    'Fase 3: Strategi Potensi - Di fase ini, Anda akan mengidentifikasi potensi unik yang dimiliki. Tidak semua orang memiliki potensi yang sama, dan itu yang membuat Anda berharga. Anda akan menyusun strategi untuk mengembangkan potensi tersebut dan mengubahnya menjadi keunggulan kompetitif di dunia kerja.',
    'Fase 4: Titik Balik - Fase ini adalah momen krusial di mana Anda mengambil keputusan hidup yang mengubah arah perjalanan Anda. Titik balik adalah saat Anda memilih untuk tidak lagi menjadi korban situasi, tapi menjadi arsitek masa depan Anda sendiri. Keputusan di fase ini akan menentukan trajectory hidup Anda ke depan.',
    'Fase 5: Transformasi - Fase terakhir adalah manifestasi dari semua proses sebelumnya. Anda akan menjadi orang yang dicari dan dihargai dunia karena nilai yang Anda bawa. Bukan karena apa yang Anda miliki, tapi karena siapa Anda dan apa yang Anda bisa berikan. Transformasi ini bukan hanya perubahan eksternal, tapi revolusi internal yang permanen.',
    NOW(),
    NOW()
);
