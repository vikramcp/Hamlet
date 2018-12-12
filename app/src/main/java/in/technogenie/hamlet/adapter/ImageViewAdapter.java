package in.technogenie.hamlet.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.CustomerVO;
import in.technogenie.hamlet.utils.Utility;

public class ImageViewAdapter extends BaseAdapter {
	private Context mContext;
	private String[] web;
	private Integer[] mThumbIds;
	ImageView imageView;
	private String page;
	List<CustomerVO> customerList;
	//String bitMapURI;


	public ImageViewAdapter(Context c, List<CustomerVO> customerList, String page) {
		mContext = c;
		this.customerList = customerList;
		this.page = page;
	}



	public ImageViewAdapter(Context c, String[] web, Integer[] mThumbIds,
			String page) {
		mContext = c;
		this.mThumbIds = mThumbIds;
		this.web = web;
		this.page = page;
	}

	/*	public ImageAdapter(Context c, String[] web, String[] role,
			Integer[] mThumbIds, String page) {
		mContext = c;
		this.mThumbIds = mThumbIds;
		this.web = web;
		this.page = page;
		this.role = role;
	}*/

	public int getCount() {
		int count = 0;
		if (customerList != null) {
			count = customerList.size();
		} else if (web != null) {
			count = web.length;
		}
		return count;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		View grid;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		

		
		if (convertView == null) {
			grid = new View(mContext);

			Log.d("ImageAdapter", "Context :" + page);
			if (page.equals("HomeActivity")) {
				grid = inflater.inflate(R.layout.home_grid_single, null);
			} else if (page.equals("MembersFragment")) {
				grid = inflater.inflate(R.layout.member_grid_single, null);
			}

			String name = null;
			TextView textView = (TextView) grid.findViewById(R.id.grid_text);
			TextView currentOrg = (TextView) grid.findViewById(R.id.current_organization);
			TextView currentRole = (TextView) grid.findViewById(R.id.current_role);
			imageView = (ImageView) grid.findViewById(R.id.grid_image);
			
			if (page.equals("MembersFragment") && customerList != null &&
					customerList.get(position) != null) {
				name = customerList.get(position).getName();
				textView.setText(name);
				currentOrg.setText(customerList.get(position).getOccupation());
				currentRole.setText(customerList.get(position).getCurrentRole());
				imageView.setImageResource(R.drawable.ic_person);
			} else if (page.equals("HomeActivity") && web != null &&
				web[position] != null) {
				textView.setText(web[position]);
				imageView.setImageResource(mThumbIds[position]);
				//imageView.setImageBitmap(decodeResourceFile(mThumbIds[position]));
				
			} /*else if ((page.equals("PastPresidentsActivity") && customerList != null &&
					customerList.get(position) != null)) {
					name = customerList.get(position).getName();
					textView.setText(name);
					currentRole.setText(customerList.get(position).getCurrentRole());
				}*/
			
			if (page.equals("HomeActivity")) {
				//imageView.setImageBitmap(decodeResourceFile(mThumbIds[position]));

			} else {
				//imageView.setImageResource(Utility.getImage(customerList.get(position).getCustomerId()));
				
				//imageView.setImageBitmap(decodeFile(customerList.get(position).getCustomerId()));
				
				/*Log.d("ImageAdapter", "ImageURL: "+ customerList.get(position).getImageURL());
				try {
					if (customerList.get(position).getImageURL() != null && 
							! customerList.get(position).getImageURL().trim().equals("")) {
						
						new Downloader(this).execute(customerList.get(position).getImageURL());
						//String bitMapURI = Utility.loadImageFromWeb(customerList.get(position).getImageURL());
						//imageView.setImageURI(Uri.parse(bitMapURI));
					} else {
						imageView.setImageResource(R.drawable.user_icon);
					}
				} catch (Exception e) {
					Log.e(page, "Error Retrieving Image :" + e);
					imageView.setImageResource(R.drawable.user_icon);
				}*/ 
					
			}
			
			return grid;	
		} else {
			grid = (View) convertView;
			TextView textView = (TextView) grid.findViewById(R.id.grid_text);
			TextView currentOrg = (TextView) grid.findViewById(R.id.current_organization);
			TextView currentRole = (TextView) grid.findViewById(R.id.current_role);
			imageView = (ImageView) grid.findViewById(R.id.grid_image);
			String name = null;
			
			if (page.equals("HomeActivity")) {
				textView.setText(web[position]);
				//imageView.setImageBitmap(decodeResourceFile(mThumbIds[position]));
				imageView.setImageResource(mThumbIds[position]);
			} else if ((page.equals("MembersFragment") || page.equals("PastPresidentsActivity"))&&
					customerList != null && 
					customerList.get(position) != null) {
				name = customerList.get(position).getName();
				textView.setText(name);
				currentOrg.setText(customerList.get(position).getOccupation());
				currentRole.setText(customerList.get(position).getCurrentRole());
				imageView.setImageResource(R.drawable.ic_person);
				//imageView.setImageResource(Utility.getImage(customerList.get(position).getCustomerId()));
				
				//imageView.setImageBitmap(decodeFile(customerList.get(position).getCustomerId()));
				
				/*Log.d("ImageAdapter", "ImageURL: "+ customerList.get(position).getImageURL());
				try {
					if (customerList.get(position).getImageURL() != null && 
							! customerList.get(position).getImageURL().trim().equals("")) {
						new Downloader(this).execute(customerList.get(position).getImageURL());
						
						//String bitMapURI = Utility.loadImageFromWeb(customerList.get(position).getImageURL());
						//imageView.setImageURI(Uri.parse(bitMapURI));
					} else {
						imageView.setImageResource(R.drawable.user_icon);
					}
				} catch (Exception e) {
					Log.e(page, "Error Retrieving Image :" + e);
					imageView.setImageResource(R.drawable.user_icon);
				}*/ 
			}
			
	        return convertView;
		}
		
	}
	
