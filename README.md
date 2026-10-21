# NetKit (Borneo-Tech)

Perkakas teknisi jaringan: kalkulator subnet, VLSM, generator config MikroTik/Cisco, denah CCTV, IPAM, laporan, dan lainnya.
Dibuat sebagai satu file web (`www/index.html`) lalu dibungkus jadi aplikasi Android memakai Capacitor.

## Isi folder

| Path | Fungsi |
|---|---|
| `www/index.html` | Seluruh aplikasi (HTML, CSS, JS dalam satu file) |
| `capacitor.config.json` | Konfigurasi aplikasi (ID `id.borneotech.netkit`) |
| `native/` | Kode Android: plugin **NetTools** (ping ICMP dan cek port TCP) dan `MainActivity` |
| `scripts/patch-android.sh` | Memasang plugin native ke project Android hasil `cap add` |
| `assets/` | Sumber ikon dan splash screen |
| `.github/workflows/` | Build APK otomatis di GitHub Actions |

Folder `android/` **tidak** disimpan di repo. Folder itu dibuat otomatis saat build.

## Cara build APK lewat GitHub (tanpa Android Studio)

1. Buat repo baru di GitHub, lalu unggah seluruh isi folder ini (termasuk folder `.github`).
2. Buka tab **Actions** di repo. Workflow **Build APK** jalan otomatis setiap push ke branch `main`, atau jalankan manual lewat **Run workflow**.
3. Setelah selesai (sekitar 5-10 menit), buka run-nya dan unduh artifact **NetKit-debug-apk**.
4. Ekstrak zip-nya, kirim `app-debug.apk` ke HP, lalu instal. Izinkan "Instal dari sumber tidak dikenal" kalau diminta.

## Mengubah aplikasi

Edit `www/index.html`, commit, push. Build berikutnya otomatis memakai versi terbaru.
Naikkan nomor versi di `package.json` dan teks `NetKit v1.0.0` di dalam `index.html` (cari `st-ver`) saat merilis versi baru.

## APK rilis bertanda tangan (opsional)

APK debug cukup untuk dipakai sendiri. Untuk dibagikan luas atau Play Store, pakai workflow **Build APK rilis**:

1. Buat keystore sekali saja (di komputer dengan Java):
   `keytool -genkeypair -v -keystore release.keystore -alias netkit -keyalg RSA -keysize 2048 -validity 10000`
2. Ubah ke base64: `base64 -w0 release.keystore`
3. Simpan di GitHub (Settings > Secrets and variables > Actions): `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`.
4. Jalankan workflow dari tab Actions.

**Simpan file keystore dan passwordnya dengan aman.** Kalau hilang, update aplikasi di perangkat yang sudah terpasang tidak bisa dilakukan dengan tanda tangan baru. Jangan pernah mengunggah `release.keystore` ke repo (sudah masuk `.gitignore`).

## Fitur khusus aplikasi Android

- **Tombol back HP**: dari menu mana pun kembali ke Beranda. Di Beranda, tekan back dua kali dalam 2 detik untuk keluar.
- **Ping ICMP dan cek port TCP asli** lewat plugin NetTools. Sapu rentang IP memakai cek TCP native, jadi jauh lebih akurat daripada versi browser.
- **Simpan dan bagikan file** (cadangan JSON, PNG denah, laporan) lewat menu bagikan Android.
- **Laporan**: aplikasi membuat file HTML lalu membagikannya. Buka di Chrome, pilih Cetak, lalu Simpan sebagai PDF.
- Akses HTTP ke perangkat lokal (router, kamera) diizinkan, karena aplikasi memakai skema `http` dan `cleartext`.

## Catatan penting

- Data (IPAM, catatan, denah, checklist) tersimpan di penyimpanan aplikasi. Menghapus data aplikasi atau uninstall menghapusnya. Gunakan menu **Cadangan data** secara berkala.
- Jangan ubah `androidScheme` di `capacitor.config.json` setelah aplikasi dipakai, karena data tersimpan per skema dan akan terlihat hilang.
- Plugin NetTools memakai perintah `ping` bawaan Android, tanpa root.
- Scan WiFi dan backup config MikroTik otomatis belum ada. Keduanya butuh plugin tambahan.

## Build lokal (opsional)

Butuh Node 20, JDK 17, dan Android SDK:

```bash
npm install
npx cap add android
npx @capacitor/assets generate --android
bash scripts/patch-android.sh
npx cap sync android
cd android && ./gradlew assembleDebug
```

APK ada di `android/app/build/outputs/apk/debug/`.
