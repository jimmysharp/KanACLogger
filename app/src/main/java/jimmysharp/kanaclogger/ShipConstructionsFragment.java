package jimmysharp.kanaclogger;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class ShipConstructionsFragment extends Fragment {

    public ShipConstructionsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ship_constructions, container, false);
        ImageButton buttonOpenConstruction = (ImageButton) view.findViewById(R.id.button_open_construction);

        buttonOpenConstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container_main, new AddShipConstructionFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

}