	/*private Bitmap decodeFile (int customerID) {
		Bitmap imageBM = null;
		Bitmap outBM = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			
			imageBM = BitmapFactory.decodeResource(mContext.getResources(), Utility.getImage(customerID), o);
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			
			System.out.println("width_tmp :"+ width_tmp);
			System.out.println("height_tmp :"+ height_tmp);
			// The new size we want to scale to
			final int REQUIRED_SIZE = 100;
			
			int scale = 1;
			while (true) {
			    if (width_tmp / 2 < REQUIRED_SIZE
			            || height_tmp / 2 < REQUIRED_SIZE)
			        break;
			    width_tmp /= 2;
			    height_tmp /= 2;
			    scale *= 2;
			}
			System.out.println("scale :"+ scale);
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			outBM = BitmapFactory.decodeResource(mContext.getResources(), Utility.getImage(customerID), o2);
			return outBM;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;

	}*/
	
	private Bitmap decodeResourceFile (int resourceID) {
		
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			
			Bitmap imageBM = BitmapFactory.decodeResource(mContext.getResources(), resourceID, o);
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			
			System.out.println("width_tmp :"+ width_tmp);
			System.out.println("height_tmp :"+ height_tmp);
			// The new size we want to scale to
			final int REQUIRED_SIZE = 100;
			
			int scale = 1;
			while (true) {
			    if (width_tmp / 2 < REQUIRED_SIZE
			            || height_tmp / 2 < REQUIRED_SIZE)
			        break;
			    width_tmp /= 2;
			    height_tmp /= 2;
			    scale *= 2;
			}
			System.out.println("scale :"+ scale);
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeResource(mContext.getResources(), resourceID, o2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	private class Downloader extends AsyncTask<String, Void, String> {
		ImageViewAdapter imageAdapter;
		String bitMapURI;

		public Downloader(ImageViewAdapter imageAdapter) {
			this.imageAdapter = imageAdapter;
		}
		@Override
		protected String doInBackground(String... urls) {
			
			bitMapURI = Utility.loadImageFromWeb(urls[0]);
			
			return bitMapURI;
		}
		
		protected void onPostExecute(String result) {
			//System.out.println(" Result :"+ result);
			imageAdapter.imageView.setImageURI(Uri.parse(bitMapURI));

		}
		
	}


}