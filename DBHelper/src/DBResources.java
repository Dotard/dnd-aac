
public class DBResources {

	public static final String CREATE_TABLE_CATEGORYS = "CREATE TABLE Categorys"  + " ( "
			+ "categoryID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "imageID INTEGER REFERENCES Images (imageID) NOT NULL, "
			+ "categoryName TEXT, "
			+ "categoryDesc TEXT "
			+ ");";
	
	public static final String CREATE_TABLE_SUBCATEGORYS = "CREATE TABLE Subcategorys" + " ( "
			+ "subcategoryID INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "categoryID INTEGER REFERENCES Categorys (categoryID) NOT NULL, "
			+ "imageID INTEGER REFERENCES Images (imageID) NOT NULL, "
			+ "subcategoryName TEXT, "
			+ "subcategoryDesc TEXT "
			+ ");";
	
	public static final String CREATE_TABLE_PICTOS = "CREATE TABLE Pictos" + " ( "
			+ "pictoID INTEGER PRIMARY KEY AUTOINCREMENT, "			
			+ "imageID INTEGER REFERENCES Images (imageID) NOT NULL, "
			+ "pictoPhrase TEXT,"
			+ "playCount INTEGER"
			+ ");";
	
	public static final String CREATE_TABLE_RECENTPICTOS = "CREATE TABLE RecentPictos" + " ( "
			+ "recentPictoID INTEGER PRIMARY KEY AUTOINCREMENT, "			
			+ "imageID INTEGER REFERENCES Images (imageID) NOT NULL, "
			+ "pictoID INTEGER REFERENCES Pictos (pictoID) NOT NULL, "
			+ "recentPictoPhrase TEXT, "
			+ "recentPictoOutdated INTEGER "
			+ ");";
			
	public static final String CREATE_TABLE_IMAGES = "CREATE TABLE Images" + " ( "
			+ "imageID INTEGER PRIMARY KEY AUTOINCREMENT, "						
			+ "imageDesc TEXT, "
			+ "imageUri TEXT "
			+ ");";
	
	public static final String CREATE_TABLE_SUBCATEGORYS_PICTOS = "CREATE TABLE Subcategorys_Pictos"  + " ( "
			+ "subcategorys_PictosID INTEGER PRIMARY KEY AUTOINCREMENT, "						
			+ "subcategoryID INTEGER REFERENCES Subcategorys (subcategoryID) NOT NULL, "
			+ "pictoID INTEGER REFERENCES Pictos (pictoID) NOT NULL "
			+ ");";
	
	public static final String CREATE_TABLE_PICTOTREE = "CREATE TABLE PictoTree"  + " ( "
			+ "trieID INTEGER PRIMARY KEY AUTOINCREMENT, "						
			+ "parentTrieID INTEGER REFERENCES PictoTree (trieID) NOT NULL DEFAULT 0, "
			+ "pictoID INTEGER REFERENCES Pictos (pictoID) NOT NULL, "
			+ "hits INTEGER DEFAULT 0"
			+ ");";

