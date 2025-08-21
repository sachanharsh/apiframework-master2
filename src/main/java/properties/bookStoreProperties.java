package properties;

public class bookStoreProperties {

    private static final PropertiesReader propertiesReader = new PropertiesReader();
    private static final String HOST = propertiesReader.getHost();
    private static final String ACCOUNT_BASEPATH = propertiesReader.get("base.account");
    private static final String BOOK_BASEPATH = propertiesReader.get("base.book");

    public static final String getAllBooks = String.format("%s/BookStore/v1/Books", HOST);
    public static final String GET_ALL_BOOKS    = HOST + BOOK_BASEPATH + "/Books";
    public static final String GET_BOOK   = HOST + BOOK_BASEPATH + "/Books";
    public static final String POST_BOOKS       = HOST + BOOK_BASEPATH + "/Books";
    public static final String DELETE_BOOK      = HOST + BOOK_BASEPATH + "/Book";

}
