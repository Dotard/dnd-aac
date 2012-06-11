package com.dnd.aac;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.dnd.aac.adapter.MyPictoAdapter;
import com.dnd.aac.adapter.MyPictoAdapter.PictoViewHolder;
import com.dnd.aac.cache.ImageCache;
import com.dnd.aac.cache.ImageCache.ImageCacheParams;
import com.dnd.aac.cache.ImageFetcher;
import com.dnd.aac.cache.ImageResizer;
import com.dnd.aac.cache.Images;
import com.dnd.aac.cache.Utils;
import com.dnd.aac.data.aacProvider;


public class DetailFragment extends android.support.v4.app.Fragment {
	
	
    private static final String TAG = "ImageGridFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";
	
	private String projection[];
	private GridView gridview;
	
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private MyPictoAdapter mAdapter;
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
		
		projection = new String[]{ "PICTOS.pictoID as _id","Pictos.pictoPhrase", "Images.imageUri" };
		if (mCursor == null)
		mCursor = getActivity().getContentResolver().query(Uri.withAppendedPath(aacProvider.PICTOS_URI ,"2/1") , projection, null, null, null);
		mAdapter = new MyPictoAdapter(getActivity(),R.layout.picto,mCursor, mImageWorker);
		   		
		final View view = inflater.inflate(R.layout.details, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
						
				/*BitmapDrawable bd;

				Cursor pictoCursor = getActivity().getContentResolver().query(aacProvider.PICTOS_URI, projection, "imageID = " + id, null, null);
				mImageWorker.loadImage(pictoCursor.getString(pictoCursor.getColumnIndex("imageUri")), image);
				pictoCursor.close();*/
				
				/*try {
					bd = (BitmapDrawable) image.getDrawable();
				} catch (Exception e){
					bd = (BitmapDrawable) ((TransitionDrawable)image.getDrawable()).getDrawable(1);
				}
				*/
				
				((MainActivity) getActivity()).editHelper.addPicto(new Picto((int) id, ((PictoViewHolder) v.getTag()).text.getText()+"", getActivity()));
				//EditText et = (EditText) getActivity().findViewById(R.id.enter);
				//if (et.length() > 0) {et.append(" "+ tv.getText());} else {et.append( tv.getText());}
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
		
		/*mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				ImageView image = (ImageView) v.findViewById(R.id.image);
				TextView tv = (TextView) v.findViewById(R.id.text);

				((MainActivity) getActivity()).addPicto(new Picto((int) id, tv.getText()+"", ((BitmapDrawable)image.getDrawable()).getBitmap()));
				
				//EditText et = (EditText) getActivity().findViewById(R.id.enter);
				//if (et.length() > 0) {et.append(" "+ tv.getText());} else {et.append( tv.getText());}
			}
		});;*/
	}
	

	public void setSubcategory(long subcategoryID)
	{
		mCursor = getActivity().getContentResolver().query(Uri.withAppendedPath(aacProvider.PICTOS_URI ,"1/" + String.valueOf(subcategoryID)), projection, null, null, null);
		mAdapter = new MyPictoAdapter(getActivity(),R.layout.picto,mCursor, mImageWorker);
		mGridView.setAdapter(mAdapter);
	}
	
	public void setCategory(long categoryID)
	{
		mCursor = getActivity().getContentResolver().query(Uri.withAppendedPath(aacProvider.PICTOS_URI,"2/" + String.valueOf(categoryID)), projection, null, null, null);
		mAdapter = new MyPictoAdapter(getActivity(),R.layout.picto,mCursor, mImageWorker);
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
	
}
