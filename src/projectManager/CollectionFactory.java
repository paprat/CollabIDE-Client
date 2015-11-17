package projectManager;

public abstract class CollectionFactory {
    public static Collections getCollection(String userIdentifier) {
        Collections collection = new Collections(userIdentifier, "");
        return collection;
    }
}