	//FKI
	public static final String CREATE_FKI_CATEGORYS_IMAGES = "CREATE TRIGGER fki_categorys_images " +
			"BEFORE INSERT ON Categorys " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT imageID FROM Images WHERE imageID = NEW.imageID) IS NULL) " +
			"	THEN RAISE(ABORT, 'insert on table \"categorys\" violates foreign key constraint \"fki_categorys_images\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKI_SUBCATEGORYS_IMAGES = "CREATE TRIGGER fki_subcategorys_images " +
			"BEFORE INSERT ON Subcategorys " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT imageID FROM Images WHERE imageID = NEW.imageID) IS NULL) " +
			"	THEN RAISE(ABORT, 'insert on table \"subcategorys\" violates foreign key constraint \"fki_subcategorys_images\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKI_SUBCATEGORYS_CATEGORYS = "CREATE TRIGGER fki_subcategorys_categorys " +
			"BEFORE INSERT ON Subcategorys " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT categoryID FROM Categorys WHERE categoryID = NEW.categoryID) IS NULL) " +
			"	THEN RAISE(ABORT, 'insert on table \"subcategorys\" violates foreign key constraint \"fki_subcategorys_categorys\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKI_PICTOS_IMAGES = "CREATE TRIGGER fki_pictos_images " +
			"BEFORE INSERT ON Pictos " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT imageID FROM Images WHERE imageID = NEW.imageID) IS NULL) " +
			"	THEN RAISE(ABORT, 'insert on table \"pictos\" violates foreign key constraint \"fki_pictos_images\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKI_RECENTPICTOS_IMAGES = "CREATE TRIGGER fki_recentpictos_images " +
			"BEFORE INSERT ON RecentPictos " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT imageID FROM Images WHERE imageID = NEW.imageID) IS NULL) " +
			"	THEN RAISE(ABORT, 'insert on table \"RecentPictos\" violates foreign key constraint \"fki_recentpictos_images\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKI_SUBCATEGORYS_PICTOS_PICTOID = "CREATE TRIGGER fki_subcategorys_pictos_pictoid " +
			"BEFORE INSERT ON SUBCATEGORYS_PICTOS " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT pictoID FROM Pictos WHERE pictoID = NEW.pictoID) IS NULL) " +
			"	THEN RAISE(ABORT, 'insert on table \"SUBCATEGORYS_PICTOS\" violates foreign key constraint \"fki_subcategorys_pictos_pictoid\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKI_SUBCATEGORYS_PICTOS_SUBCATID = "CREATE TRIGGER fki_subcategorys_pictos_subcatID " +
			"BEFORE INSERT ON Subcategorys_Pictos " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT subcategoryID FROM Subcategorys WHERE subcategoryID = NEW.subcategoryID) IS NULL) " +
			"	THEN RAISE(ABORT, 'insert on table \"SUBCATEGORYS_PICTOS\" violates foreign key constraint \"fki_subcategorys_pictos_subcatID\"') " +
			"END; "+
			"END; ";
	
	//FKU
	public static final String CREATE_FKU_CATEGORYS_IMAGES = "CREATE TRIGGER fku_categorys_images " +
			"BEFORE UPDATE ON Categorys " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT imageID FROM Images WHERE imageID = NEW.imageID) IS NULL) " +
			"	THEN RAISE(ABORT, 'update on table \"categorys\" violates foreign key constraint \"fku_categorys_images\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKU_SUBCATEGORYS_IMAGES = "CREATE TRIGGER fku_subcategorys_images " +
			"BEFORE UPDATE ON Subcategorys " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT imageID FROM Images WHERE imageID = NEW.imageID) IS NULL) " +
			"	THEN RAISE(ABORT, 'update on table \"subcategorys\" violates foreign key constraint \"fku_subcategorys_images\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKU_SUBCATEGORYS_CATEGORYS = "CREATE TRIGGER fku_subcategorys_categorys " +
			"BEFORE UPDATE ON Subcategorys " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT categoryID FROM Categorys WHERE categoryID = NEW.categoryID) IS NULL) " +
			"	THEN RAISE(ABORT, 'update on table \"subcategorys\" violates foreign key constraint \"fku_subcategorys_categorys\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKU_PICTOS_IMAGES = "CREATE TRIGGER fku_pictos_images " +
			"BEFORE UPDATE ON Pictos " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT imageID FROM Images WHERE imageID = NEW.imageID) IS NULL) " +
			"	THEN RAISE(ABORT, 'update on table \"pictos\" violates foreign key constraint \"fku_pictos_images\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKU_RECENTPICTOS_IMAGES = "CREATE TRIGGER fku_recentpictos_images " +
			"BEFORE UPDATE ON RecentPictos " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT imageID FROM Images WHERE imageID = NEW.imageID) IS NULL) " +
			"	THEN RAISE(ABORT, 'update on table \"RecentPictos\" violates foreign key constraint \"fku_recentpictos_images\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKU_SUBCATEGORYS_PICTOS_PICTOID = "CREATE TRIGGER fku_subcategorys_pictos_pictoid " +
			"BEFORE UPDATE ON SUBCATEGORYS_PICTOS " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT pictoID FROM Pictos WHERE pictoID = NEW.pictoID) IS NULL) " +
			"	THEN RAISE(ABORT, 'update on table \"SUBCATEGORYS_PICTOS\" violates foreign key constraint \"fku_subcategorys_pictos_pictoid\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_FKU_SUBCATEGORYS_PICTOS_SUBCATID = "CREATE TRIGGER fku_subcategorys_pictos_subcatID " +
			"BEFORE UPDATE ON Subcategorys_Pictos " +
			"FOR EACH ROW BEGIN " +
			"	SELECT CASE " +
			"	WHEN ( (SELECT subcategoryID FROM Subcategorys WHERE subcategoryID = NEW.subcategoryID) IS NULL) " +
			"	THEN RAISE(ABORT, 'update on table \"SUBCATEGORYS_PICTOS\" violates foreign key constraint \"fku_subcategorys_pictos_subcatID\"') " +
			"END; "+
			"END; ";
	
	public static final String CREATE_ANDROID_METADATA = "CREATE TABLE \"android_metadata\" (\"locale\" TEXT DEFAULT 'en_US')";
	public static final String INSERT_ANDROID_METADATA = "INSERT INTO \"android_metadata\" VALUES ('en_US')";
}
