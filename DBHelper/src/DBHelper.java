import java.io.File;
import java.sql.*;
import java.util.HashMap;
public class DBHelper {

	public static String PATH_TO_PICTOS = "/Users/c2lieu/Desktop/picto"; //ex: "/Users/c2lieu/Desktop/picto"
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PreparedStatement prep;
		ResultSet keys;
		HashMap<String,Long> catMap = new HashMap<String,Long>(100);
		HashMap<String,Long> subMap = new HashMap<String,Long>(150);
		
		Connection conn = null;
		try{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:aac_data");
			Statement stat = conn.createStatement();
			
			stat.executeUpdate("drop table if exists Categorys;");
			stat.executeUpdate("drop table if exists Subcategorys;");
			stat.executeUpdate("drop table if exists Pictos;");
			stat.executeUpdate("drop table if exists RecentPictos;");
			stat.executeUpdate("drop table if exists Images;");
			stat.executeUpdate("drop table if exists Subcategorys_Pictos;");
			stat.executeUpdate("drop table if exists android_metadata;");
			stat.executeUpdate("drop table if exists PictoTree;");
			
			stat.executeUpdate(DBResources.CREATE_TABLE_CATEGORYS);
			stat.executeUpdate(DBResources.CREATE_TABLE_SUBCATEGORYS);
			stat.executeUpdate(DBResources.CREATE_TABLE_PICTOS);
			stat.executeUpdate(DBResources.CREATE_TABLE_RECENTPICTOS);
			stat.executeUpdate(DBResources.CREATE_TABLE_IMAGES);
			stat.executeUpdate(DBResources.CREATE_TABLE_SUBCATEGORYS_PICTOS);
			stat.executeUpdate(DBResources.CREATE_TABLE_PICTOTREE);
			
			stat.executeUpdate(DBResources.CREATE_FKI_CATEGORYS_IMAGES);
			stat.executeUpdate(DBResources.CREATE_FKI_SUBCATEGORYS_IMAGES);
			stat.executeUpdate(DBResources.CREATE_FKI_SUBCATEGORYS_CATEGORYS);
			stat.executeUpdate(DBResources.CREATE_FKI_PICTOS_IMAGES);
			stat.executeUpdate(DBResources.CREATE_FKI_RECENTPICTOS_IMAGES);
			stat.executeUpdate(DBResources.CREATE_FKI_SUBCATEGORYS_PICTOS_PICTOID);
			stat.executeUpdate(DBResources.CREATE_FKI_SUBCATEGORYS_PICTOS_SUBCATID);

			stat.executeUpdate(DBResources.CREATE_FKU_CATEGORYS_IMAGES);
			stat.executeUpdate(DBResources.CREATE_FKU_SUBCATEGORYS_IMAGES);
			stat.executeUpdate(DBResources.CREATE_FKU_SUBCATEGORYS_CATEGORYS);
			stat.executeUpdate(DBResources.CREATE_FKU_PICTOS_IMAGES);
			stat.executeUpdate(DBResources.CREATE_FKU_RECENTPICTOS_IMAGES);
			stat.executeUpdate(DBResources.CREATE_FKU_SUBCATEGORYS_PICTOS_PICTOID);
			stat.executeUpdate(DBResources.CREATE_FKU_SUBCATEGORYS_PICTOS_SUBCATID);
			
			stat.executeUpdate(DBResources.CREATE_ANDROID_METADATA);
			stat.executeUpdate(DBResources.INSERT_ANDROID_METADATA);
			//Images
			stat.executeUpdate("insert into Images (imageDesc, imageUri) values"
					+"('Cat Food', 'Songs/FloRida/cat food.png');");
			stat.executeUpdate("insert into categorys(categoryName,categoryDesc,imageID)" +
					" values ('Favourites','Favourites',1)");
			
			conn.setAutoCommit(false); //Transaction
			
			File[] files = new File(PATH_TO_PICTOS).listFiles();
		   			
			prep = conn.prepareStatement("insert into Categorys (categoryName, categoryDesc, imageID) values(?, ?, 1)",Statement.RETURN_GENERATED_KEYS);
		    //Insert Categories
		    for(File f : files){
		    	if(f.isDirectory()){
		    		//System.out.println(f.getName());
		    		prep.setString(1, f.getName());
		    		prep.setString(2, f.getName());		    		
		    		prep.executeUpdate();
		    		
		    		keys = prep.getGeneratedKeys();

					if(keys.next()){
						catMap.put(f.getName(), keys.getLong(1));
					}
					
					conn.commit();
		    	}
		    }
		    
		    prep = conn.prepareStatement("insert into Subcategorys (subcategoryName, subcategoryDesc, categoryID,imageID) values"
					+"(?, ?, ?, 1);",Statement.RETURN_GENERATED_KEYS);
		    //Insert Subcategories
		    for(File f : files){
		    	if(f.isDirectory()){
		    		String cat = f.getName();
		    		File[] subs = f.listFiles();
		    		for(File f2 : subs){
		    			if(f2.isDirectory()){
		    				//System.out.println(cat + ": " + f2.getName());
		    				prep.setString(1, f2.getName());
		    				prep.setString(2, f2.getName());
		    				prep.setLong(3, catMap.get(cat));
		    				prep.executeUpdate();
		    				
		    				keys = prep.getGeneratedKeys();

		    				if(keys.next()){
		    					subMap.put(cat+f2.getName(), keys.getLong(1));
		    				}

		    				conn.commit();
		    			}
		    		}
		    	}
		    }
		    
		    prep = conn.prepareStatement("insert into Images (imageDesc, imageUri) values"
					+"(?, ?);",Statement.RETURN_GENERATED_KEYS);
		    PreparedStatement prepPicto = conn.prepareStatement("insert into Pictos (pictoPhrase, imageID,playCount) values"
					+"(?, ?,0);",Statement.RETURN_GENERATED_KEYS);
		    PreparedStatement prepPictoSub = conn.prepareStatement("insert into Subcategorys_Pictos (subcategoryID, pictoID) values"
					+"(?, ?);",Statement.RETURN_GENERATED_KEYS);
		    //Insert Images, Pictos, and Subcategory_Pictos
		    for(File f : files){
		    	if(f.isDirectory()){
		    		String cat = f.getName();
		    		File[] subs = f.listFiles();
		    		for(File f2 : subs){
		    			if(f2.isDirectory()){
		    				File[] pictos = f2.listFiles();
		    				String sub = f2.getName();
		    				for(File picto : pictos){
		    					if(picto.getName().matches(".*\\.png$")){
		    						prep.setString(1, picto.getName());
		    						prep.setString(2, cat + "/" + sub + "/" + picto.getName());
		    						prep.executeUpdate();
		    						
		    						keys = prep.getGeneratedKeys();
		    						
		    						Long imgId = (long) 0;
		    						if(keys.next()) imgId = keys.getLong(1);
		    						
		    						prepPicto.setString(1, rmExt(picto.getName(),".png"));
		    						prepPicto.setLong(2, imgId);
		    						prepPicto.executeUpdate();
		    						
		    						keys = prepPicto.getGeneratedKeys();
		    						
		    						Long pictoID = (long) 0;
		    						if(keys.next()) pictoID = keys.getLong(1);
		    						
		    						prepPictoSub.setLong(1, subMap.get(cat+sub));
		    						prepPictoSub.setLong(2, pictoID);
		    						prepPictoSub.executeUpdate();
		    						
		    						conn.commit();
		    						
		    					}
		    				}
		    			}
		    		}
		    	}
		    }
		    
		    
			conn.close();
		} catch(SQLException ex){
			// if the error message is "out of memory", 
			// it probably means no database file is found
			System.err.println(ex.getMessage());
			
		} catch(ClassNotFoundException ex){
			System.err.println(ex.getMessage());
		} finally{
			try {
				if(conn != null)
					conn.close();
				
				System.out.println("*****DBHelper*******");
				System.out.println("Completed database");
			} catch(SQLException e)	{
				// connection close failed.
				System.err.println(e);
			}
		}
	}
	
	private static String rmExt(String file, String ext)
	{
		if(file.endsWith(ext))
			return file.substring(0,file.length() - ext.length());
		else
			return file;
	}

}
