package br.com.arthurcvm.ifceacademico;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static WebView webview;
    public static String cookies;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    static LinearLayout load_panel;
    private NavigationView nvDrawer;
    static View headerLayout;
    public static ArrayList<DiarioObjeto> materias_list;
    public static ArrayList<FileObject> file_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        materias_list = null;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        file_list = null;
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationIcon(R.drawable.hamburger_menu);
        }

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        mDrawer.setDrawerLockMode(1);
        mDrawer.openDrawer(Gravity.LEFT);
        setupDrawerContent(nvDrawer);

        headerLayout = nvDrawer.inflateHeaderView(R.layout.drawer_header);

        //Salvar Login
        final SharedPreferences prefs = getBaseContext().getSharedPreferences("br.com.arthurcvm.ifceacademico.preferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        if(prefs.getInt("prev_error", 0)!=0 || !prefs.getBoolean("auth", false)){
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
        else{
            editor.putBoolean("auth",false);
            editor.commit();
        }

        load_panel = (LinearLayout)findViewById(R.id.load_panel);

        ProgressDialog progressDialog = new ProgressDialog(this);


        webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.addJavascriptInterface(new JavaScriptInterface(), "HTMLOUT");
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                cookies = CookieManager.getInstance().getCookie(url);
                Log.i("URL", url);
                if(url.equals("https://qacademico.ifce.edu.br/qacademico/index.asp?t=1001")) {
                    view.evaluateJavascript("javascript:document.getElementById('txtLogin').value = '"+prefs.getString("matricula","")+"'", null);
                    view.evaluateJavascript("javascript:document.getElementById('txtSenha').value = '"+prefs.getString("senha","")+"'", null);
                    view.evaluateJavascript("javascript:document.forms['frmLogin'].submit()", null);
                }
                if(url.equals("https://qacademico.ifce.edu.br/qacademico/index.asp?t=1")){
                    editor.putInt("prev_error", 1);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
                else view.evaluateJavascript("window.HTMLOUT.checkErro(document.getElementsByTagName('html')[0].innerHTML);", null);
                if(url.equals("https://qacademico.ifce.edu.br/qacademico/index.asp?t=2071")){
                    view.evaluateJavascript("window.HTMLOUT.showDiario(document.getElementsByTagName('html')[0].innerHTML);", null);
                }
                if(url.equals("https://qacademico.ifce.edu.br/qacademico/index.asp?t=2061")){
                    view.evaluateJavascript("window.HTMLOUT.showDownloads(document.getElementsByTagName('html')[0].innerHTML);", null);
                }
                if(url.equals("https://qacademico.ifce.edu.br/qacademico/index.asp?t=2000")){
                    view.evaluateJavascript("window.HTMLOUT.startPage(document.getElementsByTagName('html')[0].innerHTML);", null);
                }

                load_panel.setVisibility(View.GONE);
            }
        });

        webview.loadUrl("https://qacademico.ifce.edu.br/qacademico/index.asp?t=1001");
    }

    class JavaScriptInterface {
        @JavascriptInterface
        public void startPage(String html) {
            Document doc = Jsoup.parse(html);
            Element title = doc.select("td[class=titulo]").get(1); //Nome do Aluno
            Log.i("t", title.toString());
            ((TextView)headerLayout.findViewById(R.id.name)).setText(title.text().split(", ")[1].split(" !")[0]);
            mDrawer.setDrawerLockMode(0);
        }

        @JavascriptInterface
        public void showDiario(String html) {
            Document doc = Jsoup.parse(html);
            Element table = doc.select("table[width=100%]").get(4).select("table").get(2);
            //Elements el = table.select("table"); //já tava
            Elements materias = table.select("tr[bgcolor]");
            ArrayList<DiarioObjeto> mats = new ArrayList<DiarioObjeto>();
            int lastMat = -1;

            for(int i = 1; i < materias.size(); i++){
                DiarioObjeto materia = new DiarioObjeto();

                if(!materias.get(i).is("tr[class=conteudoTexto]")){
                    String[] titulo = materias.get(i).select("td[class=conteudoTexto]").select("strong").text().split("- ");
                    materia.nome = titulo[2].split("\\(")[0];

                    materia.carga = Integer.parseInt(titulo[2].split("\\(")[1].split("\\)")[0].replace("H",""));
                    materia.aulas = Integer.parseInt(materias.get(i).select("td").get(7).text().split(" ")[0]);
                    materia.faltas = Integer.parseInt(materias.get(i).select("td").get(11).text());


//                    Log.i("t", String.valueOf(materia.carga)+" - CARGA"); //OK
//                    Log.i("t", String.valueOf(materia.aulas)+" - AULAS"); //OK
//                    Log.i("t", String.valueOf(materia.faltas)+" - FALTAS"); //OK

                }
                else{
//                    Log.i("t", materias.get(i).child(0).toString());
                    if(materias.get(i).child(0).is("td[colspan=2]")){
                        String nomeNota = materias.get(i).child(0).select("div[class=conteudoTitulo]").text();
                        String descricao = materias.get(i).child(0).select("tr[class=conteudoTexto]").text();
                        String[] valorNota = descricao.split("Nota: ");
                        Double nota = 0.0;
//                        Log.i("t", descricao+"\n");
                        if(valorNota.length == 2) {
                            nota = Double.parseDouble(valorNota[1].split(" [0-3]")[0]);
                            if (nomeNota.equals("N1")) mats.get(lastMat).notas.getN1().add(new Nota("", nota));
                            if (nomeNota.equals("N2")) mats.get(lastMat).notas.getN2().add(new Nota("", nota));

                            Log.i("t", "LOOP => "+i+" NOME "+ nomeNota +" VALOR "+valorNota[1].split(" [0-3]")[0] + " - VALOR DA NOTA!!!!!");
                        }
                        else if(valorNota.length > 2){
                            for (int j = 1; j<=valorNota.length-1; j++) {
                                try {
                                    nota = Double.parseDouble(valorNota[j].split(" [0-3]")[0]);

                                }catch (Exception e){
                                    nota = 0.0;
                                }
//                                nota = Double.parseDouble(valorNota[j].split(" [0-3]")[0]);
                                if (nomeNota.equals("N1"))
                                    mats.get(lastMat).notas.getN1().add(new Nota("", nota));
                                if (nomeNota.equals("N2"))
                                    mats.get(lastMat).notas.getN2().add(new Nota("", nota));

                                Log.i("t", "LOOP => " + i + " NOME " + nomeNota + " VALOR " + valorNota[j].split(" [0-3]")[0] + " - VALOR DA NOTA DOBRADA!!!!!");
                            }
                        }
                    }
                }

                if(!materias.get(i).is("tr[class=conteudoTexto]")) {
                    mats.add(materia);
                    lastMat = mats.size()-1;
                }
            }

            materias_list = mats;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DiarioFragment.loaded();
                }
            });
        }

        @JavascriptInterface
        public void showDownloads(String html) {
            Log.i("i", "Show Downloads");
            Document doc = Jsoup.parse(html);
            Element table = doc.select("table[width=600]").get(0);

            Elements files_mat = table.select("tr[class=rotulo][bgcolor='#E6E7E8']");
            FileObject fo;
            ArrayList<FileObject> fol = new ArrayList<>();
            for (Element file_mat : files_mat) {
                String mat = file_mat.select("td[colspan=2]").html().split(" - ")[2].split("\\(")[0];
                Log.i("a", mat);
                Element el = file_mat;

                while (el.nextElementSibling()!=null && (el = el.nextElementSibling()).html().contains("Material")){
                    fo = new FileObject();
                    Element arquivo = el.select("td").get(1).select("a").get(0);
                    fo.materia = mat;
                    fo.data = el.select("td").get(0).text();
                    Log.i("b", fo.data);
                    fo.title = arquivo.text();
                    fo.url = arquivo.parent().html().split("\"")[1];
                    fol.add(fo);
                }
            }
            file_list = fol;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DownloadsFragment.loaded();
                }
            });
        }

        @JavascriptInterface
        public void checkErro(String html) {
            if(html.toLowerCase().contains("acesso negado")) {
                final SharedPreferences prefs = getBaseContext().getSharedPreferences("br.com.arthurcvm.ifceacademico.preferences", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("prev_error", 2);
                editor.commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        }
    }
    public static void getMaterias(){
        if(materias_list!=null)DiarioFragment.loaded();
        else webview.loadUrl("https://qacademico.ifce.edu.br/qacademico/index.asp?t=2071");
    }
    public static void getDownloads(){
        webview.loadUrl("https://qacademico.ifce.edu.br/qacademico/index.asp?t=2061");
    }

    public void exit(){

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Desconectar")
                .setMessage("Tem certeza que deseja sair da sua conta?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final SharedPreferences prefs = getBaseContext().getSharedPreferences("br.com.arthurcvm.ifceacademico.preferences", Context.MODE_PRIVATE);
                        final SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("auth", false);
                        editor.putBoolean("auto_login", false);
                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }

                })
                .setNegativeButton("Não", null)
                .show();
    }
    private void setupDrawerContent(NavigationView navigationView) {
        //navigationView.getMenu().findItem(R.id.inicio).setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_home).color(Color.BLACK).sizeDp(24));
        navigationView.getMenu().findItem(R.id.diario).setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_book).color(Color.BLACK).sizeDp(24));
        navigationView.getMenu().findItem(R.id.downloads).setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_folder).color(Color.BLACK).sizeDp(24));
        //navigationView.getMenu().findItem(R.id.config).setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_settings).color(Color.BLACK).sizeDp(24));
        navigationView.getMenu().findItem(R.id.about).setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_info).color(Color.BLACK).sizeDp(24));
        navigationView.getMenu().findItem(R.id.sair).setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_exit_to_app).color(Color.BLACK).sizeDp(24));
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass = null;
        boolean change = false;
        switch(menuItem.getItemId()) {
            //case R.id.inicio:
            //  fragmentClass = MainFragment.class;
            //  change = true;
            //  break;
            case R.id.diario:
                fragmentClass = DiarioFragment.class;
                change = true;
                break;
            case R.id.downloads:
                fragmentClass = DownloadsFragment.class;
                change = true;
                break;
            case R.id.sair:
                exit();
                break;
            case R.id.about:
                fragmentClass = AboutFragment.class;
                change = true;
                break;
            default:
                fragmentClass = DiarioFragment.class;
                change = true;
        }
        if(change) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
        }
        else menuItem.setChecked(false);
        mDrawer.closeDrawers();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
