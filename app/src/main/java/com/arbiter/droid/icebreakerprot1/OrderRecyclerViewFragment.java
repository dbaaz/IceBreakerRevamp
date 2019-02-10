package com.arbiter.droid.icebreakerprot1;

import android.content.Context;
import android.net.Uri;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;


import android.widget.Toast;
import android.os.Handler;


import android.view.ViewGroup;
import android.view.MenuInflater;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderRecyclerViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderRecyclerViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class OrderRecyclerViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;


    private OrderRecyclerViewAdapter mAdapter;

    private ArrayList<OrderAbstractModel> modelList = new ArrayList<>();


    public OrderRecyclerViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderRecyclerViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderRecyclerViewFragment newInstance(String param1, String param2) {
        OrderRecyclerViewFragment fragment = new OrderRecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static OrderRecyclerViewFragment newInstance() {
        OrderRecyclerViewFragment fragment = new OrderRecyclerViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_recycler_view, container, false);

        // ButterKnife.bind(this);
        findViews(view);

        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAdapter();


    }


    private void findViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
                    //+ " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setAdapter() {


        modelList.add(new OrderAbstractModel("Android", "Hello " + " Android"));
        modelList.add(new OrderAbstractModel("Beta", "Hello " + " Beta"));
        modelList.add(new OrderAbstractModel("Cupcake", "Hello " + " Cupcake"));
        modelList.add(new OrderAbstractModel("Donut", "Hello " + " Donut"));
        modelList.add(new OrderAbstractModel("Eclair", "Hello " + " Eclair"));
        modelList.add(new OrderAbstractModel("Froyo", "Hello " + " Froyo"));
        modelList.add(new OrderAbstractModel("Gingerbread", "Hello " + " Gingerbread"));
        modelList.add(new OrderAbstractModel("Honeycomb", "Hello " + " Honeycomb"));
        modelList.add(new OrderAbstractModel("Ice Cream Sandwich", "Hello " + " Ice Cream Sandwich"));
        modelList.add(new OrderAbstractModel("Jelly Bean", "Hello " + " Jelly Bean"));
        modelList.add(new OrderAbstractModel("KitKat", "Hello " + " KitKat"));
        modelList.add(new OrderAbstractModel("Lollipop", "Hello " + " Lollipop"));
        modelList.add(new OrderAbstractModel("Marshmallow", "Hello " + " Marshmallow"));
        modelList.add(new OrderAbstractModel("Nougat", "Hello " + " Nougat"));
        modelList.add(new OrderAbstractModel("Android O", "Hello " + " Android O"));


        mAdapter = new OrderRecyclerViewAdapter(getActivity(), modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new OrderRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, OrderAbstractModel model) {

                //handle item click events here
                Toast.makeText(getActivity(), "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();


            }
        });


    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
