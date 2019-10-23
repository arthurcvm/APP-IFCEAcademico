package br.com.arthurcvm.ifceacademico;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class DownloadsFragment extends Fragment {
    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;

    static View view;
    static Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_downloads, container, false);
        context = getContext();
        MainActivity.getDownloads();
        if(MainActivity.file_list==null)
            MainActivity.load_panel.setVisibility(View.VISIBLE);
        return view;
    }

    public static void loaded(){
        MainActivity.load_panel.setVisibility(View.GONE);

        mRecyclerView = view.findViewById(R.id.recycler_view_download);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter and pass in our data model list
        mAdapter = new DownloadAdapter(MainActivity.file_list, context);
        mRecyclerView.setAdapter(mAdapter);


//        ArrayList<Card> cards = new ArrayList<Card>();
//        List<CardSection> sections =  new ArrayList<CardSection>();
//        String lastMat = "";
//        int i = 0;
//        for(FileObject file : MainActivity.file_list) {
//            Card card = new CardsDownloads(context, file);
//            cards.add(card);
//            if(lastMat!=file.materia)
//                sections.add(new CardSection(i,file.materia));
//            lastMat = file.materia;
//            i++;
//        }
//        CardSection[] dummy = new CardSection[sections.size()];
//
//        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(context,cards);
//        CardsDownloadsSectionAdapter mAdapter = new CardsDownloadsSectionAdapter(context, mCardArrayAdapter);
//
//        mAdapter.setCardSections(sections.toArray(dummy));
//
//        CardListView listView = (CardListView) view.findViewById(R.id.list);
//
//        if (listView!=null){
//            listView.setExternalAdapter(mAdapter,mCardArrayAdapter);
//        }
    }
}
