package br.com.arthurcvm.ifceacademico;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiarioAdapter extends RecyclerView.Adapter<DiarioAdapter.DiarioViewHolder> {


    private List<DiarioObjeto> diariosList;
    private Context mContext;

    // View holder class whose objects represent each list item
    public static class DiarioViewHolder extends RecyclerView.ViewHolder {
//        public ImageView cardImageView;
//        public TextView titleTextView;
//        public TextView subTitleTextView;
        public TextView nome_diario;
        public TextView mAulas;
        public TextView mFaltas;
        public TextView mPresenca;

        public TextView N1;
        public TextView N11;
        public TextView N12;
        public TextView N13;
        public TextView N14;

        public TextView N2;
        public TextView N21;
        public TextView N22;
        public TextView N23;
        public TextView N24;

        public DiarioViewHolder(@NonNull View itemView) {
            super(itemView);
            nome_diario = itemView.findViewById(R.id.nome_diario);
            mAulas = itemView.findViewById(R.id.aulas);
            mFaltas = itemView.findViewById(R.id.faltas);
            mPresenca = itemView.findViewById(R.id.presenca);
            N1 = itemView.findViewById(R.id.N1);
            N11 = itemView.findViewById(R.id.N11);
            N12 = itemView.findViewById(R.id.N12);
            N13 = itemView.findViewById(R.id.N13);
            N14 = itemView.findViewById(R.id.N14);
            N2 = itemView.findViewById(R.id.N2);
            N21 = itemView.findViewById(R.id.N21);
            N22 = itemView.findViewById(R.id.N22);
            N23 = itemView.findViewById(R.id.N23);
            N24 = itemView.findViewById(R.id.N24);
//            cardImageView = itemView.findViewById(R.id.imageView);
//            titleTextView = itemView.findViewById(R.id.card_title);
//            subTitleTextView = itemView.findViewById(R.id.card_subtitle);
        }

        public void bindData(DiarioObjeto diario, Context context) {
            nome_diario.setText(diario.nome);
            mAulas.setText(diario.aulas+"/"+diario.carga);
            String faltas = diario.faltas + " " + (diario.faltas==1?"falta":"faltas");
            mFaltas.setText(faltas);
            mPresenca.setText((100-(diario.faltas*100)/diario.carga) + "% de presença");

            if (diario.notas.getN1().size() > 0){
                N1.setText("N1");
                for(int i = 0; i < diario.notas.getN1().size(); i++){
                    switch (i+1){
                        case 1:
                            N11.setText(String.valueOf(diario.notas.getN1().get(i).getValor()));
                            break;
                        case 2:
                            N12.setText(String.valueOf(diario.notas.getN1().get(i).getValor()));
                            break;
                        case 3:
                            N13.setText(String.valueOf(diario.notas.getN1().get(i).getValor()));
                            break;
                        case 4:
                            N14.setText(String.valueOf(diario.notas.getN1().get(i).getValor()));
                            break;
                    }
                }
            }
            if (diario.notas.getN2().size() > 0){
                N2.setText("N2");
                for(int i = 0; i < diario.notas.getN2().size(); i++){
                    switch (i+1){
                        case 1:
                            N21.setText(String.valueOf(diario.notas.getN2().get(i).getValor()));
                            break;
                        case 2:
                            N22.setText(String.valueOf(diario.notas.getN2().get(i).getValor()));
                            break;
                        case 3:
                            N23.setText(String.valueOf(diario.notas.getN2().get(i).getValor()));
                            break;
                        case 4:
                            N24.setText(String.valueOf(diario.notas.getN2().get(i).getValor()));
                            break;
                    }
                }
            }
//            cardImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.list_image));
//            titleTextView.setText(dataModel.getTitle());
//            subTitleTextView.setText(dataModel.getSubTitle());
        }
    }

    public DiarioAdapter(List<DiarioObjeto> diariosList, Context context) {
        this.diariosList = diariosList;
        mContext = context;

    }

    @NonNull
    @Override
    public DiarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diario_item, parent, false);
        // Return a new view holder
        return new DiarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiarioViewHolder holder, int position) {
        // Bind data for the item at position
        holder.bindData(diariosList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return diariosList.size();
    }
}
