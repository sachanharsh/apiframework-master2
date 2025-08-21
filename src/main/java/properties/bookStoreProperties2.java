package properties;

import utils.ConfigManager;

public class bookStoreProperties2 {

    public static final String host = ConfigManager.getInstance().get("Host");
    private static final String ACCOUNT_BASEPATH = ConfigManager.getInstance().get("base.account");
    private static final String BOOK_BASEPATH = ConfigManager.getInstance().get("base.book");

    public static final String getAllBooks = String.format("%s/BookStore/v1/Books", host);
    public static final String GET_ALL_BOOKS    = host + BOOK_BASEPATH + "/Books";
    public static final String GET_BOOK   = host + BOOK_BASEPATH + "/Books";
    public static final String POST_BOOKS       = host + BOOK_BASEPATH + "/Books";
    public static final String DELETE_BOOK      = host + BOOK_BASEPATH + "/Book";

}
