#!/usr/bin/env bash
# Dijalankan setelah "npx cap add android": memasang plugin native NetTools dan nama aplikasi.
set -euo pipefail
PKG_DIR="android/app/src/main/java/id/borneotech/netkit"
mkdir -p "$PKG_DIR"
cp native/NetToolsPlugin.java "$PKG_DIR/NetToolsPlugin.java"
cp native/MainActivity.java "$PKG_DIR/MainActivity.java"
sed -i 's|<string name="app_name">.*</string>|<string name="app_name">NetKit</string>|' android/app/src/main/res/values/strings.xml
sed -i 's|<string name="title_activity_main">.*</string>|<string name="title_activity_main">NetKit</string>|' android/app/src/main/res/values/strings.xml
echo "Patch Android selesai."
