package br.com.arthurcvm.ifceacademico;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class CardsDownloads extends AppCompatActivity {

    FileObject file;
    Context context;
    String path;

    ProgressDialog mProgressDialog;
    public CardsDownloads(Context context) {
        this(context, R.layout.card_layout_downloads);
    }

    public CardsDownloads(Context ct, FileObject m) {
//        super(ct, R.layout.card_layout_downloads);
        context = ct;
        file = m;
        init();
    }

    public CardsDownloads(Context context, int innerLayout) {
//        super(context, innerLayout);
        init();
    }

    private void init() {
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//
//// instantiate it within the onCreate method
//                mProgressDialog = new ProgressDialog(context);
//                mProgressDialog.setMessage("Baixando "+file.title);
//                mProgressDialog.setIndeterminate(true);
//                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                mProgressDialog.setCancelable(true);
//// execute this when the downloader must be fired
//                final DownloadTask downloadTask = new DownloadTask(context);
//                downloadTask.execute("http://academico.ifce.edu.br" + file.url);
//
//                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        downloadTask.cancel(true);
//                    }
//                });
//            }
//        });
    }

//    @Override
//    public void setupInnerViewElements(ViewGroup parent, View view) {
//        TextView mText = (TextView) parent.findViewById(R.id.text);
//        TextView mData = (TextView) parent.findViewById(R.id.data);
//        mText.setText(file.title);
//        mData.setText(file.data);
//    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            mWakeLock.release();
            mProgressDialog.dismiss();
            File item = new File(path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String mime =
                    URLConnection.guessContentTypeFromName(item.getName());
            Log.i("mime",mime);
            intent.setDataAndType(Uri.fromFile(item), mime);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(intent, file.title));

            //    Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
            //else
            //   Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();


        }
        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("cookie", MainActivity.cookies);
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                path = "/sdcard/IFCE-ACADEMICO/" + sUrl[0].substring(sUrl[0].lastIndexOf("/") + 1, sUrl[0].length());
                File dir = new File("/sdcard/IFCE-ACADEMICO");
                if(!dir.exists()) dir.mkdir();
                output = new FileOutputStream(path);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }
}
