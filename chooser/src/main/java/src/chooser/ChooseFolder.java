package src.chooser;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rulo on 17/03/15.
 */
public class ChooseFolder extends LinearLayout {

    private TextView txtPath;
    private RecyclerView list;
    private AdapterExplorer mAdapter;
    private ArrayList<String> items = new ArrayList<>();
    private Context context;

    private String path = Files.root;

    public ChooseFolder(Context context) {
        super(context);
        this.context = context;
        Initialize();
    }

    public ChooseFolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Initialize();
    }

    public ChooseFolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        Initialize();
    }

    private void Initialize() {


        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater cf = (LayoutInflater) getContext().getSystemService(infService);
        cf.inflate(R.layout.expl, this, true);

        this.txtPath = (TextView) findViewById(R.id.path);
        this.list = (RecyclerView) findViewById(R.id.listadoFolder);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        this.txtPath.setText(this.path.replace(Files.root, "/sdcard"));
        this.items = Files.loadItems(this.path);
        this.mAdapter = new AdapterExplorer(items);

        list.setLayoutManager(layoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setHasFixedSize(true);
        list.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new AdapterExplorer.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    setPath(Files.getPathPreviousFolder(path));
                } else {
                    setPath(genPathClickFolder(items.get(position)));
                }
            }
        });

    }

    private String genPathClickFolder(String nameFolder) {

        return this.getPath() + "/" + nameFolder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {

        this.path = path;
        this.txtPath.setText(this.path.replace(Files.root, "/sdcard"));
        this.items = Files.loadItems(this.path);
        this.mAdapter.setItems(this.items);
        this.mAdapter.notifyDataSetChanged();

    }

    public void addFolder(String name) {

        Files.addFolder(genPathClickFolder(name));
        this.items.add(1, name);
        this.mAdapter.setItems(this.items);
        this.mAdapter.notifyDataSetChanged();

    }

}
