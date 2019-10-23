package br.com.arthurcvm.ifceacademico;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class DiarioFragment extends Fragment {

    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;

    static View view;
    static Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diario, container, false);
        context = getContext();
        MainActivity.getMaterias();
        if(MainActivity.materias_list==null)
            MainActivity.load_panel.setVisibility(View.VISIBLE);
        return view;
    }

    public static void loaded(){
        MainActivity.load_panel.setVisibility(View.GONE);

        mRecyclerView = view.findViewById(R.id.recycler_view_diario);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter and pass in our data model list
        mAdapter = new DiarioAdapter(MainActivity.materias_list, context);
        mRecyclerView.setAdapter(mAdapter);

//        ArrayList<ExpandableCardView> cards = new ArrayList<ExpandableCardView>();
//        for(DiarioObjeto mat : MainActivity.materias_list) {
//            ExpandableCardView card = new ExpandableCardView(context);
//
//            card.setTitle(mat.nome);
////            card.expand();
//            card.set
//
//            CardExpand cardExpand = new CardsDiario(context, mat);
//
//            card.addCardExpand(cardExpand);
//            cards.add(card);
//        }
//
//        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(context,cards);
//
//        CardGridView gridView = (CardGridView) view.findViewById(R.id.grid);
//        gridView.setNumColumns(1);
//        if (gridView!=null){
//            gridView.setAdapter(mCardArrayAdapter);
//        }
    }
}
