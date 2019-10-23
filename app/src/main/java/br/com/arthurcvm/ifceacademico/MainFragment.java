package br.com.arthurcvm.ifceacademico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
//        ArrayList<Card> cards = new ArrayList<Card>();
//
//        //Create a Card
//        Card card = new Card(getContext());
//        //Create a CardHeader
//        CardHeader header = new CardHeader(getContext());
//        header.setTitle("asdfasdasd");
//
//        card.addCardHeader(header);
//        cards.add(card);
//
//        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(getContext(),cards);
//
//        CardGridView gridView = (CardGridView) view.findViewById(R.id.grid);
//        if (gridView!=null){
//            gridView.setAdapter(mCardArrayAdapter);
//        }

        return view;
    }
}
