package id.borneotech.netkit;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Plugin native NetKit: ping ICMP dan cek port TCP.
 * Dipanggil dari JavaScript lewat window.Capacitor.Plugins.NetTools
 */
@CapacitorPlugin(name = "NetTools")
public class NetToolsPlugin extends Plugin {

    private static final String HOST_RE = "^[A-Za-z0-9._:-]{1,253}$";

    @PluginMethod
    public void ping(PluginCall call) {
        String host = call.getString("host", "");
        int count = call.getInt("count", 4);
        if (host == null || !host.matches(HOST_RE)) {
            call.reject("Host tidak valid");
            return;
        }
        count = Math.max(1, Math.min(count, 10));
        try {
            Process p = new ProcessBuilder("/system/bin/ping", "-c", String.valueOf(count), "-W", "2", host)
                    .redirectErrorStream(true).start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line).append("\n");
            }
            int code = p.waitFor();
            JSObject o = new JSObject();
            o.put("output", sb.toString().trim());
            o.put("exit", code);
            call.resolve(o);
        } catch (Exception e) {
            call.reject("Ping gagal: " + e.getMessage());
        }
    }

    @PluginMethod
    public void tcpCheck(PluginCall call) {
        String host = call.getString("host", "");
        int port = call.getInt("port", 80);
        int timeout = call.getInt("timeout", 2000);
        if (host == null || !host.matches(HOST_RE) || port < 1 || port > 65535) {
            call.reject("Host atau port tidak valid");
            return;
        }
        long t0 = System.currentTimeMillis();
        JSObject o = new JSObject();
        try (Socket s = new Socket()) {
            s.connect(new InetSocketAddress(host, port), timeout);
            o.put("open", true);
        } catch (Exception e) {
            o.put("open", false);
            o.put("error", e.getMessage() == null ? "gagal" : e.getMessage());
        }
        o.put("ms", System.currentTimeMillis() - t0);
        call.resolve(o);
    }
}
