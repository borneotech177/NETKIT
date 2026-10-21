package id.borneotech.netkit;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(NetToolsPlugin.class);
        super.onCreate(savedInstanceState);
    }
}
