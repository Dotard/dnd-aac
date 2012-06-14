package com.dnd.aac;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Filter.FilterListener;
import android.widget.FilterQueryProvider;
import android.widget.GridView;

import com.dnd.aac.adapter.PictoGridAdapter;
import com.dnd.aac.adapter.PictoGridAdapter.PictoViewHolder;
import com.dnd.aac.cache.ImageCache;
import com.dnd.aac.cache.ImageCache.ImageCacheParams;
import com.dnd.aac.cache.ImageFetcher;
import com.dnd.aac.cache.ImageResizer;
import com.dnd.aac.cache.Images;
import com.dnd.aac.cache.Utils;
import com.dnd.aac.data.ImagesTbl;
import com.dnd.aac.data.PictosTbl;
import com.dnd.aac.data.aacProvider;


public class DetailFragment extends android.support.v4.app.Fragment {
	
	
    private static final String TAG = "ImageGridFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";
	
	private String projection[];
	private GridView gridview;
	
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private PictoGridAdapter mAdapter;
    protected static ImageResizer mImageWorker;
	private Cursor mCursor;
	GridView mGridView = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
			//	mCursor = getActivity().getContentResolver().query(Uri.withAppendedPath(aacProvider.PICTOS_URI ,"2/1") , projection, null, null, null);
		 
		 mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
	     mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
	     	     
	     ImageCacheParams cacheParams = new ImageCacheParams(IMAGE_CACHE_DIR);

	        // Allocate a third of the per-app memory limit to the bitmap memory cache. This value
	        // should be chosen carefully based on a number of factors. Refer to the corresponding
	        // Android Training class for more discussion:
	        // http://developer.android.com/training/displaying-bitmaps/
	        // In this case, we aren't using memory for much else other than this activity and the
	        // ImageDetailActivity so a third lets us keep all our sample image thumbnails in memory
	        // at once.
	        cacheParams.memCacheSize = 1024 * 1024 * Utils.getMemoryClass(getActivity()) / 3;

	        // The ImageWorker takes care of loading images into our ImageView children asynchronously
	        mImageWorker = new ImageFetcher(getActivity(), mImageThumbSize);
	        mImageWorker.setAdapter(Images.imageThumbWorkerUrlsAdapter);
	        mImageWorker.setLoadingImage(R.drawable.empty_photo);
	        mImageWorker.setImageCache(ImageCache.findOrCreateCache(getActivity(), cacheParams)); 
	        
	        
	     
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		projection = new String[]{ PictosTbl.table + "." + PictosTbl.pictoID + " as _id", PictosTbl.table + "." + PictosTbl.pictoPhrase
				, ImagesTbl.table + "." + ImagesTbl.imageUri 
		};
		if (mCursor == null)
		mCursor = getActivity().getContentResolver().query(Uri.withAppendedPath(aacProvider.PICTOS_URI ,"2/1") , projection, null, null, null);		
		   		
		final View view = inflater.inflate(R.layout.details, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridview);
        
        mAdapter = new PictoGridAdapter(getActivity(),R.layout.picto_grid,mCursor, mImageWorker,new String[]{},new int[]{},mGridView);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {
        	
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					((MainActivity) getActivity()).barHelper.addPicto(new Picto((int) id, ((PictoViewHolder) v.getTag()).text.getText()+"", getActivity()));
			}
		});
		

        // This listener is used to get the final width of the GridView and then calculate the
        // number of columns and the width of each column. The width of each column is variable
        // as the GridView has stretchMode=columnWidth. The column width is used to set the height
        // of each view so we get nice square thumbnails.
        /*mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (mAdapter.getNumColumns() == 0) {
                            final int numColumns = (int) Math.floor(
                                    mGridView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
                            if (numColumns > 0) {
                                final int columnWidth =
                                        (mGridView.getWidth() / numColumns) - mImageThumbSpacing;
                                mAdapter.setNumColumns(numColumns);
                                mAdapter.setItemHeight(columnWidth);
                                if (BuildConfig.DEBUG) {
                                    Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
                                }
                            }
                        }
                    }
                });
                */
        
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	

	public void setSubcategory(long subcategoryID)
	{
		mCursor = getActivity().getContentResolver().query(Uri.withAppendedPath(aacProvider.PICTOS_URI ,"1/" + String.valueOf(subcategoryID)), projection, null, null, null);
		mAdapter = new PictoGridAdapter(getActivity(),R.layout.picto_grid,mCursor, mImageWorker,new String[]{},new int[]{},mGridView);
		mGridView.setAdapter(mAdapter);
	}
	
	public void setCategory(long categoryID)
	{
		mCursor = getActivity().getContentResolver().query(Uri.withAppendedPath(aacProvider.PICTOS_URI,"2/" + String.valueOf(categoryID)), projection, null, null, null);
		mAdapter = new PictoGridAdapter(getActivity(),R.layout.picto_grid,mCursor, mImageWorker,new String[]{},new int[]{},mGridView);
		mGridView.setAdapter(mAdapter);
	}
	
	public void refreshGridView(){
		mAdapter.refreshPictoSize();
		mAdapter.notifyDataSetChanged();
		mGridView.invalidateViews();
	}
	
    @Override
    public void onResume() {
        super.onResume();
        //mImageWorker.setExitTasksEarly(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mImageWorker.setExitTasksEarly(true);
    }
    
    public void filterList(CharSequence constraint) {
        final PictoGridAdapter adapter = 
            (PictoGridAdapter) mGridView.getAdapter();
        final Cursor oldCursor = adapter.getCursor();
        adapter.setFilterQueryProvider(filterQueryProvider);
        adapter.getFilter().filter(constraint, new FilterListener() {
            public void onFilterComplete(int count) {
                // assuming your activity manages the Cursor 
                // (which is a recommended way)
            	getActivity().stopManagingCursor(oldCursor);
                final Cursor newCursor = adapter.getCursor();
                getActivity().startManagingCursor(newCursor);
                // safely close the oldCursor
                if (oldCursor != null && !oldCursor.isClosed()) {
                    oldCursor.close();
                }
            }
        });
    }

    private FilterQueryProvider filterQueryProvider = new FilterQueryProvider() {
        public Cursor runQuery(CharSequence constraint) {
            // assuming you have your custom DBHelper instance 
            // ready to execute the DB request
            return getActivity().getContentResolver().query(Uri.withAppendedPath(aacProvider.PICTOS_URI,"pictos"), projection,
            		PictosTbl.table + "." + PictosTbl.pictoPhrase + " LIKE '%"+constraint+"%'", null, "lower(" + PictosTbl.table + "." + PictosTbl.pictoPhrase + ") ASC");
        }
    };
	
}
