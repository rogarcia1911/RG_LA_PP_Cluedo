package com.example.rg_la_pp_cluedo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rg_la_pp_cluedo.BBDD.DataBaseConnection;
import com.example.rg_la_pp_cluedo.BBDD.Match;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tvId, tvTiempo, tvResultado;
    Long  matchTime;

    DataBaseConnection firebaseConnection = null;
    private List<Match> matchList = new ArrayList<>();
    private ArrayAdapter<Match> arrayAdapterMatch;

    public FragmentHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHistory newInstance(String param1, String param2) {
        FragmentHistory fragment = new FragmentHistory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO : No le gusta los findViewByID
        // TODO: la vista que recupera es la del FrameLayout y no el del Fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_history, null);
        // TODO: havciendo esto como he visto devuelve null
        View view = getView();
        View view2 = inflater.inflate(R.layout.fragment_history, container, false);
        View view3 = root.getFocusedChild();
        View view4 = view2.getRootView();
        View view5 = view2.findFocus();
        //Como la vista es FrameLayout no existe un tvId
        tvId = view2.findViewById(R.id.tvId);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO : No le gusta los findViewByID
        //tvId = getView().findViewById(R.id.tvI);
        //tvTiempo = getView().findViewById(R.id.tvTiempo);
        //tvResultado = getView().findViewById(R.id.tvResultado);

        //iniciar();

        firebaseConnection = DataBaseConnection.getInstance();

        //dataList();
/*
        //TODO: linear layout data revision (add player number?) and show all data
        //TODO: insert revision
        if (!matchList.isEmpty() && matchList != null) {
            for (Match matchObj : matchList) {

                if (matchObj.getEndingDate()!=null)
                    matchTime = matchObj.getEndingDate() - matchObj.getBeginningDate();
                //Date totalTime = new Date(matchTime);
                //TODO: convert long to date
                //matchObj.getPlayerNum();

                tvId.setText( ((String) tvId.getText()) + matchObj.getNum() + "\n" );

                if (matchObj.getEndingDate()!=null)
                    tvTiempo.setText( ((String) tvTiempo.getText()) + matchTime + "\n" );
                else
                    tvTiempo.setText( ((String) tvTiempo.getText()) + getString(R.string.tvTTNull));

                if (matchObj.getResultGame()) // resultado al ganar
                    tvResultado.setText( ((String) tvResultado.getText()) + getString(R.string.tvResultadoGanar) + "\n" );
                else if (( matchObj.getResultGame()==null || !matchObj.getResultGame() ) && matchObj.getEndingDate()==null) //resultado cuando no se ha terminado la partida
                    tvResultado.setText( ((String) tvResultado.getText()) + "------" + "\n" );
                else //resultad al perder
                    tvResultado.setText( ((String) tvResultado.getText()) + getString(R.string.tvResultadoPerder) + "\n" );
            }
        } else Toast.makeText(this.getContext(),getString(R.string.msjNoPartidas),Toast.LENGTH_SHORT).show();
*/
/*
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getContext(), "administracion",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor fila = db.rawQuery("SELECT id, IFNULL(tiempoTot,'"+getString(R.string.tvTTNull)+"'), resultado, fin FROM partidas ORDER BY id DESC LIMIT 20",null);
        if(fila.moveToFirst()){
            do {
                tvId.setText( ((String) tvId.getText()) + fila.getString(0) + "\n" );
                tvTiempo.setText( ((String) tvTiempo.getText()) + fila.getString(1) + "\n" );
                if (fila.getInt(2)==1) //resultado=true
                    tvResultado.setText( ((String) tvResultado.getText()) + getString(R.string.tvResultadoGanar) + "\n" );
                else if (fila.getInt(2)==0 && fila.getString(3)==null) //resultado=false y fin==null
                    tvResultado.setText( ((String) tvResultado.getText()) + "------" + "\n" );
                else //resultado=false y fin!=null
                    tvResultado.setText( ((String) tvResultado.getText()) + getString(R.string.tvResultadoPerder) + "\n" );
            }while(fila.moveToNext());

        } else Toast.makeText(this.getContext(),getString(R.string.msjNoPartidas),Toast.LENGTH_SHORT).show();

        db.close();
*/
    }


    private void dataList() {
        //TODO: select revision https://www.youtube.com/watch?v=_17qiNSMDCA&list=PL2LFsAM2rdnxv8bLBZrMtd_f3fsfgLzH7&index=5


        firebaseConnection.getFirebase(this.getActivity().getApplicationContext()).child("Match").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matchList.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Match matchView = objSnapshot.getValue(Match.class);

                    if (matchView != null)
                        matchList.add(matchView);


                    //rrayAdapterMatch = new ArrayAdapter<>(th, android.R.layout.activity_list_item, matchList);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //Método para borrar todas las partidas
    public void iniciar() {
        tvId.setText("\n");
        tvTiempo.setText("\n");
        tvResultado.setText("\n");
    }
}