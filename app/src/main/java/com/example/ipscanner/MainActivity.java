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
        
        // تهيئة العناصر
        ipRangeEditText = findViewById(R.id.ipRangeEditText);
        scanButton = findViewById(R.id.scanButton);
        resultTextView = findViewById(R.id.resultTextView);
        progressBar = findViewById(R.id.progressBar);
        
        // إعداد الزر
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
            resultTextView.setText("⚠️ الرجاء إدخال نطاق IP");
            return;
        }
        
        new NetworkScanTask().execute(ipRange);
    }
    
    private class NetworkScanTask extends AsyncTask<String, Integer, String> {
        private int totalHosts = 254;
        private int scannedHosts = 0;
        
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(totalHosts);
            scanButton.setEnabled(false);
            resultTextView.setText("جاري الفحص...");
        }
        
        @Override
        protected String doInBackground(String... ranges) {
            StringBuilder results = new StringBuilder();
            String ipRange = ranges[0];
            
            try {
                // تحليل نطاق IP
                String[] parts = ipRange.split("\\.");
                if (parts.length != 4) {
                    return "❌ تنسيق IP غير صحيح";
                }
                
                String baseIP = parts[0] + "." + parts[1] + "." + parts[2] + ".";
                
                for (int i = 1; i <= 254; i++) {
                    String ip = baseIP + i;
                    boolean isOpen = isPortOpen(ip, 80, 1000);
                    
                    if (isOpen) {
                        results.append("✅ ").append(ip).append(":80 - OPEN\n");
                    }
                    
                    scannedHosts = i;
                    publishProgress(i);
                    
                    // تأخير لتجنب الحمل الزائد
                    Thread.sleep(10);
                }
                
            } catch (Exception e) {
                return "❌ خطأ: " + e.getMessage();
            }
            
            return results.toString().isEmpty() ? 
                "❌ لم يتم العثور على أي مضيف نشط" : results.toString();
        }
        
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            resultTextView.setText("جاري فحص المضيف: " + values[0] + "/254");
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
