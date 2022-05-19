package imat;

import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.*;

public class categoryHandler {
    private ArrayList<Product> VegCategoryProducts = new ArrayList<>();
    private ArrayList<Product> MeatCategoryProducts = new ArrayList<>();
    private ArrayList<Product> DairyCategoryProducts = new ArrayList<>();
    private ArrayList<Product> PantryCategoryProducts = new ArrayList<>();
    private ArrayList<Product> DrinksCategoryProducts = new ArrayList<>();
    private HashMap<ProductCategory, String> subCategoryString = new HashMap<ProductCategory, String>();
    IMatDataHandler db = IMatDataHandler.getInstance();
    private static categoryHandler instance = null;

    private categoryHandler() {
    }

    public static categoryHandler getInstance() {
        if (instance == null) {
            instance = new categoryHandler();
            instance.init();
        }

        return instance;
    }

    private void init() {
        VegCategoryProducts.addAll(db.getProducts(ProductCategory.POD));
        PantryCategoryProducts.addAll(db.getProducts(ProductCategory.BREAD));
        VegCategoryProducts.addAll(db.getProducts(ProductCategory.BERRY));
        VegCategoryProducts.addAll(db.getProducts(ProductCategory.CITRUS_FRUIT));
        DrinksCategoryProducts.addAll(db.getProducts(ProductCategory.HOT_DRINKS));
        DrinksCategoryProducts.addAll(db.getProducts(ProductCategory.COLD_DRINKS));
        VegCategoryProducts.addAll(db.getProducts(ProductCategory.EXOTIC_FRUIT));
        MeatCategoryProducts.addAll(db.getProducts(ProductCategory.FISH));
        VegCategoryProducts.addAll(db.getProducts(ProductCategory.VEGETABLE_FRUIT));
        VegCategoryProducts.addAll(db.getProducts(ProductCategory.CABBAGE));
        MeatCategoryProducts.addAll(db.getProducts(ProductCategory.MEAT));
        DairyCategoryProducts.addAll(db.getProducts(ProductCategory.DAIRIES));
        VegCategoryProducts.addAll(db.getProducts(ProductCategory.MELONS));
        PantryCategoryProducts.addAll(db.getProducts(ProductCategory.FLOUR_SUGAR_SALT));
        PantryCategoryProducts.addAll(db.getProducts(ProductCategory.NUTS_AND_SEEDS));
        PantryCategoryProducts.addAll(db.getProducts(ProductCategory.PASTA));
        PantryCategoryProducts.addAll(db.getProducts(ProductCategory.POTATO_RICE));
        VegCategoryProducts.addAll(db.getProducts(ProductCategory.ROOT_VEGETABLE));
        VegCategoryProducts.addAll(db.getProducts(ProductCategory.FRUIT));
        PantryCategoryProducts.addAll(db.getProducts(ProductCategory.SWEET));
        VegCategoryProducts.addAll(db.getProducts(ProductCategory.HERB));
        subCategoryString.put(ProductCategory.POD, "Baljväxter");
        subCategoryString.put(ProductCategory.BREAD, "Bröd");
        subCategoryString.put(ProductCategory.BERRY, "Bär");
        subCategoryString.put(ProductCategory.CITRUS_FRUIT, "Citrusfrukter");
        subCategoryString.put(ProductCategory.HOT_DRINKS, "Varm dryck");
        subCategoryString.put(ProductCategory.COLD_DRINKS, "Kall dryck");
        subCategoryString.put(ProductCategory.EXOTIC_FRUIT, "Exotisk Frukt");
        subCategoryString.put(ProductCategory.FISH, "Fisk");
        subCategoryString.put(ProductCategory.VEGETABLE_FRUIT, "GrönsaksFrukt??");
        subCategoryString.put(ProductCategory.CABBAGE, "Kål");
        subCategoryString.put(ProductCategory.MEAT, "Kött");
        subCategoryString.put(ProductCategory.DAIRIES, "Mejeri");
        subCategoryString.put(ProductCategory.MELONS, "Meloner");
        subCategoryString.put(ProductCategory.FLOUR_SUGAR_SALT, "Basvaror");
        subCategoryString.put(ProductCategory.NUTS_AND_SEEDS, "Nötter och Frön");
        subCategoryString.put(ProductCategory.PASTA, "Pasta");
        subCategoryString.put(ProductCategory.POTATO_RICE, "Kolhydrater");
        subCategoryString.put(ProductCategory.ROOT_VEGETABLE, "Rotfrukt");
        subCategoryString.put(ProductCategory.FRUIT, "Frukt");
        subCategoryString.put(ProductCategory.SWEET, "Sötsaker");
        subCategoryString.put(ProductCategory.HERB, "Örter");
    }

    public ArrayList<Product> getDairyCategoryProducts() {
        return DairyCategoryProducts;
    }

    public ArrayList<Product> getMeatCategoryProducts() {
        return MeatCategoryProducts;
    }

    public ArrayList<Product> getDrinksCategoryProducts() {
        return DrinksCategoryProducts;
    }

    public ArrayList<Product> getPantryCategoryProducts() {
        return PantryCategoryProducts;
    }

    public ArrayList<Product> getVegCategoryProducts() {
        return VegCategoryProducts;
    }

    public String mainToString(Product p) {
        if (VegCategoryProducts.contains(p)) {
            return "Frukt & Grönt";
        }
        if (MeatCategoryProducts.contains(p)) {
            return "Kött & Fisk";
        }
        if (DairyCategoryProducts.contains(p)) {
            return "Mejeri";
        }
        if (PantryCategoryProducts.contains(p)) {
            return "Skafferi";
        }
        return "Dryck";
    }

    public String subToString(Product p) {
        return subCategoryString.get(p.getCategory());
    }

    private static <T, E> T getKeyByValue(HashMap<T, E> map, E value) {
        for (HashMap.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public List<Product> getProductsFromName(String name) {
        switch (name) {
            case "Alla Produkter":
                return db.getProducts();
            case "Frukt & Grönt":
                return getVegCategoryProducts();
            case "Mejeri":
                return getDairyCategoryProducts();
            case "Kött & Fisk":
                return getMeatCategoryProducts();
            case "Skafferi":
                return getPantryCategoryProducts();
            case "Dryck":
                return getDrinksCategoryProducts();
            default:
                return db.getProducts(getKeyByValue(subCategoryString, name));
        }
    }

    public String[] getAllMainStrings(){
        String[] mains = {"Alla Produkter", "Frukt & Grönt", "Mejeri", "Kött & Fisk", "Skafferi", "Dryck"};
        return mains;
    }

    public String[] getAllSubStrings() {
        String[] subs = new String[21];
        int i = 0;
        for (String sub : subCategoryString.values()){
            subs[i] = sub;
            i++;
        }
        return subs;
    }
}


