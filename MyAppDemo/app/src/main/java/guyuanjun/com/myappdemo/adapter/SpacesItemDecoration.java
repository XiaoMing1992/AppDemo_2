package guyuanjun.com.myappdemo.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by HP on 2016-9-2.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public SpacesItemDecoration(int space){
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left =space;
        outRect.right=space;
        outRect.bottom=space;
        if (parent.getChildAdapterPosition(view)==0){
            outRect.top =space;
        }
    }
}
