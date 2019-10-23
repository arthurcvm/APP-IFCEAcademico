package br.com.arthurcvm.ifceacademico;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CardsDiario extends AppCompatActivity {

    DiarioObjeto mat;

//    public CardsDiario(Context context) {
//        this(context, R.layout.card_layout_diario);
//    }

//    public CardsDiario(Context context, DiarioObjeto m) {
//        super(context, R.layout.card_layout_diario);
//        mat = m;
////        init();
//    }

//    public CardsDiario(Context context, int innerLayout) {
//        super(context, innerLayout);
//        init();
//    }
//    private void init(){
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//                //Toast.makeText(getContext(), "Click Listener card=", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

//    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
//        TextView mProf = (TextView) parent.findViewById(R.id.prof);
        TextView mAulas = (TextView) parent.findViewById(R.id.aulas);
        TextView mFaltas = (TextView) parent.findViewById(R.id.faltas);
        TextView mPresenca = (TextView) parent.findViewById(R.id.presenca);

        TextView N1 = (TextView) parent.findViewById(R.id.N1);
        TextView N11 = (TextView) parent.findViewById(R.id.N11);
        TextView N12 = (TextView) parent.findViewById(R.id.N12);
        TextView N13 = (TextView) parent.findViewById(R.id.N13);
        TextView N14 = (TextView) parent.findViewById(R.id.N14);

        TextView N2 = (TextView) parent.findViewById(R.id.N2);
        TextView N21 = (TextView) parent.findViewById(R.id.N21);
        TextView N22 = (TextView) parent.findViewById(R.id.N22);
        TextView N23 = (TextView) parent.findViewById(R.id.N23);
        TextView N24 = (TextView) parent.findViewById(R.id.N24);

//        mProf.setText(mat.professor);
        mAulas.setText(mat.aulas+"/"+mat.carga);
        String faltas = mat.faltas + " " + (mat.faltas==1?"falta":"faltas");
        mFaltas.setText(faltas);
        mPresenca.setText((100-(mat.faltas*100)/mat.carga) + "% de presença");

        if (mat.notas.getN1().size() > 0){
            N1.setText("N1");
            for(int i = 0; i < mat.notas.getN1().size(); i++){
                switch (i+1){
                    case 1:
                        N11.setText(String.valueOf(mat.notas.getN1().get(i).getValor()));
                        break;
                    case 2:
                        N12.setText(String.valueOf(mat.notas.getN1().get(i).getValor()));
                        break;
                    case 3:
                        N13.setText(String.valueOf(mat.notas.getN1().get(i).getValor()));
                        break;
                    case 4:
                        N14.setText(String.valueOf(mat.notas.getN1().get(i).getValor()));
                        break;
                }
            }
        }
        if (mat.notas.getN2().size() > 0){
            N2.setText("N2");
            for(int i = 0; i < mat.notas.getN2().size(); i++){
                switch (i+1){
                    case 1:
                        N21.setText(String.valueOf(mat.notas.getN2().get(i).getValor()));
                        break;
                    case 2:
                        N22.setText(String.valueOf(mat.notas.getN2().get(i).getValor()));
                        break;
                    case 3:
                        N23.setText(String.valueOf(mat.notas.getN2().get(i).getValor()));
                        break;
                    case 4:
                        N24.setText(String.valueOf(mat.notas.getN2().get(i).getValor()));
                        break;
                }
            }
        }
    }
}
