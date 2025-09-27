package com.example.ipscanner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    
    private EditText ipRangeEditText;
    private Button scanButton;
    private TextView resultTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize views
        ipRangeEditText = findViewById(R.id.ipRangeEditText);
        scanButton = findViewById(R.id.scanButton);
        resultTextView = findViewById(R.id.resultTextView);
        progressBar = findViewById(R.id.progressBar);
        
        // Setup button click listener
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });
    }
    
    private void startScan() {
        String ipRange = ipRangeEditText.getText().toString().trim();
        if (ipRange.isEmpty()) {
            resultTextView.setText("Please enter IP range");
            return;
        }
        
        // Basic IP validation
        if (!ipRange.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
            resultTextView.setText("Invalid IP format. Example: 192.168.1.0");
            return;
        }
        
        new NetworkScanTask().execute(ipRange);
    }
    
    private class NetworkScanTask extends AsyncTask<String, Integer, String> {
        
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            scanButton.setEnabled(false);
            resultTextView.setText("Scanning...");
        }
        
        @Override
        protected String doInBackground(String... ranges) {
            StringBuilder results = new StringBuilder();
            String ipRange = ranges[0];
            
            try {
                // Extract base IP (first three octets)
                String[] parts = ipRange.split("\\.");
                if (parts.length != 4) {
                    return "Invalid IP format";
                }
                
                String baseIP = parts[0] + "." + parts[1] + "." + parts[2] + ".";
                int openHosts = 0;
                
                // Scan only first 10 hosts for demo (to avoid long build times)
                for (int i = 1; i <= 10; i++) {
                    String ip = baseIP + i;
                    boolean isOpen = isPortOpen(ip, 80, 1000);
                    
                    if (isOpen) {
                        openHosts++;
                        results.append("✅ ").append(ip).append(":80 - OPEN\n");
                    }
                    
                    publishProgress(i);
                    
                    // Small delay to avoid overwhelming the network
                    Thread.sleep(50);
                }
                
                if (openHosts == 0) {
                    results.append("No open hosts found on port 80");
                }
                
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
            
            return results.toString();
        }
        
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = (values[0] * 100) / 10; // Calculate percentage
            progressBar.setProgress(progress);
            resultTextView.setText("Scanning: " + values[0] + "/10 hosts");
        }
        
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            scanButton.setEnabled(true);
            resultTextView.setText(result);
        }
        
        private boolean isPortOpen(String ip, int port, int timeout) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), timeout);
                socket.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
