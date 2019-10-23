package br.com.arthurcvm.ifceacademico;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {

    private List<FileObject> downloadsList;
    private Context mContext;
    public static FileObject file;
    public static Context context;
    public static ProgressDialog mProgressDialog;
    public static String path;



    // View holder class whose objects represent each list item
    public static class DownloadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mText;
        public TextView mData;

        @Override
        public void onClick(View v) {
            // instantiate it within the onCreate method
            mProgressDialog = new ProgressDialog(DownloadAdapter.context);
            mProgressDialog.setMessage("Baixando "+file.title);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            // execute this when the downloader must be fired
            final DownloadAdapter.DownloadTask downloadTask = new DownloadAdapter.DownloadTask(context);
            downloadTask.execute("http://academico.ifce.edu.br" + file.url);

            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    downloadTask.cancel(true);
                }
            });
        }

        public DownloadViewHolder(@NonNull View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.titulo_mat);
            mData = itemView.findViewById(R.id.data);
        }

        public void bindData(FileObject fileEx, Context contextEx) {
            file = fileEx;
            context = contextEx;
            mText.setText(file.title);
            mData.setText(file.data);
        }
    }



    public DownloadAdapter(List<FileObject> downloadsList, Context context) {
        this.downloadsList = downloadsList;
        mContext = context;

    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.download_item, parent, false);
        // Return a new view holder
        return new DownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.DownloadViewHolder holder, int position) {
        // Bind data for the item at position
        holder.bindData(downloadsList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return downloadsList.size();
    }


    private static class DownloadTask extends AsyncTask<String, Integer, String> {

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
